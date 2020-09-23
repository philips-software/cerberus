/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.constants;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public final class ProgramConstants {


    // Platform independent stuff
    public static final String NEW_LINE = String.format("%n");
    public static final String PATH_SEPARATOR = File.separator;
    public static final String JAVA_COMMENT_BEGINNING = "/*";
    public static final String JAVA_COMMENT_END = "*/";
    public static final String SPACE = StringUtils.SPACE;
    public static final String DOUBLE_SPACE = SPACE + SPACE;
    public static final String COLON = ":";
    public static final String HASH = "#";
    public static final String AT = "@";
    public static final String OPEN_SQUARE_BRACE = "[";
    public static final String OPEN_SMALL_BRACE = "(";
    public static final String PMD_CLOSE_TAG = "</pmd>";
    public static final String DOT = ".";
    public static final char COMMA = ',';
    public static final char PIPE = '|';
    // Words
    public static final String ERROR = "ERROR";
    public static final String CERBERUS = "Cerberus";
    public static final String FILE = "File";
    public static final String LINE = "Line";
    public static final String NO = "No";
    public static final String PRAGMA = "pragma";
    public static final String SUPPRESS = "Suppress";
    public static final String WARNINGS = "Warnings";
    public static final String MESSAGE = "Message";
    public static final String RESOURCES = "resources";
    public static final String JAVA_PRACTICES = "java_practices";
    public static final String JAVA_RULES = "java-rules";
    public static final String REPORT = "report";
    public static final String V1_8 = "1.8";
    public static final String JAVA_LANG_SUPPRESSWARNINGS = "java.lang.SuppressWarnings";
    public static final String RESULTS_OF_SWD = "Results of SWD";
    // File extensions
    public static final String JAVA_EXT = "java";
    public static final String XML_EXT = "xml";
    // Hounds
    public static final String COPY_PASTE_DETECTOR = "CPD";
    public static final String JAVA_CODE_METRICS_DETECTOR = "JCMD";
    public static final String JAVA_CODE_METRICS_DETECTOR_WITH_DIFF = "JCMD-DIFF";
    public static final String FIND_PROGRAMMING_MISTAKES = "FPM";
    public static final String JAVA_PROGRAMMING_MISTAKES_DETECTOR = "JPMD";
    public static final String SUPPRESSED_WARNINGS_DETECTOR = "SWD";
    // Command line options
    public static final String FILES_OPTION = "--files";
    public static final String CURRENT_FILES_OPTION = "--current";
    public static final String PREVIOUS_FILES_OPTION = "--previous";
    public static final String CONFIG = "--config";
    public static final String METHOD_CONFIG = "--method-config";
    public static final String CLASS_CONFIG = "--class-config";
    public static final String FORMAT_OPTION = "--format";
    public static final String DELIMITER_OPTION = "--delimiter";
    public static final String MINIMUM_TOKENS_OPTION = "--minimum-tokens";
    public static final String LANGUAGE_OPTION = "--language";
    public static final String STRUCTURE_OPTION = "--structure";
    public static final String EXCLUDE = "--exclude";
    // Version control of jars
    public static final String VERSION = "1.0";

    private ProgramConstants() {
    }

    public enum DelimeterForReport {
        PSV(ProgramConstants.PIPE),
        CSV(ProgramConstants.COMMA);
        private char delimeter;

        DelimeterForReport(char delimeter) {
            this.delimeter = delimeter;
        }

        public char getDelimeter() {
            return delimeter;
        }
    }

}
