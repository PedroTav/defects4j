import argparse
import pathlib
import re
from functools import partial
from typing import List

from reports.commands import COMMANDS, COMMANDS_BY_NAME
from reports.reports import (
    JumbleReport,
    MajorReport,
    MultipleClassUnderMutationError,
    MultipleFilesReport,
    MultipleJudyReport,
    PitReport,
    Report,
    ReportError,
    SingleFileReport,
    SingleJudyReport,
)
from reports.utility import get_defects4j_modified_classes


def check_pattern(arg_value: str, pattern: re.Pattern):
    if not pattern.match(arg_value):
        raise argparse.ArgumentTypeError(
            f"Invalid value provided! Must match regex pattern: {pattern.pattern}"
        )
    else:
        return arg_value


def get_file_with_ext(files: List[pathlib.Path], ext: str) -> pathlib.Path:
    if not ext.startswith("."):
        ext = "." + ext
    return [f for f in files if f.suffix.lower() == ext].pop()


# make partial function with pattern already set
check_bug_pattern = partial(check_pattern, pattern=re.compile(r"^\d+$"))


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

    parsed_reports = []

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

            if issubclass(tool_cls, (MajorReport, MultipleJudyReport)):
                if len(files) != 2:
                    raise OSError(ERR_EXP_NM_FILES.format(n=2, m=len(files)))
            if issubclass(tool_cls, MajorReport):
                csv = get_file_with_ext(files, "csv")
                log = get_file_with_ext(files, "log")
                report = tool_cls(log, csv)
            elif issubclass(tool_cls, MultipleJudyReport):
                json = get_file_with_ext(files, "json")
                log = get_file_with_ext(files, "log")
                report = tool_cls(json, log, class_under_mutation)
            else:
                report = tool_cls(*files)
        elif issubclass(tool_cls, SingleJudyReport):
            report = tool_cls(path, class_under_mutation)
        else:
            report = tool_cls(path)

        if report.class_under_mutation != class_under_mutation:
            raise ReportError(
                ERR_CLASS.format(
                    rep_cls=report.class_under_mutation, cls=class_under_mutation
                )
            )

        parsed_reports.append(report)

    return parsed_reports


# constants
TOOLS_CLASSES = {
    "judy": SingleJudyReport,
    "judylog": MultipleJudyReport,
    "jumble": JumbleReport,
    "major": MajorReport,
    "pit": PitReport,
}

HELP_PROJECT = "The report's Defects4J project"
HELP_BUG = "The project bug id; must be a numeric value"
HELP_TOOL = "The mutation tool to which the reports to study belong"
HELP_FILES = (
    "The space-separated list of reports to study;"
    " if working with a multiple files report, provide its directory"
)

ERR_NO_CMD = "Must provide a command to run!"
ERR_MULT_CLASSES = (
    "Can work with only one mutated class at time! "
    "This combination of project and bug have {n} "
    "modified classes, that are {l}"
)
ERR_CLASS = "Report class under mutation is {rep_cls}, but it should be {cls}!"
ERR_EXP_DIR = "Was expecting a directory, but found a file!"
ERR_EXP_FILE = "Was expecting a file, but found a directory!"
ERR_EXP_MULT_FILES = "Was expecting 2 or more files, but found {n}!"
ERR_EXP_NM_FILES = "Was expecting {n} files, but found {m}!"


if __name__ == "__main__":
    parser = argparse.ArgumentParser(add_help=False)

    # specify the arguments to parse Defects4J project and bug's modified classes
    parser.add_argument("-p", "--project", help=HELP_PROJECT, required=True)
    parser.add_argument(
        "-b", "--bug", help=HELP_BUG, type=check_bug_pattern, required=True
    )

    # specify the single tool to use
    parser.add_argument(
        "-t", "--tool", help=HELP_TOOL, choices=TOOLS_CLASSES.keys(), required=True
    )

    # specify the list of files to parse into reports
    parser.add_argument("files", help=HELP_FILES, nargs="+", type=pathlib.Path)

    # subparsers for commands
    parser2 = argparse.ArgumentParser()
    subparsers = parser2.add_subparsers(title="Commands", dest="command")
    subparsers.required = True

    for command in COMMANDS:
        cmd_parser = subparsers.add_parser(
            command.get_name().lower(), help=command.get_help(), parents=[parser]
        )
        for arg in command.get_arguments():
            cmd_parser.add_argument(*arg.flags, **arg.kwargs)

    # parse args
    args = parser2.parse_args()

    # get the reports
    _reports = get_reports(
        project=args.project, bug=args.bug, tool=args.tool, files=args.files
    )

    # get the selected command from args
    command_str = args.command.lower()

    # get the corresponding class
    command_cls = COMMANDS_BY_NAME[command_str]

    # and then the object, using the reports
    command = command_cls(_reports)

    # kwargs are automatically parsed from command arguments dest
    # that is their name in args_dict
    args_dict = vars(args)
    kwargs = {dest: args_dict[dest] for dest in command.get_arguments_dest()}

    # execute command
    command.execute(**kwargs)
