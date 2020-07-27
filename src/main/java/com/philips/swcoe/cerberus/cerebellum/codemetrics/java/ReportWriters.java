package com.philips.swcoe.cerberus.cerebellum.codemetrics.java;

import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsClassResult;
import java.io.IOException;
import java.util.List;

public enum ReportWriters {
    vertical {
        @Override
        public String get(List<String> classConfig, List<String> methodConfig,
                          String reportFormat, List<CodeMetricsClassResult> metricsResult)
            throws IOException {
            return new CodeMetricsVerticalWriterService(classConfig, methodConfig,
                reportFormat.toUpperCase()).generateMetricsReport(metricsResult);
        }
    },
    horizontal {
        @Override
        public String get(List<String> classConfig, List<String> methodConfig,
                          String reportFormat, List<CodeMetricsClassResult> metricsResult)
            throws IOException {
            return new CodeMetricsHorizontalWriterService(classConfig, methodConfig,
                reportFormat.toUpperCase()).generateMetricsReport(metricsResult);
        }
    };

    public abstract String get(List<String> classConfig, List<String> methodConfig,
                               String reportFormat, List<CodeMetricsClassResult> metricsResult)
        throws IOException;
}
