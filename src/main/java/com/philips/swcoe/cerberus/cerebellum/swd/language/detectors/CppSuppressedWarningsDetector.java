/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.cerebellum.swd.language.detectors;

import static com.philips.swcoe.cerberus.constants.ProgramConstants.HASH;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.PRAGMA;
import com.philips.swcoe.cerberus.cerebellum.swd.BaseSuppressedWarningsDetector;

public class CppSuppressedWarningsDetector extends BaseSuppressedWarningsDetector {
    public CppSuppressedWarningsDetector() {
        this.waysToSuppress = new String[] {HASH + PRAGMA};
    }
}
