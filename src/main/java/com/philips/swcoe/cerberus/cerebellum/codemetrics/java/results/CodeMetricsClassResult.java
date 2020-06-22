package com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private CodeMetricsDiffResult maxNestedBlocks;
    private CodeMetricsDiffResult returnCount;
    private CodeMetricsDiffResult loopCount;
    private CodeMetricsDiffResult comparisonsCount;
    private CodeMetricsDiffResult tryCatchCount;
    private CodeMetricsDiffResult parenthesizedExpsCount;
    private CodeMetricsDiffResult stringLiteralsCount;
    private CodeMetricsDiffResult numbersCount;
    private CodeMetricsDiffResult assignmentsCount;
    private CodeMetricsDiffResult mathOperationsCount;
    private CodeMetricsDiffResult variablesCount;
    private CodeMetricsDiffResult anonymousClassesCount;
    private CodeMetricsDiffResult subClassesCount;
    private CodeMetricsDiffResult lambdasCount;
    private CodeMetricsDiffResult uniqueWordsCount;
    private CodeMetricsDiffResult methodsCount;
    private CodeMetricsDiffResult staticMethodsCount;
    private CodeMetricsDiffResult publicMethodsCount;
    private CodeMetricsDiffResult privateMethodsCount;
    private CodeMetricsDiffResult protectedMethodsCount;
    private CodeMetricsDiffResult defaultMethodsCount;
    private CodeMetricsDiffResult abstractMethodsCount;
    private CodeMetricsDiffResult finalMethodsCount;
    private CodeMetricsDiffResult synchronizedMethodsCount;
    private CodeMetricsDiffResult fieldsCount;
    private CodeMetricsDiffResult staticFieldsCount;
    private CodeMetricsDiffResult publicFieldsCount;
    private CodeMetricsDiffResult privateFieldsCount;
    private CodeMetricsDiffResult protectedFieldsCount;
    private CodeMetricsDiffResult defaultFieldsCount;
    private CodeMetricsDiffResult finalFieldsCount;
    private CodeMetricsDiffResult synchronizedFieldsCount;
    private CodeMetricsDiffResult modifiersCount;

    private List<CodeMetricsMethodResult> methodMetrics;

}
