import logging
import os
import pathlib
import subprocess
from typing import Union

logger = logging.getLogger(__file__)


def read_config(filepath: Union[str, os.PathLike], separator="=") -> dict:
    """Utility method to read config files"""
    with open(filepath) as f:
        lines = f.readlines()

    result = dict()
    for line in lines:
        # remove trailing whitespaces
        line = line.strip()

        # skip empty and comment lines
        if not line or line.startswith("#"):
            continue

        # split on the first separator
        splitted = line.strip().split(separator, maxsplit=1)

        # if we don't have two elements, skip to next line
        if len(splitted) < 2:
            continue
        # else parse result
        else:
            key, value = [el.strip() for el in splitted]
            result[key] = value
    return result


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


def defects4j_cmd(cmd: str = "", *args, **kwargs):
    """Utility function to call a Defects4j command"""
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
    command = ["defects4j"]
    if cmd:
        assert cmd in possible_cmds, "Invalid command provided for defects4j!"
        command += [cmd]
    command += list(args)

    stdout = None if kwargs.get("stdout") else subprocess.DEVNULL
    stderr = None if kwargs.get("stderr") else subprocess.DEVNULL

    kwargs = dict(stdout=stdout, stderr=stderr)

    logger.debug(f"Running {command}")
    return subprocess.run(command, **kwargs)


def test_environment():
    """Tests if the environment is correctly set,
    i.e. that Defects4j is installed into PATH"""
    try:
        defects4j_cmd()
        logger.debug("defects4j found in PATH")
    except FileNotFoundError:
        raise EnvironmentError("defects4j not found in PATH!")


def defects4j_cmd_dirpath(project_dir, command: str, *args, **kwargs):
    """Execute Defects4j command in the right folder"""
    old_path = os.getcwd()
    new_path = pathlib.Path(project_dir).resolve()
    logger.debug(f"Old path is {old_path}")
    logger.debug(f"New path is {new_path}")

    change_dir = old_path != str(new_path)
    logger.debug(f"Should change dir? {change_dir}")

    if change_dir:
        os.chdir(new_path)
    defects4j_cmd(command, *args, **kwargs)
    if change_dir:
        os.chdir(old_path)


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
