package event;

import service.client.ClientService;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Client send message event.
 * <p>
 * Send the message information from a service.client to its service.server.
 *
 * @author Tomoto
 * @date 2020/10/27 20:09
 */
public class ClientSendMessageEvent extends AbstractEvent implements Serializable {
    private final String sender; // sender of the message

    /**
     * @param data   the message
     * @param client the service.client which send the message
     */
    public ClientSendMessageEvent(String data, ClientService client) {
        super(data);
        sender = client.getAccount().getAcc_nickname();

        try {
            OutputStream outputStream = client.getClientSocket().getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(this); // send the event to the service.server
//            System.out.println(client.getAccount().getAcc_nickname() + ": " + getData()); // print the event on client
        } catch (IOException e) {
            e.printStackTrace();
        } // don't close the stream
    }

    public String getSender() {
        return sender;
    }
}
