/*
 * Copyright of Koninklijke Philips N.V. 2020
 */
package com.philips.swcoe.cerberus.cerebellum.swd.language.detectors;

import static com.philips.swcoe.cerberus.constants.ProgramConstants.HASH;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.MESSAGE;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.OPEN_SMALL_BRACE;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.OPEN_SQUARE_BRACE;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.PRAGMA;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.SUPPRESS;

import com.philips.swcoe.cerberus.cerebellum.swd.BaseSuppressedWarningsDetector;

public class CsharpSuppressedWarningsDetector extends BaseSuppressedWarningsDetector {

    public CsharpSuppressedWarningsDetector() {
        this.waysToSuppress = new String[] { HASH + PRAGMA, OPEN_SQUARE_BRACE + SUPPRESS + MESSAGE + OPEN_SMALL_BRACE };
    }
}
