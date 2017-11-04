package CS;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class ServerView {
    private JPanel serverPanel;
    private JTextArea textArea;
    static Server server;
    static Socket client = null;

    public ServerView() {
        server = new Server(2333, textArea);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ServerView");
        frame.setContentPane(new ServerView().serverPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        while (true) {
            try {
                // get the client
                client = server.serverSocket.accept();
                System.out.println("Connection Success");
                server.writeToTextArea("Connection Success");
                server.makeNewThread(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
