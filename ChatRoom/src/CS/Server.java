package CS;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class Server {
    // the socket to listen for client
    ServerSocket serverSocket;
    JTextArea textArea;
    DefaultListModel userListModel;

    public Server(int port, JTextArea textArea, DefaultListModel userListModel) {
        this.textArea = textArea;
        this.userListModel = userListModel;
        try {
            // init the socket
            this.serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void writeToTextArea(String msg) {
        StringBuffer str = new StringBuffer(textArea.getText());
        str.append(msg+"\n");
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
        // the info of this client
        String userName;

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
            Protocol.writeToSocket(msg, bufferedWriter);
        }

        public String readFromClient() {
            return Protocol.readFromSocket(bufferedReader);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    System.out.println("Thread is waiting!!!");
                    // get the string from the client socket
                    String msgFromClient = readFromClient();
                    System.out.println("Server: " + msgFromClient);
                    // if get null from client, the close socket and interrupt the thread
                    if (msgFromClient.equals("ERROR")) {
                        if (userListModel.contains(userName)) {
                            userListModel.removeElement(userName);
                            writeToTextArea("[LOG]" + userName + " is offline!\n");
                        }
                        clientSocket.close();
                        interrupt();
                        continue;
                    }
                    if (msgFromClient.equals("(REQUEST)")) {
                        writeToClient(Protocol.wrapResponse(textArea.getText()));
                        continue;
                    }
                    if (msgFromClient.contains("(USERNAME)")) {
                        userName = msgFromClient.substring("(USERNAME)".length(), msgFromClient.length());
                        writeToTextArea("[LOG]" + userName + " is online!\n");
                        userListModel.addElement(userName);
                        continue;
                    }
//                    System.out.println(msgFromClient);
                    // write msg to server's textArea
                    writeToTextArea(msgFromClient);
                    // send "OK" back to client to conform
                    writeToClient("(TIME)(TEXT)OK\n(END)");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}