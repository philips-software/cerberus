package com.philips.swcoe.cerberus.cerebellum.codemetrics.java;

import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsResult;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AbstractCodeMetricsWriterService  {

    protected List<String> classConfig;
    protected List<String> methodConfig;
    protected StringWriter reportWriter;
    protected char delimiter;
    protected CSVPrinter csvPrinter;

    public AbstractCodeMetricsWriterService(List<String> classConfig, List<String> methodConfig, char delimiter) throws IOException {
        reportWriter = new StringWriter();
        this.delimiter = delimiter;
        this.classConfig = classConfig;
        this.methodConfig = methodConfig;
    }


    protected Stream<Method> getStreamMetricsMethods(Method... methods) {
        Stream<Method> methodStream = Arrays.stream(methods).filter(method -> method.getReturnType().equals(CodeMetricsResult.class));
        return methodStream;
    }

    protected boolean doesItMatterToDisplay(CodeMetricsResult codeMetricsResult, List<String> methodMetricsToDisplay) {
        Boolean doesItMatter = codeMetricsResult.getOldValue() != codeMetricsResult.getNewValue();
        return shouldWeDisplay(methodMetricsToDisplay, codeMetricsResult) && doesItMatter;
    }

    protected boolean shouldWeDisplay(List<String> listOfMetricsToDisplay, CodeMetricsResult codeMetricsResult) {
        return listOfMetricsToDisplay.contains(codeMetricsResult.getMetricName()) || listOfMetricsToDisplay.isEmpty();
    }

    protected List<String> getMetricsToDisplayFromConfig(List<String> methodConfig) {
        return methodConfig.stream().filter(metricToDisplay -> !metricToDisplay.startsWith("#")).collect(Collectors.toList());
    }

    protected CSVPrinter getCsvPrinterVertical(char delimiter, StringWriter sw) throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(
                "FILE", "CLASS", "TYPE", "METRIC", "NEW_VALUE", "OLD_VALUE"
        ).withDelimiter(delimiter);

        return new CSVPrinter(sw, csvFormat);
    }

    protected CSVPrinter getCsvPrinterHorizontal(char delimiter, StringWriter sw) throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter(delimiter);
        return new CSVPrinter(sw, csvFormat);
    }

    protected String getReport() {
        return reportWriter.toString();
    }

}
