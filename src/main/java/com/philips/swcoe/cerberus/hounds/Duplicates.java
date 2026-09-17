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

import jakarta.validation.constraints.NotNull;

import java.nio.file.Path;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import net.sourceforge.pmd.cpd.CPDConfiguration;
import net.sourceforge.pmd.cpd.CPDReportRenderer;
import net.sourceforge.pmd.cpd.CpdAnalysis;
import net.sourceforge.pmd.lang.Language;
import net.sourceforge.pmd.lang.LanguageRegistry;
import picocli.CommandLine;

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
        CPDConfiguration cpdConfiguration = new CPDConfiguration();
        cpdConfiguration.setMinimumTileSize(Integer.parseInt(minimumTokens));
        cpdConfiguration.setOnlyRecognizeLanguage(this.languageToTokenizeWith(languageOfSource));
        cpdConfiguration.addInputPath(Path.of(pathToSource));
        cpdConfiguration.setRendererName(rendererNameOf(reportFormat));
        CPDReportRenderer renderer = cpdConfiguration.getCPDReportRenderer();

        AtomicInteger duplications = new AtomicInteger();
        try (CpdAnalysis cpdAnalysis = CpdAnalysis.create(cpdConfiguration)) {
            cpdAnalysis.performAnalysis(cpdReport -> {
                duplications.set(cpdReport.getMatches().size());
                System.out.println(renderer.renderToString(cpdReport));
            });
        }
        return duplications.get();
    }

    /* PMD 6 picked a renderer by hand and fell back to plain text for a format
       it did not recognise. PMD 7 has the renderers in a registry but throws on
       an unknown name, so the fallback is kept here. Its plain text renderer is
       registered as "text"; under PMD 6 the same class was called "simple". */
    private static String rendererNameOf(String reportFormat) {
        String rendererName = reportFormat.toLowerCase(Locale.ROOT);
        return CPDConfiguration.getRenderers().contains(rendererName)
            ? rendererName : CPDConfiguration.DEFAULT_RENDERER;
    }

    /* The --language option has never reached CPD: PMD 6 was handed a Java
       tokenizer whatever was passed on the command line. Left as it was so that
       this change stays a migration and nothing else. */
    private Language languageToTokenizeWith(String languageOfSource) {
        return LanguageRegistry.CPD.getLanguageById("java");
    }

}
