package CS;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class Server {
    // the socket to listen for client
    ServerSocket serverSocket;
    JTextArea textArea;

    public Server(int port, JTextArea textArea) {
        this.textArea = textArea;
        try {
            // init the socket
            this.serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void writeToTextArea(String msg) {
        StringBuffer str = new StringBuffer(textArea.getText());
        str.append(msg);
        textArea.setText(str.toString());
    }

    public void makeNewThread(Socket client) {
        // start a new server thread for this client
        new ServerThread(client).start();
    }

    class ServerThread extends Thread {
        // the socket of the client
        private Socket clientSocket;
        // reader and writer of client socket
        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter;

        public ServerThread(Socket client) {
            this.clientSocket = client;
            try {
                this.bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void writeToClient(String msg) {
            try {
                bufferedWriter.write(msg);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String readFromClient() {
            try {
                return bufferedReader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "error";
        }

        @Override
        public void run() {
            try {
                while (true) {
                    System.out.println("Thread is waiting!!!");
                    // get the string from the client socket
                    String msgFromClient = readFromClient();
                    // write msg to server's textArea
                    writeToTextArea(msgFromClient);
                    // send "OK" back to client to conform
                    writeToClient("OK");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}