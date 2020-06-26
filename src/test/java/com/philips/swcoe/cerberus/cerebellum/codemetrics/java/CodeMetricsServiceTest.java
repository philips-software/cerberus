/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.cerebellum.codemetrics.java;

import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.CLEANEST_FILE_JAVA;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.MAIN;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.PATH_SEPARATOR;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.RESOURCES;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.SONARQUBE_SUPPRESSED_WARNINGS_JAVA;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.TEST_JAVA_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mauricioaniche.ck.CKClassResult;
import com.github.mauricioaniche.ck.CKMethodResult;
import com.google.common.collect.Iterables;
import java.util.List;

public class CodeMetricsServiceTest {

    private String path;
    private List<CKClassResult> metrics;

    @BeforeEach
    public void beforeEachTest() throws Exception {
        path = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE;
        metrics = CodeMetricsService.getCodeMetrics(path);
    }

    @Test
    public void testGathersMetricsForAnyJavaCode() throws Exception {
        assertEquals(15, metrics.size());
    }

    @Test
    public void testProcessAllJavaFilesInSpecifiedPath() throws Exception {
        CKClassResult ckClassResult = getSelectedMetrics(CLEANEST_FILE_JAVA);
        assertTrue(ckClassResult.getFile().contains(CLEANEST_FILE_JAVA));
        assertTrue(ckClassResult.getClassName().contains(MAIN));
    }

    @Test
    public void testGettingLinesOfCodeInClass() throws Exception {
        CKClassResult ckClassResult = getSelectedMetrics(CLEANEST_FILE_JAVA);
        assertEquals(13, ckClassResult.getLoc());
    }

    @Test
    public void testDepthInHeritanceTreeInClass() throws Exception {
        CKClassResult ckClassResult = getSelectedMetrics(SONARQUBE_SUPPRESSED_WARNINGS_JAVA);
        assertEquals(1, ckClassResult.getDit());
    }

    @Test
    public void testGettingCouplingBetweenObjects() throws Exception {
        CKClassResult ckClassResult = getSelectedMetrics(CLEANEST_FILE_JAVA);
        assertEquals(13, ckClassResult.getLoc());
    }

    @Test
    public void testNumberOfFieldsMetricInClass() throws Exception {
        CKClassResult ckClassResult = getSelectedMetrics(CLEANEST_FILE_JAVA);
        assertEquals(2, ckClassResult.getNumberOfFields());
    }

    @Test
    public void testNumberOfMethodsInClass() throws Exception {
        CKClassResult ckClassResult = getSelectedMetrics(CLEANEST_FILE_JAVA);
        assertEquals(1, ckClassResult.getNumberOfMethods());
    }

    @Test
    public void testWeightMethodClassMetric() throws Exception {
        CKClassResult ckClassResult = getSelectedMetrics(CLEANEST_FILE_JAVA);
        assertEquals(2, ckClassResult.getWmc());
    }

    @Test
    public void testLinesOfCodeMetricInClassMethod() throws Exception {
        CKClassResult ckClassResult = getSelectedMetrics(CLEANEST_FILE_JAVA);
        CKMethodResult result = ckClassResult.getMethods().iterator().next();
        assertEquals(8, result.getLoc());
    }

    private CKClassResult getSelectedMetrics(String nameOfFile) {
        return Iterables.find(metrics, result -> result.getFile().contains(nameOfFile));
    }


}