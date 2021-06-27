# A First-Order-Logic Agent

## Overview
The project aims at developing a first-order-logic agent. The Knowledge Base (KB), clearly, contains only first order logic sentences

The agent reads the sentences in KB in raw format from input.txt file and converts them into CNF form. Then, it reads queries one by one from the same input.txt file, infers whether these queries can be satisifed using RESOLUTION, and then writes the output in output.txt file.

## Prerequisites

* Java 1.8
## Using the experiment

If you are using an IDE like IntelliJ or Eclipse, use the following steps to run the project
* Point the working directory to LogicAgent
* Main class: LogicAgent
* JRE: 1.8
* Run the project

If you are running the project on command line, follow these steps:<br/>
* Clone/Download the project to your local machine.
* Navigate to the folder with LogicAgent.java file.
* Run the following commands:
```
javac LogicAgent.java
```

```
java LogicAgent
```


## File description

`Format for input.txt`

 <N = NUMBER OF QUERIES>

 <QUERY 1>
 
 …

 <QUERY N>
  
 <K = NUMBER OF GIVEN SENTENCES IN THE KNOWLEDGE BASE>

 <SENTENCE 1>
 
 …

 <SENTENCE K>
  
The first line contains an integer N specifying the number of queries. The following N lines contain
one query per line. The line after the last query contains an integer K specifying the number of sentences in the knowledge base. The remaining K lines contain the sentences in the knowledge base, one sentence per line.

`Query format`

Each query will be a single literal of the form Predicate(Constant_Arguments) or
~Predicate(Constant_Arguments) and will not contain any variables. Each predicate will have between 1 and 25 constant arguments. Two or more arguments will be separated by commas.

`KB format` 

Each sentence in the knowledge base has one of the following forms

1) An implication of the form p1 ∧ p2 ∧ ... ∧ pm ⇒ q, where its premise is a conjunction of literals and its conclusion is a single literal. 
2) A single literal: q or ~q

`Note`

1. & denotes the conjunction operator.<br/>
2. | denotes the disjunction operator. It will not appear in the queries nor in the KB given as
input. But you will likely need it to create your proofs. <br/>
3. => denotes the implication operator.<br/>
4. ~ denotes the negation operator.<br/>
5. No other operators besides &, =>, and ~ are used in the knowledge base.<br/>
6. There will be no parentheses in the KB except as used to denote arguments of predicates.<br/>
7. Variables are denoted by a single lowercase letter.<br/>
8. All predicates (such as HighBP) and constants (such as Alice) are case sensitive
alphabetical strings that begin with uppercase letters.<br/>
9. Each predicate takes at least one argument. Predicates will take at most 25 arguments. A
given predicate name will not appear with different number of arguments.<br/>
10. There will be at most 10 queries and 100 sentences in the knowledge base.<br/>
11. See the sample input below for spacing patterns.<br/>
12. You can assume that the input format is exactly as it is described.<br/>
13. There will be no syntax errors in the given input.<br/>
14. The KB will be true (i.e., will not contain contradictions).<br/>

`Format for output.txt:`

For each query, the agent should determine if that query can be inferred from the knowledge base or not, one query per line:

<ANSWER 1>
…
<ANSWER N>

Each answer should be either TRUE if the agent can prove that the corresponding query sentence is true given the knowledge base, otherwise FALSE.
  

## Contact

If you want to contact me, you can reach me at <nchakrav@usc.edu> or <nikhilchakravartula@gmail.com>.