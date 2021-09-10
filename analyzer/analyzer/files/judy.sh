#!/bin/bash

HERE=$(dirname "$0")
# suppose we are in base/work/<repo>
# BASE=$(cd $HERE/../.. && pwd)
BASE=$(cd $(dirname $(which defects4j))/../.. && pwd)

MUTATION_TOOLS=$(cd $BASE/mutation_tools && pwd)
JUDY=$MUTATION_TOOLS/judy-3.0.0-M1/bin/judy

echo "Base is           : $BASE"
echo "Mutation tools is : $MUTATION_TOOLS"
echo

TARGET=$HERE/target
CLS=$TARGET/classes

# the only class to mutate, if provided
# else every class in $CLS will be mutated
CLASS="$CLS/<PATH_TO_CLASS>"

# take every folder "test*" like (ignore case)
TST=$(cd $(find $TARGET -iname "test*" -type d) && pwd || exit)

if [ "$TST" == "$HOME" ]; then
  echo "TEST DIR not found!" && exit 1
fi

# if no change were made, use all the classes
if [ "$CLASS" == "$CLS/<PATH_TO_CLASS>" ]; then
  CLASS=$CLS
fi

CMD="$JUDY -p $CLASS -t $TST --result-path $HERE/result.json"
OUT="$HERE/judy.log"
SEP="-------------------------------------------------"

printf "Running command $CMD \n"
echo "Inside the box there is all Judy output (stdout and stderr)"
echo "Stdout will be redirect also to $OUT"

echo $SEP
$CMD 2>&1 | tee $OUT
