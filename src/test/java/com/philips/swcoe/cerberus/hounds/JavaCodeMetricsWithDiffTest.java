package com.philips.swcoe.cerberus.hounds;

import com.google.common.base.Splitter;
import com.philips.swcoe.cerberus.unit.test.utils.CerberusBaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.List;

import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class JavaCodeMetricsWithDiffTest extends CerberusBaseTest {

    private String previousPath = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE_PREVIOUS;
    private String currentPath = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE_CURRENT;
    private String configPath = RESOURCES + PATH_SEPARATOR + "class_metrics_to_display.properties";
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
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(new String[]{});
        assertNotEquals(0, exitCode);
        assertTrue(getModifiedErrorStream().toString().contains("ERROR: Specify Absolute Path to your source code which has previous version"));
        assertTrue(getModifiedErrorStream().toString().contains("ERROR: Specify the delimiter of the report"));
        assertTrue(getModifiedErrorStream().toString().contains("ERROR: Specify the absolute path to your source code which has current version"));
        assertTrue(getModifiedErrorStream().toString().contains("ERROR: Specify report structure vertical or horizontal"));
    }

    @Test
    public void testJCMDWithMissedOutParameterCurrent() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(new String[]{"--previous", previousPath});
        assertNotEquals(0, exitCode);
        assertTrue(getModifiedErrorStream().toString().contains("Specify the absolute path to your source code which has current version"));
    }

    @Test
    public void testJCMDWithMissedOutTwoParameters() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(new String[]{"--current", previousPath, "--delimiter", "abc"});
        assertNotEquals(0, exitCode);
        assertTrue(getModifiedErrorStream().toString().contains("Specify Absolute Path to your source code which has previous version"));
    }

    @Test
    public void testJSCMDWithInvalidReportDelimiter() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(new String[]{"--current", currentPath, "--previous", previousPath, "--delimiter", "abc"});
        assertNotEquals(0, exitCode);
        assertTrue(getModifiedErrorStream().toString().contains("ERROR: must match \"csv|psv\""));
        assertTrue(getModifiedErrorStream().toString().contains("Report delimiter CSV for comma separated or PSV for pipe separated"));
    }

    @Test
    public void shouldPrintCSVReportInVerticalForValidParameters() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(new String[]{"--current", currentPath, "--previous", previousPath, "--delimiter", "csv", "--format", "vertical"});
        assertEquals(0, exitCode);
        assertTrue(getModifiedOutputStream().toString().contains("FILE,CLASS,TYPE,METRIC,NEW_VALUE,OLD_VALUE"));
        assertTrue(getModifiedOutputStream().toString().contains("Triangle.java,Shapes.Triangle,CLASS,LINES_OF_CODE,17,0"));
        assertTrue(getModifiedOutputStream().toString().contains("Rectangle.java,Shapes.Rectangle,CLASS,LINES_OF_CODE,37,27"));
        assertTrue(getModifiedOutputStream().toString().contains("Rhombus.java,Shapes.Rhombus,CLASS,LINES_OF_CODE,0,27"));
    }

    @Test
    public void shouldPrintPSVReportInVerticalForValidParameters() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(new String[]{"--current", currentPath, "--previous", previousPath, "--delimiter", "psv", "--format", "vertical"});
        assertEquals(0, exitCode);
        assertTrue(getModifiedOutputStream().toString().contains("FILE|CLASS|TYPE|METRIC|NEW_VALUE|OLD_VALUE"));
        assertTrue(getModifiedOutputStream().toString().contains("Triangle.java|Shapes.Triangle|CLASS|LINES_OF_CODE|17|0"));
        assertTrue(getModifiedOutputStream().toString().contains("Rectangle.java|Shapes.Rectangle|CLASS|LINES_OF_CODE|37|27"));
        assertTrue(getModifiedOutputStream().toString().contains("Rhombus.java|Shapes.Rhombus|CLASS|LINES_OF_CODE|0|27"));
    }

    @Test
    public void shouldPrintCSVReportInHorizontalForValidParameters() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(new String[]{"--current", currentPath, "--previous", previousPath, "--delimiter", "csv", "--format", "horizontal"});
        assertEquals(0, exitCode);
        List<String> listOfData = Splitter.on(System.lineSeparator()).trimResults().splitToList(getModifiedOutputStream().toString());
        assertTrue(getLineToAssert(listOfData,"Triangle.java,Shapes.Triangle,CLASS").contains("NO_OF_MODIFIERS,1,0"));
        assertTrue(getLineToAssert(listOfData,"Triangle.java,Shapes.Triangle,CLASS").contains("LINES_OF_CODE,17,0"));
        assertTrue(getLineToAssert(listOfData,"Triangle.java,Shapes.Triangle,CLASS").contains("NO_OF_ASSIGNMENTS,2,0"));
        assertTrue(getLineToAssert(listOfData,"Triangle.java,Shapes.Triangle,CLASS").contains("NO_OF_UNIQUE_WORDS,8,0"));
        assertTrue(getLineToAssert(listOfData,"Triangle.java,Shapes.Triangle,CLASS").contains("DEPTH_INHERITANCE_TREE,2,0"));
        assertTrue(getLineToAssert(listOfData,"Triangle.java,Shapes.Triangle,CLASS").contains("WEIGHT_METHOD_CLASS,4,0"));
        assertTrue(getLineToAssert(listOfData,"Triangle.java,Shapes.Triangle,CLASS").contains("COUPLING_BETWEEN_OBJECTS,1,0"));
        assertTrue(getLineToAssert(listOfData,"Triangle.java,Shapes.Triangle,CLASS").contains("NO_OF_RETURNS,2,0"));
        assertTrue(getLineToAssert(listOfData,"Rectangle.java,Shapes.Rectangle::calculatePerimeter/0,METHOD").contains("START_LINE_NO,39,29"));
        assertTrue(getLineToAssert(listOfData,"Rhombus.java,Shapes.Rhombus::calculatePerimeter/0,METHOD").contains("COMPLEXITY_OF_METHOD,0,1"));
        assertTrue(getLineToAssert(listOfData,"Rhombus.java,Shapes.Rhombus::calculatePerimeter/0,METHOD").contains("LINES_OF_CODE,0,3"));
        assertTrue(getLineToAssert(listOfData,"Rhombus.java,Shapes.Rhombus::calculatePerimeter/0,METHOD").contains("START_LINE_NO,0,29"));
        assertTrue(getLineToAssert(listOfData,"Rhombus.java,Shapes.Rhombus::calculatePerimeter/0,METHOD").contains("NO_OF_NOS,0,1"));
        assertTrue(getLineToAssert(listOfData,"Rhombus.java,Shapes.Rhombus::calculatePerimeter/0,METHOD").contains("NO_OF_MATH_OPERATIONS,0,1"));
        assertTrue(getLineToAssert(listOfData,"Rhombus.java,Shapes.Rhombus::calculatePerimeter/0,METHOD").contains("NO_OF_UNIQUE_WORDS,0,2"));
    }

    @Test
    public void shouldPrintPSVReportInHorizontalForValidParameters() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(new String[]{"--current", currentPath, "--previous", previousPath, "--delimiter", "psv", "--format", "horizontal"});
        assertEquals(0, exitCode);
        List<String> listOfData = Splitter.on(System.lineSeparator()).trimResults().splitToList(getModifiedOutputStream().toString());
        assertTrue(getLineToAssert(listOfData,"Triangle.java|Shapes.Triangle|CLASS").contains("NO_OF_MODIFIERS|1|0"));
        assertTrue(getLineToAssert(listOfData,"Triangle.java|Shapes.Triangle|CLASS").contains("LINES_OF_CODE|17|0"));
        assertTrue(getLineToAssert(listOfData,"Triangle.java|Shapes.Triangle|CLASS").contains("NO_OF_ASSIGNMENTS|2|0"));
        assertTrue(getLineToAssert(listOfData,"Triangle.java|Shapes.Triangle|CLASS").contains("NO_OF_UNIQUE_WORDS|8|0"));
        assertTrue(getLineToAssert(listOfData,"Triangle.java|Shapes.Triangle|CLASS").contains("DEPTH_INHERITANCE_TREE|2|0"));
        assertTrue(getLineToAssert(listOfData,"Triangle.java|Shapes.Triangle|CLASS").contains("WEIGHT_METHOD_CLASS|4|0"));
        assertTrue(getLineToAssert(listOfData,"Triangle.java|Shapes.Triangle|CLASS").contains("COUPLING_BETWEEN_OBJECTS|1|0"));
        assertTrue(getLineToAssert(listOfData,"Triangle.java|Shapes.Triangle|CLASS").contains("NO_OF_RETURNS|2|0"));
        assertTrue(getLineToAssert(listOfData,"Rectangle.java|Shapes.Rectangle::calculatePerimeter/0|METHOD").contains("START_LINE_NO|39|29"));
        assertTrue(getLineToAssert(listOfData,"Rhombus.java|Shapes.Rhombus::calculatePerimeter/0|METHOD").contains("COMPLEXITY_OF_METHOD|0|1"));
        assertTrue(getLineToAssert(listOfData,"Rhombus.java|Shapes.Rhombus::calculatePerimeter/0|METHOD").contains("LINES_OF_CODE|0|3"));
        assertTrue(getLineToAssert(listOfData,"Rhombus.java|Shapes.Rhombus::calculatePerimeter/0|METHOD").contains("START_LINE_NO|0|29"));
        assertTrue(getLineToAssert(listOfData,"Rhombus.java|Shapes.Rhombus::calculatePerimeter/0|METHOD").contains("NO_OF_NOS|0|1"));
        assertTrue(getLineToAssert(listOfData,"Rhombus.java|Shapes.Rhombus::calculatePerimeter/0|METHOD").contains("NO_OF_MATH_OPERATIONS|0|1"));
        assertTrue(getLineToAssert(listOfData,"Rhombus.java|Shapes.Rhombus::calculatePerimeter/0|METHOD").contains("NO_OF_UNIQUE_WORDS|0|2"));
    }

    @Test
    public void testJCMDWithValidParameterForPSVReportWithConfig() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(new String[]{"--current", currentPath, "--previous", previousPath, "--delimiter", "psv", "--class-config", configPath, "--format", "vertical"});
        assertEquals(0, exitCode);

        List<String> expectedMetricsToDisplay = Arrays.asList("NO_OF_MODIFIERS", "NO_OF_PRIVATE_METHODS", "COUPLING_BETWEEN_OBJECTS");
        expectedMetricsToDisplay.stream().forEach(metric -> {
            assertTrue(getModifiedOutputStream().toString().contains("CLASS|" + metric));
        });

        List<String> metricsWhichShouldNotDisplay = Arrays.asList("NO_OF_FIELDS", "NO_OF_COMPARISONS", "NO_OF_PARENTHESIZED_EXPRESSIONS", "DEPTH_INHERITANCE_TREE");
        metricsWhichShouldNotDisplay.stream().forEach(metric -> {
            assertFalse(getModifiedOutputStream().toString().contains("CLASS|" + metric));
        });
    }

    @Test
    public void testJCMDWithValidParameterForCSVReportWithConfig() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(new String[]{"--current", currentPath, "--previous", previousPath, "--delimiter", "csv", "--class-config", configPath, "--format", "vertical"});
        assertEquals(0, exitCode);
        List<String> expectedMetricsToDisplay = Arrays.asList("NO_OF_MODIFIERS", "NO_OF_PRIVATE_METHODS", "COUPLING_BETWEEN_OBJECTS");
        expectedMetricsToDisplay.stream().forEach(metric -> {
            assertTrue(getModifiedOutputStream().toString().contains(metric));
        });
        List<String> metricsWhichShouldNotDisplay = Arrays.asList("NO_OF_PROTECTED_METHODS", "NO_OF_METHODS", "RESPONSE_FOR_A_CLASS", "DEPTH_INHERITANCE_TREE");
        metricsWhichShouldNotDisplay.stream().forEach(metric -> {
            assertFalse(getModifiedOutputStream().toString().contains(metric));
        });
    }

    @Test
    public void shouldThrowErrorForBadClassConfigFile() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(new String[]{
                "--current", currentPath,
                "--previous", previousPath,
                "--delimiter", "csv",
                "--class-config", "blahblah",
                "--method-config", configPath,
                "--format", "vertical"
        });
        assertNotEquals(0, exitCode);
        assertTrue(getModifiedErrorStream().toString().contains("Specify a valid absolute path"));
    }

    @Test
    public void shouldThrowErrorForBadMethodConfigFile() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(new String[]{
                "--current", currentPath,
                "--previous", previousPath,
                "--delimiter", "csv",
                "--class-config", configPath,
                "--method-config", "blahblah",
                "--format", "vertical"
        });
        assertNotEquals(0, exitCode);
        assertTrue(getModifiedErrorStream().toString().contains("Specify a valid absolute path"));
    }

    @Test
    public void shouldThrowErrorForBadReportFormat() throws Exception {
        int exitCode = new CommandLine(javaCodeMetricsWithDiff).execute(new String[]{
                "--current", currentPath,
                "--previous", previousPath,
                "--delimiter", "csv",
                "--class-config", configPath,
                "--method-config", "blahblah",
                "--format", "blahblah"
        });
        assertNotEquals(0, exitCode);
        assertTrue(getModifiedErrorStream().toString().contains("must match \"vertical|horizontal\""));
    }

    private String getLineToAssert(List<String> listOfData, String s) {
        return listOfData.stream().filter((line) -> line.contains(s)).findFirst().get();
    }
}
