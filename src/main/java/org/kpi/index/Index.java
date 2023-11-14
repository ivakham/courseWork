package org.kpi.index;

import org.kpi.threads.ThreadIndexer;
import org.kpi.utils.FileHandler;

import java.io.File;
import java.util.List;

public class Index {
    private FileHandler fileHandler;

    public Index(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public long index(int numberOfThreads, List<File> files) throws InterruptedException {
        ThreadIndexer[] threadForIndexers = new ThreadIndexer[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            threadForIndexers[i] = new ThreadIndexer(
                    files,
                    fileHandler,
                    (files.size() / numberOfThreads) * i,
                    (files.size() / numberOfThreads) * (i + 1)
            );
        }

        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < numberOfThreads; i++) {
            threadForIndexers[i].start();
        }
        for (int i = 0; i < numberOfThreads; i++) {
            threadForIndexers[i].join();
        }
        long executionTime = System.currentTimeMillis() - currentTime;
        return executionTime;
    }
}
