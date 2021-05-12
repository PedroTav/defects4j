#! /usr/bin/bash

# any error will terminate the script
set -e

cat "all_tests" | cut -f 2 -d "(" | cut -f 1 -d ")" | sort | uniq

