package Client.gui;

import Client.model.Game;
import Client.model.LocalGame;
import Client.model.OnlineGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class MainScreen extends JFrame implements KeyListener {

    public static final int WINDOW_SIZE = 500, TOP_TOOLBAR_HEIGHT = 20;
    private Menu menu;
    private Game game;
    private JPanel gameScreen;
    private PauseScreen pauseScreen;
    private OnlineGameMenu onlineGameMenu;
    private OnlineGameScreen onlineGameScreen;
    private GameOverScreen gameOverScreen;

    public MainScreen() {
        setResizable(false);
        setFocusable(true);
        menu = new Menu(this);
        pauseScreen = new PauseScreen(this);
        gameOverScreen = new GameOverScreen(this);
        setSize(WINDOW_SIZE, WINDOW_SIZE + TOP_TOOLBAR_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(menu);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (game != null) game.over();
            }
        });
    }

    public void startLocal() {
        remove(menu);
        LocalGame game = new LocalGame();
        GameScreen gameScreen = new GameScreen(this, game, new Dimension(WINDOW_SIZE, WINDOW_SIZE));
        this.game = game;
        this.gameScreen = gameScreen;
        game.addPropertyChangeListener(gameScreen);
        add(gameScreen);
        game.start();
        addKeyListener(this);
        revalidate();
    }

    public void startOnline() {
        onlineGameMenu = new OnlineGameMenu(this);
        remove(menu);
        add(onlineGameMenu);
        revalidate();
    }

    public void startOnlineGame(String ip, int port) throws IOException {
        OnlineGame onlineGame = new OnlineGame();
        if (!onlineGame.connect(ip, port)) {
            JOptionPane.showMessageDialog(this, "Ошибка подключения");
        } else {
            onlineGameScreen = new OnlineGameScreen(this, new Dimension(WINDOW_SIZE, WINDOW_SIZE));
            add(onlineGameScreen);
            remove(onlineGameMenu);
            addKeyListener(this);
            onlineGame.addPropertyChangeListener(onlineGameScreen);
            this.game = onlineGame;
        }
    }

    public void pause() {
        if (gameScreen != null) remove(gameScreen);
        if (onlineGameScreen != null) remove(onlineGameScreen);
        game.pause();
        add(pauseScreen);
        revalidate();
        repaint();
    }

    public void toMenu(JPanel from) {
        remove(from);
        add(menu);
        game.over();
        revalidate();
        repaint();
    }

    public void continueGame() {
        remove(pauseScreen);
        if (gameScreen != null) add(gameScreen);
        if (onlineGameScreen != null) add(onlineGameScreen);
        game.continueGame();
        revalidate();
        repaint();
    }

    public void gameOver(String string) {
        gameOverScreen.setText(string);
        if (gameScreen != null) remove(gameScreen);
        if (onlineGameScreen != null) remove(onlineGameScreen);
        add(gameOverScreen);
        revalidate();
        repaint();
    }

    public void exit() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        final Game.Direction currentDir = game.getDirection();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (currentDir != Game.Direction.DOWN) game.setDirection(Game.Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                if (currentDir != Game.Direction.UP) game.setDirection(Game.Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                if (currentDir != Game.Direction.RIGHT) game.setDirection(Game.Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                if (currentDir != Game.Direction.LEFT) game.setDirection(Game.Direction.RIGHT);
                break;
            case KeyEvent.VK_ESCAPE:
                pause();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) {
        new MainScreen();
    }
}
