# Defects4J
Defects4J is a collection of reproducible bugs and a supporting infrastructure
with the goal of advancing software engineering research.

Refer to the [original repository](https://github.com/rjust/defects4j) for more informations.

## Contents of modified Defects4J
This version of Defects4J is built on top of the original project, but adds
new features listed below.

### Mutation tools
The only mutation tool (integrated into defects4j core) is Major.

This version adds to the list also Judy, Jumble and PIT.
These tools are setupped when the project is initializated, and resided 
inside `mutation_tools`.

### Analyzer
The analyzer is a piece of software written in Python3 and used to
launch actions on a defects4j project (please check out [its readme](https://github.com/Crissal1995/defects4j/tree/master/analyzer)
for more informations).
