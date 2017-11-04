package CS;

import javax.swing.*;

public class ClientView {
    private JPanel clientPanel;
    private JTextPane inputText;
    private JButton submitBtn;
    Client client;

    public ClientView() {
        client = new Client("127.0.0.1", 2333);

        submitBtn.addActionListener(e -> {
            client.writeToServer(inputText.getText());
            String msgFromServer = client.readFromServer();
        });
    }

    public static void main(String[] args) {
        // init the frame
        JFrame frame = new JFrame("ClientView");
        frame.setContentPane(new ClientView().clientPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
