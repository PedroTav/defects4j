from abc import ABC
from typing import List, Optional

from reports.reports import Report


class Argument:
    def __init__(self, *flags: str, **kwargs):
        self.flags = flags
        self.kwargs = kwargs

    def get_dest(self):
        if self.kwargs.get("dest"):
            return self.kwargs["dest"]
        elif any(flag.startswith("--") for flag in self.flags):
            matches = [flag for flag in self.flags if flag.startswith("--")]
            return matches[0][2:].replace("-", "_")
        elif any(flag.startswith("-") for flag in self.flags):
            matches = [flag for flag in self.flags if flag.startswith("-")]
            return matches[0][1:].replace("-", "_")
        elif len(self.flags) == 1:
            return self.flags[0]
        else:
            raise ValueError(f"Cannot find dest for flags {self.flags}")


class Command(ABC):
    """The base interface for commands to perform
    on a list of reports. This interface serves
    also for generating automatically the subparsers
    and their arguments."""

    def __init__(self, reports: List[Report]):
        self.reports = reports

    @classmethod
    def get_name(cls) -> str:
        """The command name to use in main parser"""
        raise NotImplementedError

    @classmethod
    def get_help(cls) -> Optional[str]:
        """The help to associate to command"""
        return None

    @classmethod
    def get_arguments(cls) -> List[Argument]:
        """The list of arguments to associate to command"""
        return []

    @classmethod
    def get_arguments_dest(cls) -> List[str]:
        """Returns the destination of arguments, to use
        in parser when passing kwargs to command to execute"""
        return [arg.get_dest() for arg in cls.get_arguments()]

    def execute(self, *args, **kwargs):
        """The main method to run; it will
        perform the desired command over the
        list of reports"""
        raise NotImplementedError

    def __repr__(self):
        return f"Command(reports={self.reports}"


class SummaryCommand(Command):
    def __repr__(self):
        return "Summary" + super(SummaryCommand, self).__repr__()

    @classmethod
    def get_name(cls):
        return "summary"

    @classmethod
    def get_help(cls) -> Optional[str]:
        return "For each report print its summary, then exit"

    @classmethod
    def get_arguments(cls) -> List[Argument]:
        return [
            Argument(
                "-v",
                "--verbose",
                help="Increase summary verbosity, printing killed and live mutants",
                action="store_true",
            )
        ]

    def execute(self, *args, **kwargs):
        print_mutants = kwargs.get("verbose", False)
        summaries = [rep.summary(print_mutants=print_mutants) for rep in self.reports]
        thestring = "\n\n".join(summaries)
        print(thestring)
        return thestring


COMMANDS = [SummaryCommand]
COMMANDS_BY_NAME = {cmd.get_name().lower(): cmd for cmd in COMMANDS}
