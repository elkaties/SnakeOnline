package Client.gui;


import javax.swing.*;
import java.io.IOException;

public class OnlineGameMenu extends JPanel {
    private JTextField ip;
    private JTextField port;
    private MainScreen mainScreen;
    public OnlineGameMenu(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        ip = new JTextField("localhost");
        port = new JTextField("5050");
        JButton connect = new JButton("Подключиться");
        JButton cancel = new JButton("Назад");

        add(ip);
        add(port);
        add(port);
        add(connect);
        add(cancel);
        connect.addActionListener(e -> connect());
        cancel.addActionListener(e -> mainScreen.toMenu(this));
    }

    private void connect() {
        try {
            mainScreen.startOnlineGame(ip.getText(), Integer.valueOf(port.getText()));
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ошибка подключения");
        }
    }
}
