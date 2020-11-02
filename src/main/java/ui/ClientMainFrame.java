package ui;

import client.ChatroomClient;
import ui.util.GBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * Main window of the chatroom client
 *
 * @author Tomoto
 * @date 2020/10/26 20:25
 */
public class ClientMainFrame extends JFrame {
    private static final Integer DEFAULT_WIDTH = 640;
    private static final Integer DEFAULT_HEIGHT = 480;

    private final ChatroomClient client;

    private final JTextArea messageBox; // history message box
    private final JTextArea inputBox; // message input box

    public ClientMainFrame(ChatroomClient client) {
        setLayout(new GridBagLayout());
        setTitle("Chatroom -by Tomoto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        this.client = client;

        // generate history message box
        messageBox = new JTextArea();
        messageBox.setEditable(false); // message box is not editable
        messageBox.setTabSize(2);
        JScrollPane messagePane = new JScrollPane(messageBox);
        setScrollToBottomAutomatically(messagePane);
        messagePane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Message"));

        // generate message input box
        inputBox = new JTextArea();
        inputBox.setTabSize(0);
        // set shortcut of 'send message'
        InputMap keyMap = inputBox.getInputMap();
        keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_MASK), "send");
        ActionMap actionMap = inputBox.getActionMap();
        actionMap.put("send", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        JScrollPane inputPane = new JScrollPane(inputBox);
        setScrollToBottomAutomatically(inputPane);
        inputPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Input (Ctrl + Enter to send message)"));

        // generate friends list
        JList<String> friendList = new JList<>(new String[]{"Tomoto", "IzzelAliz"});
        JScrollPane friendsPane = new JScrollPane(friendList);
        friendsPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Friends"));

        add(messagePane, new GBC(0, 0, 1, 1).setFill(GBC.BOTH).setWeight(100, 100));
        add(inputPane, new GBC(0, 1, 1, 1).setFill(GBC.BOTH).setWeight(100, 100));
        add(friendsPane, new GBC(1, 0, 1, 2).setFill(GBC.BOTH).setWeight(50, 100));

        pack();

        // close the client when closing this frame
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    client.getClientSocket().close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }

    private void setScrollToBottomAutomatically(JScrollPane pane) {
        pane.getVerticalScrollBar().addAdjustmentListener(event -> // when update the message pane
                event.getAdjustable().setValue(event.getAdjustable().getMaximum())); // automatically scroll to bottom
    }

    /**
     * Send message to the server.
     */
    public void sendMessage() {
        client.sendMessage(inputBox.getText());
        inputBox.setText("");
    }

    /**
     * Receive the new message from the server.
     *
     * @param message the new message
     */
    public void receiveMessage(String message) {
        messageBox.append("\n" + message);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public JTextArea getMessageBox() {
        return messageBox;
    }

    public JTextArea getInputBox() {
        return inputBox;
    }
}
