import enum
import logging
import os
import pathlib
import re
import shutil
from typing import Sequence, Union

from analyzer import tools, utility

logger = logging.getLogger(__name__)


class BugStatus(enum.Enum):
    """Status of a checkout project's bug"""

    BUGGY = "b"
    FIXED = "f"


class Project:
    """Interface of a Defects4j Project"""

    default_backup_tests = "dev_backup"

    dummy_test_template = """
package {package};

import junit.framework.TestCase;

public class {classname} extends TestCase {{
    public void testDummy() {{
        // do nothing
    }}
}}
""".strip()

    def __init__(self, filepath: Union[str, os.PathLike]):
        """Create a (Defects4j compatible) Project"""
        self.filepath = pathlib.Path(filepath)

        config = self.read_defects4j_config()
        self.name = config["pid"]

        match = re.match(r"(\d+)(\w+)", config["vid"])
        if not match:
            raise ValueError("Missing bug identifier from config!")

        self.bug, bug_status = match.groups()
        if bug_status == "b":
            self.bug_status = BugStatus.BUGGY
        elif bug_status == "f":
            self.bug_status = BugStatus.FIXED
        else:
            raise ValueError(f"Invalid bug status found in config, found: {bug_status}")

        properties = self.read_defects4j_build_properties()
        self.relevant_class = properties["d4j.classes.relevant"]
        self.test_dir = self.filepath / properties["d4j.dir.src.tests"]
        self.package = ".".join(self.relevant_class.split(".")[:-1])
        package_path = self.package.replace(".", "/")
        self.full_test_dir = self.test_dir / package_path

        # agreement that testclasses are in the format package.to.ClassTest
        self.test_class = str(self.relevant_class) + "Test"

        logger.debug(f"relevant_class is {self.relevant_class}")
        logger.debug(f"test_dir is {self.test_dir}")
        logger.debug(f"package is {self.package}")
        logger.debug(f"full_test_dir is {self.full_test_dir}")

        # make a backup of tests at the end of init phase
        self.backup_tests()

    @property
    def test_classes(self) -> list:
        """Retrieve the relevant test classes (as found in D4J framework dir)
        given a Project name"""
        root = utility.get_defects4j_root_path()
        path = root / "framework" / "projects" / self.name / "relevant_tests" / self.bug
        tests = open(path).read().split()
        return tests

    def __repr__(self):
        return f"{self.name} {self.bug}{self.bug_status.value} [fp: {self.filepath}]"

    def read_defects4j_build_properties(self) -> dict:
        """Read defects4j.build.properties as key-value dictionary"""
        return utility.read_config(self.filepath / "defects4j.build.properties")

    def read_defects4j_config(self) -> dict:
        """Read .defects4j.config as key-value dictionary"""
        return utility.read_config(self.filepath / ".defects4j.config")

    def backup_tests(self, name=default_backup_tests):
        """Backup original/dev tests"""
        src = os.fspath(self.test_dir)
        dst = self.test_dir.with_name(name)
        if dst.is_dir():
            logger.debug(f"Backup already made to {dst}")
        else:
            dst = os.fspath(dst)
            shutil.move(src, dst)
            logger.debug(f"Backupped tests to {dst}")

    def restore_tests(self, name=default_backup_tests):
        """Restore original/dev tests"""
        src = os.fspath(self.test_dir.with_name(name))
        dst = os.fspath(self.test_dir)
        shutil.move(src, dst)
        logger.info(f"Restored tests to {dst}")

    def set_testsuite(self, **kwargs):
        """Method to set the testsuite for an execution.

        If skip_setup is true, then the function will exit immediately.

        If testsuite is set to a valid path, then it will be used as test suite:
            if it's a file, it will be placed in full_test_dir
            if it's a directory, then every java file will be placed in full_test_dire

        If all_dev, relevant_dev and single_dev are true (only one because of mutually
        exclusivity in interface), then respectively
            all developer tests;
            only the D4J relevant tests;
            the single relevant class test
        will be placed in test_dir
        """

        skip = kwargs.get("skip_setup")
        if skip:
            logger.info("Skipping setup of testsuite...")
            return
        else:
            shutil.rmtree(self.test_dir, ignore_errors=True)
            logger.debug("Old test dir removed")

            os.makedirs(self.full_test_dir)
            logger.debug("Created empty test dir up to class package")

        dummy = kwargs.get("dummy")
        if dummy:
            # create ad-hoc empty test class for current project
            clsname = f"{self.name}DummyTest"
            clsfile = f"{clsname}.java"
            dst = self.full_test_dir / clsfile

            with open(dst, "w") as fp:
                fp.write(
                    self.dummy_test_template.format(
                        package=self.package,
                        classname=clsname,
                    )
                )

            logger.info(f"{clsfile} was created and inserted in current test suite")

        testsuite = kwargs.get("testsuite")
        if testsuite:
            # check if provided path exists
            path = pathlib.Path(testsuite)
            if not path.exists():
                raise FileNotFoundError(path)

            # path exists
            if path.is_file():
                files = [path]
            elif path.is_dir():
                files = list(path.glob("*.java"))
            else:
                raise NotImplementedError("This line should never be executed")

            # for each file, put it in right folder
            for file in files:
                # destination is the full test dir, or
                # the relevant class package structure in the test dir
                dst = self.full_test_dir / file.name

                # then copy the test file in the correct spot
                shutil.copyfile(file, dst)

            if path.is_file():
                msg = f"{path.name} was inserted in current test suite"
            else:
                msg = "All test files were inserted in current test suite"
            logger.info(msg)

        all_dev = kwargs.get("all_dev")
        relevant_dev = kwargs.get("relevant_dev")
        single_dev = kwargs.get("single_dev")

        if all_dev:
            dev_test = self.test_dir.parent / self.default_backup_tests
            logger.debug(f"Dev test: {dev_test}")
            if dev_test.exists():
                shutil.copytree(dev_test, self.test_dir, dirs_exist_ok=True)
                logger.info(f"All dev tests copied into {self.test_dir}")
            else:
                msg = f"Dev tests backup doesn't exist! Did you removed it from {self.test_dir}?"
                logger.error(msg)
        elif relevant_dev:
            # get the list of relevant testsuites
            for i, test in enumerate(self.test_classes):
                testfile = test.replace(".", "/") + ".java"
                src_path = self.test_dir.parent / self.default_backup_tests / testfile
                dst_path = (pathlib.Path(self.test_dir) / testfile).parent

                logger.debug(f"src no {i} is {src_path.name}")
                os.makedirs(dst_path, exist_ok=True)
                shutil.copy(os.fspath(src_path), os.fspath(dst_path))

            logger.info("Relevant developer tests were inserted in current test suite")
        elif single_dev:
            src = (
                self.test_dir.parent
                / self.default_backup_tests
                / self.test_class.replace(".", "/")
            )
            src = src.with_suffix(".java")
            logger.debug(f"src is {src.resolve()}")

            dst = self.full_test_dir
            logger.debug(f"dst is {dst.resolve()}")

            shutil.copy(src, dst)
            logger.info("Single developer test was inserted in current test suite")
        else:
            logger.info("No developer test was inserted in current test suite")

    def get_tests(self, filter_out_nontest: bool = True) -> Sequence[str]:
        """Return a list of tests in current testsuite with Java format,
        i.e. package.to.TestClass"""

        # get old cwd
        cwd = pathlib.Path().resolve()

        # change dir to test dir root
        os.chdir(self.test_dir)

        # get tests as glob of all java files here (rglob recursive)
        tests = list(pathlib.Path().rglob("*.java"))
        logger.debug(f"Found {len(tests)} tests inside {self.test_dir}")

        if filter_out_nontest:
            _tests = []
            logger.debug("Filtering out non-tests")

            # abstract classes are forbidden

            junit4_match = r"\s*@Test\(?.*\)?\s+(public|private)?\s*void\s+\w+\(.*\)"
            junit4_pattern = re.compile(junit4_match, re.M)

            for testfile in tests:
                content = open(testfile).read()
                # in Java filename and class declaration must match
                classname = testfile.stem

                junit3_match = (
                    r"^(public|public\s+final|final\s+public)\s+"
                    rf"class\s+{classname}\s+extends\s+\w*Test\w*"
                )

                match3 = re.search(junit3_match, content, re.M)
                match4 = junit4_pattern.search(content)

                if any(match for match in (match3, match4)):
                    _tests.append(testfile)
                else:
                    logger.debug(f"{classname} is not an actual testclass")
            tests = _tests

        # convert from package/to/Test.java to package.to.Test
        tests = [str(test).replace("/", ".").replace(".java", "") for test in tests]

        # change dir to old cwd
        os.chdir(cwd)

        return tests

    def _execute_defects4j_cmd(self, command: str, *args, **kwargs):
        """Execute Defects4j command in the right folder"""
        return utility.defects4j_cmd_dirpath(self.filepath, command, *args, **kwargs)

    def d4j_test(self):
        """Execute defects4j test"""
        return self._execute_defects4j_cmd("test")

    def clean(self):
        """Remove compiled files (both src and tests)"""
        target = self.filepath / "target"
        if target.exists():
            shutil.rmtree(os.fspath(target))
            logger.debug("Cleaned project")
        else:
            logger.debug("Project was already clean")

    def d4j_compile(self, **kwargs):
        """Execute defects4j compile"""
        return self._execute_defects4j_cmd("compile", **kwargs)

    def d4j_coverage(self, **kwargs):
        """Execute defects4j coverage"""
        return self._execute_defects4j_cmd("coverage", **kwargs)

    def _get_tools(self, tools_list: Union[tools.Tool, Sequence[tools.Tool]] = None):
        # if None, take every tool
        if tools_list is None:
            tools_list = tools.get_all_tools(self.filepath, self.relevant_class)

        # if one tool is given, create list
        if isinstance(tools_list, tools.Tool):
            tools_list = [tools_list]

        return tools_list

    @staticmethod
    def _run_tool(tool: tools.Tool, **kwargs):
        # execute tool
        logger.debug(f"{tool} kwargs: {kwargs}")

        logger.info(f"Setupping {tool}...")
        tool.setup(**kwargs)
        logger.info("Setup completed")

        logger.info(f"Running {tool}...")
        tool.run(**kwargs)
        logger.info("Execution completed")

        logger.info("Collecting output...")
        tool.get_output()
        logger.info("Output collected")

    def run_tools(
        self, tools_list: Union[tools.Tool, Sequence[tools.Tool]] = None, **kwargs
    ):
        """Run the specified tools against the current testsuite.
        If 'tools_list' is None, every tool will be selected.
        """
        tools_list = self._get_tools(tools_list)
        logger.info(f"Executing run_tools on tools {tools_list}")

        if not tools_list:
            logger.warning("No mutation tool specified, exit...")
            return

        self.set_testsuite(**kwargs)

        for tool in tools_list:
            logger.info(f"Start run_tools for {tool}")

            # clean and compile the testsuite again
            self.clean()
            self.d4j_compile(**kwargs)

            # must specify tests and class for replacement of dummy text
            # inside bash scripts
            if isinstance(tool, (tools.Jumble, tools.Pit, tools.Judy)):
                # class under mutation name is project relevant class
                class_under_mutation = self.relevant_class
                tests = ""

                # if I have a Jumble tool, I must specify the list of all tests
                if isinstance(tool, tools.Jumble):
                    tests = " ".join(self.get_tests())
                # if I have a Pit tool, I must specify the regex of all tests
                elif isinstance(tool, tools.Pit):
                    tests = "*Test*"
                # if I have a Judy tool, I can specify the list class under mutation
                elif isinstance(tool, tools.Judy):
                    class_under_mutation = class_under_mutation.replace(".", "/")

                kwargs.update(
                    {
                        "tests": tests,
                        "class": class_under_mutation,
                    }
                )

            self._run_tool(tool, **kwargs)

    def get_mutants(
        self, tools_list: Union[tools.Tool, Sequence[tools.Tool]] = None, **kwargs
    ):
        """Get all mutants generated by the selected tools.
        If 'tools_list' is None, every tool will be selected.
        """
        tools_list = self._get_tools(tools_list)
        logger.info(f"Executing get_mutants on tools {tools_list}")

        if not tools_list:
            logger.warning("Empty toolset, exit...")
            return

        # set dummy testsuite as only testclass
        self.set_testsuite(dummy=True)

        # clean compiled and compile again
        self.clean()
        self.d4j_compile()
        logger.info("Project cleaned and compiled")

        # get dummy test name
        dummy_test_name = f"{self.name}DummyTest"
        dummy_test = ".".join([self.package, dummy_test_name])

        # and also class under mutation name
        class_under_mutation = self.relevant_class

        # cycle over tools
        for tool in tools_list:
            # must specify tests and class for replacement of dummy text
            # inside bash scripts
            if isinstance(tool, (tools.Jumble, tools.Pit)):
                kwargs.update(
                    {
                        "tests": dummy_test,
                        "class": class_under_mutation,
                    }
                )

            self._run_tool(tool, **kwargs)
