/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.hounds;

import static com.philips.swcoe.cerberus.constants.DescriptionConstants.FILES_CMD_LINE_OPTION_DESCRIPTION;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.FILES_OPTION_NOT_NULL_ARGUMENT_MESSAGE;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.JAVA_CODE_METRICS_DETECTOR_DESCRIPTION;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.FILES_OPTION;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.JAVA_CODE_METRICS_DETECTOR;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import com.github.mauricioaniche.ck.CKClassResult;
import com.google.gson.GsonBuilder;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.CodeMetricsService;
import java.util.List;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@Component
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
