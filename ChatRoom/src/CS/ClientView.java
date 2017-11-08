package CS;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class ClientView {
    private JPanel clientPanel;
    private JButton submitBtn;
    private JTextField hostInput;
    private JTextField portInput;
    private JButton connectBtn;
    private JTextArea inputText;
    private JTextArea outputText;
    Client client = null;

    public ClientView(String userName) {
        // maintain the scroll always at last
        DefaultCaret caret = (DefaultCaret)outputText.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        // default value for host and port
        hostInput.setText("127.0.0.1");
        portInput.setText("2333");
        // btn action
        connectBtn.addActionListener(e -> {
            String host = hostInput.getText();
            int port = Integer.valueOf(portInput.getText());
            // init this client
            client = new Client(host, port, outputText);
            client.writeToServer( Protocol.wrapUserName(userName));
        });
        submitBtn.addActionListener(e -> {
            // wrap the text from input by protocol and sent to server
            client.writeToServer(Protocol.wrapText(inputText.getText(), userName));
            // clean the text pane
            cleanInput();
            // get the return msg from server
            String msgFromServer = client.readFromServer();
        });
    }

    public void cleanInput() {
        inputText.setText("");
    }

    public static void main(String[] args) {
        String userName = args[0];
        // init the frame
        JFrame frame = new JFrame("ClientView");
        frame.setTitle(userName);
        frame.setContentPane(new ClientView(userName).clientPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
