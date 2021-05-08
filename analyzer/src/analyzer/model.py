import abc
import json
import logging
import os
import pathlib
import re
import shutil
from typing import Union

from src.analyzer import utility

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

    def get_output_dir(self):
        """Returns the output directory created inside the project directory"""
        return self.project_dir / self.tools_output / self.name

    def __init__(self, project_dir: Union[str, os.PathLike]):
        self.project_dir = pathlib.Path(project_dir)

    def _get_output_text(self, index: int = 0):
        """Return the text of a specified (index) output of the tool"""
        with open(self.get_output_dir() / self.output[index]) as f:
            text = f.read()
        return text

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

    def _get_mutation_score(self) -> dict:
        """Returns a dict, holding killed count, live count, all count and score"""
        raise NotImplementedError

    def get_mutation_score(self, json_output: str = None) -> float:
        """Get mutation score for current testsuite and tool"""
        output_dir = self.get_output_dir()
        logger.debug(f"Output dir is {output_dir.resolve()}")

        for outfile in self.output:
            outfile = output_dir / outfile
            if not outfile.exists():
                msg = f"{outfile} not found. Did you execute run() before?"
                raise FileNotFoundError(msg)
        score_dict = self._get_mutation_score()
        logger.debug(f"Score dict is {score_dict}")

        if json_output:
            if not json_output.endswith(".json"):
                json_output += ".json"
            json_output_path = output_dir / json_output
            with open(json_output_path, "w") as f:
                json.dump(score_dict, f)
            logger.info(f"Written score json to {json_output_path}")

        return score_dict["score"]

    def get_output(self):
        """Get the tool output and place it under
        the specified output directory"""

        # cast output dir as pathlib object
        output_dir = self.get_output_dir()

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
                msg = f"{outfile} not found. Did you execute run() before?"
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

    def _get_mutation_score(self) -> float:
        raise NotImplementedError


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

    def _get_mutation_score(self) -> dict:
        live_mutant_pattern = re.compile(r"M FAIL:\s*([a-zA-Z.]+):(\d+):\s*(.+)")
        start_pattern = re.compile(
            r"Mutation points = \d+, unit test time limit \d+\.\d+s"
        )
        end_pattern = re.compile(r"Jumbling took \d+\.\d+s")

        text = self._get_output_text()

        # get indices where the mutants are defined
        i = start_pattern.search(text).end()
        j = end_pattern.search(text[i:]).start() + i

        # subtract from text all the fails + get count of them
        killed_text, live_mutants_count = live_mutant_pattern.subn("", text[i:j])

        # get killed count as length of mutations with whitespaces removed
        killed_mutants_count = len(re.sub(r"\s+", "", killed_text))

        all_count = live_mutants_count + killed_mutants_count

        score_full = killed_mutants_count / all_count
        score = round(score_full, 3)

        return dict(
            killed=killed_mutants_count,
            live=live_mutants_count,
            all=all_count,
            score=score,
            score_full=score_full,
        )


class Major(Tool):
    """Major tool"""

    name = "major"

    output = ["kill.csv", "mutants.log"]

    def run(self, **kwargs):
        return utility.defects4j_cmd_dirpath(self.project_dir, "mutation")

    def _get_mutation_score(self) -> float:
        raise NotImplementedError


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

    def _get_mutation_score(self) -> float:
        raise NotImplementedError


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
