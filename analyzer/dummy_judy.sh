#!/bin/bash

HERE=$(pwd)
# suppose we are in base/work/<repo>
BASE=$(cd ../.. && pwd)
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

echo "Command to run:"
echo $CMD
echo
$CMD

# result is in "result.json"