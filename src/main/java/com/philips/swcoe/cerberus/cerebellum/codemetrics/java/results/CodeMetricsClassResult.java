package com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CodeMetricsClassResult {

    private String file;

    private String className;

    private String type;

    private CodeMetricsResult depthInheritanceTree;
    private CodeMetricsResult weightMethodClass;
    private CodeMetricsResult couplingBetweenObjects;
    private CodeMetricsResult responseForClass;
    private CodeMetricsResult numberOfStaticInvocations;
    private CodeMetricsResult linesOfCode;
    private CodeMetricsResult returnQty;
    private CodeMetricsResult loopQty;
    private CodeMetricsResult comparisonsQty;
    private CodeMetricsResult tryCatchQty;
    private CodeMetricsResult parenthesizedExpsQty;
    private CodeMetricsResult stringLiteralsQty;
    private CodeMetricsResult numbersQty;
    private CodeMetricsResult assignmentsQty;
    private CodeMetricsResult mathOperationsQty;
    private CodeMetricsResult variablesQty;
    private CodeMetricsResult maxNestedBlocks;
    private CodeMetricsResult anonymousClassesQty;
    private CodeMetricsResult subClassesQty;
    private CodeMetricsResult lambdasQty;
    private CodeMetricsResult uniqueWordsQty;
    private CodeMetricsResult numberOfMethods;
    private CodeMetricsResult numberOfStaticMethods;
    private CodeMetricsResult numberOfPublicMethods;
    private CodeMetricsResult numberOfPrivateMethods;
    private CodeMetricsResult numberOfProtectedMethods;
    private CodeMetricsResult numberOfDefaultMethods;
    private CodeMetricsResult numberOfAbstractMethods;
    private CodeMetricsResult numberOfFinalMethods;
    private CodeMetricsResult numberOfSynchronizedMethods;
    private CodeMetricsResult numberOfFields;
    private CodeMetricsResult numberOfStaticFields;
    private CodeMetricsResult numberOfPublicFields;
    private CodeMetricsResult numberOfPrivateFields;
    private CodeMetricsResult numberOfProtectedFields;
    private CodeMetricsResult numberOfDefaultFields;
    private CodeMetricsResult numberOfFinalFields;
    private CodeMetricsResult numberOfSynchronizedFields;
    private CodeMetricsResult modifiers;

    private List<CodeMetricsMethodResult> methodMetrics;

}
