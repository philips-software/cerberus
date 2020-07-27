/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.constants;

public final class DescriptionConstants {


    // General descriptions
    public static final String CERBERUS_DESCRIPTION =
        "Waking Cerberus to devour bad things in the system";
    public static final String COULDNT_FIND_DIRECTORY = "Couldn't find directory";
    public static final String JAVA_CODE_METRICS_DETECTOR_DESCRIPTION =
        "Java Code Metrics Detector";
    public static final String JAVA_CODE_METRICS_DETECTOR_DESCRIPTION_WITH_DIFF =
        "Java Code Metrics Detector with Diff";
    public static final String FIND_PROGRAMMING_MISTAKES_DESCRIPTION =
        "Find Programming mistakes in code";
    public static final String JAVA_PROGRAMMING_MISTAKES_DETECTOR_DESCRIPTION =
        "Java Programming Mistakes Detector";
    public static final String SUPPRESSED_WARNINGS_DETECTOR_DESCRIPTION =
        "Detect all the warnings which are suppressed in your code";
    // Argument not null messages
    public static final String FILES_OPTION_NOT_NULL_ARGUMENT_MESSAGE =
        "Specify the absolute path to source code";
    public static final String CURRENT_FILES_OPTION_NOT_NULL_ARGUMENT_MESSAGE =
        "Specify the absolute path to your source code which has current version";
    public static final String PREVIOUS_FILES_OPTION_NOT_NULL_ARGUMENT_MESSAGE =
        "Specify Absolute Path to your source code which has previous version";
    public static final String INVALID_FILE_PATH = "Specify a valid absolute path";
    public static final String REPORT_FORMAT_OPTION_NOT_NULL_ARGUMENT_MESSAGE =
        "Specify the format of the report";
    public static final String REPORT_DELIMITER_OPTION_NOT_NULL_ARGUMENT_MESSAGE =
        "Specify the delimiter of the report";
    public static final String MINIMUM_TOKEN_OPTION_NOT_NULL_ARGUMENT_MESSAGE =
        "Specify minimum token length";
    public static final String LANGUAGE_OPTION_NOT_NULL_ARGUMENT_MESSAGE =
        "Specify language of the source code";
    public static final String ALLOWED_LANGUAGES_EXCEPTION_MESSAGE =
        "Unsupported language, Please specify 'java' for java language, 'cs' for csharp language and 'cpp' for C++";
    public static final String REPORT_STRUCTURE_OPTION_NOT_NULL_ARGUMENT_MESSAGE =
        "Specify report structure vertical or horizontal";
    public static final String LANGUAGE_VERSION_OPTION = "Specify Java language version";
    public static final String RULESET_OPTION = "Specify absolute path to your ruleset";
    // Command line option description
    public static final String FILES_CMD_LINE_OPTION_DESCRIPTION =
        "Absolute Path to your source code.";
    public static final String CURRENT_FILES_CMD_LINE_OPTION_DESCRIPTION =
        "Absolute Path to your source code which has current version.";
    public static final String PREVIOUS_FILES_CMD_LINE_OPTION_DESCRIPTION =
        "Absolute Path to your source code which has previous version.";
    public static final String CONFIG_CMD_LINE_OPTION_DESCRIPTION =
        "Absolute Path to your config file which has all required configuration.";
    public static final String REPORT_FORMAT_CMD_LINE_OPTION_DESCRIPTION = "Report format.";
    public static final String REPORT_FORMAT_CMD_LINE_OPTION_DESCRIPTION_WITH_DETAILS =
        "Report format CSV for comma separated format or PSV for pipe separated format or MD for markdown or HTML for html format.";
    public static final String REPORT_STRUCTURE_CMD_LINE_OPTION_DESCRIPTION_VERTICAL_HORIZONTAL =
        "Report structure vertical or horizontal";
    public static final String PROGRAMMING_LANGUAGE_USED_OPTION = "Language Of Source Code";
    public static final String MINIMUM_TOKEN_CMD_LINE_OPTION_DESCRIPTION =
        "The minimum token length which should be reported as a duplicate.";
    public static final String LANGUAGE_CMD_LINE_OPTION_DESCRIPTION = "Sources code language. ";
    // Other errors
    public static final String INVALID_REPORT_FORMAT =
        "Markdown and HTML format can be used only with vertical metrics structure";

    private DescriptionConstants() {
    }

}
