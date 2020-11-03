package event;

import service.client.ClientService;

import java.io.IOException;

/**
 * Client logout event.
 *
 * @author Tomoto
 * <p>
 * 2020/11/3 16:51
 */
public class ClientLogoutEvent extends AbstractEvent {
    public ClientLogoutEvent(ClientService clientService, String data) {
        super(data);

        try {
            clientService.getClientSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
