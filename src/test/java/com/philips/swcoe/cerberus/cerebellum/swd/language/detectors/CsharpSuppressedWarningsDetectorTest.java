/*
 * Copyright of Koninklijke Philips N.V. 2020
 */
package com.philips.swcoe.cerberus.cerebellum.swd.language.detectors;

import com.philips.swcoe.cerberus.cerebellum.swd.BaseSuppressedWarningsDetector;
import com.philips.swcoe.cerberus.cerebellum.swd.SuppressedWarningDetectors;
import com.philips.swcoe.cerberus.cerebellum.swd.SuppressedWarningsDetectorFactory;
import com.philips.swcoe.cerberus.cerebellum.tokenizer.DirectoryTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CsharpSuppressedWarningsDetectorTest {

    private DirectoryTokenizer tokenizer;
    private String language;
    private String path;

    @BeforeEach
    public void beforeEachTest() throws Exception {
        path = RESOURCES + PATH_SEPARATOR + TEST_CSHARP_CODE + PATH_SEPARATOR;
        tokenizer = new DirectoryTokenizer();
        language = CSHARP;
        tokenizer.tokenize(new File(path), language);
    }

    @Test
    public void testDoesNotDetectCommentedOut() throws Exception {
        BaseSuppressedWarningsDetector aswd = SuppressedWarningsDetectorFactory.newInstance(SuppressedWarningDetectors.valueOf(language));
        aswd.detect(tokenizer);
        Map<String, Map<String, String>> suppressedWarnings = aswd.getSuppressedWarnings();
        String fileWithCommentedOutWarning1 = System.getProperty("user.dir") + path + DIRECTORY_3 + PATH_SEPARATOR + SUB_DIRECTORY_1 + PATH_SEPARATOR + COMMENTED_OUT_PRAGMA_CS + DOT + CSHARP_EXT;
        assertFalse(suppressedWarnings.containsKey(fileWithCommentedOutWarning1));
    }

    @Test
    public void testDetectAllTypes() throws Exception {
        BaseSuppressedWarningsDetector aswd = SuppressedWarningsDetectorFactory.newInstance(SuppressedWarningDetectors.valueOf(language));
        aswd.detect(tokenizer);
        Map<String, Map<String, String>> suppressedWarnings = aswd.getSuppressedWarnings();
        assertEquals(3, suppressedWarnings.size());
    }
}