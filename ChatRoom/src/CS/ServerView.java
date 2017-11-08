package CS;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;
import java.io.IOException;
import java.net.Socket;
import java.time.Period;

public class ServerView {
    private JPanel serverPanel;
    private JTextArea textArea;
    private JTextField portInput;
    private JButton listenBtn;
    private JScrollPane scrollPane;
    private JList userList;
    static Server server = null;
    static Socket client = null;

    public ServerView() {
        // maintain the scroll always at last
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        // list model for username
        DefaultListModel userListModel = new DefaultListModel();
        userList.setModel(userListModel);
        // set default port
        portInput.setText("2333");
        // action for listen
        listenBtn.addActionListener(e -> {
            if (server == null) {
                int port = Integer.valueOf(portInput.getText());
                // initial a new server object
                server = new Server(port, textArea, userListModel);
                // write the log into textArea
                server.writeToTextArea("[LOG]Listen on port: " + String.valueOf(port) + "\n");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ServerView");
        frame.setContentPane(new ServerView().serverPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        while (true) {
            try {
                System.out.println("Not has server");
                if (server != null) {
                    // get the client
                    System.out.println("Waiting for client");
                    client = server.serverSocket.accept();
//                    server.writeToTextArea("[LOG]Connection access\n");
                    // make a new thread for client
                    server.makeNewThread(client);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
