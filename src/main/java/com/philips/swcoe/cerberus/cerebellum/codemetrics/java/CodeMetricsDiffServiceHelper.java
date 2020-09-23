package com.philips.swcoe.cerberus.cerebellum.codemetrics.java;

import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsClassResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CodeMetricsDiffServiceHelper {

    public List<CodeMetricsClassResult> getMetricsAfterExclusion(String exclusionFiles,
            List<CodeMetricsClassResult> codeMetricsClassResultsList) {
        if (exclusionFiles != null) {
            return doExclusion(exclusionFiles, codeMetricsClassResultsList);
        } else {
            return codeMetricsClassResultsList;
        }
    }

    private List<CodeMetricsClassResult> doExclusion(String exclusionFiles,
            List<CodeMetricsClassResult> codeMetricsClassResults) {
        List<CodeMetricsClassResult> codeStatisticsWithExclusion = new ArrayList<>();

        List<String> exclusionLists = Arrays.asList(exclusionFiles.split(","));
        for (CodeMetricsClassResult codeMetricsClassResult : codeMetricsClassResults) {
            for (String exclusionList : exclusionLists) {
                if (!codeMetricsClassResult.getFile().contains(exclusionList)) {
                    codeStatisticsWithExclusion.add(codeMetricsClassResult);
                }
            }
        }
        return codeStatisticsWithExclusion;

    }
}
