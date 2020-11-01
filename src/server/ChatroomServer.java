package server;

import event.AbstractEvent;
import event.ClientSendMessageEvent;
import event.ServerBroadcastMessageEvent;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Chatroom server core
 *
 * @author Tomoto
 * @date 2020/10/27 18:18
 */
public class ChatroomServer {
    private static final Map<Class<? extends AbstractEvent>, EventHandler> HANDLER_MAP = new HashMap<>();

    static {
        // generate event handler map
        HANDLER_MAP.put(ClientSendMessageEvent.class, // when receive a message from client
                (server, event) -> new ServerBroadcastMessageEvent( // send the message to all clients
                        event.getData(), ((ClientSendMessageEvent) event).getSender(), server));
    }

    private final List<Socket> clientSockets = new ArrayList<>();
    private ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        ChatroomServer server = new ChatroomServer();
        server.serverSocket = new ServerSocket(9999);

        while (!server.serverSocket.isClosed()) {
            Socket client = server.serverSocket.accept();
            server.clientSockets.add(client);
            System.out.println("Client No." + server.clientSockets.size() + " joined.");

            new Thread(new ReceiveEvent(server, client)).start();
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public List<Socket> getClientSockets() {
        return clientSockets;
    }

    /**
     * Handle the event
     * <p>
     * Server and client have different handler.
     */
    public interface EventHandler {
        void handle(ChatroomServer server, AbstractEvent event);
    }

    /**
     * Keep receiving and handle the event with HANDLER_MAP
     */
    public static class ReceiveEvent implements Runnable {
        private final ChatroomServer server;
        private final Socket client;

        public ReceiveEvent(ChatroomServer server, Socket client) {
            this.server = server;
            this.client = client;
        }

        @Override
        public void run() {
            try {
                while (!client.isClosed()) {
                    try {
                        ObjectInputStream in = new ObjectInputStream(client.getInputStream());
                        AbstractEvent event = (AbstractEvent) in.readObject();
                        HANDLER_MAP.get(event.getClass()).handle(server, event);
                    } catch (EOFException ignored) { // if the client is closed
                        client.close(); // close
                        System.out.println("sign out"); // show information on server console
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } // don't close the stream
        }
    }
}
