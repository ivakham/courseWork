package org.kpi.client;

import java.io.*;
import java.net.Socket;
import java.rmi.NoSuchObjectException;
import java.util.Scanner;

public class Client {
    private static Scanner scanner = new Scanner(System.in);
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;

    public static void findWord(boolean isIndexed, String wordToSearch) throws IOException {
        if (!isIndexed) {
            scanner.nextLine();
        }
        outputStream.writeUTF(wordToSearch);
        System.out.println("Searching...");
        String indexedFiles = inputStream.readUTF();
        if (indexedFiles.equals("no files found")) {
            System.out.println("No such word in files.");
        } else {
            String cleanString = indexedFiles.replace("[", "").replace("]", "");
            String[] filesWithWord = cleanString.split(", ");
            System.out.println("------------------");
            System.out.println("Found files:");
            for (String filePath : filesWithWord) {
                System.out.println(filePath);
            }
            System.out.println("------------------");
        }
    }

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 8000)) {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            boolean findOneMoreWord = true;
            boolean isIndexed = inputStream.readBoolean();
            System.out.println("System is indexed: " + isIndexed);
            if (!isIndexed) {
                System.out.println("Please, enter number of threads: ");
                int numberOfThreads = scanner.nextInt();
                outputStream.writeInt(numberOfThreads);
                long executionTime = inputStream.readLong();
                System.out.printf("Time for parallel execution with %s wordToSearch: %s ms\n", numberOfThreads, executionTime);
            } else {
                System.out.println("Files have already been indexed by another client.");
            }
            String wordToFind;
            System.out.print("Enter word to find: ");
            wordToFind = scanner.nextLine();

            do {
                findWord(isIndexed, wordToFind);
                isIndexed = true;
                System.out.print("Enter another word to find or 'q' for leave program: ");
                wordToFind = scanner.nextLine().toLowerCase();
                if(wordToFind.equals("q")) {
                    outputStream.writeUTF(wordToFind);
                    findOneMoreWord = false;
                }
               
            } while (findOneMoreWord);

        } catch (Exception e) {
            throw e;
        }
    }
}
