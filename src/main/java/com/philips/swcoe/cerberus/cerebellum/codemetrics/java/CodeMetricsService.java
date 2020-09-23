/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.cerebellum.codemetrics.java;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKClassResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CodeMetricsService {

    public static List<CKClassResult> getCodeMetrics(String pathToSource, String exclusionFiles) {
        List<CKClassResult> codeStatistics = new ArrayList<>();
        new CK().calculate(pathToSource, false, result -> {
            codeStatistics.add(result);
        });
        if (exclusionFiles != null) {
            return doExclusion(exclusionFiles, codeStatistics);
        } else {
            return codeStatistics;
        }
    }

    private static List<CKClassResult> doExclusion(String exclusionFiles, List<CKClassResult> codeStatistics) {
        List<CKClassResult> codeStatisticsWithExclusion = new ArrayList<>();

        List<String> exclusionLists = Arrays.asList(exclusionFiles.split(","));
        for (CKClassResult codeStatistic : codeStatistics) {
            for (String exclusionList : exclusionLists) {
                if (!codeStatistic.getFile().contains(exclusionList)) {
                    codeStatisticsWithExclusion.add(codeStatistic);
                }
            }
        }
        return codeStatisticsWithExclusion;

    }

}
