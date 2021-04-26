#!/bin/bash
OUT='jumble_output'

# take content of output file
CONTENT=$(cat $OUT)

# first we remove everything until Mutating
# we must take last occurence, because after the
# Jumble "Mutating" there are no more
# grep -n -> xx:match
# cut field 1, -d (decimals) on semicolon :
# tail -1 take last occurrence
LINE_NUMBER=$(echo "$CONTENT" | grep -n Mutating | cut -f1 -d: | tail -1)

# start from line number + 3
CONTENT=$(echo "$CONTENT" | tail -n +$(expr $LINE_NUMBER + 3))

# head -n -2 -> exclude last two rows
CONTENT=$(echo "$CONTENT" | head -n -2)

# get live mutants with grep pattern -c (count)
LIVE=$(echo "$CONTENT" | grep -E "M FAIL[^\n]+" -c)

# get killed mutants removing fail pattern, then joining the lines, and finally counting chars
KILLED=$(echo "$CONTENT" | sed "s/M FAIL.*//" | tr -d '\n' | wc -c)

# generated mutants are the sum of the two vars
GENERATED=$(expr $KILLED + $LIVE)

echo "Generated $GENERATED mutants, killed $KILLED and live $LIVE"

if [ "$GENERATED" -eq 0 ]; then
  echo "Maybe there were some errors?"
fi
