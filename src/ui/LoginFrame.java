package ui;

import client.ChatroomClient;
import ui.util.GBC;

import javax.swing.*;
import java.awt.*;

/**
 * Login frame for user to login
 * @author Tomoto
 * @date 2020/10/27 18:28
 */
public class LoginFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 480;
    private static final int DEFAULT_HEIGHT = 320;

    private String address;
    private String account;
    private String password;

    public LoginFrame(ChatroomClient client) {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        // generate address input box and label
        JPanel addressPanel = new JPanel(new GridLayout(1, 2));
        JLabel addressLabel = new JLabel("Address: ");
        JTextArea addressInput = new JTextArea(1, 16);
        addressInput.setBorder(BorderFactory.createEtchedBorder());
        addressInput.setTabSize(0);
        addressLabel.setLabelFor(addressInput);
        addressPanel.add(addressLabel);
        addressPanel.add(addressInput);

        // generate account input box and label
        JPanel accountPanel = new JPanel(new GridLayout(1, 2));
        JLabel accountLabel = new JLabel("Account: ");
        JTextArea accountInput = new JTextArea(1, 16);
        accountInput.setBorder(BorderFactory.createEtchedBorder());
        accountInput.setTabSize(0);
        accountLabel.setLabelFor(accountInput);
        accountPanel.add(accountLabel);
        accountPanel.add(accountInput);

        // generate password input box and label
        JPanel passwordPanel = new JPanel(new GridLayout(1, 2));
        JLabel passwordLabel = new JLabel("Password: ");
        JPasswordField passwordInput = new JPasswordField(16);
        passwordLabel.setLabelFor(passwordInput);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordInput);

        // generate confirm button
        JButton confirmButton = new JButton("Confirm");
        confirmButton.setSize(2, 1);
        confirmButton.addActionListener(event -> { // set address, account, and password, then login
            address = addressInput.getText();
            account = accountInput.getText();
            password = new String(passwordInput.getPassword());
            client.login();
        });

        add(addressPanel, new GBC(0, 0).setWeight(0, 1));
        add(accountPanel, new GBC(0, 1).setWeight(0, 1));
        add(passwordPanel, new GBC(0, 2).setWeight(0, 1));
        add(confirmButton, new GBC(0, 3).setWeight(0, 1));
        pack();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public String getServerAddress() {
        return address;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }
}
