import argparse
import logging

from src.analyzer.model import get_tool
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

    actions = ("mutants", "coverage")

    # create argument parser
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "action", help="the action to perform with analyzer", choices=actions
    )
    parser.add_argument("path", help="path to Defects4j project")

    parser.add_argument("--tools", help="mutation tools to use", nargs="*")
    parser.add_argument(
        "-v", "--verbose", help="increase verbosity", action="store_true", default=False
    )
    parser.add_argument(
        "--stdout", help="collect tools stdout", action="store_true", default=False
    )
    parser.add_argument(
        "--stderr", help="collect tools stderr", action="store_true", default=False
    )

    # parse user input
    args = parser.parse_args()

    # increase verbosity (debug logging)
    if args.verbose:
        stream_handler.setLevel(logging.DEBUG)

    # create project from path provided
    project = Project(args.path)

    # select all tools if None
    tools = None

    # if specified, select tools subset
    if args.tools:
        tools = []
        for tool in args.tools:
            tools.append(get_tool(tool, project.filepath))

    if args.action == "mutants":
        project.get_mutants(tools, stdout=args.stdout, stderr=args.stderr)
    elif args.action == "coverage":
        project.coverage(tools)
    else:
        raise ValueError(f"Invalid action provided: {args.action}. Valid are {actions}")


if __name__ == "__main__":
    main()
