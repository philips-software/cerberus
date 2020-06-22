package com.philips.swcoe.cerberus.cerebellum.codemetrics.java;

import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsClassResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class CodeMetricsVerticalWriterServiceTest {

    private String previousPath = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE_PREVIOUS;
    private String currentPath = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE_CURRENT;
    private String classConfigPath = RESOURCES + PATH_SEPARATOR + "class_metrics_to_display.properties";
    private String methodConfigPath = RESOURCES + PATH_SEPARATOR + "method_metrics_to_display.properties";

    private CodeMetricsDiffService codeMetricsDiffService;
    private CodeMetricsVerticalWriterService metricsCSVWriterService;
    private CodeMetricsVerticalWriterService metricsPSVWriterService;
    private CodeMetricsVerticalWriterService metricsMDWriterService;
    private CodeMetricsVerticalWriterService metricsHTMLWriterService;


    @BeforeEach
    public void setupJavaCodeMetricsWithDiff() throws IOException {
        codeMetricsDiffService = new CodeMetricsDiffService(previousPath, currentPath);
        List<String> classConfig = Files.readAllLines(new File(classConfigPath).toPath(), Charset.defaultCharset());
        List<String> methodConfig = Files.readAllLines(new File(methodConfigPath).toPath(), Charset.defaultCharset());
        metricsCSVWriterService = new CodeMetricsVerticalWriterService(classConfig, methodConfig, "CSV");
        metricsPSVWriterService = new CodeMetricsVerticalWriterService(classConfig, methodConfig, "PSV");
        metricsMDWriterService = new CodeMetricsVerticalWriterService(classConfig, methodConfig, "MD");
        metricsHTMLWriterService = new CodeMetricsVerticalWriterService(classConfig, methodConfig, "HTML");
    }

    @Test
    public void shouldWriteClassMetricsandMethodMetricsInVerticalFashion() throws Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList = codeMetricsDiffService.getMetricsFromSourceCode();
        String csvData = metricsCSVWriterService.generateMetricsReport(codeMetricsClassResultList);
        assertEquals(44,  csvData.split(System.getProperty("line.separator")).length);
    }


    @Test
    public void shouldWriteClassMetricsWhichAreChangedInCSVFormat() throws  Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList = codeMetricsDiffService.getMetricsFromSourceCode();
        String csvData = metricsCSVWriterService.generateMetricsReport(codeMetricsClassResultList);
        assertTrue(csvData.contains("FILE,CLASS,TYPE,METRIC,NEW_VALUE,OLD_VALUE"));
        assertTrue(csvData.contains("Rhombus.java,Shapes.Rhombus,CLASS,NO_OF_MODIFIERS,0,1"));
        assertTrue(csvData.contains("Triangle.java,Shapes.Triangle,CLASS,COUPLING_BETWEEN_OBJECTS,1,0"));
        assertTrue(csvData.contains("Rectangle.java,Shapes.Rectangle,CLASS,COUPLING_BETWEEN_OBJECTS,3,2"));
    }

    @Test
    public void shouldNotWriteClassMetricsWhichAreNotChangedInCSVFormat() throws  Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList = codeMetricsDiffService.getMetricsFromSourceCode();
        String csvData = metricsCSVWriterService.generateMetricsReport(codeMetricsClassResultList);
        assertFalse(csvData.contains("Circle.java"));
        assertFalse(csvData.contains("Shape.java"));
        assertFalse(csvData.contains("Main.java"));
    }

    @Test
    public void reportsDataInCSVFormatThrowsException() throws  Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList = codeMetricsDiffService.getMetricsFromSourceCode();
        assertDoesNotThrow(() -> metricsCSVWriterService.generateMetricsReport(codeMetricsClassResultList));
    }

    @Test
    public void reportsDataInCSVFormatBasedOnClassFilter() throws  Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList = codeMetricsDiffService.getMetricsFromSourceCode();
        String csvData = metricsCSVWriterService.generateMetricsReport(codeMetricsClassResultList);
        List<String> expectedMetricsToDisplay = Arrays.asList("NO_OF_MODIFIERS","NO_OF_PRIVATE_METHODS","COUPLING_BETWEEN_OBJECTS");
        expectedMetricsToDisplay.stream().forEach(metric -> {
            assertTrue(csvData.contains("CLASS," + metric));
        });

        List<String> metricsWhichShouldNotDisplay = Arrays.asList("NO_OF_FIELDS","NO_OF_COMPARISONS","NO_OF_PARENTHESIZED_EXPRESSIONS", "DEPTH_INHERITANCE_TREE");
        metricsWhichShouldNotDisplay.stream().forEach(metric -> {
            assertFalse(csvData.contains("CLASS," + metric));
        });
    }

    @Test
    public void reportsDataInPSVFormatBasedOnClassFilter() throws  Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList = codeMetricsDiffService.getMetricsFromSourceCode();
        String csvData = metricsPSVWriterService.generateMetricsReport(codeMetricsClassResultList);
        List<String> expectedMetricsToDisplay = Arrays.asList("NO_OF_MODIFIERS","NO_OF_PRIVATE_METHODS","COUPLING_BETWEEN_OBJECTS");
        expectedMetricsToDisplay.stream().forEach(metric -> {
            assertTrue(csvData.contains("CLASS|" + metric));
        });

        List<String> metricsWhichShouldNotDisplay = Arrays.asList("NO_OF_FIELDS","NO_OF_COMPARISONS","NO_OF_PARENTHESIZED_EXPRESSIONS", "DEPTH_INHERITANCE_TREE");
        metricsWhichShouldNotDisplay.stream().forEach(metric -> {
            assertFalse(csvData.contains("CLASS|" + metric));
        });
    }

    @Test
    public void reportsDataInMDFormatBasedOnClassFilter() throws  Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList = codeMetricsDiffService.getMetricsFromSourceCode();
        String markdownData = metricsMDWriterService.generateMetricsReport(codeMetricsClassResultList);
        List<String> expectedMetricsToDisplay = Arrays.asList("NO_OF_MODIFIERS","NO_OF_PRIVATE_METHODS","COUPLING_BETWEEN_OBJECTS");
        expectedMetricsToDisplay.stream().forEach(metric -> {
            assertTrue(markdownData.contains(metric));
        });

        List<String> metricsWhichShouldNotDisplay = Arrays.asList("NO_OF_FIELDS","NO_OF_COMPARISONS","NO_OF_PARENTHESIZED_EXPRESSIONS", "DEPTH_INHERITANCE_TREE");
        metricsWhichShouldNotDisplay.stream().forEach(metric -> {
            assertFalse(markdownData.contains(metric));
        });
    }

    @Test
    public void reportsDataInHTMLFormatBasedOnClassFilter() throws  Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList = codeMetricsDiffService.getMetricsFromSourceCode();
        String htmlData = metricsHTMLWriterService.generateMetricsReport(codeMetricsClassResultList);
        List<String> expectedMetricsToDisplay = Arrays.asList("NO_OF_MODIFIERS","NO_OF_PRIVATE_METHODS","COUPLING_BETWEEN_OBJECTS");
        expectedMetricsToDisplay.stream().forEach(metric -> {
            assertTrue(htmlData.contains("<td align=\"center\">" + metric + "</td>"));
        });

        List<String> metricsWhichShouldNotDisplay = Arrays.asList("NO_OF_FIELDS","NO_OF_COMPARISONS","NO_OF_PARENTHESIZED_EXPRESSIONS", "DEPTH_INHERITANCE_TREE");
        metricsWhichShouldNotDisplay.stream().forEach(metric -> {
            assertFalse(htmlData.contains("<td align=\"center\">" + metric + "</td>"));
        });
    }

}