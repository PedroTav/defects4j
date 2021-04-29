import argparse
import logging

from src.analyzer.model import get_all_tools, get_tool
from src.analyzer.project import Project
from src.analyzer.utility import test_environment

# set logging format
FORMAT = "%(levelname)s :: [%(module)s.%(funcName)s.%(lineno)d] :: %(message)s"
formatter = logging.Formatter(FORMAT)

# get root logger
logger = logging.getLogger()
logger.setLevel(logging.DEBUG)

# create handlers
stream_handler = logging.StreamHandler()
stream_handler.setLevel(logging.INFO)

file_handler = logging.FileHandler(".analyzer.log", "w")
file_handler.setLevel(logging.INFO)

file_debug_handler = logging.FileHandler(".analyzer.debug.log", "w")
file_debug_handler.setLevel(logging.DEBUG)

for handler in (stream_handler, file_handler, file_debug_handler):
    handler.setFormatter(formatter)
    logger.addHandler(handler)


def main():
    # assure we're in a good env
    test_environment()

    actions = ("backup", "restore", "mutants", "coverage")

    # create argument parser
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "action", help="the action to perform with analyzer", choices=actions
    )
    parser.add_argument("path", help="path to Defects4j project")

    parser.add_argument("--tools", help="mutation tools to use", nargs="*")
    parser.add_argument("--group", help="students group's testsuite to use")
    parser.add_argument(
        "-v", "--verbose", help="increase verbosity", action="store_true", default=False
    )
    parser.add_argument(
        "--stdout", help="collect tools stdout", action="store_true", default=False
    )
    parser.add_argument(
        "--stderr", help="collect tools stderr", action="store_true", default=False
    )
    parser.add_argument(
        "--with-dev",
        help="add original dev tests to testsuite",
        action="store_true",
        default=False,
    )

    # parse user input
    args = parser.parse_args()

    # increase verbosity (debug logging)
    if args.verbose:
        stream_handler.setLevel(logging.DEBUG)

    # create project from path provided
    project = Project(args.path)

    # specify mutation tools to use
    tools = []

    # if flag specified, select tools subset
    if args.tools:
        for tool in args.tools:
            tools.append(get_tool(tool, project.filepath))
    # otherwise take all tools
    else:
        tools = get_all_tools(project.filepath)

    kwargs = dict(
        stdout=args.stdout, stderr=args.stderr, group=args.group, with_dev=args.with_dev
    )

    action = args.action
    if action == "mutants":
        project.get_mutants(tools, **kwargs)
    elif action == "coverage":
        project.coverage(tools, **kwargs)
    elif action == "backup":
        project.backup_tests()
    elif action == "restore":
        project.restore_tests()
    else:
        raise ValueError(f"Invalid action provided: {action}. Valid are {actions}")


if __name__ == "__main__":
    main()
