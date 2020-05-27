/*
 * Copyright of Koninklijke Philips N.V. 2020
 */
package com.philips.swcoe.cerberus;

import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;
import picocli.CommandLine.Help.Column;
import picocli.CommandLine.Help.TextTable;
import picocli.CommandLine.IHelpSectionRenderer;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Model.UsageMessageSpec;

import static com.philips.swcoe.cerberus.constants.ProgramConstants.DOUBLE_SPACE;

public class DisplayHounds implements IHelpSectionRenderer {

    @Override
    public String render(CommandLine.Help help) {
        CommandSpec spec = help.commandSpec();
        if (spec.subcommands().isEmpty()) {
            return StringUtils.EMPTY;
        }

        TextTable textTable = TextTable.forColumns(help.ansi(), new Column(15, 2, Column.Overflow.SPAN), new Column(spec.usageMessage().width() - 15, 2, Column.Overflow.WRAP));
        textTable.setAdjustLineBreaksForWideCJKCharacters(spec.usageMessage().adjustLineBreaksForWideCJKCharacters());

        for (CommandLine subcommand : spec.subcommands().values()) {
            addHierarchy(subcommand, textTable, StringUtils.EMPTY);
        }
        return textTable.toString();
    }

    private void addHierarchy(CommandLine cmd, TextTable textTable, String indent) {
        // create comma-separated list of command name and aliases
        String names = cmd.getCommandSpec().names().toString();
        names = names.substring(1, names.length() - 1); // remove leading '[' and trailing ']'

        // command description is taken from header or description
        String description = description(cmd.getCommandSpec().usageMessage());

        // add a line for this command to the layout
        textTable.addRowValues(indent + names, description);

        // add its subcommands (if any)
        for (CommandLine sub : cmd.getSubcommands().values()) {
            addHierarchy(sub, textTable, indent + DOUBLE_SPACE);
        }
    }

    private String description(UsageMessageSpec usageMessage) {
        if (usageMessage.header().length > 0) {
            return usageMessage.header()[0];
        }
        if (usageMessage.description().length > 0) {
            return usageMessage.description()[0];
        }
        return "";
    }
}