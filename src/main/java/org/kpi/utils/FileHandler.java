package org.kpi.utils;

import org.kpi.index.InvertedIndex;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FileHandler {
    private final InvertedIndex invertedIndex;
    private final List<File> files = new ArrayList<>();

    public FileHandler(InvertedIndex invertedIndex) {
        this.invertedIndex = invertedIndex;
    }

    public String readByLines(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        return builder.toString();
    }

    public void scanDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File file : Objects.requireNonNull(files)) {
                scanDirectory(file);
            }
        } else {
            files.add(directory);
        }
    }

    public void readFileContent(File file) throws IOException {
        String readFile = readByLines(file);
        String[] splited = readFile.split("\\W");
        List<String> cleanList = Arrays.stream(splited).filter(str -> !str.isEmpty()).toList();
        for (String word : cleanList) {
            invertedIndex.pushToVocabulary(word.toLowerCase(), file.toString());
        }
    }

    public List<File> getAllFiles() {
        return files;
    }
}
