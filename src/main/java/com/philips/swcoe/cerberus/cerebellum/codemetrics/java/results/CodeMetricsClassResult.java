package com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CodeMetricsClassResult implements CodeMetricsResult {

    private String file;
    private String className;
    private String type;

    private CodeMetricsDiffResult depthInheritanceTree;
    private CodeMetricsDiffResult weightMethodClass;
    private CodeMetricsDiffResult couplingBetweenObjects;
    private CodeMetricsDiffResult responseForClass;
    private CodeMetricsDiffResult numberOfStaticInvocations;
    private CodeMetricsDiffResult linesOfCode;
    private CodeMetricsDiffResult returnQty;
    private CodeMetricsDiffResult loopQty;
    private CodeMetricsDiffResult comparisonsQty;
    private CodeMetricsDiffResult tryCatchQty;
    private CodeMetricsDiffResult parenthesizedExpsQty;
    private CodeMetricsDiffResult stringLiteralsQty;
    private CodeMetricsDiffResult numbersQty;
    private CodeMetricsDiffResult assignmentsQty;
    private CodeMetricsDiffResult mathOperationsQty;
    private CodeMetricsDiffResult variablesQty;
    private CodeMetricsDiffResult maxNestedBlocks;
    private CodeMetricsDiffResult anonymousClassesQty;
    private CodeMetricsDiffResult subClassesQty;
    private CodeMetricsDiffResult lambdasQty;
    private CodeMetricsDiffResult uniqueWordsQty;
    private CodeMetricsDiffResult numberOfMethods;
    private CodeMetricsDiffResult numberOfStaticMethods;
    private CodeMetricsDiffResult numberOfPublicMethods;
    private CodeMetricsDiffResult numberOfPrivateMethods;
    private CodeMetricsDiffResult numberOfProtectedMethods;
    private CodeMetricsDiffResult numberOfDefaultMethods;
    private CodeMetricsDiffResult numberOfAbstractMethods;
    private CodeMetricsDiffResult numberOfFinalMethods;
    private CodeMetricsDiffResult numberOfSynchronizedMethods;
    private CodeMetricsDiffResult numberOfFields;
    private CodeMetricsDiffResult numberOfStaticFields;
    private CodeMetricsDiffResult numberOfPublicFields;
    private CodeMetricsDiffResult numberOfPrivateFields;
    private CodeMetricsDiffResult numberOfProtectedFields;
    private CodeMetricsDiffResult numberOfDefaultFields;
    private CodeMetricsDiffResult numberOfFinalFields;
    private CodeMetricsDiffResult numberOfSynchronizedFields;
    private CodeMetricsDiffResult modifiers;

    private List<CodeMetricsMethodResult> methodMetrics;

}
