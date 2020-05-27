package com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CodeMetricsMethodResult {

    private String methodName;
    private String type;
    private String file;
    private CodeMetricsResult complexity;
    private CodeMetricsResult parametersQty;
    private CodeMetricsResult linesOfCode;
    private CodeMetricsResult variablesQty;
    private CodeMetricsResult startLineNo;
    private CodeMetricsResult loopQty;
    private CodeMetricsResult comparisonsQty;
    private CodeMetricsResult tryCatchQty;
    private CodeMetricsResult parenthesizedExpsQty;
    private CodeMetricsResult stringLiteralsQty;
    private CodeMetricsResult numbersQty;
    private CodeMetricsResult assignmentsQty;
    private CodeMetricsResult mathOperationsQty;
    private CodeMetricsResult maxNestedBlocks;
    private CodeMetricsResult anonymousClassesQty;
    private CodeMetricsResult subClassesQty;
    private CodeMetricsResult lambdasQty;
    private CodeMetricsResult uniqueWordsQty;
    
}
