package view;

import service.client.ClientService;
import view.util.GBC;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Login frame for user to login
 * @author Tomoto
 * @date 2020/10/27 18:28
 */
public class ClientLoginFrame extends JFrame implements View {
    public static final String ENTER_ADDRESS_HINT = "请输入服务器地址！";
    public static final String ENTER_ACCOUNT_HINT = "请输入账号名称！";
    public static final String ENTER_PASSWORD_HINT = "请输入账号密码！";
    public static final String WRONG_PASSWORD_HINT = "密码错误！";

    private static final int DEFAULT_WIDTH = 480;
    private static final int DEFAULT_HEIGHT = 320;

    private final ClientService clientService;
    private String serverAddress;
    private String accountName;
    private String accountPassword;

    private ClientLoginFrame() {
        clientService = ClientService.getInstance();

        setTitle("登录");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridLayout inputPanelLayout = new GridLayout(3, 1);
        inputPanelLayout.setVgap(20);
        JPanel inputPanel = new JPanel(inputPanelLayout);
//        inputPanel.setBorder(BorderFactory.createEtchedBorder());

        // generate address input box and label
        JPanel addressPanel = new JPanel(new GridLayout(1, 3));
        JLabel addressLabel = new JLabel("地址: ");
        JTextArea addressInput = new JTextArea(1, 16);
        addressInput.setBorder(BorderFactory.createEtchedBorder());
        addressInput.setTabSize(0);
        addressLabel.setLabelFor(addressInput);
        addressPanel.add(addressLabel);
        addressPanel.add(addressInput);
//        addressPanel.setBorder(BorderFactory.createEtchedBorder());
        inputPanel.add(addressPanel);

        // generate account input box and label
        JPanel accountPanel = new JPanel(new GridLayout(1, 3));
        JLabel accountLabel = new JLabel("账号: ");
        JTextArea accountInput = new JTextArea(1, 16);
        accountInput.setBorder(BorderFactory.createEtchedBorder());
        accountInput.setTabSize(0);
        accountLabel.setLabelFor(accountInput);
        accountPanel.add(accountLabel);
        accountPanel.add(accountInput);
//        accountPanel.setBorder(BorderFactory.createEtchedBorder());
        inputPanel.add(accountPanel);

        // generate password input box and label
        JPanel passwordPanel = new JPanel(new GridLayout(1, 3));
        JLabel passwordLabel = new JLabel("密码: ");
        JPasswordField passwordInput = new JPasswordField(16);
        passwordLabel.setLabelFor(passwordInput);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordInput);
//        passwordPanel.setBorder(BorderFactory.createEtchedBorder());
        inputPanel.add(passwordPanel);

        // generate confirm button
        JButton confirmButton = new JButton("确定");
        confirmButton.setSize(2, 1);
        confirmButton.addActionListener(event -> { // set address, account, and password, then login
            serverAddress = addressInput.getText();
            accountName = accountInput.getText();
            accountPassword = new String(passwordInput.getPassword());
            clientService.login();
        });

        add(inputPanel, new GBC(0, 1).setWeight(0, 1));
        add(confirmButton, new GBC(0, 2).setWeight(0, 1));
        pack();
    }

    public static ClientLoginFrame getInstance() {
        return Instance.INSTANCE;
    }

    public int showAccountNotExistDialog() {
        return JOptionPane.showConfirmDialog(this, "账号不存在！\n是否使用当前信息进行注册？");
    }

    public String showSetNicknameDialog(String defaultName) {
        return JOptionPane.showInputDialog(this, "请输入昵称：", defaultName);
    }

    public void showRegisterSuccessfulDialog(String accountName, String password) {
        JOptionPane.showMessageDialog(this, "注册成功，请保存好账号信息。" +
                "\n账号: " + accountName +
                "\n密码: " + password);
    }

    public void showHintDialog(String hint) {
        JOptionPane.showMessageDialog(this, hint);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    @Override
    public void close() throws SQLException {
        clientService.close();
    }

    private static class Instance {
        public static final ClientLoginFrame INSTANCE = new ClientLoginFrame();
    }
}
