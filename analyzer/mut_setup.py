import argparse
import logging
import os
import pathlib
import re
import shutil
import subprocess
import threading
import time
from pathlib import Path

try:
    subprocess.run(["defects4j"], stdout=subprocess.DEVNULL)
except FileNotFoundError:
    raise OSError("defects4j not found in path!")

FORMAT = "%(levelname)s :: [%(module)s.%(funcName)s.%(lineno)d] :: %(message)s"

subjects = ["cli", "gson", "lang"]
tools = ["judy", "jumble", "major", "pit"]

parser = argparse.ArgumentParser()

parser.add_argument("subject", help="The subject of interest", choices=subjects)
parser.add_argument(
    "version", help="The version of the subject", choices=("buggy", "fixed")
)
parser.add_argument(
    "action", help="The action to perform", choices=("coverage", "mutants")
)
parser.add_argument(
    "--stdout",
    help="Wether to capture or not tools stdout",
    action="store_true",
    default=False,
)
parser.add_argument(
    "--stderr",
    help="Wether to capture or not tools stderr",
    action="store_true",
    default=False,
)
parser.add_argument("-v", "--verbose", action="store_true", default=False)
parser.add_argument("-t", "--tool", choices=tools)

args = parser.parse_args()

subject = args.subject
version = args.version
action = args.action

_tool = args.tool

if _tool:
    tools = [_tool]


if args.verbose:
    level = logging.DEBUG
else:
    level = logging.INFO
logging.basicConfig(format=FORMAT, level=level)

subjects_dict = {
    "cli": {
        "package": "org/apache/commons/cli",
        "class": "HelpFormatter",
        "test": "src/test/java",
        "test_class": "target/test-classes",
        "name": "cli32",
        "tests": "cli_tests",
    },
    "gson": {
        "package": "com/google/gson/stream",
        "class": "JsonWriter",
        "test": "gson/src/test/java",
        "test_class": "target/test-classes",
        "name": "gson15",
        "tests": "gson_tests",
    },
    "lang": {
        "package": "org/apache/commons/lang/time",
        "class": "DateUtils",
        "test": "src/test",
        "test_class": "target/tests",
        "name": "lang53",
        "tests": "lang_tests",
    },
}

subject_dict = subjects_dict[subject]
home = Path().resolve()

if args.version == "buggy":
    version = "b"
elif args.version == "fixed":
    version = "f"
else:
    raise ValueError(args.version)

name = f"{subject}{version}".upper()
logging.info(f"WORKING ON {name}")

subject_dir = Path(f"{subject_dict['name']}{version}")
subject_test_own_dir = Path(subject_dict["tests"])

assert all(p.exists() for p in (subject_dir, subject_test_own_dir))

# get test root, where to place other files
subject_test_dir = subject_dir / subject_dict["test"]
java_package_path = subject_dict["package"]

# get package as Java one
java_package = java_package_path.replace("/", ".")

class_under_mutation = subject_dict["class"]


def defects4j_cmd(cmd: str, *other_args, change_dir=True, **kwargs):
    possible_cmds = (
        "bids",
        "checkout",
        "compile",
        "coverage",
        "env",
        "export",
        "info",
        "monitor.test",
        "mutation",
        "pids",
        "query",
        "test",
    )
    assert cmd in possible_cmds
    sub_cmd = ["defects4j", cmd]
    for arg in other_args:
        sub_cmd += [arg]

    logging.debug(f"Running {sub_cmd}")

    if change_dir:
        os.chdir(subject_dir)
        logging.debug(f"Changed dir to {subject_dir.resolve()}")
    subprocess.run(sub_cmd, **kwargs)
    if change_dir:
        os.chdir(home)
        logging.debug(f"Changed dir to {home.resolve()}")


# if test root already exists, move it to prevent deletion
def backup_test_dir():
    if subject_test_dir.exists():
        src = os.fspath(subject_test_dir)
        dst = os.fspath(subject_test_dir.with_name(f"{subject_test_dir.name}_backup"))
        try:
            shutil.move(src, dst)
        except shutil.Error:  # backup already made
            pass


def set_tool_as_test(_tool):
    logging.debug(f"Tool is {_tool}")
    # 1 copy java tests in test_root/../<tool>/
    tool_test = subject_test_dir.parent / _tool
    logging.debug(f"Tool test is {tool_test}")

    shutil.rmtree(tool_test, ignore_errors=True)
    shutil.copytree(subject_test_own_dir / _tool, tool_test)

    # 2 remove old test dir
    final_test_dir = subject_test_dir / java_package_path
    logging.debug(f"Final test dir is {final_test_dir}")
    shutil.rmtree(final_test_dir, ignore_errors=True)

    # 3 copy these tests in correct package
    # (copytree will create also directories)
    logging.debug(f"Copytree from {tool_test} to {final_test_dir}")
    shutil.copytree(tool_test, final_test_dir)


def get_students_name():
    subject_tool_test_dir = subject_test_dir / java_package_path
    java_files = list(subject_tool_test_dir.glob("*.java"))
    logging.debug(f"Java files inside {subject_tool_test_dir}: {java_files}")
    try:
        return [re.search(r"\w\d+", java_file.name).group() for java_file in java_files]
    except AttributeError:
        logging.warning("No student name found!")
        return []


def generate_xml(_tool):
    # change dir for running defects4j cmd
    defects4j_cmd("coverage")


def move_xml(_tool, student_names=None):
    _src = subject_dir / "coverage.xml"
    dst_name = f"coverage_{subject_dir.stem}_{_tool}"
    if student_names:
        dst_name += "_" + "_".join(student_names)
    dst_name += ".xml"
    _dst = subject_dir / dst_name
    shutil.move(_src, _dst)


