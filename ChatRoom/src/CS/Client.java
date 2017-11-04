package CS;

import java.io.*;
import java.net.*;

public class Client {
    Socket clientSocket;
    // the reader to read the msg from server
    BufferedReader bufferedReader;
    // write msg to server
    BufferedWriter bufferedWriter;

    public Client(String host, int port) {
        try {
            // init the socket in client end
            clientSocket = new Socket(host, port);
            clientSocket.setSoTimeout(10000);
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeToServer(String msg) {
        try {
            bufferedWriter.write(msg);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readFromServer() {
        try {
            return bufferedReader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
}
