package com.philips.swcoe.cerberus;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.philips.swcoe.cerberus.unit.test.utils.CerberusBaseTest;
import picocli.CommandLine;

class DisplayHoundsTest extends CerberusBaseTest {

    @BeforeEach
    public void beforeAll() {
        super.setUpStreams();
    }

    @AfterEach
    public void afterAll() {
        super.restoreStreams();
    }

    @Test
    public void testRenderingHoundsOfCerberus() {
        String textTableString = getHelpStringToTest();
        String[] arrayOfTables = textTableString.split("\n");
        assertTrue(arrayOfTables.length == 5);
    }

    @Test
    public void testFormatRenderingHoundsOfCerberus() {
        String textTableString = getHelpStringToTest();
        String[] arrayOfHelpStringsDisplayed = textTableString.split("\n");
        assertTrue(arrayOfHelpStringsDisplayed[0]
            .contains("CPD            Detect duplicated blocks of code in your source code"));
        assertTrue(arrayOfHelpStringsDisplayed[1]
            .contains("SWD            Detect all the warnings which are suppressed in your code"));
        assertTrue(
            arrayOfHelpStringsDisplayed[2].contains("JCMD           Java Code Metrics Detector"));
        assertTrue(arrayOfHelpStringsDisplayed[3]
            .contains("JCMD-DIFF      Java Code Metrics Detector with Diff"));
    }

    private String getHelpStringToTest() {
        CommandLine cmd = new CommandLine(new Cerberus());
        DisplayHounds displayHounds = new DisplayHounds();
        return displayHounds.render(cmd.getHelp());
    }


}