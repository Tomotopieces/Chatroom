package service.client;

import dao.entity.Account;
import event.AbstractEvent;
import event.ClientLogoutEvent;
import event.ClientReceiveMessageEvent;
import event.ClientSendMessageEvent;
import event.ServerBroadcastMessageEvent;
import service.Service;
import service.impl.AccountService;
import view.ClientLoginFrame;
import view.ClientViewFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Chatroom client logic core
 *
 * @author Tomoto
 * <p>
 * 2020/10/27 16:11
 */
public class ClientService implements Service {
    private static final Map<Class<? extends AbstractEvent>, EventHandler> HANDLER_MAP = new HashMap<>();

    static {
        // setup the handler map
        HANDLER_MAP.put(ServerBroadcastMessageEvent.class, // when receive a message from service.server
                (client, event) -> new ClientReceiveMessageEvent( // let service.client update the message box
                        event.getData(), ((ServerBroadcastMessageEvent) event).getSender(), client));
    }

    private final AccountService accountService;
    private Socket clientSocket;
    private ClientViewFrame clientViewFrame;

    private Account account;
    private String serverAddress;

    private ClientService() {
        accountService = AccountService.getInstance();
    }

    public static ClientService getInstance() {
        return Instance.INSTANCE;
    }

    /**
     * Login process.
     */
    public void login() {
        // get necessary information from login frame
        ClientLoginFrame loginFrame = ClientLoginFrame.getInstance();
        account = new Account(loginFrame.getAccountName(), loginFrame.getAccountPassword());
        serverAddress = loginFrame.getServerAddress();

        // empty information judgement
        if (serverAddress.isEmpty()) {
            loginFrame.showHintDialog(ClientLoginFrame.ENTER_ADDRESS_HINT);
        } else if (account.getAcc_name().isEmpty()) {
            loginFrame.showHintDialog(ClientLoginFrame.ENTER_ACCOUNT_HINT);
        } else if (account.getAcc_password().isEmpty()) {
            loginFrame.showHintDialog(ClientLoginFrame.ENTER_PASSWORD_HINT);
        } else /* if all information is not empty */ {
            if (accountService.isExist(account.getAcc_name())) {
                if (accountService.match(account.getAcc_name(), account.getAcc_password())) { // if login successful
                    // generate socket
                    try {
                        if (clientSocket == null) {
                            clientSocket = new Socket(loginFrame.getServerAddress(), 9999);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // switch frame
                    loginFrame.setVisible(false);
                    openMainFrame();
                } else { // if password wrong
                    loginFrame.showHintDialog(ClientLoginFrame.WRONG_PASSWORD_HINT);
                }
            } else { // if the account is not exists
                int select = loginFrame.showAccountNotExistDialog(); // ask whether to register
                if (select == JOptionPane.YES_OPTION) { // if agree
                    // register
                    account.setAcc_nickname(loginFrame.showSetNicknameDialog(account.getAcc_name()));
                    accountService.register(account.getAcc_name(), account.getAcc_nickname(), account.getAcc_password());
                    loginFrame.showRegisterSuccessfulDialog(account.getAcc_name(), account.getAcc_password());
                }
            }
        }
    }

    /**
     * Client logout.
     */
    public void logout() {
        new ClientLogoutEvent(this, "");
    }

    /**
     * Open a main frame.
     */
    public void openMainFrame() {
        EventQueue.invokeLater(() -> {
            clientViewFrame = new ClientViewFrame(this);
            clientViewFrame.setVisible(true);
        });

        new Thread(new ReceiveEvent(this)).start(); // start a new thread for event handling
    }

    /**
     * Send a new message to the service.server
     *
     * @param message the new message
     */
    public void sendMessage(String message) {
        new ClientSendMessageEvent(message, this);
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public ClientService setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
        return this;
    }

    public ClientViewFrame getMainFrame() {
        return clientViewFrame;
    }

    public ClientService setMainFrame(ClientViewFrame clientViewFrame) {
        this.clientViewFrame = clientViewFrame;
        return this;
    }

    public Account getAccount() {
        return account;
    }

    public ClientService setAccount(Account account) {
        this.account = account;
        return this;
    }

    @Override
    public void close() throws SQLException {
        accountService.close();
    }

    /**
     * Handle the event
     * <p>
     * Server and service.client have different handler.
     */
    public interface EventHandler {
        void handle(ClientService client, AbstractEvent event);
    }

    /**
     * Keep receiving and handle the event with HANDLER_MAP
     */
    public static class ReceiveEvent implements Runnable {
        private final ClientService client;

        public ReceiveEvent(ClientService client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                while (!client.clientSocket.isClosed()) {
                    try {
                        ObjectInputStream in = new ObjectInputStream(client.clientSocket.getInputStream());
                        AbstractEvent event = (AbstractEvent) in.readObject();
                        HANDLER_MAP.get(event.getClass()).handle(client, event);
                    } catch (SocketException ignore) { // if the service.client is closed
                        client.clientSocket.close(); // close
                        System.out.println("sign out"); // show information on service.client console
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } // don't close the stream
        }
    }

    private static class Instance {
        public static final ClientService INSTANCE = new ClientService();
    }
}
