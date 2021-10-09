from abc import ABC
from typing import Any, List, Optional

import pandas as pd
from reports.reports import Report
from reports.utility import get_unique_substrings


class CommandError(Exception):
    """Base class for errors associated with a command"""


class TooFewReportsProvidedError(CommandError):
    """Error raised when X reports are required, but Y
    reports where provided, where Y < X"""


class NullListFoundInReportError(CommandError):
    """Error raised when a null list is met while
    working in a command; an empty list is fine"""


class NullMutantsFoundInBaseReportError(CommandError):
    """Error raised when a supposedly base report
    for commands like effectiveness has nan elements,
    meaning that there are mutants in other reports
    that do not appears in this base row"""


ERR_TOO_FEW = "Too few reports were provided!"
ERR_TOO_FEW_N = ERR_TOO_FEW + " Please provide at least {n} of them"

ERR_NULL_LIST = (
    "Found one or more null lists! "
    "Maybe this report doesn't allow the extraction of this kind of mutants?"
)

ERR_NULL_BASE_REPORT = (
    "Found one or more null mutants in base report!"
    "Other reports should be obtained with the base testsuite and one or more test added"
)


class Argument:
    def __init__(self, *flags: str, **kwargs):
        self.flags = flags
        self.kwargs = kwargs

    def __repr__(self):
        return f"{self.__class__.__name__}(flags={self.flags}, kwargs={self.kwargs})"

    def get_dest(self) -> str:
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

    def execute(self, *args, **kwargs) -> Any:
        """The main method to run; it will
        perform the desired command over the
        list of reports"""
        raise NotImplementedError

    def __repr__(self):
        return f"Command(reports={self.reports}"


class SummaryCommand(Command):
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
                "--full",
                help="Increase summary verbosity, printing killed and live mutants",
                action="store_true",
            )
        ]

    def execute(self, *args, **kwargs) -> str:
        print_mutants = kwargs.get("full", False)
        summaries = [rep.summary(print_mutants=print_mutants) for rep in self.reports]
        thestring = "\n\n".join(summaries)
        print(thestring)
        return thestring


class MutantsTableCommand(Command):
    @classmethod
    def get_name(cls) -> str:
        return "table"

    @classmethod
    def get_help(cls) -> Optional[str]:
        return "Get the mutants' table of the provided reports"

    @classmethod
    def get_arguments(cls) -> List[Argument]:
        return [
            Argument(
                "--killed",
                help="Use killed mutants instead of live mutants for computations",
                action="store_true",
            ),
            Argument(
                "-o",
                "--output",
                help="Where to write table in csv format; "
                "if missing, table will be printed to stdout",
            ),
        ]

    def get_table(self, use_killed_mutants: bool):
        """Utility method to get the mutations table;
        it can be used by other commands too as an
        intermediate product"""

        series_list = []
        for report in self.reports:
            thelist = (
                report.killed_mutants if use_killed_mutants else report.live_mutants
            )

            if thelist is None:
                raise NullListFoundInReportError(ERR_NULL_LIST)

            data = thelist
            hash_data = [mutant.hash_string() for mutant in data]
            hash_data_reduced = get_unique_substrings(
                hash_data, min_length=8, max_length=16
            )

            series = pd.Series(
                data=data, index=hash_data_reduced, name=report.hash_string()
            )
            series_list.append(series)

        df = pd.DataFrame(series_list).T
        df.columns = get_unique_substrings(
            df.columns.tolist(), min_length=8, max_length=16
        )
        df.index.name = "Mutant"

        return df

    def execute(self, *args, **kwargs) -> pd.DataFrame:
        # transform every report in series
        use_killed_mutants = kwargs.get("killed", False)

        df = self.get_table(use_killed_mutants=use_killed_mutants)

        output: str = kwargs.get("output")
        if output:
            if not output.endswith(".csv"):
                output += ".csv"
            df.to_csv(output)
        else:
            print(df)

        return df


class EffectivenessCommand(Command):
    @classmethod
    def get_name(cls) -> str:
        return "effectiveness"

    @classmethod
    def get_help(cls) -> Optional[str]:
        return (
            "Get the effectiveness of the reports"
            " using one as base; provides at least two reports."
            " Live mutants will be used for the calculations"
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
                "-o",
                "--output",
                help="Where to write table in csv format; "
                "if missing, table will be printed to stdout",
            ),
        ]

    def execute(self, *args, **kwargs) -> pd.DataFrame:
        n = len(self.reports)
        min_reports_count = 2
        if n < min_reports_count:
            raise TooFewReportsProvidedError(ERR_TOO_FEW_N.format(n=min_reports_count))

        # get the base table from other command - reuse code
        base_table = MutantsTableCommand(self.reports).get_table(
            use_killed_mutants=False
        )

        # if I'm here, the number of reports is ok,
        # so I get the index of the base report to use
        base_index = kwargs.get("base_index", 0)

        # clip negative values to 0
        base_index = max(base_index, 0)

        # clip bigger values to n
        base_index = min(base_index, n - 1)

        # take the base column from the parsed dataframe
        base_column: pd.Series = base_table.iloc[:, base_index]

        # precondition is that the number of unique mutants must be equal for every row
        # and that is true because nans will be added accordingly;
        # furthermore, to calculate the effectiveness, we mustn't have nans in base row,
        # because if there is a nan, there is a live mutant in report i that is not found in base
        if base_column.hasnans:
            raise NullMutantsFoundInBaseReportError(ERR_NULL_BASE_REPORT)
        else:
            total_count = base_column.count()

        # count elements on rows
        df = pd.DataFrame(base_table.count(), columns=["live_count"])
        df["live_total_count"] = total_count
        df["effectiveness"] = 1 - df["live_count"] / total_count

        # put again reports as columns
        # df = df.T

        # remove base column/report from table
        #
        # if trasponse is done, then the command
        # should be columns=... instead of index=...
        # df = df.drop(index=[base_column.name])

        output: str = kwargs.get("output")
        if output:
            if not output.endswith(".csv"):
                output += ".csv"
            df.to_csv(output)
        else:
            print(df)

        return df


COMMANDS = [SummaryCommand, MutantsTableCommand, EffectivenessCommand]
COMMANDS_BY_NAME = {cmd.get_name().lower(): cmd for cmd in COMMANDS}
