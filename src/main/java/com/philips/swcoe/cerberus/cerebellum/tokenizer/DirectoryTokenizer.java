/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.cerebellum.tokenizer;

import static com.philips.swcoe.cerberus.constants.DescriptionConstants.COULDNT_FIND_DIRECTORY;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.SPACE;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.sourceforge.pmd.cpd.Language;
import net.sourceforge.pmd.cpd.LanguageFactory;
import net.sourceforge.pmd.cpd.SourceCode;
import net.sourceforge.pmd.cpd.Tokens;
import net.sourceforge.pmd.util.FileFinder;

public class DirectoryTokenizer implements Tokenizer {

    private Language language;
    private Tokens tokens;
    private String encoding;
    private Map<String, SourceCode> source;
    private DirectoryTokenizerHelper directoryTokenizerHelper;

    public DirectoryTokenizer() {
        tokens = new Tokens();
        encoding = System.getProperty("file.encoding");
        source = new TreeMap<>();
        directoryTokenizerHelper = new DirectoryTokenizerHelper();
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Tokens getTokens() {
        return tokens;
    }

    public void setTokens(Tokens tokens) {
        this.tokens = tokens;
    }

    public Map<String, SourceCode> getSource(String exclusionLists) {
        return directoryTokenizerHelper.getSource(source, exclusionLists);
    }

    public void setSource(Map<String, SourceCode> source) {
        this.source = source;
    }

    @Override
    public void tokenize(File dir, String languageOfSource) throws IOException {
        language = LanguageFactory.createLanguage(languageOfSource);
        tokenizeDirectory(dir, true);
    }

    private void tokenizeLanguageFiles(List<File> files) throws IOException {
        for (File f : files) {
            tokenizeFile(f);
        }
    }

    private void tokenizeFile(File file) throws IOException {
        SourceCode sourceCode = new SourceCode(new SourceCode.FileCodeLoader(file, encoding));
        language.getTokenizer().tokenize(sourceCode, tokens);
        source.put(sourceCode.getFileName(), sourceCode);
    }

    private void tokenizeDirectory(File dir, boolean recurse) throws IOException {
        if (!dir.exists()) {
            throw new FileNotFoundException(COULDNT_FIND_DIRECTORY + SPACE + dir);
        }
        FileFinder finder = new FileFinder();
        tokenizeLanguageFiles(finder.findFilesFrom(dir, language.getFileFilter(), recurse));
    }

}
