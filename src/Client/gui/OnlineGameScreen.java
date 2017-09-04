package Client.gui;

import Client.model.GameInformation;
import Client.model.LocalGame;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class OnlineGameScreen extends JPanel implements PropertyChangeListener {
    private GameInformation gameInformation;
    private MainScreen mainScreen;
    private int scale;
    private int windowSize;

    public OnlineGameScreen(MainScreen mainScreen, Dimension dimension) {
        this.windowSize = dimension.width;
        this.mainScreen = mainScreen;
        setSize(dimension);
        scale = MainScreen.WINDOW_SIZE / LocalGame.GRID_SIZE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.white);
        g.fillRect(0, 0, windowSize, windowSize);
        if (gameInformation.getFood() != null) {
            g.setColor(Color.green);
            g.fillRect(gameInformation.getFood().x * scale, gameInformation.getFood().y * scale, scale, scale);

            g.setColor(Color.black);
            for (Point p : gameInformation.getSnake1()) {
                g.fillRect(p.x * scale, p.y * scale, scale, scale);
            }
            g.setColor(Color.blue);
            for (Point p : gameInformation.getSnake2()) {
                g.fillRect(p.x * scale, p.y * scale, scale, scale);
            }
            g.drawString("Score 1st player: " + gameInformation.getScore1(), 20, 20);
            g.drawString("Score 2nd player: " + gameInformation.getScore2(), windowSize / 2 + 20, 20);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        gameInformation = (GameInformation) evt.getNewValue();
        repaint();
        if (gameInformation.isOver()) {
            final int score = Math.max(gameInformation.getScore1(), gameInformation.getScore2());
            mainScreen.gameOver("Победил : " + gameInformation.getWinner() + ", набрав " + score +" очка(ов)");
            mainScreen.repaint();
        }
    }
}

