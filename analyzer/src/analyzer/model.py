import abc
import logging
import os
import pathlib
import shutil
from typing import Union

from src.analyzer import utility

logger = logging.getLogger(__file__)

FILES = pathlib.Path(__file__).parent / "files"


class Tool(abc.ABC):
    """Interface for mutation tools"""

    name: str = ""

    bash_script = None
    output = []

    def __repr__(self):
        return f"{self.name.capitalize()}Tool"

    def __init__(self, project_dir: Union[str, os.PathLike]):
        self.project_dir = pathlib.Path(project_dir)

    def setup(self, **kwargs):
        """Setup tool files, copying them into the project dir"""
        if self.bash_script:
            src = os.fspath(FILES / self.bash_script)
            dst = os.fspath(self.project_dir / self.bash_script)
            shutil.copy(src, dst)

    def run(self, **kwargs):
        """Run the tool via its bash script"""
        script = self.project_dir / self.bash_script

        capture_out = kwargs.get("stdout", False)
        capture_err = kwargs.get("stderr", False)
        utility.bash_script(script, capture_out, capture_err)

    def get_output(self, output_dir="tools_output"):
        """Get the tool output and place it under
        the specified output directory"""

        output_dir = self.project_dir / output_dir / self.name
        # create output directory if didn't exist
        if not output_dir.exists():
            os.makedirs(output_dir)
            logger.info(f"Created {output_dir}")

        for outfile in self.output:
            outfile = self.project_dir / outfile
            if outfile.exists():
                src = os.fspath(outfile)
                dst = os.fspath(output_dir / outfile.name)
                shutil.move(src, dst)
                logger.info(f"Moved {outfile.name} to {output_dir}")
            else:
                msg = f"File not found: {outfile} - did you execute run() before?"
                logger.error(msg)
                raise FileNotFoundError(msg)

    def replace(self, mapping: dict):
        """Overwrite tool flags with actual values"""
        file = self.project_dir / self.bash_script

        # read file
        with open(file) as f:
            content = f.read()

        # change its content (flags)
        fixed = content.replace(
            mapping["tests"]["original"], mapping["tests"]["replacement"]
        ).replace(mapping["class"]["original"], mapping["class"]["replacement"])

        # write to file
        with open(file, "w") as f:
            f.write(fixed)


class Judy(Tool):
    """Judy tool"""

    name = "judy"

    bash_script = "judy.sh"
    output = ["result.json"]


class Jumble(Tool):
    """Jumble tool"""

    name = "jumble"

    bash_script = "jumble.sh"
    output = ["jumble_output.txt"]

    def setup(self, **kwargs):
        super(Jumble, self).setup()
        mapping = {
            "tests": {"original": "<REPLACE_TESTS>", "replacement": kwargs["tests"]},
            "class": {"original": "<REPLACE_CLASS>", "replacement": kwargs["class"]},
        }
        self.replace(mapping=mapping)


class Major(Tool):
    """Major tool"""

    name = "major"

    output = ["kill.csv", "mutants.log"]

    def run(self, **kwargs):
        return utility.defects4j_cmd_dirpath(self.project_dir, "mutation")


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


def get_tool(tool_name: str, project_dir: Union[str, os.PathLike]):
    """Utility function to retrieve a tool from a name and a project dir"""
    valid_tools = {
        Judy.name: Judy(project_dir),
        Jumble.name: Jumble(project_dir),
        Major.name: Major(project_dir),
        Pit.name: Pit(project_dir),
    }
    if tool_name not in valid_tools.keys():
        msg = f"Invalid tool provided: {tool_name}. Valid tools are {list(valid_tools.keys())}"
        logger.error(msg)
        raise ValueError(msg)

    return valid_tools[tool_name]


def get_all_tools(project_dir: Union[str, os.PathLike]):
    """Utility function to retrieve all mutation tools with a project dir"""
    return [
        get_tool(tool_name=name, project_dir=project_dir)
        for name in (Judy.name, Jumble.name, Major.name, Pit.name)
    ]
