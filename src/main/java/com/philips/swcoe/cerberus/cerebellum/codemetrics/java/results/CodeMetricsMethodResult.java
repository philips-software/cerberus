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
    private CodeMetricsDiffResult parametersQty;
    private CodeMetricsDiffResult linesOfCode;
    private CodeMetricsDiffResult variablesQty;
    private CodeMetricsDiffResult startLineNo;
    private CodeMetricsDiffResult loopQty;
    private CodeMetricsDiffResult comparisonsQty;
    private CodeMetricsDiffResult tryCatchQty;
    private CodeMetricsDiffResult parenthesizedExpsQty;
    private CodeMetricsDiffResult stringLiteralsQty;
    private CodeMetricsDiffResult numbersQty;
    private CodeMetricsDiffResult assignmentsQty;
    private CodeMetricsDiffResult mathOperationsQty;
    private CodeMetricsDiffResult maxNestedBlocks;
    private CodeMetricsDiffResult anonymousClassesQty;
    private CodeMetricsDiffResult subClassesQty;
    private CodeMetricsDiffResult lambdasQty;
    private CodeMetricsDiffResult uniqueWordsQty;
    
}
