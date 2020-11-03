import view.ClientLoginFrame;

import java.awt.*;

/**
 * @author Tomoto
 * <p>
 * 2020/11/3 16:42
 */
public class ClientMain {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> { // open a login frame
            ClientLoginFrame frame = ClientLoginFrame.getInstance();
            frame.setVisible(true);
        });
    }
}
