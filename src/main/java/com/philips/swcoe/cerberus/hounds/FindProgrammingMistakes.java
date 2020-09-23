/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.hounds;

import static com.philips.swcoe.cerberus.constants.DescriptionConstants.EXCLUSION_DESCRIPTION;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.FILES_CMD_LINE_OPTION_DESCRIPTION;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.FILES_OPTION_NOT_NULL_ARGUMENT_MESSAGE;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.FIND_PROGRAMMING_MISTAKES_DESCRIPTION;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.LANGUAGE_OPTION_NOT_NULL_ARGUMENT_MESSAGE;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.LANGUAGE_VERSION_OPTION;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.PROGRAMMING_LANGUAGE_USED_OPTION;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.RULESET_OPTION;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.EXCLUDE;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.FILES_OPTION;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.FIND_PROGRAMMING_MISTAKES;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.LANGUAGE_OPTION;

import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Callable;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.cli.PMDCommandLineInterface;
import net.sourceforge.pmd.cli.PMDParameters;
import picocli.CommandLine;

@CommandLine.Command(name = FIND_PROGRAMMING_MISTAKES, description = FIND_PROGRAMMING_MISTAKES_DESCRIPTION)
public class FindProgrammingMistakes extends BaseCommand implements Callable<Integer> {

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
    
    @CommandLine.Option(names = EXCLUDE, description = EXCLUSION_DESCRIPTION)
    private String exclusionFiles;

    @Override
    public Integer call() throws Exception {
        this.validate();
        String ignoreList = null;
        String ignoreParamter = null;
        if (!StringUtils.isEmpty(exclusionFiles)) {
            ignoreList = this.createIgnoreList(exclusionFiles);
            ignoreParamter = "-ignorelist";
        }
        String[] argumentsOfPMD = {
            "-rulesets", pathToRulesets,
            "-dir", pathToSource,
            "-language", language,
            "-version", languageVersion,
            "-format", reportFormat,
            "-reportfile", pathToSource + "/mistakes-report.html",
            "-no-cache",
            ignoreParamter, ignoreList
        };

        PMDParameters pmdParameters =
            PMDCommandLineInterface.extractParameters(new PMDParameters(), 
                    Arrays.stream(argumentsOfPMD).filter(Objects::nonNull).toArray(String[]::new), "pmd");
        PMDConfiguration pmdConfiguration = pmdParameters.toConfiguration();

        int violations = PMD.doPMD(pmdConfiguration);
        if (violations > 0) {
            this.writeToUI("Found " + violations + " violations in the specified source path");
        } else {
            this.writeToUI("No Violations Found");
        }
        return 0;
    }
    
    private String createIgnoreList(String exclusionFiles) throws IOException {
        File temp = File.createTempFile("ignoreList", ".txt");
        String[] fileList = exclusionFiles.split(",");
        for (String file : fileList) {
            Files.write(Paths.get(temp.toURI()), (file + System.lineSeparator()).getBytes(Charset.forName("UTF-8")), StandardOpenOption.APPEND);
        }
        return temp.toString();
    }
}
