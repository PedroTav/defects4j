import hashlib
import xml.etree.ElementTree as ET
from abc import ABC
from collections import defaultdict

import pandas as pd


class Mutant(ABC):
    def __init__(self, line: int):
        # assert line >= 0
        self.line = line

    def hash_tuple(self) -> tuple:
        raise NotImplementedError

    def __hash__(self):
        s = b"_".join(str(e).encode("utf-8") for e in self.hash_tuple())
        h = hashlib.sha256(s)
        digest = int(h.hexdigest(), base=16)
        return digest

    def __eq__(self, other):
        return type(self) is type(other) and hash(self) == hash(other)

    def __repr__(self):
        return f"Mutant{self.hash_tuple()}"


class MutantWithCounter(Mutant):
    hash_counter = defaultdict(int)
    hash_count: int = None

    def get_hash_count(self):
        """Method to get the current hash count.
        If it's missing from the current object, assign it"""

        # the key is the reduced hash tuple
        key = self.hash_tuple_reduced()

        # we get its current count (defaults to 0)
        count = self.hash_counter[key]

        # if we don't have it associated to the object, assign it
        if self.hash_count is None:
            self.hash_count = count

        # and then increment this counter
        self.hash_counter[key] += 1
        return count

    def hash_tuple_reduced(self) -> tuple:
        raise NotImplementedError

    def hash_tuple(self) -> tuple:
        return self.hash_tuple_reduced() + (self.hash_count,)


class JudyMutant(MutantWithCounter):
    operator: str

    def __repr__(self):
        return "Judy" + super(JudyMutant, self).__repr__()

    def hash_tuple_reduced(self) -> tuple:
        return self.line, self.operator

    @classmethod
    def from_dict(cls, thedict: dict) -> "JudyMutant":
        operator = thedict["operators"][0]
        # points = thedict["points"][0]
        line = thedict["lines"][0]

        mutant = cls(int(line))
        mutant.operator = operator
        mutant.get_hash_count()

        return mutant


class JumbleMutant(MutantWithCounter):
    description: str

    def __repr__(self):
        return "Jumble" + super(JumbleMutant, self).__repr__()

    def hash_tuple_reduced(self) -> tuple:
        return self.line, self.description

    @classmethod
    def from_tuple(cls, thetuple: tuple) -> "JumbleMutant":
        classpath, line, description = thetuple
        mutant = cls(int(line))
        mutant.description = description
        mutant.get_hash_count()

        return mutant


class MajorMutant(MutantWithCounter):
    status: str
    operator: str
    original: str
    mutated: str
    signature: str
    description: str

    def __repr__(self):
        return "Major" + super(MajorMutant, self).__repr__()

    def hash_tuple_reduced(self) -> tuple:
        return (
            self.line,
            self.status,
            self.operator,
            self.original,
            self.mutated,
            self.signature,
            self.description,
        )

    @classmethod
    def from_series(cls, row: pd.Series) -> "MajorMutant":
        line = row.LineNumber
        mutant = cls(int(line))

        mutant.status = row.Status
        mutant.operator = row.Operator
        mutant.original = row.From
        mutant.mutated = row.To
        mutant.signature = row.Signature
        mutant.description = row.Description

        mutant.get_hash_count()

        return mutant


class PitMutant(Mutant):
    xml2py_bool = {"true": True, "false": False}

    # XML 'mutation' attributes
    detected: bool
    status: str

    # XML 'mutation' children
    mutated_class: str
    mutated_method: str
    method_description: str
    mutator: str
    description: str
    index: int
    block: int

    def __repr__(self):
        return "Pit" + super(PitMutant, self).__repr__()

    def hash_tuple(self) -> tuple:
        return (
            self.line,
            self.mutated_class,
            self.mutated_method,
            self.method_description,
            self.mutator,
            self.description,
            self.block,
        )

    @classmethod
    def from_xml_element(cls, element: ET.Element) -> "PitMutant":
        line = element.find("lineNumber").text
        mutant = cls(int(line))

        attribs = element.attrib
        mutant.detected = mutant.xml2py_bool[attribs["detected"]]
        mutant.status = attribs["status"]

        mutant.mutated_class = element.find("mutatedClass").text
        mutant.mutated_method = element.find("mutatedMethod").text
        mutant.method_description = element.find("methodDescription").text
        mutant.mutator = element.find("mutator").text
        mutant.description = element.find("description").text

        mutant.index = int(element.find("index").text)
        mutant.block = int(element.find("block").text)

        return mutant
