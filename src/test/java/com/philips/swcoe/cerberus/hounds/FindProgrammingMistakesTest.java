package com.philips.swcoe.cerberus.hounds;

import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.PATH_SEPARATOR;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.RESOURCES;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.TEST_EXCLUSION_CODE;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.TEST_JAVA_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import picocli.CommandLine;

class FindProgrammingMistakesTest extends BaseCommandTest {

    private final String path = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE;

    private final String externalRuleSet = RESOURCES + PATH_SEPARATOR + "java_practices.xml";
    private final String exclusionPath = path + PATH_SEPARATOR + TEST_EXCLUSION_CODE;


    @BeforeEach
    public void beforeEach() {
        super.setUpStreams();
        FileUtils.deleteQuietly(new File(path + PATH_SEPARATOR + "mistakes-report.html"));
    }

    @AfterEach
    public void afterEach() {
        super.restoreStreams();
        FileUtils.deleteQuietly(new File(path + PATH_SEPARATOR + "mistakes-report.html"));
    }

    @Test
    public void findProgrammingMistakesShouldThrowErrorsForEmptyArguments() throws Exception {
        FindProgrammingMistakes findProgrammingMistakes = new FindProgrammingMistakes();
        int exitCode = executeFindProgrammingMistakes(findProgrammingMistakes, "", "", "", "");
        assertNotEquals(0, exitCode);
        assertErrorOutputFromFPM();
    }

    @Test
    public void findProgrammingMistakesShouldThrowErrorsWithNullArguments() throws Exception {
        FindProgrammingMistakes findProgrammingMistakes = new FindProgrammingMistakes();
        int exitCode = new CommandLine(findProgrammingMistakes).execute();
        assertNotEquals(0, exitCode);
        assertErrorOutputFromFPM();
    }

    @Test
    public void findProgrammingMistakesShouldGenerateReportWithCorrectArguments() throws Exception {
        FindProgrammingMistakes findProgrammingMistakes = new FindProgrammingMistakes();
        int exitCode = executeFindProgrammingMistakes(findProgrammingMistakes, path, "java", "8",
            "category/java/documentation.xml");
        assertEquals(0, exitCode);
        assertTrue(getModifiedOutputStream().toString()
            .contains("Found 57 violations in the specified source path"));
        assertTrue(new File(path + PATH_SEPARATOR + "mistakes-report.html").exists());
    }

    @Test
    public void findProgrammingMistakesShouldGenerateReportWithExternalRuleSet() throws Exception {
        FindProgrammingMistakes findProgrammingMistakes = new FindProgrammingMistakes();
        int exitCode = executeFindProgrammingMistakes(findProgrammingMistakes, path, "java", "8",
            externalRuleSet);
        assertEquals(0, exitCode);
        assertTrue(getModifiedOutputStream().toString()
            .contains("Found 18 violations in the specified source path"));
        assertTrue(new File(path + PATH_SEPARATOR + "mistakes-report.html").exists());
    }

    @Test
    public void findProgrammingMistakesShouldNotFindErrorsForCleanestJavaCode() throws Exception {
        FindProgrammingMistakes findProgrammingMistakes = new FindProgrammingMistakes();
        int exitCode = executeFindProgrammingMistakes(findProgrammingMistakes,
            path + PATH_SEPARATOR + "CleanestJavaCode", "java", "8", externalRuleSet);
        assertEquals(0, exitCode);
        assertTrue(getModifiedOutputStream().toString().contains("No Violations Found"));
        assertFalse(new File(path + PATH_SEPARATOR + "mistakes-report.html").exists());
    }

    private int executeFindProgrammingMistakes(FindProgrammingMistakes findProgrammingMistakes,
                                               String path, String java, String s, String s2) {
        return new CommandLine(findProgrammingMistakes).execute("--files", path,
            "--language", java,
            "--java-version", s,
            "--rulesets", s2,
            "--exclude", exclusionPath);
    }

    private void assertErrorOutputFromFPM() {
        assertTrue(
            getModifiedErrorStream().toString().contains("ERROR: Specify Java language version"));
        assertTrue(getModifiedErrorStream().toString()
            .contains("ERROR: Specify the absolute path to source code"));
        assertTrue(getModifiedErrorStream().toString()
            .contains("ERROR: Specify absolute path to your ruleset"));
        assertTrue(getModifiedErrorStream().toString()
            .contains("ERROR: Specify language of the source code"));
    }


}
