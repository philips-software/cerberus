/*
 * Copyright of Koninklijke Philips N.V. 2020
 */
package com.philips.swcoe.cerberus.cerebellum.swd;

import com.philips.swcoe.cerberus.cerebellum.swd.language.detectors.CppSuppressedWarningsDetector;
import com.philips.swcoe.cerberus.cerebellum.swd.language.detectors.CsharpSuppressedWarningsDetector;
import com.philips.swcoe.cerberus.cerebellum.swd.language.detectors.JavaSuppressedWarningsDetector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SuppressedWarningDetectorsTest {

    @Test
    public void testCsharpDetectorInstantiation() throws Exception {
        BaseSuppressedWarningsDetector abswd = SuppressedWarningDetectors.valueOf("CS").create();
        assertTrue(abswd instanceof CsharpSuppressedWarningsDetector);
    }

    @Test
    public void testJavaDetectorInstantiation() throws Exception {
        BaseSuppressedWarningsDetector abswd = SuppressedWarningDetectors.valueOf("JAVA").create();
        assertTrue(abswd instanceof JavaSuppressedWarningsDetector);
    }

    @Test
    public void testCppDetectorInstantiation() throws Exception {
        BaseSuppressedWarningsDetector abswd = SuppressedWarningDetectors.valueOf("CPP").create();
        assertTrue(abswd instanceof CppSuppressedWarningsDetector);
    }

    @Test
    public void testThrowsExceptionForUnknown() throws Exception {
        assertThrows(IllegalArgumentException.class,() -> {
            SuppressedWarningDetectors.valueOf("unknown").create();
        });
    }
}