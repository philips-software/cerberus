package com.philips.swcoe.cerberus.cerebellum.codemetrics.java;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class CodeMetricsMarkdownWriterService  {

    private static Logger log = Logger.getLogger(CodeMetricsMarkdownWriterService.class);
    protected List<String> classConfig;
    protected List<String> methodConfig;

    public CodeMetricsMarkdownWriterService(List<String> classConfig, List<String> methodConfig) throws IOException {

    }

    public CodeMetricsMarkdownWriterService() {
    }
}
