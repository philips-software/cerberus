# JCMD : - Java Code Metrics Detection

This feature of Cerberus helps you to gather CK metrics in two modes 

1. Metrics for the entire source code.
2. Difference in Metrics of two different versions of same set of files.


### Below are the Metrics that could be gathered at both method and class level

**COUPLING_BETWEEN_OBJECTS** : Counts the number of dependencies a class has. The tools checks for any type used in the entire class (field declaration, method return types, variable declarations, etc). It ignores dependencies to Java itself (e.g. java.lang.String).

**DEPTH_INHERITANCE_TREE** : It counts the number of "fathers" a class has. All classes have DIT at least 1 (everyone inherits java.lang.Object). In order to make it happen, classes must exist in the project (i.e. if a class depends upon X which relies in a jar/dependency file, and X depends upon other classes, DIT is counted as 2).

**NO_OF_STATIC_INVOCATIONS** : Counts the number of invocations to static methods. It can only count the ones that can be resolved by the JDT.

**RESPONSE_FOR_A_CLASS** : Counts the number of unique method invocations in a class. As invocations are resolved via static analysis, this implementation fails when a method has overloads with same number of parameters, but different types.

**WEIGHT_METHOD_CLASS** :  McCabe's complexity. It counts the number of branch instructions in a class. 

**LINES_OF_CODE** : It counts the lines of count, ignoring empty lines and comments (i.e., it's Source Lines of Code, or SLOC). The number of lines here might be a bit different from the original file, as we use JDT's internal representation of the source code to calculate it.

**NO_OF_RETURNS** : The number of return instructions.

**NO_OF_LOOPS** : The number of loops (i.e., for, while, do while, enhanced for).

**NO_OF_COMPARISONS** : The number of comparisons (i.e., == and !=). 

**NO_OF_TRYCATCH_BLOCKS** : The number of try/catches

**NO_OF_PARENTHESIZED_EXPRESSIONS** : The number of expressions inside parenthesis.

**NO_OF_STRING_LITERALS** : The number of string literals (e.g., "Cereberus"). Repeated strings count as many times as they appear.

**NO_OF_NOS** : The number of numbers (i.e., int, long, double, float) literals.

**NO_OF_MATH_OPERATIONS** : The number of math operations (times, divide, remainder, plus, minus, left shit, right shift).

**NO_OF_VARIABLES** : Number of declared variables.

**NO_OF_MAX_NESTED_BLOCKS** : The highest number of blocks nested together.

**NO_OF_ANONYMOUS_CLASSES, NO_OF_SUBCLASSES, NO_OF_LAMBDAS** : 
Quantity of Anonymous classes, inner classes, and lambda expressions. The name says it all. 

**NO_OF_UNIQUE_WORDS**: - Number of unique words in the source code. 

**NO_OF_MODIFIERS**: - Number of public/abstract/private/protected/native modifiers of classes/methods 

#### Metrics on Number of Methods based on its type : - 
Specific numbers for total number of methods, static, public, abstract, private, protected, default, final, and synchronized methods. Constructor methods also count here . The format is NO_OF_METHOD-TYPE_METHODS

	NO_OF_METHODS : - Number of methods
	NO_OF_STATIC_METHODS   : - Number of methods which are Static
	NO_OF_PRIVATE_METHODS  : - Number of methods which are Private
	NO_OF_DEFAULT_METHODS  : - Number of methods which are Default
	NO_OF_ABSTRACT_METHODS  : - Number of methods which are Abstract
	NO_OF_FINAL_METHODS: - Number of methods which are Final
	NO_OF_SYNCHRONIZED_METHODS : - Number of methods which are Synchronized
	NO_OF_PUBLIC_METHODS : - Number of methods which are Public
	NO_OF_PROTECTED_METHODS : - Number of methods which are Protected

#### Metrics on Number of Fields based on its type : - 
Specific numbers for total number of fields, static, public, private, protected, default, final, and synchronized fields. The format is NO_OF_FIELD-TYPE_FIELDS

	NO_OF_STATIC_FIELDS : - Number of methods which are Static
	NO_OF_PUBLIC_FIELDS : - Number of methods which are Static
	NO_OF_PROTECTED_FIELDS : - Number of methods which are Static
	NO_OF_DEFAULT_FIELDS : - Number of methods which are Static
	NO_OF_FINAL_FIELDS : - Number of methods which are Static
	NO_OF_SYNCHRONIZED_FIELDS : - Number of methods which are Static

**COMPLEXITY_OF_METHOD** : - McCabe's complexity. It counts the number of branch instructions in a specific Method .


#### Below are the sample commands to run this feature

    java -jar cereberus-executable.jar JCMD-DIFF --class-config=/your/path/class-metrics.config --method-config=/your/path/method-metrics.config --current=/your/path/current --previous=/your/path/previous --format=csv --structure=horizontal

    java -jar cereberus-executable.jar JCMD-DIFF --class-config=/your/path/class-metrics.config --method-config=/your/path/method-metrics.config --current=/your/path/current --previous=/your/path/previous --format=psv --structure=vertical
    
    java -jar cereberus-executable.jar JCMD-DIFF --class-config=/your/path/class-metrics.config --method-config=/your/path/method-metrics.config --current=/your/path/current --previous=/your/path/previous --format=MD --structure=vertical



