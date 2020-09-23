package com.philips.swcoe.cerberus.cerebellum.codemetrics.java;

import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.PATH_SEPARATOR;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.RESOURCES;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.TEST_EXCLUSION_CODE;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.TEST_JAVA_CODE_CURRENT;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.TEST_JAVA_CODE_PREVIOUS;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsClassResult;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

class CodeMetricsVerticalWriterServiceTest {

    private final String previousPath = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE_PREVIOUS;
    private final String currentPath = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE_CURRENT;
    private final String classConfigPath =
        RESOURCES + PATH_SEPARATOR + "class_metrics_to_display.properties";
    private final String methodConfigPath =
        RESOURCES + PATH_SEPARATOR + "method_metrics_to_display.properties";
    private final String exclusionPath = RESOURCES + PATH_SEPARATOR + TEST_EXCLUSION_CODE;

    private CodeMetricsDiffService codeMetricsDiffService;
    private CodeMetricsVerticalWriterService metricsCSVWriterService;
    private CodeMetricsVerticalWriterService metricsPSVWriterService;
    private CodeMetricsVerticalWriterService metricsMDWriterService;
    private CodeMetricsVerticalWriterService metricsHTMLWriterService;


    @BeforeEach
    public void setupJavaCodeMetricsWithDiff() throws IOException {
        codeMetricsDiffService = new CodeMetricsDiffService(previousPath, currentPath);
        List<String> classConfig =
            Files.readAllLines(new File(classConfigPath).toPath(), Charset.defaultCharset());
        List<String> methodConfig =
            Files.readAllLines(new File(methodConfigPath).toPath(), Charset.defaultCharset());
        metricsCSVWriterService =
            new CodeMetricsVerticalWriterService(classConfig, methodConfig, "CSV");
        metricsPSVWriterService =
            new CodeMetricsVerticalWriterService(classConfig, methodConfig, "PSV");
        metricsMDWriterService =
            new CodeMetricsVerticalWriterService(classConfig, methodConfig, "MD");
        metricsHTMLWriterService =
            new CodeMetricsVerticalWriterService(classConfig, methodConfig, "HTML");
    }

