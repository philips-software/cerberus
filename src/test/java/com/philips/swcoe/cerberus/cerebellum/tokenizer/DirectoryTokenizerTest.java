
/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.cerebellum.tokenizer;

import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.DOT;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.JAVA;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.JAVASCRIPT_EXT;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.JAVA_EXT;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.PATH_SEPARATOR;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.RESOURCES;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.TEST_MIX_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DirectoryTokenizerTest {

    private final String path = RESOURCES + PATH_SEPARATOR + TEST_MIX_CODE;

    @Test
    public void testTokenizeSpecifiedLanguage() throws Exception {
        DirectoryTokenizer tokenizer = new DirectoryTokenizer();
        File pathToTestSource = new File(path);
        tokenizer.tokenize(pathToTestSource, JAVA_EXT);
        Map<String, List<String>> source = tokenizer.getSource();
        for (Map.Entry<String, List<String>> entry : source.entrySet()) {
            assertTrue(entry.getKey().endsWith(DOT + JAVA_EXT));
            assertFalse(entry.getKey().endsWith(DOT + JAVASCRIPT_EXT));
        }
    }

    @Test
    public void testInvalidDirectory() throws Exception {
        assertThrows(FileNotFoundException.class, () -> {
            DirectoryTokenizer tokenizer = new DirectoryTokenizer();
            String badPath = "bad/path/";
            tokenizer.tokenize(new File(badPath), JAVA);
        });
    }

    @Test
    public void testGettersAndSettersOfTokenizer() throws IOException {
        DirectoryTokenizer tokenizer = new DirectoryTokenizer();
        File pathToTestSource = new File(path);
        tokenizer.tokenize(pathToTestSource, JAVA_EXT);

        Map<String, List<String>> expectedSource = tokenizer.getSource();
        assertNotNull(expectedSource);
        tokenizer.setSource(null);
        assertNull(tokenizer.getSource());
        tokenizer.setSource(expectedSource);
        assertEquals(expectedSource, tokenizer.getSource());
    }

}
