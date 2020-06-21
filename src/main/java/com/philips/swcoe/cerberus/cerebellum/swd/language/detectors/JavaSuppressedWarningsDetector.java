/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.cerebellum.swd.language.detectors;

import static com.philips.swcoe.cerberus.constants.ProgramConstants.AT;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.JAVA_LANG_SUPPRESSWARNINGS;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.SUPPRESS;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.WARNINGS;
import com.philips.swcoe.cerberus.cerebellum.swd.BaseSuppressedWarningsDetector;

public class JavaSuppressedWarningsDetector extends BaseSuppressedWarningsDetector {
    public JavaSuppressedWarningsDetector() {
        this.waysToSuppress =
            new String[] {AT + SUPPRESS + WARNINGS, AT + JAVA_LANG_SUPPRESSWARNINGS};
    }
}
