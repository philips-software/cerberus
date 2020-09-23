/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.cerebellum.tokenizer;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import net.sourceforge.pmd.cpd.SourceCode;

public class DirectoryTokenizerHelper {
    private Map<String, SourceCode> sourceAfterExclusion = new TreeMap<String, SourceCode>();

    Map<String, SourceCode> getSource(Map<String, SourceCode> source, String exclusionLists) {
        if (StringUtils.isEmpty(exclusionLists)) {
            return source;
        }
        return getSourceAfterExclusion(source, exclusionLists.split(","));
    }

    private Map<String, SourceCode> getSourceAfterExclusion(Map<String, SourceCode> source, String... exclusionLists) {
        for (Entry<String, SourceCode> entry : source.entrySet()) {
            for (String exclusionList : exclusionLists) {
                if (!entry.getKey().contains(exclusionList)) {
                    sourceAfterExclusion.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return sourceAfterExclusion;
    }

}
