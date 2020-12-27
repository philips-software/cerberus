/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus;

import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.CLEAN_JAVA_CODE;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.PATH_SEPARATOR;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.RESOURCES;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.TEST_JAVA_CODE;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import com.philips.swcoe.cerberus.unit.test.utils.CerberusBaseTest;


public class CerberusTest extends CerberusBaseTest {

    private final String badCodePath = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE;
    private final String goodCodePath = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE + PATH_SEPARATOR + CLEAN_JAVA_CODE;
    private final String externalRuleSet = RESOURCES + PATH_SEPARATOR + "java_practices.xml";

    @BeforeEach
    public void beforeAll() {
        super.setUpStreams();
    }

    @AfterEach
    public void afterAll() {
        super.restoreStreams();
    }

    @Test
    @ExpectSystemExitWithStatus(2)
    public void shouldReturnExitStatusForNoArguments()  {
        Cerberus.main(new String[] {});
    }

    @Test
    @ExpectSystemExitWithStatus(8)
    public void shouldReturnExitStatusAsNumberOfDuplicatesForCPDHound() {
        getOriginalOutputStream().flush();
        Cerberus.main(new String[] {"CPD", "--files", badCodePath, "--format", "text", "--minimum-tokens", "30",
            "--language", "java"});
    }

    @Test
    @ExpectSystemExitWithStatus(15)
    public void shouldReturnExitStatusAsNumberOfViolationsForPMDAnyHound() {
        getOriginalOutputStream().flush();
        Cerberus.main(new String[] {"FPM", "--files", badCodePath, "--language", "JAVA", "--java-version", "8", "--rulesets", externalRuleSet});

    }

    @Test
    @ExpectSystemExitWithStatus(0)
    public void shouldReturnExitStatusAsZeroWhenThereAreNoViolations() {
        getOriginalOutputStream().flush();
        Cerberus.main(new String[] {"FPM", "--files", goodCodePath, "--language", "JAVA", "--java-version", "8", "--rulesets", externalRuleSet});
    }


    @Test
    @ExpectSystemExitWithStatus(2)
    public void shouldReturnExitStatusAsNumberOfSuppressions() {
        getOriginalOutputStream().flush();
        Cerberus.main(new String[] {"SWD", "--files", badCodePath});
    }

}
