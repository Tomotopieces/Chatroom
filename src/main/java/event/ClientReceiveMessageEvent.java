package event;

import client.ChatroomClient;

import java.io.Serializable;

/**
 * Client receive new message event.
 * <p>
 * This will accor when the server broadcast the new message.
 *
 * @author Tomoto
 * @date 2020/10/27 20:38
 */
public class ClientReceiveMessageEvent extends AbstractEvent implements Serializable {
    /**
     * @param data   the new message content
     * @param sender the sender of the message
     * @param client the client which receive the message
     */
    public ClientReceiveMessageEvent(String data, String sender, ChatroomClient client) {
        super(data);

        // format the message
        String message = sender + " " +
                        DATE_FORMAT.get().format(getTime()) + " >\n\t" +
                        getData();

        // let client receive the message
        client.getMainFrame().receiveMessage(message);
    }
}
