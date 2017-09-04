package Client.gui;

import javax.swing.*;
import java.awt.*;

public class PauseScreen extends JPanel{
    private Button cont;
    private Button exit;

    public PauseScreen (MainScreen mainScreen) {
        cont = new Button("Продолжить");
        cont.addActionListener((e -> mainScreen.continueGame()));
        exit = new Button("Выйти");
        exit.addActionListener(e -> mainScreen.toMenu(this));
        add(cont);
        add(exit);
    }
}
