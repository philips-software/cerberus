/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.cerebellum.swd;

public class SuppressedWarningsDetectorFactory {

    private SuppressedWarningsDetectorFactory() {

    }

    public static BaseSuppressedWarningsDetector newInstance(SuppressedWarningDetectors language) {
        return language.create();
    }
}
