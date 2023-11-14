package org.kpi.threads;

import org.kpi.utils.FileHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ThreadIndexer extends Thread {
    List<File> files;
    FileHandler fileHandler;
    int startIndex;
    int endIndex;

    public ThreadIndexer(List<File> files, FileHandler fileHandler, int startIndex, int endIndex) {
        this.files = files;
        this.fileHandler = fileHandler;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public void run() {
        for (int i = startIndex; i < endIndex; i++) {
            try {
                fileHandler.readFileContent(files.get(i));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
