package Client.gui;

import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel {
    private Button localGame;
    private Button onlineGame;
    private Button exit;

    public Menu(MainScreen mainScreen) {
        localGame = new Button("Играть");
        localGame.addActionListener((e -> mainScreen.startLocal()));
        onlineGame = new Button("Online");
        onlineGame.addActionListener(e -> mainScreen.startOnline());
        exit = new Button("Выйти");
        exit.addActionListener(e -> mainScreen.exit());
        add(localGame);
        add(onlineGame);
        add(exit);
    }
}
