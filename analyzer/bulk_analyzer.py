import argparse
import logging
import pathlib

from analyzer.model import get_all_tools, get_tool
from analyzer.project import Project
from analyzer.utility import test_environment

# set logging format
FORMAT = "%(levelname)s :: [%(module)s.%(funcName)s.%(lineno)d] :: %(message)s"
formatter = logging.Formatter(FORMAT)

# get root logger
logger = logging.getLogger()
logger.setLevel(logging.DEBUG)

# create handlers
stream_handler = logging.StreamHandler()
stream_handler.setLevel(logging.INFO)

HERE = pathlib.Path(__file__).parent

file_handler = logging.FileHandler(HERE / "bulk.analyzer.log")
file_handler.setLevel(logging.INFO)

file_debug_handler = logging.FileHandler(HERE / "bulk.analyzer.debug.log")
file_debug_handler.setLevel(logging.DEBUG)

for handler in (stream_handler, file_handler, file_debug_handler):
    handler.setFormatter(formatter)
    logger.addHandler(handler)


def execute(action: str, project: Project, tool: str, **kwargs):
    if action == "mutants":
        return project.get_mutants(tool, **kwargs)
    elif action == "mutscore":
        return project.get_mutation_scores(tool, **kwargs)
    elif action == "coverage":
        return project.coverage(tool, **kwargs)
    elif action == "backup":
        return project.backup_tests()
    elif action == "restore":
        return project.restore_tests()
    elif action == "killed":
        return project.get_killed_mutants(tool, **kwargs)


def main():
    # assure we're in a good env
    test_environment()

    # define actions and sort them automatically
    actions = ("backup", "restore", "mutants", "coverage", "mutscore", "killed")
    actions = sorted(actions)

    # create argument parser
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "action", help="the action to perform with analyzer", choices=actions
    )
    parser.add_argument("path", help="path to Defects4j project")

    # optional args
    parser.add_argument("--tools", help="mutation tools to use", nargs="*")
    parser.add_argument(
        "-v", "--verbose", help="increase verbosity", action="store_true"
    )
    parser.add_argument("--stdout", help="collect tools stdout", action="store_true")
    parser.add_argument("--stderr", help="collect tools stderr", action="store_true")

    # parse user input
    args = parser.parse_args()

    logger.info(f"args are {args}")

    if args.verbose:
        stream_handler.setLevel(logging.DEBUG)

    # create project from path provided
    project = Project(args.path)

    # if flag specified, select tools subset
    if args.tools:
        tools = []
        for tool in args.tools:
            tools.append(get_tool(tool, project.filepath, project.relevant_class))
    # otherwise take all tools
    else:
        tools = get_all_tools(project.filepath, project.relevant_class)

    # parse action from args
    action = args.action

    # check if is a valid action
    action = str(args.action).lower()
    action_err = f"Invalid action provided: {action}. Valid actions are: {actions}"
    if action not in actions:
        parser.error(action_err)

    # create kwargs
    kwargs = dict(
        stdout=args.stdout,
        stderr=args.stderr,
    )

    # define size of delimiter
    SIZE = 100
    assert SIZE % 4 == 0, "Provide a SIZE multiple of 4"

    for i, tool in enumerate(tools):
        if i > 0:
            logger.info("-" * SIZE)
        logger.info(f"Working on tool {tool}")

        logger.info("Execution dev only")
        execute(action, project, tool, **kwargs)
        logger.info("-" * (SIZE // 2))

        student_groups = list(project.get_student_names(tool))
        logger.info(f"Found groups {student_groups}")

        for j, group in enumerate(student_groups):
            if j > 0:
                logger.info("-" * (SIZE // 2))
            combinations = [
                dict(group=group, with_dev=False),
                dict(group=group, with_dev=True),
            ]
            for k, combination in enumerate(combinations):
                if k > 0:
                    logger.info("-" * (SIZE // 4))
                logger.info(f"Combination under test: {combination}")

                kwargs_copy = kwargs.copy()
                kwargs_copy.update(combination)
                logger.debug(f"Kwargs passed: {kwargs_copy}")

                try:
                    execute(action, project, tool, **kwargs_copy)
                except Exception as e:
                    logger.error(f"Error raised from execution: {e}")
                    c = combination
                    c.update(tool=str(tool))
                    logger.warning(f"Combination was {c}")


if __name__ == "__main__":
    main()
