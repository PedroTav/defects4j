import abc
import datetime
import logging
import os
import pathlib
import shutil
from typing import Union

from analyzer import utility

logger = logging.getLogger(__file__)

FILES = pathlib.Path(__file__).parent / "files"


class Tool(abc.ABC):
    """Interface for mutation tools"""

    name: str = ""

    bash_script = None
    tools_output = "tools_output"
    output = []

    def __repr__(self):
        return f"{self.name.capitalize()}Tool"

    def get_output_dir(self, subdirectory: str = None):
        """Returns the output directory created inside the project directory."""
        path = self.project_dir / self.tools_output / self.name
        if subdirectory:
            path /= subdirectory
        return path

    def __init__(self, project_dir: Union[str, os.PathLike], class_under_mutation: str):
        self.project_dir = pathlib.Path(project_dir)
        self.class_under_mutation = class_under_mutation

    def remove_output(self, **kwargs):
        """Utility function to remove output files"""
        for outfile in self.output:
            outfile = self.project_dir / outfile
            if outfile.is_file():
                os.remove(outfile)
            elif outfile.is_dir():
                shutil.rmtree(outfile)

    def setup(self, **kwargs):
        """Setup tool files, copying them into the project dir"""
        if self.bash_script:
            src = os.fspath(FILES / self.bash_script)
            dst = os.fspath(self.project_dir / self.bash_script)
            shutil.copy(src, dst)

        # remove output files, to not parse them as current out
        self.remove_output(**kwargs)

    def run(self, **kwargs):
        """Run the tool via its bash script"""
        if not self.bash_script:
            raise ValueError("Cannot run bash script if it's null!")

        script = self.project_dir / self.bash_script

        capture_out = kwargs.get("stdout", False)
        capture_err = kwargs.get("stderr", False)
        utility.bash_script(script, capture_out, capture_err)

    def get_output(self, subdirectory: str = None):
        """Get the tool output and place it under
        the specified output directory"""

        now = datetime.datetime.now()
        nowstr = now.strftime("%Y-%m-%d_%H-%M-%S")

        # cast output dir as pathlib object
        output_dir = self.get_output_dir(subdirectory)

        logger.debug(f"Output dir is {output_dir}")

        # create output directory if didn't exist
        if not output_dir.exists():
            os.makedirs(output_dir)
            logger.info(f"Created {output_dir}")

        for outfile in self.output:
            # src is outfile in project directory
            outfile = self.project_dir / outfile

            logger.debug(f"Outfile (src) is {outfile}")

            # dst is projectDir / outputDir / outfile
            outfile_dst = output_dir / outfile.name
            outfile_dst = outfile_dst.with_stem(f"{outfile.stem}_{nowstr}")
            outfile_dst = outfile_dst.resolve()

            logger.debug(f"outfile_dst (dst) is {outfile_dst}")

            logger.debug(f"Working on {outfile}")

            if outfile.exists():
                src = os.fspath(outfile)
                dst = os.fspath(outfile_dst)
                shutil.move(src, dst)
                logger.info(f"Moved {outfile.name} to {outfile_dst}")
            else:
                msg = (
                    f"{outfile} not found.\n"
                    "If you executed run() before, then the tool got an error.\n"
                    "Try re-executing with --stdout and --stderr to see logs."
                )
                raise FileNotFoundError(msg)

    def replace(self, mapping: dict, file=None):
        """Overwrite tool flags with actual values"""
        if not file:
            file = self.bash_script
        if not file:
            raise FileNotFoundError("No file was found to be replaced!")

        filepath = self.project_dir / file

        # read file
        with open(filepath) as f:
            content = f.read()

        # change its content (flags)
        fixed = content
        for adict in mapping.values():
            fixed = fixed.replace(adict["original"], adict["replacement"])

        # write to file
        with open(filepath, "w") as f:
            f.write(fixed)


class Judy(Tool):
    """Judy tool"""

    name = "judy"

    bash_script = "judy.sh"
    output = ["result.json", "judy.log"]

    def setup(self, **kwargs):
        super(Judy, self).setup()
        theclass = kwargs.get("class")
        if not theclass:
            return
        theclass += ".class"
        toreplace = 'CLASS="$CLS/<PATH_TO_CLASS>"'
        replacement = f'CLASS="$CLS/{theclass}"'
        mapping = {
            "class": {"original": toreplace, "replacement": replacement},
        }
        self.replace(mapping=mapping)


class Jumble(Tool):
    """Jumble tool"""

    name = "jumble"

    bash_script = "jumble.sh"
    verbose_bash_script = "jumble_verbose.sh"
    output = ["jumble_output.txt"]

    def _create_verbose_script(self):
        src = self.project_dir / self.bash_script
        dst = src.with_name(self.verbose_bash_script)
        shutil.copy(os.fspath(src), os.fspath(dst))
        self.replace(
            {
                "verbose": {
                    "original": 'VERBOSE=""',
                    "replacement": 'VERBOSE="--verbose"',
                }
            },
            file=self.verbose_bash_script,
        )
        logger.debug("Verbose Jumble script created")

    def setup(self, **kwargs):
        super(Jumble, self).setup()
        mutations = kwargs.get("mutations", "MUTATIONS_ALL")
        mapping = {
            "tests": {"original": "<REPLACE_TESTS>", "replacement": kwargs["tests"]},
            "class": {"original": "<REPLACE_CLASS>", "replacement": kwargs["class"]},
            "mutations": {"original": "<REPLACE_MUTATIONS>", "replacement": mutations},
        }
        self.replace(mapping=mapping)

        # create also a verbose script of Jumble that can display errors
        self._create_verbose_script()


class Major(Tool):
    """Major tool"""

    name = "major"

    output = ["kill.csv", "mutants.log"]

    def run(self, **kwargs):
        return utility.defects4j_cmd_dirpath(self.project_dir, "mutation", **kwargs)

    def setup(self, **kwargs):
        """Remove compiled directory that prevents
        multiple mutations, if mutations.log is missing"""
        super(Major, self).setup()

        target = self.project_dir / ".classes_mutated"
        shutil.rmtree(target, ignore_errors=True)


class Pit(Tool):
    """Pit tool"""

    name = "pit"

    bash_script = "pit.sh"
    output = ["pit_report/mutations.xml"]

    def setup(self, **kwargs):
        super(Pit, self).setup()
        mapping = {
            "tests": {"original": "<TEST_REGEXP>", "replacement": kwargs["tests"]},
            "class": {"original": "<CLASS_REGEXP>", "replacement": kwargs["class"]},
        }
        self.replace(mapping=mapping)


def get_tool(
    tool_name: str, project_dir: Union[str, os.PathLike], class_under_mutation: str
):
    """Utility function to retrieve a tool from a name and a project dir"""
    valid_tools = {
        Judy.name: Judy(project_dir, class_under_mutation),
        Jumble.name: Jumble(project_dir, class_under_mutation),
        Major.name: Major(project_dir, class_under_mutation),
        Pit.name: Pit(project_dir, class_under_mutation),
    }
    if tool_name not in valid_tools.keys():
        msg = f"Invalid tool provided: {tool_name}. Valid tools are {list(valid_tools.keys())}"
        logger.error(msg)
        raise ValueError(msg)

    return valid_tools[tool_name]


def get_all_tools(project_dir: Union[str, os.PathLike], class_under_mutation: str):
    """Utility function to retrieve all mutation tools with a project dir"""
    return [
        get_tool(
            tool_name=name,
            project_dir=project_dir,
            class_under_mutation=class_under_mutation,
        )
        for name in (Judy.name, Jumble.name, Major.name, Pit.name)
    ]
