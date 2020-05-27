package com.philips.swcoe.cerberus.cerebellum.codemetrics.java;

import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsClassResult;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsMethodResult;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsResult;
import io.vavr.control.Try;
import org.apache.commons.csv.CSVPrinter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class CodeMetricsVerticalWriterService extends AbstractCodeMetricsWriterService {

    private static Logger log = Logger.getLogger(CodeMetricsVerticalWriterService.class);

    public CodeMetricsVerticalWriterService(List<String> classConfig, List<String> methodConfig, char delimiter) throws IOException {
        super(classConfig, methodConfig, delimiter);
        csvPrinter = getCsvPrinterVertical(this.delimiter, reportWriter);
    }

    public String generateMetricsReport(List<CodeMetricsClassResult> codeMetricsClassResults) {
        codeMetricsClassResults.stream().forEach(javaCodeMetricsClassResult -> {
            getStreamMetricsMethods(javaCodeMetricsClassResult.getClass().getMethods()).forEach(methodInClassResult -> {
                Try.of(() -> (CodeMetricsResult) methodInClassResult.invoke(javaCodeMetricsClassResult))
                                    .andThen(codeMetricsResultForClass -> Try.run(() -> writeClassMetrics(csvPrinter, javaCodeMetricsClassResult, codeMetricsResultForClass ))
                                    .onFailure(exception -> log.trace(exception.getStackTrace())));
            });
            List<CodeMetricsMethodResult> codeMetricsMethodResults = javaCodeMetricsClassResult.getMethodMetrics();
            codeMetricsMethodResults.stream().forEach(codeMetricsMethodResult -> {
                getStreamMetricsMethods(codeMetricsMethodResult.getClass().getMethods()).forEach(methodInMethodResult -> {
                    Try.of(() -> (CodeMetricsResult) methodInMethodResult.invoke(codeMetricsMethodResult))
                                    .andThen(codeMetricsResultForMethod -> Try.run(() -> writeMethodMetrics(csvPrinter, codeMetricsMethodResult, codeMetricsResultForMethod, javaCodeMetricsClassResult))
                                    .onFailure(exception -> log.trace(exception.getStackTrace())));
                });
            });
        });
        return this.getReport();
    }

    private void writeClassMetrics(CSVPrinter csvPrinter,
                                   CodeMetricsClassResult codeMetricsClassResult,
                                   CodeMetricsResult codeMetricsResult) throws IOException {
        List<String> classMetricsToDisplay = getMetricsToDisplayFromConfig(classConfig);
        if(doesItMatterToDisplay(codeMetricsResult, classMetricsToDisplay)) {
            csvPrinter.printRecord(
                    codeMetricsClassResult.getFile(),
                    codeMetricsClassResult.getClassName(),
                    codeMetricsClassResult.getType(),
                    codeMetricsResult.getMetricName(),
                    codeMetricsResult.getNewValue(),
                    codeMetricsResult.getOldValue()
            );
        }
    }

    private void writeMethodMetrics(CSVPrinter csvPrinter,
                                    CodeMetricsMethodResult codeMetricsMethodResult,
                                    CodeMetricsResult codeMetricsResult, CodeMetricsClassResult codeMetricsClassResult) throws IOException {
        List<String> methodMetricsToDisplay = getMetricsToDisplayFromConfig(methodConfig);
        if(doesItMatterToDisplay(codeMetricsResult, methodMetricsToDisplay)) {
            csvPrinter.printRecord(
                    codeMetricsClassResult.getFile(),
                    codeMetricsClassResult.getClassName() + "::" + codeMetricsMethodResult.getMethodName(),
                    "METHOD",
                    codeMetricsResult.getMetricName(),
                    codeMetricsResult.getNewValue(),
                    codeMetricsResult.getOldValue()
            );
        }
    }


}
