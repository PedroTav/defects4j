#!/bin/bash

HERE=$(pwd)
# suppose we are in base/work/<repo>
BASE=$(cd ../.. && pwd)
MUTATION_TOOLS=$(cd $BASE/mutation_tools && pwd)
JUMBLE=$MUTATION_TOOLS/jumble/jumble_binary_1.3.0.jar

TARGET=$HERE/target
CLS=$TARGET/classes
TST=$(cd $(find $TARGET -iname "test*" -type d) && pwd)

CLASSPATH=$CLS:$TST

# take classes from target folder
CLASSES=$(cd $CLS && find . -name *.class | sort | sed "s/\.\///g" | tr / . | sed "s/\.class//g")

# set tests to use
TESTS="<REPLACE_TESTS>"

# fix multiline tests into a single line
TESTS=$(echo $TESTS | tr " " "\n")
TESTS_ONELINE=$(echo $TESTS | tr "\n" " ")

# echo "Valid tests are: $TESTS"
# echo "Classes are: $CLASSES"

# CLASS TO MUTATE
CLASS="<REPLACE_CLASS>"
printf "\nClass to mutate: $CLASS \n"

printf "\nTests to use:\n$TESTS \n\n"

# mutations flags, "-S" doesn't work with "-t" (csv report)
MUTATIONS="-w -i -k -r -X -S -j"

CMD="java -jar $JUMBLE $MUTATIONS -c $CLASSPATH $CLASS $TESTS_ONELINE"

OUT="jumble_output.txt"
SEP="----------------------------------------------------------------------------------------------------------"

printf "Running command $CMD \n"
echo "Inside the box there is all jumble output (stdout and stderr)"
echo "Stdout will be redirect also to $OUT"

echo $SEP
$CMD | tee $OUT
echo $SEP

./.jumble_parser.sh || exit 1
# [ -f "$OUT" ] && mv $OUT "$OUT $(date).txt" && echo "Added timestamp to $OUT"
# [ -d jumble_stats ] && mv jumble_stats "jumble_stats $(date)" && echo "Added timestamp to jumble_stats folder"

