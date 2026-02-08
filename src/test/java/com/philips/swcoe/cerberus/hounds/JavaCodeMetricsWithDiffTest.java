package com.philips.swcoe.cerberus.hounds;

import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.PATH_SEPARATOR;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.RESOURCES;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.TEST_JAVA_CODE_CURRENT;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.TEST_JAVA_CODE_PREVIOUS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.base.Splitter;
import com.philips.swcoe.cerberus.unit.test.utils.CerberusBaseTest;
import java.util.Arrays;
import java.util.List;
import picocli.CommandLine;

public class JavaCodeMetricsWithDiffTest extends CerberusBaseTest {

    private final String previousPath = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE_PREVIOUS;
    private final String currentPath = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE_CURRENT;
    private final String classConfig = RESOURCES + PATH_SEPARATOR + "class_metrics_to_display.properties";
    private final String methodConfig =
        RESOURCES + PATH_SEPARATOR + "method_metrics_to_display.properties";
    private JavaCodeMetricsWithDiff javaCodeMetricsWithDiff;

    @BeforeEach
    public void beforeEach() {
        super.setUpStreams();
        javaCodeMetricsWithDiff = new JavaCodeMetricsWithDiff();
    }

    @AfterEach
    public void afterEach() {
        super.restoreStreams();
    }

