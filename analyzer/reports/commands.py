from abc import ABC
from typing import List, Optional

import pandas as pd
from reports.reports import Report


class CommandError(Exception):
    """Base class for errors associated with a command"""


class TooFewReportsProvidedError(CommandError):
    """Error raised when X reports are required, but Y
    reports where provided, where Y < X"""


ERR_TOO_FEW = "Too few reports were provided!"
ERR_TOO_FEW_N = ERR_TOO_FEW + " Please provide at least {n}"


class NullListFoundInReportError(CommandError):
    """Error raised when a null list is met while
    working in a command; an empty list is fine"""


ERR_NULL_LIST = (
    "Found one or more null lists! "
    "Maybe this report doesn't allow the extraction of this kind of mutants?"
)


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
    def get_name(cls) -> str:
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


class EffectivenessCommand(Command):
    def __repr__(self):
        return "Effectiveness" + super(EffectivenessCommand, self).__repr__()

    @classmethod
    def get_name(cls) -> str:
        return "effectiveness"

    @classmethod
    def get_help(cls) -> Optional[str]:
        return (
            "Get the effectiveness of the reports"
            " using one as base; provides at least two reports"
        )

    @classmethod
    def get_arguments(cls) -> List[Argument]:
        return [
            Argument(
                "--base-index",
                help="The zero-based index of the report to use as base in "
                "the list of provided reports. "
                "If missing or negative, the first will be used;"
                "if too big, the last will be used",
                type=int,
                default=0,
            ),
            Argument(
                "--killed",
                help="Use killed mutants instead of live mutants for computations",
                action="store_true",
            ),
        ]

    def execute(self, *args, **kwargs):
        n = len(self.reports)
        min_reports_count = 2
        if n < min_reports_count:
            raise TooFewReportsProvidedError(ERR_TOO_FEW_N.format(n=min_reports_count))

        # if I'm here, the number of reports is ok,
        # so I get the index of the base report to use
        base_index = kwargs.get("base_index", 0)

        # clip negative values to 0
        base_index = max(base_index, 0)

        # clip bigger values to n
        base_index = min(base_index, n - 1)

        # take base report
        _ = self.reports[base_index]

        # transform every report in series
        use_killed_mutants = kwargs.get("killed", False)

        series_list = []
        for report in self.reports:
            thelist = (
                report.killed_mutants if use_killed_mutants else report.live_mutants
            )

            if thelist is None:
                raise NullListFoundInReportError(ERR_NULL_LIST)

            data = thelist
            hash_data = [hash(mutant) for mutant in data]

            series = pd.Series(data=data, index=hash_data, name=hash(report))
            series_list.append(series)

        df = pd.DataFrame(series_list)
        df.index.name = "Report hash"
        print(df)


COMMANDS = [SummaryCommand, EffectivenessCommand]
COMMANDS_BY_NAME = {cmd.get_name().lower(): cmd for cmd in COMMANDS}
