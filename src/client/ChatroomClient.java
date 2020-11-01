package client;

import event.AbstractEvent;
import event.ClientReceiveMessageEvent;
import event.ClientSendMessageEvent;
import event.ServerBroadcastMessageEvent;
import ui.LoginFrame;
import ui.MainFrame;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

/**
 * Chatroom client core
 *
 * @author Tomoto
 * @date 2020/10/27 16:11
 */
public class ChatroomClient {
    private static final Map<Class<? extends AbstractEvent>, EventHandler> HANDLER_MAP = new HashMap<>();

    static {
        // setup the handler map
        HANDLER_MAP.put(ServerBroadcastMessageEvent.class, // when receive a message from server
                (client, event) -> new ClientReceiveMessageEvent( // let client update the message box
                        event.getData(), ((ServerBroadcastMessageEvent) event).getSender(), client));
    }

    private Socket clientSocket;
    private MainFrame mainFrame;
    private LoginFrame loginFrame;

    private String nickName;

    public static void main(String[] args) {
        ChatroomClient client = new ChatroomClient();

        // open a login frame
        EventQueue.invokeLater(() -> {
            client.loginFrame = new LoginFrame(client);
            client.loginFrame.setVisible(true);
        });
    }

    public void login() {
        // generate socket
        try {
            clientSocket = new Socket(loginFrame.getServerAddress(), 9999);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // get user account from the login frame
        String account = loginFrame.getAccount();
        String password = loginFrame.getPassword();

        // get user information from database
        nickName = account;

        // verify account and password

        // if login successfully, close the login frame
        loginFrame.setVisible(false);

        // open a main frame
        EventQueue.invokeLater(() -> {
            mainFrame = new MainFrame(this);
            mainFrame.setVisible(true);
        });

        // start a new thread for event handling
        new Thread(new ReceiveEvent(this)).start();
    }

    /**
     * Send a new message to the server
     * @param message the new message
     */
    public void sendMessage(String message) {
        new ClientSendMessageEvent(message, this);
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public ChatroomClient setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
        return this;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public ChatroomClient setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        return this;
    }

    public LoginFrame getLoginFrame() {
        return loginFrame;
    }

    public ChatroomClient setLoginFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public ChatroomClient setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    /**
     * Handle the event
     * <p>
     * Server and client have different handler.
     */
    public interface EventHandler {
        void handle(ChatroomClient client, AbstractEvent event);
    }

    /**
     * Keep receiving and handle the event with HANDLER_MAP
     */
    public static class ReceiveEvent implements Runnable {
        private final ChatroomClient client;

        public ReceiveEvent(ChatroomClient client) {
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
                    } catch (SocketException ignore) { // if the client is closed
                        client.clientSocket.close(); // close
                        System.out.println("sign out"); // show information on client console
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } // don't close the stream
        }
    }
}
