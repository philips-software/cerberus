
### Usage of FPM 

Cerberus can currently identify, programming mistakes in java language, we are currently working on enabling FPM for other languages.

FPM takes four arguments as listed below
```
      --files=<pathToSource>
         Absolute Path to your source code.
      --java-version=<languageVersion>
         Java Language Version
      --language=<language>
         Language Of Source Code
      --rulesets=<pathToRulesets>
         Your Desired Ruleset for your
 
```

For java version, you can use any of the values listed below. 

1.3, 1.4, 1.5, 5, 1.6, 6, 1.7, 7, 1.8, 8, 9, 1.9, 10, 1.10, 11, 12, 13

For language it should be always java. 

Rulesets can be any [PMD ruleset](https://pmd.github.io/pmd/pmd_rules_java.html) .You can either define your own ruleset, or you can use built in rule set. 
If you want a sample ruleset refer the file 

[java_practices.xml](resources/java_practices.xml)   

The above file has the rules that we follow at SwCOE and the same rule is used to guard the quality of code of Cerberus itself.

If you want to use some built in rule sets that are built with in the system. 

1. [category/java/design.xml] (https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/category/java/design.xml)
2. [category/java/documentation.xml] (https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/category/java/documentation.xml)
3. [category/java/codestyle.xml] (https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/category/java/codestyle.xml)
4. [category/java/errorprone.xml] (https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/category/java/errorprone.xml)
5. [category/java/multithreading.xml] (https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/category/java/multithreading.xml)
6. [category/java/performance.xml] (https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/category/java/performance.xml)
7. [category/java/security.xml] (https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/category/java/security.xml)
8. [category/java/bestpractices.xml] (https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/category/java/bestpractices.xml)

To use built in rules just specify the category rule path ( make sure to give the path correct ) below is an example usage
```
java -jar cerberus-executable.jar FPM --files=./resources/testJavaCode --java-version=1.8 --language=java --rulesets=category/java/bestpractices.xml

```