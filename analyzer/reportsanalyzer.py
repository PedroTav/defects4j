import argparse
import pathlib
import re
from functools import partial
from typing import List

from reports.reports import (
    JudyReport,
    JumbleReport,
    MajorReport,
    MultipleClassUnderMutationError,
    MultipleFilesReport,
    PitReport,
    Report,
    ReportError,
    SingleFileReport,
)
from reports.utility import get_defects4j_modified_classes


def check_pattern(arg_value: str, pattern: re.Pattern):
    if not pattern.match(arg_value):
        raise argparse.ArgumentTypeError(
            f"Invalid value provided! Must match regex pattern: {pattern.pattern}"
        )
    else:
        return arg_value


# make partial function with pattern already set
check_bug_pattern = partial(check_pattern, pattern=re.compile(r"\d+"))

# constants
TOOLS = ["judy", "jumble", "major", "pit"]
TOOLS_CLASSES = {
    "judy": JudyReport,
    "jumble": JumbleReport,
    "major": MajorReport,
    "pit": PitReport,
}

PROJECT_HELP = "The report's Defects4J project"
BUG_HELP = "The project bug id; must be a numeric value"
TOOL_HELP = "The mutation tool to which the reports to study belong"
FILES_HELP = (
    "The space-separated list of reports to study;"
    " if working with a multiple files report, provide its directory"
)
ERR_MULT_CLASSES = (
    "Can work with only one mutated class at time! "
    "This combination of project and bug have {n} "
    "modified classes, that are {l}"
)
ERR_CLASS = "Report class under mutation is {rep_cls}, but it should be {cls}!"
ERR_EXP_DIR = "Was expecting a directory, but found a file!"
ERR_EXP_FILE = "Was expecting a file, but found a directory!"
ERR_EXP_MULT_FILES = "Was expecting 2 or more files, but found {n}!"


def get_reports(project: str, bug: str, tool: str, files: List[str]) -> List[Report]:
    # get modified classes from defects4j framework
    # if there are 2+, raise an error
    # else get the single class under mutation
    modified_classes = get_defects4j_modified_classes(project=project, bug=bug)
    if len(modified_classes) > 1:
        raise MultipleClassUnderMutationError(
            ERR_MULT_CLASSES.format(n=len(modified_classes), l=modified_classes)
        )
    else:
        class_under_mutation = modified_classes[0]

    # from the tool string, get the corresponding class
    tool_cls = TOOLS_CLASSES[tool]

    # check if the input is a single file, or a directory, i.e. multifiles
    if issubclass(tool_cls, SingleFileReport):
        should_be_dir = False
    elif issubclass(tool_cls, MultipleFilesReport):
        should_be_dir = True
    else:
        raise ReportError(
            f"{tool_cls} is neither single file nor multiple files report!"
        )

    reports = []

    for file in files:
        # convert the file to a Path object
        path = pathlib.Path(file)

        # if it doesn't exist, there is a problem
        if not path.exists():
            raise FileNotFoundError(path)

        # if I'm here, the file/dir exists
        # now I check what I expected
        if should_be_dir and not path.is_dir():
            raise OSError(ERR_EXP_DIR)
        elif not should_be_dir and path.is_dir():
            raise OSError(ERR_EXP_FILE)

        # if I'm here, everything is ok with this argument
        if should_be_dir:
            files = list(path.iterdir())
            if len(files) < 2:
                raise OSError(ERR_EXP_MULT_FILES.format(n=len(files)))

            report = tool_cls(*files)
        else:
            if issubclass(tool_cls, JudyReport):
                report = tool_cls(path, class_under_mutation=class_under_mutation)
            else:
                report = tool_cls(path)

        if report.class_under_mutation != class_under_mutation:
            raise ReportError(
                ERR_CLASS.format(
                    rep_cls=report.class_under_mutation, cls=class_under_mutation
                )
            )

        reports.append(report)

    return reports


if __name__ == "__main__":
    parser = argparse.ArgumentParser()

    parser.add_argument("-p", "--project", help=PROJECT_HELP, required=True)
    parser.add_argument(
        "-b", "--bug", help=BUG_HELP, type=check_bug_pattern, required=True
    )
    parser.add_argument("-t", "--tool", help=TOOL_HELP, choices=TOOLS, required=True)

    parser.add_argument("files", help=FILES_HELP, nargs="+")

    args = parser.parse_args()

    _reports = get_reports(
        project=args.project, bug=args.bug, tool=args.tool, files=args.files
    )

    for r in _reports:
        print(r.summary(), "\n")
