package com.philips.swcoe.cerberus.cerebellum.codemetrics.java;

import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsDiffResult;
import com.philips.swcoe.cerberus.constants.ProgramConstants;
import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.table.Table.Builder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AbstractCodeMetricsWriterService {

    protected List<String> classConfig;
    protected List<String> methodConfig;
    protected StringWriter reportWriter;
    protected String format;
    protected CSVPrinter csvPrinter;
    protected Builder markdownPrinter;

    public AbstractCodeMetricsWriterService(List<String> classConfig, List<String> methodConfig, String format) throws IOException {
        reportWriter = new StringWriter();
        this.format = format;
        this.classConfig = classConfig;
        this.methodConfig = methodConfig;
    }

    protected Stream<Method> getStreamMetricsMethods(Method... methods) {
        Stream<Method> methodStream = Arrays.stream(methods).filter(method -> method.getReturnType().equals(CodeMetricsDiffResult.class));
        return methodStream;
    }

    protected boolean doesItMatterToDisplay(CodeMetricsDiffResult codeMetricsDiffResult, List<String> methodMetricsToDisplay) {
        Boolean doesItMatter = codeMetricsDiffResult.getOldValue() != codeMetricsDiffResult.getNewValue();
        return shouldWeDisplay(methodMetricsToDisplay, codeMetricsDiffResult) && doesItMatter;
    }

    protected boolean shouldWeDisplay(List<String> listOfMetricsToDisplay, CodeMetricsDiffResult codeMetricsDiffResult) {
        return listOfMetricsToDisplay.contains(codeMetricsDiffResult.getMetricName()) || listOfMetricsToDisplay.isEmpty();
    }

    protected List<String> getMetricsToDisplayFromConfig(List<String> config) {
        return config.stream().filter(metricToDisplay -> !metricToDisplay.startsWith("#")).collect(Collectors.toList());
    }

    protected CSVPrinter getVerticalPrinterWithDelimiter(String format, StringWriter sw) throws IOException {
        try {
            char delimiter =  ProgramConstants.DelimeterForReport.valueOf(format.toUpperCase()).getDelimeter();
            CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(
                    "FILE", "CLASS", "TYPE", "METRIC", "NEW_VALUE", "OLD_VALUE"
            ).withDelimiter(delimiter);
            return new CSVPrinter(sw, csvFormat);
        } catch(IllegalArgumentException e) {
            return null;
        }
    }

    protected CSVPrinter getHorizontalPrinterWithDelimiter(String format, StringWriter sw) throws IOException {
        char delimiter =  ProgramConstants.DelimeterForReport.valueOf(format.toUpperCase()).getDelimeter();
        CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter(delimiter);
        return new CSVPrinter(sw, csvFormat);
    }

    protected Builder getVerticalMarkdownPrinter() {
        Builder tableBuilder = new Builder();
        tableBuilder.withAlignments(Table.ALIGN_CENTER, Table.ALIGN_CENTER, Table.ALIGN_CENTER, Table.ALIGN_CENTER, Table.ALIGN_CENTER, Table.ALIGN_CENTER);
        tableBuilder.addRow("FILE", "CLASS", "TYPE", "METRIC", "NEW_VALUE", "OLD_VALUE");
        return tableBuilder;
    }

    protected String getReport() {
        return ReportGenerators.valueOf(format).render(this);
    }

}
