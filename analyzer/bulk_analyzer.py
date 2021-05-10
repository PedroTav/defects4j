import argparse
import logging

from src.analyzer.model import get_all_tools
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

file_handler = logging.FileHandler(".bulk.analyzer.log")
file_handler.setLevel(logging.INFO)

file_debug_handler = logging.FileHandler(".bulk.analyzer.debug.log")
file_debug_handler.setLevel(logging.DEBUG)

for handler in (stream_handler, file_handler, file_debug_handler):
    handler.setFormatter(formatter)
    logger.addHandler(handler)


def main():
    # assure we're in a good env
    test_environment()

    # define actions and sort them automatically
    actions = ("backup", "restore", "mutants", "coverage", "mutscore")
    actions = sorted(actions)

    # create argument parser
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "action", help="the action to perform with analyzer", choices=actions
    )
    parser.add_argument("path", help="path to Defects4j project")

    # parse user input
    args = parser.parse_args()

    logger.info(f"args are {args}")

    # create project from path provided
    project = Project(args.path)

    # get all tools
    tools = get_all_tools(project.filepath, project.relevant_class)

    # parse action from args
    action = args.action

    if action not in actions:
        raise ValueError(f"Invalid action provided: {action}. Valid are {actions}")

    # define size of delimiter
    SIZE = 100
    assert SIZE % 4 == 0, "Provide a SIZE multiple of 4"

    for i, tool in enumerate(tools):
        if i > 0:
            logger.info("-" * SIZE)
        logger.info(f"Working on tool {tool}")

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

                try:
                    if action == "mutants":
                        project.get_mutants(tool, **combination)
                    elif action == "mutscore":
                        project.get_mutation_scores(tool, **combination)
                    elif action == "coverage":
                        project.coverage(tool, **combination)
                    elif action == "backup":
                        project.backup_tests()
                    elif action == "restore":
                        project.restore_tests()
                except Exception as e:
                    logger.error(f"Error raised from execution: {e}")
                    c = combination
                    c.update(tool=str(tool))
                    logger.warning(f"Combination was {c}")


if __name__ == "__main__":
    main()
