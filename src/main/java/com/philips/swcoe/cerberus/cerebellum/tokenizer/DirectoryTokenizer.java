/*
 * Copyright of Koninklijke Philips N.V. 2020
 */

package com.philips.swcoe.cerberus.cerebellum.tokenizer;

import static com.philips.swcoe.cerberus.constants.DescriptionConstants.COULDNT_FIND_DIRECTORY;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.SPACE;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

public class DirectoryTokenizer implements Tokenizer {

    /* The extensions each language is read from. PMD used to supply both these
       and the lexer, but the only consumer of this class is the
       suppressed-warnings detection, which matches on the text of each line and
       never looked at the tokens. Reading the files directly keeps that working
       without PMD having to be able to lex the language. */
    private static final Map<String, Set<String>> EXTENSIONS_BY_LANGUAGE = Map.of(
        "java", Set.of("java"),
        "cpp", Set.of("cpp", "cxx", "cc", "c", "h", "hpp", "hxx"));

    private static final char BYTE_ORDER_MARK = '\uFEFF';

    private String encoding;
    private Map<String, List<String>> source;

    public DirectoryTokenizer() {
        encoding = System.getProperty("file.encoding");
        source = new TreeMap<>();
    }

    public Map<String, List<String>> getSource() {
        return source;
    }

    public void setSource(Map<String, List<String>> source) {
        this.source = source;
    }

    @Override
    public void tokenize(File dir, String languageOfSource) throws IOException {
        if (!dir.exists()) {
            throw new FileNotFoundException(COULDNT_FIND_DIRECTORY + SPACE + dir);
        }
        Set<String> extensions = extensionsOf(languageOfSource);
        try (Stream<Path> paths = Files.walk(dir.toPath())) {
            List<Path> filesOfLanguage = paths.filter(Files::isRegularFile)
                .filter(file -> hasOneOfExtensions(file, extensions)).toList();
            for (Path file : filesOfLanguage) {
                source.put(file.toAbsolutePath().toString(), readLines(file));
            }
        }
    }

    private List<String> readLines(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file, Charset.forName(encoding));
        /* Drop a byte order mark, which readAllLines keeps as a leading U+FEFF
           on the first line where PMD used to discard it. The detectors match on
           the start of a line, so a mark in front of a "#pragma warning disable"
           would hide the suppression. Several of the C# fixtures have one. */
        if (!lines.isEmpty() && !lines.get(0).isEmpty()
            && lines.get(0).charAt(0) == BYTE_ORDER_MARK) {
            lines.set(0, lines.get(0).substring(1));
        }
        return lines;
    }

    private static Set<String> extensionsOf(String languageOfSource) {
        String language = languageOfSource.toLowerCase(Locale.ROOT);
        /* For a language that is not listed - "cs" being the only other one the
           CLI accepts - the extension is the language name itself. That is what
           PMD fell back to, and it is why "cs" worked even though pmd-cs was
           never on the classpath. */
        return EXTENSIONS_BY_LANGUAGE.getOrDefault(language, Set.of(language));
    }

    private static boolean hasOneOfExtensions(Path file, Set<String> extensions) {
        Path fileName = file.getFileName();
        /* Only a root path has no file name, and a root is never walked into as a
           regular file. */
        if (fileName == null) {
            return false;
        }
        String name = fileName.toString();
        int lastDot = name.lastIndexOf('.');
        return lastDot >= 0
            && extensions.contains(name.substring(lastDot + 1).toLowerCase(Locale.ROOT));
    }

}
