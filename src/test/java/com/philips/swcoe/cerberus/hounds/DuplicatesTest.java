/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.hounds;

import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.DOT;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.JAVA_EXT;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.PATH_SEPARATOR;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.RESOURCES;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.SUPPRESSED_WARNINGS_WITH_FULL_PACKAGE_NAME_JAVA;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.TEST_JAVA_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.philips.swcoe.cerberus.unit.test.utils.CerberusBaseTest;
import picocli.CommandLine;

public class DuplicatesTest extends CerberusBaseTest {

    private final String path = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE;

    @BeforeEach
    public void beforeEach() {
        super.setUpStreams();
    }

    @AfterEach
    public void afterEach() {
        super.restoreStreams();
    }

    @Test
    public void testDuplicateHoundWithNoParameter() throws Exception {
        Duplicates duplicateHound = new Duplicates();
        new CommandLine(duplicateHound).execute();
        assertTrue(getModifiedErrorStream().toString().contains("Specify minimum token length"));
        assertTrue(
            getModifiedErrorStream().toString().contains("Specify the format of the report"));
        assertTrue(
            getModifiedErrorStream().toString().contains("Specify language of the source code"));
        assertTrue(getModifiedErrorStream().toString()
            .contains("Specify the absolute path to source code"));
    }

    @Test
    public void testExecutionofCPD() throws Exception {
        getExitCode("3");
        String actualString = getModifiedOutputStream().toString();
        assertTrue(actualString.contains("duplication in the following files"));
        assertTrue(actualString
            .contains(SUPPRESSED_WARNINGS_WITH_FULL_PACKAGE_NAME_JAVA + DOT + JAVA_EXT));
        assertTrue(actualString.contains("Rectangle.java"));
        assertTrue(actualString.contains("Rhombus.java"));
    }

    @Test
    public void shouldReturnZeroExitCodeWhenDuplicatesAreFound() throws Exception {
        assertEquals(0, getExitCode("300"));
    }

    @Test
    public void shouldReturnNonZeroExitCodeWhenDuplicatesAreFound() throws Exception {
        assertNotEquals(0, getExitCode("1"));
    }

    @Test
    public void shouldReturnNonZeroExitCodeWithInvalidArguments() throws Exception {
        assertNotEquals(0, getExitCode("5"));
    }

    private int getExitCode(String tokenSize) {
        Duplicates duplicateHound = new Duplicates();
        return new CommandLine(duplicateHound).execute(
            "--files", path, "--format", "text", "--minimum-tokens", tokenSize,
            "--language", "java");
    }
}
