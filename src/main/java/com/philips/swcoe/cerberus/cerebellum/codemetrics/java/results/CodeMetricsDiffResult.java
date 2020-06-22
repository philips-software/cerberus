package com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CodeMetricsDiffResult {

    private String constructName;
    private String fileName;
    private String constructType;
    private String metricName;
    private int oldValue;
    private int newValue;

}
