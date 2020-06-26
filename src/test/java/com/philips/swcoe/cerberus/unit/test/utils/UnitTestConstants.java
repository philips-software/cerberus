/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.unit.test.utils;

import java.io.File;

public class UnitTestConstants {
    // Unit test constants that are also present in ProgramConstants.java. Intentionally kept separate!
    public static final String RESOURCES = "resources";
    public static final String PATH_SEPARATOR = File.separator;
    public static final String NEW_LINE = String.format("%n");
    // Directories in resources
    public static final String TEST_JAVA_CODE = "testJavaCode";
    public static final String TEST_JAVA_CODE_PREVIOUS =
        TEST_JAVA_CODE + PATH_SEPARATOR + "previous";
    public static final String TEST_JAVA_CODE_CURRENT = TEST_JAVA_CODE + PATH_SEPARATOR + "current";
    public static final String TEST_CPP_CODE = "testCPPCode";
    public static final String TEST_CSHARP_CODE = "testCsharpCode";
    public static final String TEST_MIX_CODE = "testMixCode";
    public static final String SUB_DIRECTORY_ONE = "subDirectoryOne";
    public static final String SUB_DIRECTORY_TWO = "subDirectoryTwo";
    public static final String SUB_DIRECTORY_THREE = "subDirectoryThree";
    public static final String DIRECTORY_3 = "Directory3";
    public static final String SUB_DIRECTORY_1 = "SubDirectory1";
    // File names in resources folder
    public static final String CLEANEST_FILE_JAVA = "CleanestFileJava";
    public static final String COMMENTED_OUT_SUPPRESSED_WARNINGS_JAVA =
        "CommentedOutSuppressedWarnings";
    public static final String GENERIC_SUPPRESSED_WARNINGS_JAVA = "GenericSuppressedWarnings";
    public static final String MULTI_LINE_COMMENTED_SUPPRESSED_WARNINGS_JAVA =
        "MultiLineCommentedSuppressedWarnings";
    public static final String SONARQUBE_SUPPRESSED_WARNINGS_JAVA = "SonarQubeSuppressedWarnings";
    public static final String SUPPRESSED_WARNINGS_WITH_FULL_PACKAGE_NAME_JAVA =
        "SuppressedWarningWithFullPackageName";
    public static final String HEAP_JAVA = "Heap";
    public static final String FIRST_JAVA = "First";
    public static final String SAMPLE_JAVA = "Sample";
    public static final String HANGMAN_JAVA = "Hangman";
    public static final String MY_STORIES = "MyStories";
    public static final String MULTI_LINE_COMMENT_TEST_CPP = "multiLineCommentTest";
    public static final String SINGLE_LINE_COMMENT_TEST_CPP = "singlelineCommentTest";
    public static final String COMMENTED_OUT_PRAGMA_CS = "commentedOutPragma";
    public static final String DIRECTORY_JS = "directory";
    public static final String READ_FILE_JS = "readFile";
    public static final String WRITE_FILE_JS = "writeFile";
    // Words
    public static final String MAIN = "Main";
    // Languages
    public static final String CPP = "CPP";
    public static final String JAVA = "JAVA";
    public static final String CSHARP = "CS";
    // Punctuation
    public static final String DOT = ".";
    // File Extensions
    public static final String CPP_EXT = "cpp";
    public static final String JAVA_EXT = "java";
    public static final String CSHARP_EXT = "cs";
    public static final String JAVASCRIPT_EXT = "js";
    private UnitTestConstants() {
    }

}