    @Test
    public void testJavacodeMetricsHoundWithNoParameter() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute();
        assertNotEquals(0, exitCode);
        assertTrue(getModifiedErrorStream().toString().contains(
            "ERROR: Specify Absolute Path to your source code which has previous version"));
        assertTrue(getModifiedErrorStream().toString()
            .contains("ERROR: Specify the format of the report"));
        assertTrue(getModifiedErrorStream().toString().contains(
            "ERROR: Specify the absolute path to your source code which has current version"));
        assertTrue(getModifiedErrorStream().toString()
            .contains("ERROR: Specify report structure vertical or horizontal"));
    }

    @Test
    public void testJCMDWithMissedOutParameterCurrent() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff)
            .execute("--previous", previousPath);
        assertNotEquals(0, exitCode);
        assertTrue(getModifiedErrorStream().toString()
            .contains("Specify the absolute path to your source code which has current version"));
    }

    @Test
    public void testJCMDWithMissedOutTwoParameters() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff)
            .execute("--current", previousPath, "--format", "abc");
        assertNotEquals(0, exitCode);
        assertTrue(getModifiedErrorStream().toString()
            .contains("Specify Absolute Path to your source code which has previous version"));
    }

    @Test
    public void testJSCMDWithInvalidReportDelimiter() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(
            "--current", currentPath, "--previous", previousPath, "--format", "abc");
        assertNotEquals(0, exitCode);
        assertTrue(
            getModifiedErrorStream().toString().contains("ERROR: must match \"csv|psv|md|html\""));
        assertTrue(getModifiedErrorStream().toString()
            .contains("ERROR: Specify report structure vertical or horizontal"));
    }

    @Test
    public void shouldPrintCSVReportInVerticalForValidParameters() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(
            "--current", currentPath, "--previous", previousPath, "--format", "csv",
            "--structure", "vertical");
        assertEquals(0, exitCode);
        assertTrue(getModifiedOutputStream().toString()
            .contains("FILE,TYPE,METRIC,NEW_VALUE,OLD_VALUE,CLASS"));
        assertTrue(getModifiedOutputStream().toString()
            .contains("Triangle.java,CLASS,LINES_OF_CODE,16,0,Shapes.Triangle"));
        assertTrue(getModifiedOutputStream().toString()
            .contains("Rectangle.java,CLASS,LINES_OF_CODE,36,26,Shapes.Rectangle"));
        assertTrue(getModifiedOutputStream().toString()
            .contains("Rhombus.java,CLASS,LINES_OF_CODE,0,26,Shapes.Rhombus"));
        assertTrue(getModifiedOutputStream().toString().contains(
            "Triangle.java,METHOD,COMPLEXITY_OF_METHOD,1,0,Shapes.Triangle::getHeight/0"));
        assertTrue(getModifiedOutputStream().toString().contains(
            "Rectangle.java,METHOD,"
                + "COMPLEXITY_OF_METHOD,0,1,\"Shapes.Rectangle:CONSTRUCTOR:Rectangle/2[double,"
                + "double]\""));
    }

    @Test
    public void shouldPrintPSVReportInVerticalForValidParameters() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(
            "--current", currentPath, "--previous", previousPath, "--format", "psv",
            "--structure", "vertical");
        assertEquals(0, exitCode);
        assertTrue(getModifiedOutputStream().toString()
            .contains("FILE|TYPE|METRIC|NEW_VALUE|OLD_VALUE|CLASS"));
        assertTrue(getModifiedOutputStream().toString()
            .contains("Triangle.java|CLASS|LINES_OF_CODE|16|0|Shapes.Triangle"));
        assertTrue(getModifiedOutputStream().toString()
            .contains("Rectangle.java|CLASS|LINES_OF_CODE|36|26|Shapes.Rectangle"));
        assertTrue(getModifiedOutputStream().toString()
            .contains("Rhombus.java|CLASS|LINES_OF_CODE|0|26|Shapes.Rhombus"));
        assertTrue(getModifiedOutputStream().toString().contains(
            "Triangle.java|METHOD|COMPLEXITY_OF_METHOD|1|0|Shapes.Triangle::getHeight/0"));
        assertTrue(getModifiedOutputStream().toString().contains(
            "Rectangle.java|METHOD|COMPLEXITY_OF_METHOD|0|1|Shapes.Rectangle:CONSTRUCTOR"
                + ":Rectangle/2[double,double]"));
        assertTrue(getModifiedOutputStream().toString()
            .contains("Rhombus.java|METHOD|LINES_OF_CODE|0|3|Shapes.Rhombus::setHeight/1[double]"));
    }


    @Test
    public void shouldPrintCSVReportInHorizontalForValidParameters() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(
            "--current", currentPath, "--previous", previousPath, "--format", "csv",
            "--structure", "horizontal");
        assertEquals(0, exitCode);
        List<String> listOfData = Splitter.on(System.lineSeparator()).trimResults()
            .splitToList(getModifiedOutputStream().toString());
        assertTrue(getLineToAssert(listOfData, "Triangle.java,Shapes.Triangle,CLASS")
            .contains("NO_OF_MODIFIERS,1,0"));
        assertTrue(getLineToAssert(listOfData, "Triangle.java,Shapes.Triangle,CLASS")
            .contains("LINES_OF_CODE,16,0"));
        assertTrue(getLineToAssert(listOfData, "Triangle.java,Shapes.Triangle,CLASS")
            .contains("NO_OF_ASSIGNMENTS,2,0"));
        assertTrue(getLineToAssert(listOfData, "Triangle.java,Shapes.Triangle,CLASS")
            .contains("NO_OF_UNIQUE_WORDS,8,0"));
        assertTrue(getLineToAssert(listOfData, "Triangle.java,Shapes.Triangle,CLASS")
            .contains("DEPTH_INHERITANCE_TREE,2,0"));
        assertTrue(getLineToAssert(listOfData, "Triangle.java,Shapes.Triangle,CLASS")
            .contains("WEIGHT_METHOD_CLASS,4,0"));
        assertTrue(getLineToAssert(listOfData, "Triangle.java,Shapes.Triangle,CLASS")
            .contains("COUPLING_BETWEEN_OBJECTS,1,0"));
        assertTrue(getLineToAssert(listOfData, "Triangle.java,Shapes.Triangle,CLASS")
            .contains("NO_OF_RETURNS,2,0"));
        assertTrue(getLineToAssert(listOfData,
            "Rectangle.java,Shapes.Rectangle::calculatePerimeter/0,METHOD")
            .contains("START_LINE_NO,39,29"));
        assertTrue(
            getLineToAssert(listOfData, "Rhombus.java,Shapes.Rhombus::calculatePerimeter/0,METHOD")
                .contains("COMPLEXITY_OF_METHOD,0,1"));
        assertTrue(
            getLineToAssert(listOfData, "Rhombus.java,Shapes.Rhombus::calculatePerimeter/0,METHOD")
                .contains("LINES_OF_CODE,0,3"));
        assertTrue(
            getLineToAssert(listOfData, "Rhombus.java,Shapes.Rhombus::calculatePerimeter/0,METHOD")
                .contains("START_LINE_NO,0,29"));
        assertTrue(
            getLineToAssert(listOfData, "Rhombus.java,Shapes.Rhombus::calculatePerimeter/0,METHOD")
                .contains("NO_OF_NOS,0,1"));
        assertTrue(
            getLineToAssert(listOfData, "Rhombus.java,Shapes.Rhombus::calculatePerimeter/0,METHOD")
                .contains("NO_OF_MATH_OPERATIONS,0,1"));
        assertTrue(
            getLineToAssert(listOfData, "Rhombus.java,Shapes.Rhombus::calculatePerimeter/0,METHOD")
                .contains("NO_OF_UNIQUE_WORDS,0,2"));
    }

    @Test
    public void shouldPrintPSVReportInHorizontalForValidParameters() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(
            "--current", currentPath, "--previous", previousPath, "--format", "psv",
            "--structure", "horizontal");
        assertEquals(0, exitCode);
        List<String> listOfData = Splitter.on(System.lineSeparator()).trimResults()
            .splitToList(getModifiedOutputStream().toString());
        assertTrue(getLineToAssert(listOfData, "Triangle.java|Shapes.Triangle|CLASS")
            .contains("NO_OF_MODIFIERS|1|0"));
        assertTrue(getLineToAssert(listOfData, "Triangle.java|Shapes.Triangle|CLASS")
            .contains("LINES_OF_CODE|16|0"));
        assertTrue(getLineToAssert(listOfData, "Triangle.java|Shapes.Triangle|CLASS")
            .contains("NO_OF_ASSIGNMENTS|2|0"));
        assertTrue(getLineToAssert(listOfData, "Triangle.java|Shapes.Triangle|CLASS")
            .contains("NO_OF_UNIQUE_WORDS|8|0"));
        assertTrue(getLineToAssert(listOfData, "Triangle.java|Shapes.Triangle|CLASS")
            .contains("DEPTH_INHERITANCE_TREE|2|0"));
        assertTrue(getLineToAssert(listOfData, "Triangle.java|Shapes.Triangle|CLASS")
            .contains("WEIGHT_METHOD_CLASS|4|0"));
        assertTrue(getLineToAssert(listOfData, "Triangle.java|Shapes.Triangle|CLASS")
            .contains("COUPLING_BETWEEN_OBJECTS|1|0"));
        assertTrue(getLineToAssert(listOfData, "Triangle.java|Shapes.Triangle|CLASS")
            .contains("NO_OF_RETURNS|2|0"));
        assertTrue(getLineToAssert(listOfData,
            "Rectangle.java|Shapes.Rectangle::calculatePerimeter/0|METHOD")
            .contains("START_LINE_NO|39|29"));
        assertTrue(
            getLineToAssert(listOfData, "Rhombus.java|Shapes.Rhombus::calculatePerimeter/0|METHOD")
                .contains("COMPLEXITY_OF_METHOD|0|1"));
        assertTrue(
            getLineToAssert(listOfData, "Rhombus.java|Shapes.Rhombus::calculatePerimeter/0|METHOD")
                .contains("LINES_OF_CODE|0|3"));
        assertTrue(
            getLineToAssert(listOfData, "Rhombus.java|Shapes.Rhombus::calculatePerimeter/0|METHOD")
                .contains("START_LINE_NO|0|29"));
        assertTrue(
            getLineToAssert(listOfData, "Rhombus.java|Shapes.Rhombus::calculatePerimeter/0|METHOD")
                .contains("NO_OF_NOS|0|1"));
        assertTrue(
            getLineToAssert(listOfData, "Rhombus.java|Shapes.Rhombus::calculatePerimeter/0|METHOD")
                .contains("NO_OF_MATH_OPERATIONS|0|1"));
        assertTrue(
            getLineToAssert(listOfData, "Rhombus.java|Shapes.Rhombus::calculatePerimeter/0|METHOD")
                .contains("NO_OF_UNIQUE_WORDS|0|2"));
    }

    @Test
    public void testJCMDWithValidParameterForPSVReportWithConfig() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(
            getArgsForHound("psv", classConfig, methodConfig, "vertical"));

        assertEquals(0, exitCode);
        assertDisplayOfMetricsBasedOnConfig("CLASS|");
    }

    @Test
    public void testJCMDWithValidParameterForMDReportWithConfig() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(
            getArgsForHound("md", classConfig, methodConfig, "vertical"));
        assertEquals(0, exitCode);
        assertDisplayOfMetricsBasedOnConfig("");
    }

    @Test
    public void testJCMDWithValidParameterForHTMLReportWithConfig() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(
            getArgsForHound("html", classConfig, methodConfig, "vertical"));

        assertEquals(0, exitCode);
        assertDisplayOfMetricsBasedOnConfig("");
    }

    @Test
    public void testJCMDWithValidParameterForCSVReportWithConfig() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(
            getArgsForHound("csv", classConfig, methodConfig, "vertical"));
        assertEquals(0, exitCode);
        assertDisplayOfMetricsBasedOnConfig("");
    }

    @Test
    public void shouldThrowErrorForBadClassConfigFile() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(
            getArgsForHound("csv", "blahblah", classConfig, "vertical"));
        assertNotEquals(0, exitCode);
        assertTrue(getModifiedErrorStream().toString().contains("Specify a valid absolute path"));
    }

    @Test
    public void shouldThrowErrorForBadMethodConfigFile() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(
            getArgsForHound("csv", classConfig, "blahblah", "vertical"));
        assertNotEquals(0, exitCode);
        assertTrue(getModifiedErrorStream().toString().contains("Specify a valid absolute path"));
    }

    @Test
    public void shouldThrowErrorForBadReportFormat() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(
            getArgsForHound("csv", classConfig, "blahblah", "blahblah"));
        assertNotEquals(0, exitCode);
        assertTrue(
            getModifiedErrorStream().toString().contains("must match \"vertical|horizontal\""));
    }

    @Test
    public void shouldThrowErrorForMarkdownInHorizontalFormat() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(
            getArgsForHound("md", classConfig, methodConfig, "horizontal"));
        assertNotEquals(0, exitCode);
        assertTrue(getModifiedErrorStream().toString()
            .contains("Markdown and HTML format can be used only with vertical metrics structure"));
    }

    @Test
    public void shouldThrowErrorForHTMLInHorizontalFormat() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(
            getArgsForHound("html", classConfig, methodConfig, "horizontal"));
        assertNotEquals(0, exitCode);
        assertTrue(getModifiedErrorStream().toString()
            .contains("Markdown and HTML format can be used only with vertical metrics structure"));
    }

    private String getLineToAssert(List<String> listOfData, String s) {
        return listOfData.stream().filter((line) -> line.contains(s)).findFirst().get();
    }


    private String[] getArgsForHound(String csv, String classConfig, String methodConfig,
                                     String vertical) {
        return new String[] {
            "--current", currentPath,
            "--previous", previousPath,
            "--format", csv,
            "--class-config", classConfig,
            "--method-config", methodConfig,
            "--structure", vertical
        };
    }


    private void assertDisplayOfMetricsBasedOnConfig(String prefix) {
        List<String> expectedMetricsToDisplay = Arrays
            .asList("NO_OF_MODIFIERS", "NO_OF_PRIVATE_METHODS", "COUPLING_BETWEEN_OBJECTS");
        expectedMetricsToDisplay.stream().forEach(metric -> {
            assertTrue(getModifiedOutputStream().toString().contains(prefix + metric));
        });

        List<String> metricsWhichShouldNotDisplay = Arrays
            .asList("NO_OF_FIELDS", "NO_OF_COMPARISONS", "NO_OF_PARENTHESIZED_EXPRESSIONS",
                "DEPTH_INHERITANCE_TREE");
        metricsWhichShouldNotDisplay.stream().forEach(metric -> {
            assertFalse(getModifiedOutputStream().toString().contains(prefix + metric));
        });
    }
}
