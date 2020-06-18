package com.philips.swcoe.cerberus.cerebellum.codemetrics.java;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Arrays;
import java.util.List;

public enum ReportGenerators {
    PSV {
        @Override
        public String render(CodeMetricsWriterService writerService) {
            return writerService.reportWriter.toString();
        }
    },
    CSV {
        @Override
        public String render(CodeMetricsWriterService writerService) {
            return writerService.reportWriter.toString();
        }
    },
    MD {
        @Override
        public String render(CodeMetricsWriterService writerService) {
            return String.valueOf(writerService.markdownPrinter.build());
        }
    },
    HTML {
        @Override
        public String render(CodeMetricsWriterService writerService) {
            List<Extension> extensions = Arrays.asList(TablesExtension.create());
            Parser parser = Parser.builder()
                    .extensions(extensions)
                    .build();
            Node document = parser.parse(String.valueOf(writerService.markdownPrinter.build()));
            HtmlRenderer renderer = HtmlRenderer.builder()
                    .extensions(extensions)
                    .build();
            return renderer.render(document);
        }
    };

    public abstract String render(CodeMetricsWriterService writerService);
}