/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.hounds;

import static com.philips.swcoe.cerberus.constants.DescriptionConstants.FILES_CMD_LINE_OPTION_DESCRIPTION;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.FILES_OPTION_NOT_NULL_ARGUMENT_MESSAGE;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.LANGUAGE_CMD_LINE_OPTION_DESCRIPTION;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.LANGUAGE_OPTION_NOT_NULL_ARGUMENT_MESSAGE;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.MINIMUM_TOKEN_CMD_LINE_OPTION_DESCRIPTION;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.MINIMUM_TOKEN_OPTION_NOT_NULL_ARGUMENT_MESSAGE;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.REPORT_FORMAT_CMD_LINE_OPTION_DESCRIPTION;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.REPORT_FORMAT_OPTION_NOT_NULL_ARGUMENT_MESSAGE;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.COPY_PASTE_DETECTOR;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.FILES_OPTION;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.FORMAT_OPTION;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.LANGUAGE_OPTION;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.MINIMUM_TOKENS_OPTION;
import static net.sourceforge.pmd.cpd.CPDCommandLineInterface.addSourceFilesToCPD;

import jakarta.validation.constraints.NotNull;

import org.springframework.stereotype.Component;
import com.google.common.collect.Lists;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import net.sourceforge.pmd.cpd.CPD;
import net.sourceforge.pmd.cpd.CPDConfiguration;
import net.sourceforge.pmd.cpd.Language;
import net.sourceforge.pmd.cpd.JavaLanguage;
import net.sourceforge.pmd.cpd.SimpleRenderer;
import net.sourceforge.pmd.cpd.XMLRenderer;
import net.sourceforge.pmd.cpd.CSVRenderer;
import net.sourceforge.pmd.cpd.VSRenderer;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = COPY_PASTE_DETECTOR, mixinStandardHelpOptions = true, description = "Detect duplicated blocks of code in your source code")
public class Duplicates extends BaseCommand implements Callable<Integer> {

    @NotNull(message = FILES_OPTION_NOT_NULL_ARGUMENT_MESSAGE)
    @CommandLine.Option(names = FILES_OPTION, description = FILES_CMD_LINE_OPTION_DESCRIPTION)
    private String pathToSource;

    @NotNull(message = REPORT_FORMAT_OPTION_NOT_NULL_ARGUMENT_MESSAGE)
    @CommandLine.Option(names = FORMAT_OPTION, description = REPORT_FORMAT_CMD_LINE_OPTION_DESCRIPTION)
    private String reportFormat;

    @NotNull(message = MINIMUM_TOKEN_OPTION_NOT_NULL_ARGUMENT_MESSAGE)
    @CommandLine.Option(names = MINIMUM_TOKENS_OPTION, description = MINIMUM_TOKEN_CMD_LINE_OPTION_DESCRIPTION)
    private String minimumTokens;

    @NotNull(message = LANGUAGE_OPTION_NOT_NULL_ARGUMENT_MESSAGE)
    @CommandLine.Option(names = LANGUAGE_OPTION, description = LANGUAGE_CMD_LINE_OPTION_DESCRIPTION)
    private String languageOfSource;
    
    @Override
    public Integer call() throws Exception {
        this.validate();
        CPDConfiguration arguments = new CPDConfiguration();
        arguments.setMinimumTileSize(Integer.parseInt(minimumTokens));
        // Set language - PMD 6.x supports direct language instantiation  
        Language language = createLanguage(languageOfSource);
        arguments.setLanguage(language);
        // Set source files path
        arguments.setFiles(Lists.newArrayList(new java.io.File(pathToSource)));
        CPD cpd = new CPD(arguments);
        addSourceFilesToCPD(cpd, arguments);
        cpd.go();

        // Manually render based on format in PMD 6.x
        net.sourceforge.pmd.cpd.Renderer renderer = getRenderer(reportFormat);
        String output = renderer.render(cpd.getMatches());
        System.out.println(output);
        return Lists.newArrayList(cpd.getMatches()).size();
    }
    
    private net.sourceforge.pmd.cpd.Renderer getRenderer(String format) {
        // In PMD 6.x, renderers return formatted String from render() method
        // Use a simple if-else chain to avoid high cyclomatic complexity from switch
        String fmt = format.toLowerCase();
        if ("xml".equals(fmt)) {
            return new XMLRenderer();
        } else if ("csv".equals(fmt)) {
            return new CSVRenderer();
        } else if ("vs".equals(fmt)) {
            return new VSRenderer();
        } else {
            // SimpleRenderer outputs formatted text (default)
            return new SimpleRenderer();
        }
    }

    private Language createLanguage(String lang) {
        // PMD 6.x uses direct language instantiation
        // Currently only Java is supported
        return new JavaLanguage();
    }

}
