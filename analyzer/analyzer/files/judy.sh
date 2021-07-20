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

# take every folder "test*" like (ignore case)
TST=$(cd $(find $TARGET -iname "test*" -type d) && pwd || exit)

if [ "$TST" == "$HOME" ]; then
  echo "TEST DIR not found!" && exit
fi

RGX=""
TGX=""

CMD="$JUDY -p $CLS -t $TST"

# add regexp if they are not null
if [ ! -z $RGX ]; then
  CMD="$CMD -r $RGX"
fi

if [ ! -z $TGX ]; then
  CMD="$CMD --test-file-regex $TGX"
fi

# result path of result.json file
CMD="$CMD --result-path $HERE/result.json"

echo "Command to run:"
echo $CMD
echo
$CMD

# result is in "result.json"