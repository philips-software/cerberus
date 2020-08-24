/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus;

import static com.philips.swcoe.cerberus.constants.DescriptionConstants.CERBERUS_DESCRIPTION;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.CERBERUS;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.VERSION;
import static picocli.CommandLine.Model.UsageMessageSpec.SECTION_KEY_COMMAND_LIST;
import com.philips.swcoe.cerberus.hounds.Duplicates;
import com.philips.swcoe.cerberus.hounds.FindProgrammingMistakes;
import com.philips.swcoe.cerberus.hounds.JavaCodeMetrics;
import com.philips.swcoe.cerberus.hounds.JavaCodeMetricsWithDiff;
import com.philips.swcoe.cerberus.hounds.SuppressedWarnings;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;


@Command(
    description = CERBERUS_DESCRIPTION,
    name = CERBERUS,
    version = VERSION,
    subcommands = {
        Duplicates.class,
        SuppressedWarnings.class,
        JavaCodeMetrics.class,
        JavaCodeMetricsWithDiff.class,
        FindProgrammingMistakes.class
    }
)
public class Cerberus implements Callable<Integer> {

    public static void main(String[] args) {
        CommandLine cmd = new CommandLine(new Cerberus());
        cmd.getHelpSectionMap().put(SECTION_KEY_COMMAND_LIST, new DisplayHounds());
        if (args.length < 1) {
            cmd.usage(System.out);
        } else {
            cmd.execute(args);
        }
    }

    @Override
    public Integer call() throws Exception {
        return 0;
    }
}
