package Client.gui;

import Client.model.LocalGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GameScreen extends JPanel implements ActionListener, PropertyChangeListener {
    private LocalGame game;
    private int scale;
    private int windowSize;
    private MainScreen mainScreen;

    public GameScreen(MainScreen mainScreen, LocalGame game, Dimension dimension) {
        this.mainScreen = mainScreen;
        this.windowSize = dimension.width;
        this.game = game;
        game.getTimer().addActionListener(this);
        setSize(dimension);
        scale = MainScreen.WINDOW_SIZE / LocalGame.GRID_SIZE;
    }

    /**
     * Этот метод не вызывается при завершении игры
     * поэтому нужно найти иной способ вызова
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.white);
        g.fillRect(0, 0, windowSize, windowSize);
        g.setColor(Color.green);
        g.fillRect(game.getFood().x * scale, game.getFood().y * scale, scale, scale);
        g.setColor(Color.black);
        for (Point p : game.getSnake()) {
            g.fillRect(p.x * scale, p.y * scale, scale, scale);
        }
        g.drawString("Score: " + game.getScore(), 20, 20);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        mainScreen.gameOver("Набрано : " + game.getScore() + " очка(ов)");
        mainScreen.repaint();
    }
}