def coverage():
    logging.info("COVERAGE START")

    backup_test_dir()

    for tool in tools:
        logging.info("-" * 70)
        logging.info(f"Working on tool {tool.upper()}")

        set_tool_as_test(tool)
        names = get_students_name()
        generate_xml(tool)
        move_xml(tool, student_names=names)


def run_tool(
    tool, *, prefix="", suffix="", extension=".sh", stdout=False, stderr=False
):
    logging.info(f"Running tool {tool}")

    stdout = subprocess.DEVNULL if not stdout else None
    stderr = subprocess.DEVNULL if not stderr else None

    if tool != "major":
        tool_sh = f"{prefix}{tool}{suffix}{extension}"
        cmd = f"./{tool_sh}"

        logging.debug(f"Tool sh: {tool_sh}")
        subprocess.run([cmd], stdout=stdout, stderr=stderr)
    else:
        logging.debug("Running defects4j mutation...")
        defects4j_cmd("mutation", change_dir=False, stdout=stdout, stderr=stderr)


def setup_tool(tool: str, *, prefix="", suffix="", ext=".sh", **kwargs):
    if tool == "major":
        logging.info("Major doesn't have a sh helper script")
        return

    tool_sh = f"{prefix}{tool}{suffix}{ext}"

    if not Path(tool_sh).exists():
        err = f"File not found: {tool_sh}"
        logging.error(err)
        raise FileNotFoundError(err)

    src = Path(tool_sh)
    dst = subject_dir / tool_sh
    logging.debug(f"Copying {src.resolve()} to {dst.resolve()}")
    shutil.copy(tool_sh, dst)

    # judy doesn't have filters
    if tool == "judy":
        return

    # copy other files for jumble
    if tool == "jumble":
        jumble_parser = ".jumble_parser.sh"
        _src = Path(jumble_parser)
        _dst = subject_dir / jumble_parser
        logging.debug(f"Copying {_src.resolve()} to {_dst.resolve()}")
        shutil.copy(_src, _dst)

    TESTS = kwargs["TESTS"]
    CLASS = kwargs["CLASS"]

    if tool == "jumble":
        test_repl = "<REPLACE_TESTS>"
        cls_repl = "<REPLACE_CLASS>"
    elif tool == "pit":
        test_repl = "<TEST_REGEXP>"
        cls_repl = "<CLASS_REGEXP>"
    else:
        raise NotImplementedError("This line should not be reached")

    logging.info("Replacing placeholder tags with actual values")
    logging.info(f"tests: {TESTS} - class: {CLASS}")

    with open(dst) as f:
        content = f.read()
    content_fixed = content.replace(test_repl, TESTS).replace(cls_repl, CLASS)
    with open(dst, "w") as f:
        f.write(content_fixed)


def get_output(tool: str, base_dir="."):
    prefix = f"{args.version}_"

    output_list = {
        "judy": ["result.json"],
        "jumble": ["jumble_output.txt"],
        "major": ["kill.csv", "mutants.log"],
        "pit": ["mutations.xml"],
    }
    tool_output_dir = pathlib.Path(base_dir)

    if tool == "pit":
        tool_output_dir /= "pit_report"

    tool_outputs = [tool_output_dir / out for out in output_list[tool]]

    # create output dir for files
    output_dir = home / "mut_setup_output" / subject_dict["name"] / tool
    os.makedirs(output_dir, exist_ok=True)

    for tool_out in tool_outputs:
        logging.debug(f"Retrieving file {tool_out} ...")
        assert tool_out.exists(), f"File doesn't exist! - {tool_out.resolve()}"
        shutil.copy(
            tool_out, output_dir / tool_out.with_name(f"{prefix}{tool_out.name}").name
        )
        logging.info(f"File {tool_out} copied in {output_dir}")


def generate_test_classes(delete_on_exist=True):
    logging.info("Generating classes...")
    test_classes = subject_dir / subject_dict["test_class"]
    if test_classes.exists() and delete_on_exist:
        shutil.rmtree(test_classes)
    defects4j_cmd("compile")


class Worker(threading.Thread):
    def __init__(self, tool, **kwargs):
        super(Worker, self).__init__()
        self.tool = tool
        self.kwargs = kwargs

    def run(self):
        run_tool(self.tool, **self.kwargs)
        logging.info(f"[THREAD {self.tool}] Execution finished")
        get_output(self.tool)
        logging.info(f"[THREAD {self.tool}] Get output finished")


def mutants():
    logging.info("MUTANTS START")

    backup_test_dir()
    set_tool_as_test("dummy")
    generate_test_classes()

    testclass = f"{subject.upper()}_DUMMY_TEST"
    testclass_with_package = ".".join([java_package, testclass])
    class_with_package = ".".join([java_package, class_under_mutation])

    tool_kwargs = {
        "judy": {},
        "jumble": {
            "TESTS": testclass_with_package,
            "CLASS": class_with_package,
        },
        "major": {},
        "pit": {
            "TESTS": testclass_with_package,
            "CLASS": class_with_package,
        },
    }

    threads = []

    for tool in tools:
        logging.info(f"Setupping {tool} inside {subject_dir}")
        setup_tool(tool, prefix="dummy_", **tool_kwargs[tool])

    # must do os operations at process level
    os.chdir(subject_dir)

    # spawn a thread for every tool
    for tool in tools:
        logging.info(f"Running {tool} in a separate thread")
        time.sleep(1)
        worker = Worker(tool, prefix="dummy_", stdout=args.stdout, stderr=args.stderr)
        worker.start()
        threads.append(worker)

    # join them
    for thread in threads:
        thread.join()


if args.action == "coverage":
    coverage()
elif args.action == "mutants":
    mutants()
else:
    raise NotImplementedError
