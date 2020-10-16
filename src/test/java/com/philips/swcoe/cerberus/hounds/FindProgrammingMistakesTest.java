package com.philips.swcoe.cerberus.hounds;

import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.PATH_SEPARATOR;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.RESOURCES;
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

    private FindProgrammingMistakes findProgrammingMistakes;

    @BeforeEach
    public void beforeEach() {
        super.setUpStreams();
        FileUtils.deleteQuietly(new File(path + PATH_SEPARATOR + "mistakes-report.html"));
        findProgrammingMistakes = new FindProgrammingMistakes();
    }

    @AfterEach
    public void afterEach() {
        super.restoreStreams();
        FileUtils.deleteQuietly(new File(path + PATH_SEPARATOR + "mistakes-report.html"));
    }

    @Test
    public void findProgrammingMistakesShouldThrowErrorsForEmptyArguments() throws Exception {
        executeFindProgrammingMistakes(findProgrammingMistakes, "", "", "", "");
        assertErrorOutputFromFPM();
    }

    @Test
    public void findProgrammingMistakesShouldThrowErrorsWithNullArguments() throws Exception {
        new CommandLine(findProgrammingMistakes).execute();
        assertErrorOutputFromFPM();
    }

    @Test
    public void findProgrammingMistakesShouldGenerateReportWithCorrectArguments() throws Exception {
        executeFindProgrammingMistakes(findProgrammingMistakes, path, "java", "8",
            "category/java/documentation.xml");
        assertTrue(getModifiedOutputStream().toString()
            .contains("Found 57 violations in the specified source path"));
        assertTrue(new File(path + PATH_SEPARATOR + "mistakes-report.html").exists());
    }

    @Test
    public void findProgrammingMistakesShouldGenerateReportWithExternalRuleSet() throws Exception {
        executeFindProgrammingMistakes(findProgrammingMistakes, path, "java", "8",
            externalRuleSet);
        assertTrue(getModifiedOutputStream().toString()
            .contains("Found 18 violations in the specified source path"));
        assertTrue(new File(path + PATH_SEPARATOR + "mistakes-report.html").exists());
    }

    @Test
    public void findProgrammingMistakesShouldNotFindErrorsForCleanestJavaCode() throws Exception {
        executeFindProgrammingMistakes(findProgrammingMistakes,
            path + PATH_SEPARATOR + "CleanestJavaCode", "java", "8", externalRuleSet);
        assertTrue(getModifiedOutputStream().toString().contains("No Violations Found"));
        assertFalse(new File(path + PATH_SEPARATOR + "mistakes-report.html").exists());
    }

    @Test
    public void shouldReturnZeroExitCodeWhenViolationsAreNotFound() throws Exception {
        assertEquals(0,  executeFindProgrammingMistakes(findProgrammingMistakes,
            path + PATH_SEPARATOR + "CleanestJavaCode", "java", "8", externalRuleSet));
    }

    @Test
    public void shouldReturnNonZeroExitCodeWhenViolationsAreFound() throws Exception {
        assertNotEquals(0, executeFindProgrammingMistakes(findProgrammingMistakes, path, "java", "8",
            externalRuleSet));
    }

    @Test
    public void shouldReturnNonZeroExitCodeWithInvalidArguments() throws Exception {
        assertNotEquals(0, executeFindProgrammingMistakes(findProgrammingMistakes, "", "", "", ""));
    }

    private int executeFindProgrammingMistakes(FindProgrammingMistakes findProgrammingMistakes,
                                               String path, String java, String s, String s2) {
        return new CommandLine(findProgrammingMistakes).execute("--files", path,
            "--language", java,
            "--java-version", s,
            "--rulesets", s2);
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
