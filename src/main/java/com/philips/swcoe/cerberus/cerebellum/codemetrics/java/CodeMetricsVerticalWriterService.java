package com.philips.swcoe.cerberus.cerebellum.codemetrics.java;

import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsClassResult;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsDiffResult;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsMethodResult;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsResult;
import io.vavr.control.Try;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class CodeMetricsVerticalWriterService extends AbstractCodeMetricsWriterService {

    private static Logger log = Logger.getLogger(CodeMetricsVerticalWriterService.class);

    public CodeMetricsVerticalWriterService(List<String> classConfig, List<String> methodConfig, String reportFormat) throws IOException {
        super(classConfig, methodConfig, reportFormat);
        csvPrinter = getVerticalPrinterWithDelimiter(reportFormat, reportWriter);
        markdownPrinter = getVerticalMarkdownPrinter();
    }

    public String generateMetricsReport(List<CodeMetricsClassResult> codeMetricsClassResults) {
        codeMetricsClassResults.stream().forEach(codeMetricsClassResult -> {
            processMetrics(codeMetricsClassResult, classConfig);
            List<CodeMetricsMethodResult> codeMetricsMethodResults = codeMetricsClassResult.getMethodMetrics();
            codeMetricsMethodResults.stream().forEach(codeMetricsMethodResult -> {
                processMetrics(codeMetricsMethodResult, methodConfig);
            });
        });
        return this.getReport();
    }

    private void processMetrics(CodeMetricsResult codeMetricsResult, List<String> config) {
        getStreamMetricsMethods(codeMetricsResult.getClass().getMethods()).forEach(methodInResult -> {
            Try.of(() -> (CodeMetricsDiffResult) methodInResult.invoke(codeMetricsResult))
                                .andThen(codeMetricsResultForConstruct -> Try.run(() -> writeMetrics(codeMetricsResultForConstruct, config))
                                .onFailure(exception -> log.trace(exception.getStackTrace())));
        });
    }

    private void writeMetrics(CodeMetricsDiffResult codeMetricsDiffResult, List<String> config) throws IOException {
        if(null == csvPrinter) {
            writeMetricsInMarkDown(codeMetricsDiffResult, config);
        } else  {
            writeMetricsInCSV(codeMetricsDiffResult, config);
        }
    }

    private void writeMetricsInMarkDown(CodeMetricsDiffResult codeMetricsDiffResult, List<String> config) {
        if(doesItMatterToDisplay(codeMetricsDiffResult, getMetricsToDisplayFromConfig(config))) {
            markdownPrinter.addRow(
                    codeMetricsDiffResult.getFileName(),
                    codeMetricsDiffResult.getConstructName(),
                    codeMetricsDiffResult.getConstructType(),
                    codeMetricsDiffResult.getMetricName(),
                    codeMetricsDiffResult.getNewValue(), codeMetricsDiffResult.getOldValue());
        }
    }

    private void writeMetricsInCSV(CodeMetricsDiffResult codeMetricsDiffResult, List<String> config) throws IOException {
        if(doesItMatterToDisplay(codeMetricsDiffResult, getMetricsToDisplayFromConfig(config))) {
            csvPrinter.printRecord( codeMetricsDiffResult.getFileName(), codeMetricsDiffResult.getConstructName(), codeMetricsDiffResult.getConstructType(), codeMetricsDiffResult.getMetricName(), codeMetricsDiffResult.getNewValue(), codeMetricsDiffResult.getOldValue());
        }
    }


}
