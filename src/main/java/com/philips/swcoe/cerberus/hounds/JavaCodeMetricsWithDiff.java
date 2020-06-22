/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.hounds;

import static com.philips.swcoe.cerberus.constants.DescriptionConstants.CONFIG_CMD_LINE_OPTION_DESCRIPTION;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.CURRENT_FILES_CMD_LINE_OPTION_DESCRIPTION;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.CURRENT_FILES_OPTION_NOT_NULL_ARGUMENT_MESSAGE;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.INVALID_REPORT_FORMAT;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.JAVA_CODE_METRICS_DETECTOR_DESCRIPTION_WITH_DIFF;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.PREVIOUS_FILES_CMD_LINE_OPTION_DESCRIPTION;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.PREVIOUS_FILES_OPTION_NOT_NULL_ARGUMENT_MESSAGE;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.REPORT_FORMAT_CMD_LINE_OPTION_DESCRIPTION_WITH_DETAILS;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.REPORT_FORMAT_OPTION_NOT_NULL_ARGUMENT_MESSAGE;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.REPORT_STRUCTURE_CMD_LINE_OPTION_DESCRIPTION_VERTICAL_HORIZONTAL;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.REPORT_STRUCTURE_OPTION_NOT_NULL_ARGUMENT_MESSAGE;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.CLASS_CONFIG;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.CURRENT_FILES_OPTION;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.FORMAT_OPTION;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.JAVA_CODE_METRICS_DETECTOR_WITH_DIFF;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.METHOD_CONFIG;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.PREVIOUS_FILES_OPTION;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.STRUCTURE_OPTION;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.CodeMetricsDiffService;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.CodeMetricsHorizontalWriterService;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.CodeMetricsVerticalWriterService;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsClassResult;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(name = JAVA_CODE_METRICS_DETECTOR_WITH_DIFF, description = JAVA_CODE_METRICS_DETECTOR_DESCRIPTION_WITH_DIFF)
public class JavaCodeMetricsWithDiff extends BaseCommand implements Callable<Integer> {

    @NotNull(message = CURRENT_FILES_OPTION_NOT_NULL_ARGUMENT_MESSAGE)
    @CommandLine.Option(names = CURRENT_FILES_OPTION, description = CURRENT_FILES_CMD_LINE_OPTION_DESCRIPTION)
    private String pathToCurrent;

    @NotNull(message = PREVIOUS_FILES_OPTION_NOT_NULL_ARGUMENT_MESSAGE)
    @CommandLine.Option(names = PREVIOUS_FILES_OPTION, description = PREVIOUS_FILES_CMD_LINE_OPTION_DESCRIPTION)
    private String pathToPrevious;

    @NotNull(message = REPORT_FORMAT_OPTION_NOT_NULL_ARGUMENT_MESSAGE)
    @Pattern(regexp = "csv|psv|md|html", flags = Pattern.Flag.CASE_INSENSITIVE)
    @CommandLine.Option(names = FORMAT_OPTION, description = REPORT_FORMAT_CMD_LINE_OPTION_DESCRIPTION_WITH_DETAILS)
    private String reportFormat;

    @NotNull(message = REPORT_STRUCTURE_OPTION_NOT_NULL_ARGUMENT_MESSAGE)
    @Pattern(regexp = "vertical|horizontal", flags = Pattern.Flag.CASE_INSENSITIVE)
    @CommandLine.Option(names = STRUCTURE_OPTION, description = REPORT_STRUCTURE_CMD_LINE_OPTION_DESCRIPTION_VERTICAL_HORIZONTAL)
    private String metricFormat;

    @CommandLine.Option(names = CLASS_CONFIG, description = CONFIG_CMD_LINE_OPTION_DESCRIPTION)
    private String classConfigFile;

    @CommandLine.Option(names = METHOD_CONFIG, description = CONFIG_CMD_LINE_OPTION_DESCRIPTION)
    private String methodConfigFile;

    @Override
    public Integer call() throws Exception {
        this.validate();
        List<String> classConfig = new ArrayList<>();
        if (classConfigFile != null) {
            this.validateFilePath(classConfigFile);
            classConfig =
                Files.readAllLines(new File(classConfigFile).toPath(), Charset.defaultCharset());
        }
        List<String> methodConfig = new ArrayList<>();
        if (methodConfigFile != null) {
            this.validateFilePath(methodConfigFile);
            methodConfig =
                Files.readAllLines(new File(methodConfigFile).toPath(), Charset.defaultCharset());
        }
        if ("md".equalsIgnoreCase(reportFormat) || "html".equalsIgnoreCase(reportFormat)) {
            this.validateReportFormat(metricFormat);
        }
        CodeMetricsDiffService codeMetricsDiffService =
            new CodeMetricsDiffService(pathToPrevious, pathToCurrent);
        List<CodeMetricsClassResult> metricsResult =
            codeMetricsDiffService.getMetricsFromSourceCode();
        if ("vertical".equalsIgnoreCase(metricFormat)) {
            this.writeToUI(new CodeMetricsVerticalWriterService(classConfig, methodConfig,
                reportFormat.toUpperCase()).generateMetricsReport(metricsResult));
        } else {
            this.writeToUI(new CodeMetricsHorizontalWriterService(classConfig, methodConfig,
                reportFormat.toUpperCase()).generateMetricsReport(metricsResult));
        }
        return 0;
    }

    private void validateReportFormat(String metricFormat) {
        if ("horizontal".equalsIgnoreCase(metricFormat)) {
            throw new CommandLine.ParameterException(spec.commandLine(), INVALID_REPORT_FORMAT);
        }
    }
}

