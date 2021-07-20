#!/bin/bash

HERE=$(dirname "$0")
# suppose we are in base/work/<repo>
# BASE=$(cd $HERE/../.. && pwd)
BASE=$(cd $(dirname $(which defects4j))/../.. && pwd)

MUTATION_TOOLS=$(cd $BASE/mutation_tools && pwd)
LIB_HOME="$MUTATION_TOOLS/lib"
PIT_HOME="$MUTATION_TOOLS/pitest-1.6.3-jars"

JUNIT="$LIB_HOME/junit-4.12.jar"
JUNITX="$LIB_HOME/junit-addons-1.4.jar"
PITEST="$PIT_HOME/pitest-1.6.0-SNAPSHOT.jar"
PITEST_ENTRY="$PIT_HOME/pitest-entry-1.6.0-SNAPSHOT.jar"
PITEST_CLI="$PIT_HOME/pitest-command-line-1.6.0-SNAPSHOT.jar"

CP="$JUNIT:$JUNITX:$PITEST:$PITEST_ENTRY:$PITEST_CLI"

echo "Base is           : $BASE"
echo "Mutation tools is : $MUTATION_TOOLS"
echo "PIT home is       : $PIT_HOME"
echo

TARGET=$HERE/target
CLS=$TARGET/classes

# take every folder "test*" like (ignore case)
TST=$(cd $(find $TARGET -iname "test*" -type d) && pwd || exit)

if [ "$TST" == "$HOME" ]; then
  echo "TEST DIR not found!" && exit
fi

PIT_CMD="org.pitest.mutationtest.commandline.MutationCoverageReport"
CLS_FLAG="--classPath $CLS,$TST"
CP="$CP:$CLS:$TST"

TEST_TARGET="<TEST_REGEXP>"
CLASS_TARGET="<CLASS_REGEXP>"
TARGET_FLAG="--targetClasses $CLASS_TARGET --targetTests $TEST_TARGET"

REPORT="--reportDir $HERE/pit_report"
TIMESTAMPED_REPORTS="--timestampedReports false"
SRC="--sourceDirs $HERE/src/main/java"

DEFAULTS="--mutators DEFAULTS"
STRONGER="--mutators STRONGER"
ALL="--mutators ALL"
MUTATORS=$STRONGER

OUTPUT_FORMATS="--outputFormats html,xml,csv"

CMD="java -cp $CP $PIT_CMD $TARGET_FLAG $REPORT $SRC $MUTATORS $OUTPUT_FORMATS $TIMESTAMPED_REPORTS"

echo "Command to run:"
echo $CMD 
echo
$CMD

