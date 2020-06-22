/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.cerebellum.swd;

import static com.philips.swcoe.cerberus.constants.ProgramConstants.FILE;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.JAVA_COMMENT_BEGINNING;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.JAVA_COMMENT_END;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.LINE;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.NO;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.SPACE;

import org.apache.commons.lang3.StringUtils;

import com.philips.swcoe.cerberus.cerebellum.tokenizer.DirectoryTokenizer;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;
import net.sourceforge.pmd.cpd.SourceCode;

public class BaseSuppressedWarningsDetector {
    protected String[] waysToSuppress;
    private Map<String, Map<String, String>> suppressedWarnings;

    public Map<String, Map<String, String>> getSuppressedWarnings() {
        return suppressedWarnings;
    }

    protected void setSuppressedWarnings(Map<String, Map<String, String>> suppressedWarnings) {
        this.suppressedWarnings = suppressedWarnings;
    }

    public void detect(DirectoryTokenizer tokenizer) {
        this.setSuppressedWarnings(new TreeMap<String, Map<String, String>>());
        Map<String, SourceCode> source = tokenizer.getSource();
        for (Map.Entry<String, SourceCode> entry : source.entrySet()) {
            Map<String, String> suppressionsInCode = this.getSuppressorsInSourceFile(entry);
            this.collectDetectedSuppressedWarnings(entry, suppressionsInCode);
        }
    }

    protected void collectDetectedSuppressedWarnings(Map.Entry<String, SourceCode> entry,
                                                     Map<String, String> suppressionsInCode) {
        if (!suppressionsInCode.isEmpty()) {
            this.getSuppressedWarnings().put(FILE + SPACE + entry.getKey(), suppressionsInCode);
        }
    }

    protected boolean isWithInComment(String previousLine, String nextLine) {
        return !previousLine.startsWith(JAVA_COMMENT_BEGINNING)
            && !nextLine.endsWith(JAVA_COMMENT_END);
    }

    protected Map<String, String> getSuppressorsInSourceFile(Map.Entry<String, SourceCode> entry) {
        List<String> listOfCode = entry.getValue().getCode();
        Map<String, String> suppressorsInCode = new TreeMap<>();
        int sizeOfListOfCode = listOfCode.size();
        for (int j = 0; j < sizeOfListOfCode; j++) {
            String line = listOfCode.get(j);
            String previousLine = (j != 0) ? listOfCode.get(j - 1) : StringUtils.EMPTY;
            String nextLine =
                ((j + 1) >= sizeOfListOfCode) ? StringUtils.EMPTY : listOfCode.get(j + 1);
            if (Stream.of(this.waysToSuppress).anyMatch(line::startsWith)
                && isWithInComment(previousLine, nextLine)) {
                suppressorsInCode.put(LINE + SPACE + NO + SPACE + j, line);
            }
        }
        return suppressorsInCode;
    }
}
