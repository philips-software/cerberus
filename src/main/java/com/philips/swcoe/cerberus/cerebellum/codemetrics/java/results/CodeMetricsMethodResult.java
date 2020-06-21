package com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CodeMetricsMethodResult implements CodeMetricsResult {

    private String methodName;
    private String type;
    private String file;

    private CodeMetricsDiffResult complexity;
    private CodeMetricsDiffResult linesOfCode;
    private CodeMetricsDiffResult startLineNo;
    private CodeMetricsDiffResult maxNestedBlocks;
    private CodeMetricsDiffResult variablesCount;
    private CodeMetricsDiffResult parametersCount;
    private CodeMetricsDiffResult loopCount;
    private CodeMetricsDiffResult comparisonsCount;
    private CodeMetricsDiffResult tryCatchCount;
    private CodeMetricsDiffResult parenthesizedExpsCount;
    private CodeMetricsDiffResult stringLiteralsCount;
    private CodeMetricsDiffResult numbersCount;
    private CodeMetricsDiffResult assignmentsCount;
    private CodeMetricsDiffResult mathOperationsCount;
    private CodeMetricsDiffResult anonymousClassesCount;
    private CodeMetricsDiffResult subClassesCount;
    private CodeMetricsDiffResult lambdasCount;
    private CodeMetricsDiffResult uniqueWordsCount;

}
