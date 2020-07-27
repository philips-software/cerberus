/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.cerebellum.swd.language.detectors;

import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.COMMENTED_OUT_SUPPRESSED_WARNINGS_JAVA;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.DOT;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.JAVA;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.JAVA_EXT;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.MULTI_LINE_COMMENTED_SUPPRESSED_WARNINGS_JAVA;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.PATH_SEPARATOR;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.RESOURCES;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.TEST_JAVA_CODE;
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

public class JavaSuppressedWarningsDetectorTest {

    private DirectoryTokenizer tokenizer;
    private String language;
    private String path;

    @BeforeEach
    public void beforeEachTest() throws Exception {
        path = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE + PATH_SEPARATOR;
        tokenizer = new DirectoryTokenizer();
        language = JAVA;
        tokenizer.tokenize(new File(path), language);
    }

    @Test
    public void testDoesNotDetectCommentedOut() throws Exception {
        BaseSuppressedWarningsDetector aswd = SuppressedWarningsDetectorFactory
            .newInstance(SuppressedWarningDetectors.valueOf("JAVA"));
        aswd.detect(tokenizer);
        Map<String, Map<String, String>> suppressedWarnings = aswd.getSuppressedWarnings();
        String fileWithCommentedOutWarning1 =
            System.getProperty("user.dir") + path + COMMENTED_OUT_SUPPRESSED_WARNINGS_JAVA + DOT +
                JAVA_EXT;
        assertFalse(suppressedWarnings.containsKey(fileWithCommentedOutWarning1));
        String fileWithCommentedOutWarning2 =
            System.getProperty("user.dir") + path + MULTI_LINE_COMMENTED_SUPPRESSED_WARNINGS_JAVA +
                DOT + JAVA_EXT;
        assertFalse(suppressedWarnings.containsKey(fileWithCommentedOutWarning2));
    }

    @Test
    public void testDetectAllTypes() throws Exception {
        BaseSuppressedWarningsDetector aswd = SuppressedWarningsDetectorFactory
            .newInstance(SuppressedWarningDetectors.valueOf("JAVA"));
        aswd.detect(tokenizer);
        Map<String, Map<String, String>> suppressedWarnings = aswd.getSuppressedWarnings();
        assertEquals(3, suppressedWarnings.size());
    }

}