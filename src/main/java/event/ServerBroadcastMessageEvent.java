package event;

import service.server.ServerService;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Server broadcast message event.
 * <p>
 * Broadcast the message to every registered service.client.
 *
 * @author Tomoto
 * @date 2020/10/27 20:48
 */
public class ServerBroadcastMessageEvent extends AbstractEvent implements Serializable {
    private final String sender;

    /**
     * @param data   the message
     * @param server the service.server which receive the message
     */
    public ServerBroadcastMessageEvent(String data, String sender, ServerService server) {
        super(data);
        this.sender = sender;

        server.getClientSockets().forEach(client -> {
            try {
                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                out.writeObject(this); // send the event to the service.server
            } catch (IOException e) {
                e.printStackTrace();
            } // don't close the stream
        });
    }

    public String getSender() {
        return sender;
    }
}
