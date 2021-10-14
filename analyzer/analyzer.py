import argparse
import logging
import pathlib

from analyzer.project import Project
from analyzer.tools import get_all_tools, get_tool
from analyzer.utility import test_environment

# set logging format
FORMAT = "%(asctime)s :: %(levelname)s :: [%(module)s.%(funcName)s.%(lineno)d] :: %(message)s"
formatter = logging.Formatter(FORMAT)

# get root logger
logger = logging.getLogger()
logger.setLevel(logging.DEBUG)

# create handlers
stream_handler = logging.StreamHandler()
stream_handler.setLevel(logging.INFO)

HERE = pathlib.Path(__file__).parent

file_handler = logging.FileHandler(HERE / "analyzer.log")
file_handler.setLevel(logging.INFO)

file_debug_handler = logging.FileHandler(HERE / "analyzer.debug.log")
file_debug_handler.setLevel(logging.DEBUG)

for handler in (stream_handler, file_handler, file_debug_handler):
    handler.setFormatter(formatter)
    logger.addHandler(handler)


def main():
    # assure we're in a good env
    test_environment()

    # define actions and sort them automatically
    actions = ("backup", "restore", "mutants", "run")
    actions = sorted(actions)

    # create argument parser
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "action", help="the action to perform with analyzer", choices=actions
    )
    parser.add_argument("path", help="path to Defects4j project")

    parser.add_argument("--tools", help="mutation tools to use", nargs="*")

    parser.add_argument(
        "-t",
        "--testsuite",
        help="test suite to use, can be a single Java file or a directory of Java files",
    )

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
        "--skip-setup",
        help="skip the setup of the tool (running actions against current testsuite)",
        action="store_true",
        default=False,
    )
    group = parser.add_mutually_exclusive_group()
    group.add_argument(
        "--all-dev",
        help="add original dev tests to testsuite",
        action="store_true",
        default=False,
    )
    group.add_argument(
        "--single-dev",
        help="add the single dev test to testsuite",
        action="store_true",
        default=False,
    )
    group.add_argument(
        "--relevant-dev",
        help="add the relevant dev tests to testsuite",
        action="store_true",
        default=False,
    )

    # parse user input
    args = parser.parse_args()

    # increase verbosity (debug logging)
    if args.verbose:
        stream_handler.setLevel(logging.DEBUG)

    logger.debug(f"args are {args}")

    # check if is a valid action
    action = str(args.action).lower()
    action_err = f"Invalid action provided: {action}. Valid actions are: {actions}"
    if action not in actions:
        parser.error(action_err)

    # create project from path provided
    project = Project(args.path)

    # specify mutation tools to use
    tools = []

    # if flag specified, select tools subset
    if args.tools:
        for tool in args.tools:
            tools.append(get_tool(tool, project.filepath, project.relevant_class))
    # otherwise take all tools
    else:
        tools = get_all_tools(project.filepath, project.relevant_class)

    kwargs = vars(args)

    action = args.action
    if action == "mutants":
        project.get_mutants(tools, **kwargs)
    elif action == "backup":
        project.backup_tests()
    elif action == "restore":
        project.restore_tests()
    elif action == "run":
        project.run_tools(tools, **kwargs)
    else:
        raise ValueError(action_err)


if __name__ == "__main__":
    main()
