import base64
import logging
import pathlib
import subprocess
from typing import List, Optional

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


def get_base64(astring: str) -> str:
    """Converts a string to its base64 version"""
    return base64.b64encode(astring.encode("utf-8")).decode("utf-8")


def get_unique_substrings(
    strings: List[str],
    min_length: Optional[int] = None,
    max_length: Optional[int] = None,
    on_equal: str = "ignore",
) -> List[str]:
    """Get the unique starting substring for a list of strings;
    this method is useful for hash values, when you want to reduce
    their size without losing context of which hash they are.

    min_length is the minimum length desired for the substrings to
    be returned

    on_equal specifies what to do in case of equal strings; the
    default behaviour is to 'ignore' the problem, otherwise an error
    can also be raised with 'raise'"""

    on_equal = on_equal.lower()
    if on_equal not in ("ignore", "raise"):
        raise ValueError("Invalid on_equal value provided!")

    max_len = min(len(s) for s in strings)
    if max_len < 1:
        raise ValueError("Cannot get unique substrings for empty strings")
    characters_needed = 1
    substrings = [s[:characters_needed] for s in strings]

    while len(substrings) != len(set(substrings)) and characters_needed <= max_len:
        characters_needed += 1
        substrings = [s[:characters_needed] for s in strings]

    if characters_needed > max_len and on_equal == "raise":
        raise ValueError("Strings are equal!")

    if not min_length:
        min_length = 0
    if not max_length:
        max_length = max_len

    if max_length < min_length:
        min_length, max_length = max_length, min_length

    if characters_needed < min_length:
        limit = min_length
    elif characters_needed > max_length:
        limit = max_length
    else:  # min_length <= characters_needed <= max_length
        limit = characters_needed

    substrings = [s[:limit] for s in strings]
    return substrings
