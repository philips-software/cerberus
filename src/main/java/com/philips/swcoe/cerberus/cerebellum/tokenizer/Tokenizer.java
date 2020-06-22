/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.cerebellum.tokenizer;

import java.io.File;
import java.io.IOException;

public interface Tokenizer {
    void tokenize(File dir, String languageOfSource) throws IOException;
}
