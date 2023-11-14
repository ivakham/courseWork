package org.kpi.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static Scanner scanner = new Scanner(System.in);
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;

    public static void findWord(boolean isIndexed) throws IOException {
        System.out.println("Please, enter word, you want to search: ");
        if (!isIndexed) {
            scanner.nextLine();
        }
        String wordToSearch = scanner.nextLine().toLowerCase();
        outputStream.writeUTF(wordToSearch);

        String indexedFiles = inputStream.readUTF();
        if (indexedFiles.equals("no files found")) {
            System.out.println("Sorry, but there is no such word in files.");
        } else {
            String cleanString = indexedFiles.replace("[", "").replace("]", "");
            String[] filesWithWord = cleanString.split(", ");
            System.out.println("Files that contain the word that you have searched for:");
            for (String filePath : filesWithWord) {
                System.out.println(filePath);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 8000)) {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            boolean findOneMoreWord = false;
            System.out.println("Welcome to 'Indexer'!!! \nHere you can search words in movie reviews. \nSo we can start our work ;)");

            System.out.println("First step: files indexing. Now you will enter number of threads that will be used to index files.");

            boolean isIndexed = inputStream.readBoolean();
            System.out.println(isIndexed);
            if (!isIndexed) {
                System.out.println("Please, enter number of threads: ");
                int numberOfThreads = scanner.nextInt();
                outputStream.writeInt(numberOfThreads);
                long executionTime = inputStream.readLong();
                System.out.printf("Time for parallel execution with %s wordToSearch: %s wordToSearch ms\n", numberOfThreads, executionTime);
            } else {
                System.out.println("Files have already been indexed by another client.");
            }

            System.out.println("Second step: word search.");
            do {
                findWord(isIndexed);
                isIndexed = true;
                System.out.println("Do You want to find one more word?(y - yes, n - no)");
                String wantToFindMore = scanner.nextLine().toLowerCase();
                outputStream.writeUTF(wantToFindMore);
                findOneMoreWord = wantToFindMore.equals("y");
            } while (findOneMoreWord);

        } catch (Exception e) {
            throw e;
        }
    }
}
