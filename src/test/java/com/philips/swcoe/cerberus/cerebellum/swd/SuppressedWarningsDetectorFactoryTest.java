/*
 * Copyright of Koninklijke Philips N.V. 2020
 */
package com.philips.swcoe.cerberus.cerebellum.swd;

import com.philips.swcoe.cerberus.cerebellum.swd.language.detectors.JavaSuppressedWarningsDetector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SuppressedWarningsDetectorFactoryTest {

    @Test
    public void testManufacturingOfLanguageDetector() throws Exception {
        BaseSuppressedWarningsDetector abswd = SuppressedWarningsDetectorFactory.newInstance(SuppressedWarningDetectors.valueOf("JAVA"));
        assertTrue(abswd instanceof JavaSuppressedWarningsDetector);
    }

    @Test
    public void testExceptionForUnknownRequest() throws Exception {
        assertThrows(IllegalArgumentException.class,() -> {
            SuppressedWarningsDetectorFactory.newInstance(SuppressedWarningDetectors.valueOf("unknown"));
        });
    }
}