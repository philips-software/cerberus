/*
 * Copyright of Koninklijke Philips N.V. 2020
 */
package com.philips.swcoe.cerberus;

import com.philips.swcoe.cerberus.unit.test.utils.CerberusBaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.philips.swcoe.cerberus.constants.ProgramConstants.NEW_LINE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CerberusTest extends CerberusBaseTest {

    @BeforeEach
    public void beforeAll() {
        super.setUpStreams();
    }

    @AfterEach
    public void afterAll() {
        super.restoreStreams();
    }

    @Test
    public void testCerebruswithOutArguments() {
        Cerberus.main(new String[] {});
        String expectedOutputString = getCerberusCommandLineUsageString();
        assertEquals(expectedOutputString, getModifiedOutputStream().toString());
    }

    @Test
    public void testCerebrusWithArguments() {
        getOriginalOutputStream().flush();
        Cerberus.main(new String[] { "CPD" });
        assertTrue(getModifiedErrorStream().toString().contains("Usage: Cerberus CPD"));
    }

    @Test
    public void testCerebruswithWrongArguments() {
        String dummyArgument = "dummy argument";
        Cerberus.main(new String[] { dummyArgument });
        String expectedOutputString = new StringBuilder().append("Unmatched argument at index 0: 'dummy argument'").append(NEW_LINE).append(getCerberusCommandLineUsageString()).toString();
        assertEquals(expectedOutputString.trim(), getModifiedErrorStream().toString().trim());
    }

    @Test
    public void testCallMethod() throws Exception {
        assertEquals(Integer.valueOf(0), new Cerberus().call());
    }

    private String getCerberusCommandLineUsageString() {
        return new StringBuilder().append("Usage: Cerberus [COMMAND]").append(NEW_LINE).append("Waking Cerberus to devour bad things in the system").append(NEW_LINE).append("Commands:")
                        .append(NEW_LINE).append("  CPD            Detect duplicated blocks of code in your source code").append(NEW_LINE)
                        .append("  SWD            Detect all the warnings which are suppressed in your code")
                        .append(NEW_LINE).append("  JCMD           Java Code Metrics Detector")
                        .append(NEW_LINE).append("  JCMD-DIFF      Java Code Metrics Detector with Diff").append(NEW_LINE).toString();
    }
}
