package CS;

import javax.swing.*;

public class LoginView {
    private JTextField usernameText;
    private JButton confirmBtn;
    private JPasswordField passwordText;
    private JPanel loginPanel;
    static volatile boolean close = false;

    public LoginView() {
        confirmBtn.addActionListener(e -> {
            String userName = usernameText.getText();
            String passWord = passwordText.getPassword().toString();
            close = true;
            ClientView.main(new String[]{userName});
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LoginView");
        frame.setContentPane(new LoginView().loginPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        while (!close) { }
        frame.dispose();
    }
}
