/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.cerebellum.swd.language.detectors;

import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.COMMENTED_OUT_PRAGMA_CS;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.CSHARP;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.CSHARP_EXT;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.DIRECTORY_3;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.DOT;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.PATH_SEPARATOR;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.RESOURCES;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.SUB_DIRECTORY_1;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.TEST_CSHARP_CODE;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.TEST_EXCLUSION_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.philips.swcoe.cerberus.cerebellum.swd.BaseSuppressedWarningsDetector;
import com.philips.swcoe.cerberus.cerebellum.swd.SuppressedWarningDetectors;
import com.philips.swcoe.cerberus.cerebellum.swd.SuppressedWarningsDetectorFactory;
import com.philips.swcoe.cerberus.cerebellum.tokenizer.DirectoryTokenizer;
import java.io.File;
import java.util.Map;

public class CsharpSuppressedWarningsDetectorTest {

    private DirectoryTokenizer tokenizer;
    private String language;
    private String path;
    private String exclusionPath;

    @BeforeEach
    public void beforeEachTest() throws Exception {
        path = RESOURCES + PATH_SEPARATOR + TEST_CSHARP_CODE + PATH_SEPARATOR;
        exclusionPath = path + PATH_SEPARATOR  + TEST_EXCLUSION_CODE;
        tokenizer = new DirectoryTokenizer();
        language = CSHARP;
        tokenizer.tokenize(new File(path), language);
    }

    @Test
    public void testDoesNotDetectCommentedOut() throws Exception {
        BaseSuppressedWarningsDetector aswd = SuppressedWarningsDetectorFactory
            .newInstance(SuppressedWarningDetectors.valueOf(language));
        aswd.detect(tokenizer, exclusionPath);
        Map<String, Map<String, String>> suppressedWarnings = aswd.getSuppressedWarnings();
        String fileWithCommentedOutWarning1 =
            System.getProperty("user.dir") + path + DIRECTORY_3 + PATH_SEPARATOR + SUB_DIRECTORY_1
                + PATH_SEPARATOR + COMMENTED_OUT_PRAGMA_CS + DOT + CSHARP_EXT;
        assertFalse(suppressedWarnings.containsKey(fileWithCommentedOutWarning1));
    }

    @Test
    public void testDetectAllTypes() throws Exception {
        BaseSuppressedWarningsDetector aswd = SuppressedWarningsDetectorFactory
            .newInstance(SuppressedWarningDetectors.valueOf(language));
        aswd.detect(tokenizer, exclusionPath);
        Map<String, Map<String, String>> suppressedWarnings = aswd.getSuppressedWarnings();
        assertEquals(3, suppressedWarnings.size());
    }
}
