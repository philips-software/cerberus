/*
 * Copyright of Koninklijke Philips N.V. 2020
 */
package com.philips.swcoe.cerberus.hounds;

import com.github.mauricioaniche.ck.CKClassResult;
import com.google.gson.GsonBuilder;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.CodeMetricsService;
import picocli.CommandLine;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.Callable;

import static com.philips.swcoe.cerberus.constants.DescriptionConstants.*;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.FILES_OPTION;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.JAVA_CODE_METRICS_DETECTOR;

@CommandLine.Command(name = JAVA_CODE_METRICS_DETECTOR, description = JAVA_CODE_METRICS_DETECTOR_DESCRIPTION)
public class JavaCodeMetrics extends BaseCommand implements Callable<Integer> {

    @NotNull(message = FILES_OPTION_NOT_NULL_ARGUMENT_MESSAGE)
    @CommandLine.Option(names = FILES_OPTION, description = FILES_CMD_LINE_OPTION_DESCRIPTION)
    private String pathToSource;

    @Override
    public Integer call() throws Exception {
        this.validate();
        List<CKClassResult> metricsResult = CodeMetricsService.getCodeMetrics(pathToSource);
        GsonBuilder gson = new GsonBuilder();
        this.writeToUI(gson.setPrettyPrinting().create().toJson(metricsResult)); //NOPMD
        return 0;
    }

}
