package com.philips.swcoe.cerberus.cerebellum.codemetrics.java;

import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsClassResult;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsMethodResult;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsDiffResult;
import io.vavr.control.Try;
import org.apache.commons.csv.CSVPrinter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CodeMetricsHorizontalWriterService extends CodeMetricsWriterService {
    private static Logger log = Logger.getLogger(CodeMetricsHorizontalWriterService.class);

    public CodeMetricsHorizontalWriterService(List<String> classConfig, List<String> methodConfig, String reportFormat) throws IOException {
        super(classConfig, methodConfig, reportFormat);
        csvPrinter = getHorizontalPrinterWithDelimiter(reportFormat, reportWriter);
    }

    public String generateMetricsReport(List<CodeMetricsClassResult> codeMetricsClassResults) {
        codeMetricsClassResults.stream().forEach(getCodeMetricsClassResultConsumer());
        return this.getReport();
    }

    private Consumer<CodeMetricsClassResult> getCodeMetricsClassResultConsumer() {
        return codeMetricsClassResult -> {
            writeClassMetrics(csvPrinter, codeMetricsClassResult);
            writeMethodMetrics(csvPrinter, codeMetricsClassResult.getMethodMetrics());
        };
    }

    private void writeMethodMetrics(CSVPrinter csvPrinter, List<CodeMetricsMethodResult> codeMetricsMethodResults) {
        codeMetricsMethodResults.stream().forEach(codeMetricsMethodResult -> {
            List<String> dataToWriteForMethod = new ArrayList<String>();
            dataToWriteForMethod.add(codeMetricsMethodResult.getFile());
            dataToWriteForMethod.add(codeMetricsMethodResult.getMethodName());
            dataToWriteForMethod.add(codeMetricsMethodResult.getType());
            getStreamMetricsMethods(codeMetricsMethodResult.getClass().getMethods()).forEach(methodInMethodResult -> {
                Try.run(() -> processMetricResult(dataToWriteForMethod, (CodeMetricsDiffResult) methodInMethodResult.invoke(codeMetricsMethodResult), methodConfig))
                        .onFailure(exception -> log.trace(exception.getStackTrace()));
            });
            writeReportData(csvPrinter, dataToWriteForMethod);
        });
    }

    private void writeClassMetrics(CSVPrinter csvPrinter, CodeMetricsClassResult codeMetricsClassResult) {
        List<String> dataToWriteForClass = new ArrayList<String>();
        dataToWriteForClass.add(codeMetricsClassResult.getFile());
        dataToWriteForClass.add(codeMetricsClassResult.getClassName());
        dataToWriteForClass.add(codeMetricsClassResult.getType());
        getStreamMetricsMethods(codeMetricsClassResult.getClass().getMethods()).forEach(methodInClassResult -> {
            Try.run(() -> processMetricResult(dataToWriteForClass, (CodeMetricsDiffResult) methodInClassResult.invoke(codeMetricsClassResult), classConfig))
                    .onFailure(exception -> log.trace(exception.getStackTrace()));
        });
        writeReportData(csvPrinter, dataToWriteForClass);
    }

    private void processMetricResult(List<String> dataToWriteForClass, CodeMetricsDiffResult codeMetricsDiffResult, List<String> classConfig) {
        Try.of(() -> codeMetricsDiffResult)
                .andThen(codeMetricsResultForClass -> Try.run(() -> pushMetricsToWrite(codeMetricsResultForClass, dataToWriteForClass, classConfig))
                        .onFailure(exception -> log.trace(exception.getStackTrace())));
    }


    private void writeReportData(CSVPrinter csvPrinter, List<String> dataToWrite) {
        if (dataToWrite.size() > 3) {
            Try.run(() -> csvPrinter.printRecord(dataToWrite.toArray()))
                    .onFailure(exception -> log.trace(exception.getStackTrace()));
        }
    }

    private void pushMetricsToWrite(CodeMetricsDiffResult codeMetricsDiffResult, List<String> dataToWrite, List<String> displayConfig) {
        List<String> metricsToDisplay = getMetricsToDisplayFromConfig(displayConfig);
        if(doesItMatterToDisplay(codeMetricsDiffResult, metricsToDisplay)) {
            dataToWrite.add(codeMetricsDiffResult.getMetricName());
            dataToWrite.add(String.valueOf(codeMetricsDiffResult.getNewValue()));
            dataToWrite.add(String.valueOf(codeMetricsDiffResult.getOldValue()));
        }
    }


}
