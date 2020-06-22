/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.cerebellum.codemetrics.java;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKClassResult;
import java.util.ArrayList;
import java.util.List;

public class CodeMetricsService {

    public static List<CKClassResult> getCodeMetrics(String pathToSource) {
        List<CKClassResult> codeStatistics = new ArrayList<>();
        new CK().calculate(pathToSource, false, result -> {
            codeStatistics.add(result);
        });
        return codeStatistics;
    }

}
