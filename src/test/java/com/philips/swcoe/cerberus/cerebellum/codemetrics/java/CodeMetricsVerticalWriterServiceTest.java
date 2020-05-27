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
    private CodeMetricsVerticalWriterService javaMetricsCSVWriterService;
    private List<CodeMetricsClassResult> codeMetricsClassResultList;


    @BeforeEach
    public void setupJavaCodeMetricsWithDiff() throws IOException {
        codeMetricsDiffService = new CodeMetricsDiffService(previousPath, currentPath);
        List<String> classConfig = Files.readAllLines(new File(classConfigPath).toPath(), Charset.defaultCharset());
        List<String> methodConfig = Files.readAllLines(new File(methodConfigPath).toPath(), Charset.defaultCharset());
        javaMetricsCSVWriterService = new CodeMetricsVerticalWriterService(classConfig, methodConfig, ',');
        codeMetricsClassResultList = codeMetricsDiffService.getMetricsFromSourceCode();
    }

    @Test
    public void shouldWriteClassMetricsandMethodMetricsInVerticalFashion() throws Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList = codeMetricsDiffService.getMetricsFromSourceCode();
        String csvData = javaMetricsCSVWriterService.generateMetricsReport(codeMetricsClassResultList);
        assertEquals(44,  csvData.split(System.getProperty("line.separator")).length);
    }


    @Test
    public void shouldWriteClassMetricsWhichAreChangedInCSVFormat() throws  Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList = codeMetricsDiffService.getMetricsFromSourceCode();
        String csvData = javaMetricsCSVWriterService.generateMetricsReport(codeMetricsClassResultList);
        assertTrue(csvData.contains("FILE,CLASS,TYPE,METRIC,NEW_VALUE,OLD_VALUE"));
        assertTrue(csvData.contains("Rhombus.java,Shapes.Rhombus,CLASS,NO_OF_MODIFIERS,0,1"));
        assertTrue(csvData.contains("Triangle.java,Shapes.Triangle,CLASS,COUPLING_BETWEEN_OBJECTS,1,0"));
        assertTrue(csvData.contains("Rectangle.java,Shapes.Rectangle,CLASS,COUPLING_BETWEEN_OBJECTS,3,2"));
    }

    @Test
    public void shouldNotWriteClassMetricsWhichAreNotChangedInCSVFormat() throws  Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList = codeMetricsDiffService.getMetricsFromSourceCode();
        String csvData = javaMetricsCSVWriterService.generateMetricsReport(codeMetricsClassResultList);
        assertFalse(csvData.contains("Circle.java"));
        assertFalse(csvData.contains("Shape.java"));
        assertFalse(csvData.contains("Main.java"));
    }

    @Test
    public void reportsDataInCSVFormatThrowsException() throws  Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList = codeMetricsDiffService.getMetricsFromSourceCode();
        assertDoesNotThrow(() -> javaMetricsCSVWriterService.generateMetricsReport(codeMetricsClassResultList));
    }

    @Test
    public void reportsDataInCSVFormatBasedOnClassFilter() throws  Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList = codeMetricsDiffService.getMetricsFromSourceCode();
        String csvData = javaMetricsCSVWriterService.generateMetricsReport(codeMetricsClassResultList);
        List<String> expectedMetricsToDisplay = Arrays.asList("NO_OF_MODIFIERS","NO_OF_PRIVATE_METHODS","COUPLING_BETWEEN_OBJECTS");
        expectedMetricsToDisplay.stream().forEach(metric -> {
            assertTrue(csvData.contains("CLASS," + metric));
        });

        List<String> metricsWhichShouldNotDisplay = Arrays.asList("NO_OF_FIELDS","NO_OF_COMPARISONS","NO_OF_PARENTHESIZED_EXPRESSIONS", "DEPTH_INHERITANCE_TREE");
        metricsWhichShouldNotDisplay.stream().forEach(metric -> {
            assertFalse(csvData.contains("CLASS," + metric));
        });
    }

}