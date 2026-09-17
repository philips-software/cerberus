/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.hounds;

import static com.philips.swcoe.cerberus.constants.DescriptionConstants.FILES_CMD_LINE_OPTION_DESCRIPTION;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.FILES_OPTION_NOT_NULL_ARGUMENT_MESSAGE;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.FIND_PROGRAMMING_MISTAKES_DESCRIPTION;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.LANGUAGE_OPTION_NOT_NULL_ARGUMENT_MESSAGE;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.LANGUAGE_VERSION_OPTION;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.PROGRAMMING_LANGUAGE_USED_OPTION;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.RULESET_OPTION;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.SUPPORTED_VERSIONS_MESSAGE;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.UNKNOWN_LANGUAGE_MESSAGE;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.UNKNOWN_LANGUAGE_VERSION_MESSAGE;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.FILES_OPTION;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.FIND_PROGRAMMING_MISTAKES;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.LANGUAGE_OPTION;

import jakarta.validation.constraints.NotEmpty;


import java.nio.file.Path;
import java.util.Locale;
import java.util.concurrent.Callable;
import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.PmdAnalysis;
import net.sourceforge.pmd.lang.Language;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.reporting.Report;
import picocli.CommandLine;

@CommandLine.Command(name = FIND_PROGRAMMING_MISTAKES, description = FIND_PROGRAMMING_MISTAKES_DESCRIPTION)
public class FindProgrammingMistakes extends BaseCommand implements Callable<Integer> {

    private static final String REPORT_FILE_NAME = "mistakes-report.html";

    @NotEmpty(message = FILES_OPTION_NOT_NULL_ARGUMENT_MESSAGE)
    @CommandLine.Option(names = FILES_OPTION, description = FILES_CMD_LINE_OPTION_DESCRIPTION)
    private String pathToSource;

    private String reportFormat = "html";

    @NotEmpty(message = LANGUAGE_OPTION_NOT_NULL_ARGUMENT_MESSAGE)
    @CommandLine.Option(names = LANGUAGE_OPTION, description = PROGRAMMING_LANGUAGE_USED_OPTION)
    private String language;

    @NotEmpty(message = LANGUAGE_VERSION_OPTION)
    @CommandLine.Option(names = "--java-version", description = "Java Language Version")
    private String languageVersion;

    @NotEmpty(message = RULESET_OPTION)
    @CommandLine.Option(names = "--rulesets", description = "Your Desired Ruleset for your ")
    private String pathToRulesets;

    @Override
    public Integer call() throws Exception {
        this.validate();
        PMDConfiguration pmdConfiguration = new PMDConfiguration();
        pmdConfiguration.addRuleSet(pathToRulesets);
        pmdConfiguration.addInputPath(Path.of(pathToSource));
        pmdConfiguration.setDefaultLanguageVersion(this.languageVersionOfSource());
        pmdConfiguration.setReportFormat(reportFormat);
        pmdConfiguration.setReportFile(Path.of(pathToSource, REPORT_FILE_NAME));
        /* PMD 6's -no-cache. Cerberus is run against whatever path it is given,
           so an incremental analysis cache has nothing to be incremental about
           and would only risk reporting a previous run's results. */
        pmdConfiguration.setIgnoreIncrementalAnalysis(true);

        int violations;
        /* Closing the analysis is what flushes the HTML report to the file
           configured above, so the count is read inside the block. */
        try (PmdAnalysis pmdAnalysis = PmdAnalysis.create(pmdConfiguration)) {
            Report report = pmdAnalysis.performAnalysisAndCollectReport();
            violations = report.getViolations().size();
        }

        if (violations > 0) {
            this.writeToUI("Found " + violations + " violations in the specified source path");
        } else {
            this.writeToUI("No Violations Found");
        }
        return violations;
    }

    /* PMD 6 rejected an unknown language or version while parsing its command
       line. PMD 7 has no command line here and simply answers null, so the
       checks have to be made explicitly or the run would silently fall back to
       analysing every file as the newest Java. */
    private LanguageVersion languageVersionOfSource() {
        Language languageOfSource = this.languageOfSource();
        LanguageVersion versionOfSource = languageOfSource.getVersion(languageVersion);
        if (versionOfSource == null) {
            throw new CommandLine.ParameterException(spec.commandLine(),
                languageVersion + UNKNOWN_LANGUAGE_VERSION_MESSAGE + languageOfSource.getName()
                    + SUPPORTED_VERSIONS_MESSAGE + languageOfSource.getVersions());
        }
        return versionOfSource;
    }

    private Language languageOfSource() {
        Language languageOfSource =
            LanguageRegistry.PMD.getLanguageById(language.toLowerCase(Locale.ROOT));
        if (languageOfSource == null) {
            throw new CommandLine.ParameterException(spec.commandLine(),
                UNKNOWN_LANGUAGE_MESSAGE + language);
        }
        return languageOfSource;
    }
}
