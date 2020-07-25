# HAMILTONIAN PATH PROBLEM

A Hamiltonian path is a path through a graph such that every node is visited once.

This group of programs will take a list encoding of a graph.
first enter the number of nodes, then on each successive line,
write one pair of connected nodes.  For example,

A F

says node A has a path TO node F.  If the path is bidirectional,
F A will also have to appear in the list of connections.  The first
program takes this list as an input and generates a long list of
clauses in propositional logic that must be satisfied in order
to have a Hamiltonian path.

The second program takes the output of the first, and runs the
Davis-Putnam algorithm on it to determine if there is some order of
nodes that solves the Hamiltonian Path problem.

The third program takes the output of the Davis-Putnam algorithm
that I implemented and interprets the output to decide whether or
not a conclusion was reached.

## PART 1:

In the part1 folder, compile the files together with:

javac ClauseGen.java Edge.java GraphTranslator.java Vertices.java;

The program expects a command line argument to the input file.
Run the program with:

java ClauseGen [inputFileName.txt];

this will produce an output file called DPLLInput.txt.

## PART2:

In the part2 folder, compile the files together with:

javac DPLL.java Clause.java Literal.java Model.java Symbol.java;

The program expects a command line argument to the correctly
formatted input file.  If you have not changed the name, that will
be the "DPLLInput.txt" file from part 1.  Run the program with:

java DPLL [inputFileName.txt];

this will produce an output file called DPLLResults.txt.

## PART3:

In the part3 folder, compile the files together with:

javac Entry.java ResultGen.java;

The program expects a command line argument to the input file
produced by DPLL.  This will work as long as the file is correctly
formatted (my DPLL program creates the project specified format).
If you have not changed the name, that will be the "DPLLResults.txt"
file from part 2.  Run the program with:

java ResultGen [inputFileName.txt];

this will print the results ON TERMINAL.  If there is a solution,
it will be printed.  Otherwise, the program will print "NO SOLUTION"