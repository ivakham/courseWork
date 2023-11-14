package org.kpi.server;

import org.kpi.index.InvertedIndex;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            InvertedIndex invertedIndex = new InvertedIndex();
            while (true) {
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket, invertedIndex);
                serverThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}