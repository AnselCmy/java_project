package CS;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class Client {
    Socket clientSocket;
    // the reader to read the msg from server
    BufferedReader bufferedReader = null;
    // write msg to server
    BufferedWriter bufferedWriter = null;
    // pull msg from server
    JTextArea outputText;
    String currMsg = "";
    boolean ifChange = false;

    public Client(String host, int port, JTextArea outputText) {
        try {
            // init the socket in client end
            clientSocket = new Socket(host, port);
            clientSocket.setSoTimeout(10000);
            this.outputText = outputText;
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            new pullThread().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeToServer(String msg) {
        Protocol.writeToSocket(msg, bufferedWriter);
    }

    public String readFromServer() {
        return Protocol.readFromSocket(bufferedReader);
    }

    public class pullThread extends Thread {
        final long timeInterval = 100;
        int oldLength = 0;
        int newLength = 0;

        @Override
        public void run() {
            while (true) {
                if (bufferedReader != null && bufferedWriter != null && outputText != null) {
                    writeToServer("(REQUEST)\n(END)");
                    currMsg = readFromServer();
                    outputText.setText(currMsg);
                    newLength = outputText.getDocument().getLength();
                    if (newLength != oldLength) {
                        outputText.setCaretPosition(outputText.getDocument().getLength());
                        oldLength = newLength;
                    }
                }
                try {
                    Thread.sleep(timeInterval);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
