package org.kpi.index;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InvertedIndex {
    private final Map<String, Set<String>> vocabulary = new ConcurrentHashMap<>();

    public void pushToVocabulary(String word, String file) {
        if (word == null || file == null) {
            return;
        }
        if (vocabulary.containsKey(word)) {
            vocabulary.get(word).add(file);
        } else {
            Set<String> files = new HashSet<>();
            files.add(file);
            vocabulary.put(word, files);
        }
    }

    public Set<String> getListOfFilesByKey(String word) {
        return vocabulary.get(word);
    }

    public Map<String, Set<String>> getVocabulary() {
        return vocabulary;
    }

    public boolean isIndexed(){
        return !vocabulary.isEmpty();
    }
}
