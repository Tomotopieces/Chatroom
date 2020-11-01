package event;

import client.ChatroomClient;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Client send message event.
 * <p>
 * Send the message information from a client to its server.
 *
 * @author Tomoto
 * @date 2020/10/27 20:09
 */
public class ClientSendMessageEvent extends AbstractEvent implements Serializable {
    private final String sender; // sender of the message

    /**
     * @param data   the message
     * @param client the client which send the message
     */
    public ClientSendMessageEvent(String data, ChatroomClient client) {
        super(data);
        sender = client.getNickName();

        try {
            OutputStream outputStream = client.getClientSocket().getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(this); // send the event to the server
            System.out.println(client.getNickName() + ": " + getData()); // output the event on client console
        } catch (IOException e) {
            e.printStackTrace();
        } // don't close the stream
    }

    public String getSender() {
        return sender;
    }
}
