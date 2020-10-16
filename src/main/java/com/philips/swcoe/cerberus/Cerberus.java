package com.philips.swcoe.cerberus;

import static com.philips.swcoe.cerberus.constants.DescriptionConstants.CERBERUS_DESCRIPTION;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.CERBERUS;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.VERSION;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.philips.swcoe.cerberus.hounds.Duplicates;
import com.philips.swcoe.cerberus.hounds.FindProgrammingMistakes;
import com.philips.swcoe.cerberus.hounds.JavaCodeMetrics;
import com.philips.swcoe.cerberus.hounds.JavaCodeMetricsWithDiff;
import com.philips.swcoe.cerberus.hounds.SuppressedWarnings;
import picocli.CommandLine;

@SpringBootApplication
@CommandLine.Command(
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
public class Cerberus  {
    public static void main(String[] args) {
        SpringApplication.run(Cerberus.class, args);
        CommandLine cmd = new CommandLine(new Cerberus());
        System.exit(cmd.execute(args));
    }
}
