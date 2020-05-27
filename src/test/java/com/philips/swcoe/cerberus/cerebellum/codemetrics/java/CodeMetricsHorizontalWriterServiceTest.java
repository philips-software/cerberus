package com.philips.swcoe.cerberus.cerebellum.codemetrics.java;

import com.google.common.base.Splitter;
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

class CodeMetricsHorizontalWriterServiceTest {


    private String previousPath = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE_PREVIOUS;
    private String currentPath = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE_CURRENT;
    private String classConfigPath = RESOURCES + PATH_SEPARATOR + "class_metrics_to_display.properties";
    private String methodConfigPath = RESOURCES + PATH_SEPARATOR + "method_metrics_to_display.properties";

    private CodeMetricsDiffService codeMetricsDiffService;
    private CodeMetricsHorizontalWriterService codeMetricsHorizontalWriterService;
    private List<CodeMetricsClassResult> codeMetricsClassResultList;
    List<String> classConfig;
    List<String> methodConfig;

    @BeforeEach
    public void setupJavaCodeMetricsWithDiff() throws IOException {
        codeMetricsDiffService = new CodeMetricsDiffService(previousPath, currentPath);
        classConfig = Files.readAllLines(new File(classConfigPath).toPath(), Charset.defaultCharset());
        methodConfig = Files.readAllLines(new File(methodConfigPath).toPath(), Charset.defaultCharset());
    }

    @Test
    public void shouldWriteClassMetricsandMethodMetricsInHorizontalFormatInCSV() throws Exception {
        getCodeClassResult(',');
        String csvData = codeMetricsHorizontalWriterService.generateMetricsReport(codeMetricsClassResultList);
        assertEquals(15,  csvData.split(System.getProperty("line.separator")).length);
    }

    private void getCodeClassResult(char c) throws IOException {
        codeMetricsHorizontalWriterService = new CodeMetricsHorizontalWriterService(classConfig, methodConfig, c);
        codeMetricsClassResultList = codeMetricsDiffService.getMetricsFromSourceCode();
    }

    @Test
    public void shouldRespectClassFilterInCSVReport() throws  Exception {
        getCodeClassResult(',');
        String csvData = codeMetricsHorizontalWriterService.generateMetricsReport(codeMetricsClassResultList);
        List<String> listOfData = Splitter.on(System.lineSeparator()).trimResults().splitToList(csvData);

        assertTrue(getLineToAssert(listOfData, "Triangle.java,Shapes.Triangle,CLASS").contains("NO_OF_MODIFIERS,1,0"));
        assertTrue(getLineToAssert(listOfData, "Triangle.java,Shapes.Triangle,CLASS").contains("COUPLING_BETWEEN_OBJECTS,1,0"));
        assertTrue(getLineToAssert(listOfData, "Rectangle.java,Shapes.Rectangle,CLASS").contains("COUPLING_BETWEEN_OBJECTS,3,2"));
        assertTrue( getLineToAssert(listOfData, "Rhombus.java,Shapes.Rhombus,CLASS").contains("NO_OF_MODIFIERS,0,1"));
        assertTrue( getLineToAssert(listOfData, "Rhombus.java,Shapes.Rhombus,CLASS").contains("NO_OF_PRIVATE_METHODS,0,2"));
        assertTrue( getLineToAssert(listOfData, "Rhombus.java,Shapes.Rhombus,CLASS").contains("COUPLING_BETWEEN_OBJECTS,0,2"));

        List<String> metricsWhichShouldNotDisplay = Arrays.asList("NO_OF_FIELDS","NO_OF_COMPARISONS","NO_OF_PARENTHESIZED_EXPRESSIONS", "DEPTH_INHERITANCE_TREE");
        metricsWhichShouldNotDisplay.stream().forEach(metric -> {
            assertFalse(csvData.contains(metric));
        });

    }

    private String getLineToAssert(List<String> listOfData, String s) {
        return listOfData.stream().filter((line) -> line.contains(s)).findFirst().get();
    }

    @Test
    public void shouldNotThrowAnExceptionWhenAllIsWellWhileWritingCSV() throws  Exception {
        getCodeClassResult(',');
        assertDoesNotThrow(() -> codeMetricsHorizontalWriterService.generateMetricsReport(codeMetricsClassResultList));
    }

    @Test
    public void shouldNotWriteClassMetricsOfClassesWhichAreNotChangedInCSV() throws  Exception {
        getCodeClassResult(',');
        String csvData = codeMetricsHorizontalWriterService.generateMetricsReport(codeMetricsClassResultList);
        assertFalse(csvData.contains("Circle.java"));
        assertFalse(csvData.contains("Shape.java"));
        assertFalse(csvData.contains("Main.java"));
    }

    @Test
    public void shouldWriteClassMetricsandMethodMetricsInHorizontalFormatInPSV() throws Exception {
        getCodeClassResult('|');
        String csvData = codeMetricsHorizontalWriterService.generateMetricsReport(codeMetricsClassResultList);
        assertEquals(15,  csvData.split(System.getProperty("line.separator")).length);
    }

    @Test
    public void shouldRespectClassFilterInPSVReport() throws  Exception {
        getCodeClassResult('|');
        String psvData = codeMetricsHorizontalWriterService.generateMetricsReport(codeMetricsClassResultList);

        List<String> listOfData = Splitter.on(System.lineSeparator()).trimResults().splitToList(psvData);

        assertTrue(getLineToAssert(listOfData, "Triangle.java|Shapes.Triangle|CLASS").contains("NO_OF_MODIFIERS|1|0"));
        assertTrue(getLineToAssert(listOfData, "Triangle.java|Shapes.Triangle|CLASS").contains("COUPLING_BETWEEN_OBJECTS|1|0"));
        assertTrue(getLineToAssert(listOfData, "Rectangle.java|Shapes.Rectangle|CLASS").contains("COUPLING_BETWEEN_OBJECTS|3|2"));
        assertTrue( getLineToAssert(listOfData, "Rhombus.java|Shapes.Rhombus|CLASS").contains("NO_OF_MODIFIERS|0|1"));
        assertTrue( getLineToAssert(listOfData, "Rhombus.java|Shapes.Rhombus|CLASS").contains("NO_OF_PRIVATE_METHODS|0|2"));
        assertTrue( getLineToAssert(listOfData, "Rhombus.java|Shapes.Rhombus|CLASS").contains("COUPLING_BETWEEN_OBJECTS|0|2"));

        List<String> metricsWhichShouldNotDisplay = Arrays.asList("NO_OF_FIELDS","NO_OF_COMPARISONS","NO_OF_PARENTHESIZED_EXPRESSIONS", "DEPTH_INHERITANCE_TREE");
        metricsWhichShouldNotDisplay.stream().forEach(metric -> {
            assertFalse(psvData.contains(metric));
        });
    }

    @Test
    public void shouldNotThrowAnExceptionWhenAllIsWellWhileWritingPSV() throws  Exception {
        getCodeClassResult('|');
        assertDoesNotThrow(() -> codeMetricsHorizontalWriterService.generateMetricsReport(codeMetricsClassResultList));
    }

    @Test
    public void shouldNotWriteClassMetricsOfClassesWhichAreNotChangedInPSV() throws  Exception {
        getCodeClassResult('|');
        String csvData = codeMetricsHorizontalWriterService.generateMetricsReport(codeMetricsClassResultList);
        assertFalse(csvData.contains("Circle.java"));
        assertFalse(csvData.contains("Shape.java"));
        assertFalse(csvData.contains("Main.java"));
    }

}