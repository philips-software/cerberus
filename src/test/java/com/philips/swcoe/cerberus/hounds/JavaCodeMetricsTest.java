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

public class JavaCodeMetricsTest extends CerberusBaseTest {

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
        JavaCodeMetrics javaCodeMetrics = new JavaCodeMetrics();
        int exitCode = new CommandLine(javaCodeMetrics).execute(new String[] {});
        assertNotEquals(0, exitCode);
        assertTrue(getModifiedErrorStream().toString().contains("Specify the absolute path to source code"));
    }

    @Test
    public void testJCMDWithInvalidParameter() throws Exception {
        JavaCodeMetrics javaCodeMetrics = new JavaCodeMetrics();
        int exitCode = new CommandLine(javaCodeMetrics).execute(new String[] { "--files", path });
        assertEquals(0, exitCode);
    }

    @Test
    public void testGatheringOfMetrics() throws Exception {
        JavaCodeMetrics javaCodeMetrics = new JavaCodeMetrics();
        int exitCode = new CommandLine(javaCodeMetrics).execute(new String[] { "--files", path });
        assertEquals(0, exitCode);
        String actualString = getModifiedOutputStream().toString();
        String expectedFileName = COMMENTED_OUT_SUPPRESSED_WARNINGS_JAVA + DOT + JAVA_EXT;
        assertTrue(actualString.contains(expectedFileName));

    }
}