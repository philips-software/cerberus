/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.hounds;

import static com.philips.swcoe.cerberus.constants.DescriptionConstants.INVALID_FILE_PATH;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.COLON;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.ERROR;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.NEW_LINE;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.SPACE;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Set;
import picocli.CommandLine;

public class BaseCommand {

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    protected void validate() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<BaseCommand>> violations = validator.validate(this);
        if (!violations.isEmpty()) {
            StringBuilder errorMsg = new StringBuilder();
            violations.stream().forEach(violation -> {
                errorMsg.append(ERROR).append(COLON).append(SPACE).append(violation.getMessage())
                    .append(NEW_LINE);
            });
            throw new CommandLine.ParameterException(spec.commandLine(), errorMsg.toString());
        }
    }

    protected void writeToUI(String stuffToWrite) throws IOException {
        OutputStreamWriter uiWriter = new OutputStreamWriter(System.out);
        uiWriter.write(stuffToWrite);
        uiWriter.flush();
    }

    protected void validateFilePath(String filePath) {
        File file = new File(filePath);
        if ((!file.canRead())) {
            throw new CommandLine.ParameterException(spec.commandLine(), INVALID_FILE_PATH);
        }
    }


}