    @Test
    public void shouldWriteClassMetricsandMethodMetricsInVerticalFashion() throws Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList =
            codeMetricsDiffService.getMetricsFromSourceCode(exclusionPath);
        String csvData = metricsCSVWriterService.generateMetricsReport(codeMetricsClassResultList);
        assertEquals(44, csvData.split(System.getProperty("line.separator")).length);
    }


    @Test
    public void shouldWriteClassMetricsWhichAreChangedInCSVFormat() throws Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList =
            codeMetricsDiffService.getMetricsFromSourceCode(exclusionPath);
        String csvData = metricsCSVWriterService.generateMetricsReport(codeMetricsClassResultList);
        assertTrue(csvData.contains("FILE,TYPE,METRIC,NEW_VALUE,OLD_VALUE,CLASS"));
        assertTrue(csvData.contains("Rhombus.java,CLASS,NO_OF_MODIFIERS,0,1,Shapes.Rhombus"));
        assertTrue(
            csvData.contains("Triangle.java,CLASS,COUPLING_BETWEEN_OBJECTS,1,0,Shapes.Triangle"));
        assertTrue(
            csvData.contains("Rectangle.java,CLASS,COUPLING_BETWEEN_OBJECTS,3,2,Shapes.Rectangle"));
    }

    @Test
    public void shouldNotWriteClassMetricsWhichAreNotChangedInCSVFormat() throws Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList =
            codeMetricsDiffService.getMetricsFromSourceCode(exclusionPath);
        String csvData = metricsCSVWriterService.generateMetricsReport(codeMetricsClassResultList);
        assertFalse(csvData.contains("Circle.java"));
        assertFalse(csvData.contains("Shape.java"));
        assertFalse(csvData.contains("Main.java"));
    }

    @Test
    public void reportsDataInCSVFormatThrowsException() throws Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList =
            codeMetricsDiffService.getMetricsFromSourceCode(exclusionPath);
        assertDoesNotThrow(
            () -> metricsCSVWriterService.generateMetricsReport(codeMetricsClassResultList));
    }

    @Test
    public void reportsDataInCSVFormatBasedOnClassFilter() throws Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList =
            codeMetricsDiffService.getMetricsFromSourceCode(exclusionPath);
        String csvData = metricsCSVWriterService.generateMetricsReport(codeMetricsClassResultList);
        List<String> expectedMetricsToDisplay =
            Arrays.asList("NO_OF_MODIFIERS", "NO_OF_PRIVATE_METHODS", "COUPLING_BETWEEN_OBJECTS");
        expectedMetricsToDisplay.stream().forEach(metric -> {
            assertTrue(csvData.contains("CLASS," + metric));
        });

        List<String> metricsWhichShouldNotDisplay = Arrays
            .asList("NO_OF_FIELDS", "NO_OF_COMPARISONS", "NO_OF_PARENTHESIZED_EXPRESSIONS",
                "DEPTH_INHERITANCE_TREE");
        metricsWhichShouldNotDisplay.stream().forEach(metric -> {
            assertFalse(csvData.contains("CLASS," + metric));
        });
    }

    @Test
    public void reportsDataInPSVFormatBasedOnClassFilter() throws Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList =
            codeMetricsDiffService.getMetricsFromSourceCode(exclusionPath);
        String csvData = metricsPSVWriterService.generateMetricsReport(codeMetricsClassResultList);
        List<String> expectedMetricsToDisplay =
            Arrays.asList("NO_OF_MODIFIERS", "NO_OF_PRIVATE_METHODS", "COUPLING_BETWEEN_OBJECTS");
        expectedMetricsToDisplay.stream().forEach(metric -> {
            assertTrue(csvData.contains("CLASS|" + metric));
        });

        List<String> metricsWhichShouldNotDisplay = Arrays
            .asList("NO_OF_FIELDS", "NO_OF_COMPARISONS", "NO_OF_PARENTHESIZED_EXPRESSIONS",
                "DEPTH_INHERITANCE_TREE");
        metricsWhichShouldNotDisplay.stream().forEach(metric -> {
            assertFalse(csvData.contains("CLASS|" + metric));
        });
    }

    @Test
    public void reportsDataInMDFormatBasedOnClassFilter() throws Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList =
            codeMetricsDiffService.getMetricsFromSourceCode(exclusionPath);
        String markdownData =
            metricsMDWriterService.generateMetricsReport(codeMetricsClassResultList);
        List<String> expectedMetricsToDisplay =
            Arrays.asList("NO_OF_MODIFIERS", "NO_OF_PRIVATE_METHODS", "COUPLING_BETWEEN_OBJECTS");
        expectedMetricsToDisplay.stream().forEach(metric -> {
            assertTrue(markdownData.contains(metric));
        });

        List<String> metricsWhichShouldNotDisplay = Arrays
            .asList("NO_OF_FIELDS", "NO_OF_COMPARISONS", "NO_OF_PARENTHESIZED_EXPRESSIONS",
                "DEPTH_INHERITANCE_TREE");
        metricsWhichShouldNotDisplay.stream().forEach(metric -> {
            assertFalse(markdownData.contains(metric));
        });
    }

    @Test
    public void reportsDataInHTMLFormatBasedOnClassFilter() throws Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList =
            codeMetricsDiffService.getMetricsFromSourceCode(exclusionPath);
        String htmlData =
            metricsHTMLWriterService.generateMetricsReport(codeMetricsClassResultList);
        List<String> expectedMetricsToDisplay =
            Arrays.asList("NO_OF_MODIFIERS", "NO_OF_PRIVATE_METHODS", "COUPLING_BETWEEN_OBJECTS");
        expectedMetricsToDisplay.stream().forEach(metric -> {
            assertTrue(htmlData.contains("<td align=\"center\">" + metric + "</td>"));
        });

        List<String> metricsWhichShouldNotDisplay = Arrays
            .asList("NO_OF_FIELDS", "NO_OF_COMPARISONS", "NO_OF_PARENTHESIZED_EXPRESSIONS",
                "DEPTH_INHERITANCE_TREE");
        metricsWhichShouldNotDisplay.stream().forEach(metric -> {
            assertFalse(htmlData.contains("<td align=\"center\">" + metric + "</td>"));
        });
    }

}
