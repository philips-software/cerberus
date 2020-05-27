/*
 * Copyright of Koninklijke Philips N.V. 2020
 */
package com.philips.swcoe.cerberus.hounds;

import com.philips.swcoe.cerberus.unit.test.utils.CerberusBaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class DuplicatesTest extends CerberusBaseTest {

    private String path = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE;

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
        int exitCode = new CommandLine(duplicateHound).execute(new String[] {});
        assertNotEquals(0, exitCode);
        assertTrue(getModifiedErrorStream().toString().contains("Specify minimum token length"));
        assertTrue(getModifiedErrorStream().toString().contains("Specify the format of the report"));
        assertTrue(getModifiedErrorStream().toString().contains("Specify language of the source code"));
        assertTrue(getModifiedErrorStream().toString().contains("Specify the absolute path to source code"));
    }

    @Test
    public void testDuplicateHoundWithInvalidParameter() throws Exception {
        Duplicates duplicateHound = new Duplicates();
        int exitCode = new CommandLine(duplicateHound).execute(new String[] { "--files", path, "--format", "text", "--minimum-tokens", "5", "--language", "java" });
        assertEquals(0, exitCode);
    }

    @Test
    public void testExecutionofCPD() throws Exception {
        Duplicates duplicateHound = new Duplicates();
        int exitCode = new CommandLine(duplicateHound).execute(new String[] { "--files", path, "--format", "text", "--minimum-tokens", "3", "--language", "java" });
        assertEquals(0, exitCode);
        String actualString = getModifiedOutputStream().toString();
        assertTrue(actualString.contains("duplication in the following files"));
        assertTrue(actualString.contains(SUPPRESSED_WARNINGS_WITH_FULL_PACKAGE_NAME_JAVA + DOT + JAVA_EXT));
        assertTrue(actualString.contains("Rectangle.java"));
        assertTrue(actualString.contains("Rhombus.java"));
    }
}