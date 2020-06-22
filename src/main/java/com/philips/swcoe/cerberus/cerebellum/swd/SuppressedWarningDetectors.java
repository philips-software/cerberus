/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.cerebellum.swd;

import com.philips.swcoe.cerberus.cerebellum.swd.language.detectors.CppSuppressedWarningsDetector;
import com.philips.swcoe.cerberus.cerebellum.swd.language.detectors.CsharpSuppressedWarningsDetector;
import com.philips.swcoe.cerberus.cerebellum.swd.language.detectors.JavaSuppressedWarningsDetector;

public enum SuppressedWarningDetectors {
    JAVA {
        @Override
        public BaseSuppressedWarningsDetector create() {
            return new JavaSuppressedWarningsDetector();
        }
    },
    CS {
        @Override
        public BaseSuppressedWarningsDetector create() {
            return new CsharpSuppressedWarningsDetector();
        }
    },
    CPP {
        @Override
        public BaseSuppressedWarningsDetector create() {
            return new CppSuppressedWarningsDetector();
        }
    };

    public BaseSuppressedWarningsDetector create() {
        return null;
    }
}
