import logging
import pathlib
import subprocess
from typing import List

logger = logging.getLogger(__file__)


def bash_command(command: str, *args, **kwargs):
    """Utility function to run a bash command"""
    cmd = [command] + list(args)
    logger.debug(f"Running {cmd}")
    return subprocess.run(cmd, **kwargs)


def bash_script(script, capture_out=True, capture_err=True):
    """Utility function to run a bash script"""
    command = ["bash", script]

    stdout = None if capture_out else subprocess.DEVNULL
    stderr = None if capture_err else subprocess.DEVNULL

    logger.debug(
        f"Running {command} - Capture out? {capture_out} - Capture err? {capture_err}"
    )
    return subprocess.run(command, stdout=stdout, stderr=stderr)


def test_environment():
    """Tests if the environment is correctly set,
    i.e. that Defects4j is installed into PATH"""
    try:
        bash_command("defects4j", stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
        logger.debug("defects4j found in PATH")
    except FileNotFoundError:
        raise EnvironmentError("defects4j not found in PATH!") from None


def get_defects4j_root_path() -> pathlib.Path:
    """Get the root of current Defects4J installation"""
    test_environment()  # assure defects4j is present

    out = bash_command("which", "defects4j", capture_output=True)
    logger.debug(f"which defects4j: {out}")

    d4j_path = pathlib.Path(out.stdout.decode())
    logger.debug(f"d4j path: {d4j_path}")

    # defects4j is found in <ROOT>/framework/bin/defects4j
    root = d4j_path.parent.parent.parent
    logger.debug(f"root is {root}")

    return root


def get_defects4j_framework_path() -> pathlib.Path:
    """Get the <framework> folder in current Defects4J installation"""
    return get_defects4j_root_path() / "framework"


def get_defects4j_modified_classes(project: str, bug: str) -> List[str]:
    """Get the list of modified classes for provided
    project identifier and bug number"""
    projects = get_defects4j_framework_path() / "projects"
    path = projects / project / "modified_classes" / f"{bug}.src"
    logger.debug(f"Path to modified classes file: {path}")
    if not path.is_file():
        raise FileNotFoundError(
            f"Missing path: {path}!\nMaybe wrong project or bug provided?"
        )
    else:
        return open(path).read().splitlines(keepends=False)
