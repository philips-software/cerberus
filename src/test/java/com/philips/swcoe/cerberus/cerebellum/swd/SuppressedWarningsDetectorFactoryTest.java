/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.cerebellum.swd;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.philips.swcoe.cerberus.cerebellum.swd.language.detectors.JavaSuppressedWarningsDetector;

public class SuppressedWarningsDetectorFactoryTest {

    @Test
    public void testManufacturingOfLanguageDetector() throws Exception {
        BaseSuppressedWarningsDetector abswd = SuppressedWarningsDetectorFactory
            .newInstance(SuppressedWarningDetectors.valueOf("JAVA"));
        assertTrue(abswd instanceof JavaSuppressedWarningsDetector);
    }

    @Test
    public void testExceptionForUnknownRequest() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> {
            SuppressedWarningsDetectorFactory
                .newInstance(SuppressedWarningDetectors.valueOf("unknown"));
        });
    }
}
