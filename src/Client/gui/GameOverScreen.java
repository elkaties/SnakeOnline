package Client.gui;

import javax.swing.*;
import java.awt.*;

public class GameOverScreen extends JPanel {
    private Button menu;
    private JLabel text;
    private JLabel text1;

    public GameOverScreen(MainScreen mainScreen) {
        Container info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        menu = new Button("Меню");
        text = new JLabel("Игра завершена");
        text1 = new JLabel();
        menu.addActionListener(e -> mainScreen.toMenu(this));
        info.add(text);
        info.add(text1);
        add(info);
        add(menu);
    }

    public void setText(String info) {
        text1.setText(info);
        repaint();
    }
}
