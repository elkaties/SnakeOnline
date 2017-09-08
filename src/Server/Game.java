package Server;

import Client.model.GameInformation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Game implements ActionListener {

    public static final int GRID_SIZE = 50;

    private Random random;

    private ArrayList<Point> snake1;
    private ArrayList<Point> snake2;
    private Point head1;
    private Point head2;
    private Point food;
    private Client.model.Game.Direction direction1;
    private Client.model.Game.Direction direction2;

    private String FIRST_PLAYER = "First Player", SECOND_PLAYER = "Second Player";

    private Socket connection1;
    private Socket connection2;
    private ServerSocket serverSocket;
    private Timer timer;
    private ObjectOutputStream objectOutputStream1;
    private ObjectOutputStream objectOutputStream2;
    private GameInformation gameInformation;

    public Game() throws IOException {
        random = new Random();
        snake1 = new ArrayList<>();
        snake2 = new ArrayList<>();

        serverSocket = new ServerSocket(5050); /*открыть порт 55 и ожидать на нем подключения*/
        timer = new Timer(100, this);
        setUpGame();
    }

    private void setUpGame() throws IOException {
        head1 = new Point(0, 0);
        head2 = new Point(0, GRID_SIZE);
        snake1.add(head1);
        snake2.add(head2);
        food = new Point(GRID_SIZE / 2, GRID_SIZE / 2);
        direction1 = Client.model.Game.Direction.RIGHT;
        direction2 = Client.model.Game.Direction.LEFT;
        gameInformation = new GameInformation(snake1, snake2, 0, 0, food);

        getConnect();
        startInputStreamReaderThreads();
        timer.start();
    }
//метод который запускает новые потоки для считывания направления//
    private void startInputStreamReaderThreads() {
        new Thread(() -> {
            try (final ObjectInputStream objectInputStream1 = new ObjectInputStream(connection1.getInputStream())) {
                while (!gameInformation.isOver()) {
                    direction1 = (Client.model.Game.Direction) objectInputStream1.readObject(); //читывает объект который направояет клиент//
                }
            } catch (Exception e) {
                try {
                    connection1.close();
                } catch (IOException e1) {
                }
                gameOver(SECOND_PLAYER);
            }
        }).start();
//считывает направления второго игрока//
        new Thread(() -> {
            try (final ObjectInputStream objectInputStream2 = new ObjectInputStream(connection2.getInputStream())) {
                while (!gameInformation.isOver()) {
                    direction2 = (Client.model.Game.Direction) objectInputStream2.readObject();
                }
            } catch (Exception e) {
                try {
                    connection2.close();
                } catch (IOException e1) {
                }
                gameOver(FIRST_PLAYER);
            }
        }).start();
    }

    private void getConnect() throws IOException {
        connection1 = serverSocket.accept(); /*блокирующий метод, пока кто нибудь не подключится*/
        System.out.println("Connected First");
        objectOutputStream1 = new ObjectOutputStream(connection1.getOutputStream());
        connection2 = serverSocket.accept();
        objectOutputStream2 = new ObjectOutputStream(connection2.getOutputStream());
        System.out.println("Connected Second");
    }


    private void move() {
        if (head1.equals(food)) {
            gameInformation.setScore1(gameInformation.getScore1() + 1);
            refreshFood();
        } else {
            for (Point p : snake1) {
                if (head1.equals(p) & head1 != p) gameOver(FIRST_PLAYER);
            }
            snake1.remove(0);
        }

        if (head2.equals(food)) {
            gameInformation.setScore2(gameInformation.getScore2() + 1);
            refreshFood();
        } else {
            for (Point p : snake2) {
                if (head2.equals(p) & head2 != p) gameOver(SECOND_PLAYER);
            }
            snake2.remove(0);
        }
        head1 = new Point((head1.x + direction1.getX() + GRID_SIZE) % GRID_SIZE,
                (head1.y + direction1.getY() + GRID_SIZE) % GRID_SIZE);
        snake1.add(head1);

        head2 = new Point((head2.x + direction2.getX() + GRID_SIZE) % GRID_SIZE,
                (head2.y + direction2.getY() + GRID_SIZE) % GRID_SIZE);
        snake2.add(head2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        try {
            sendToClients();
        } catch (IOException f) {
        }
    }

    private void sendToClients() throws IOException {
        if (!connection1.isClosed()) {
            objectOutputStream1.writeObject(gameInformation);
            objectOutputStream1.flush();
            objectOutputStream1.reset();
        }
        if (!connection2.isClosed()) {
            objectOutputStream2.writeObject(gameInformation);
            objectOutputStream2.flush();
            objectOutputStream2.reset();
        }
    }

    private void gameOver(String winner) {
        timer.stop();
        snake1.clear();
        snake2.clear();
        gameInformation.setOver(true);
        gameInformation.setWinner(winner);
        try {
            sendToClients();
            setUpGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshFood() {
        /**
         * НУЖНО ОБНОВИТЬ ЗДЕСЬ ПОЛОЖЕНИЕ ЕДЫ
         */
        int x = random.nextInt(GRID_SIZE);
        int y = random.nextInt(GRID_SIZE);
        food.setLocation(new Point(x, y));
        for (Point p : snake1) {
            if (p.equals(food)) refreshFood();
        }
        for (Point p : snake2) {
            if (p.equals(food)) refreshFood();
        }
    }

    public static void main(String[] args) throws IOException {
        new Game();
    }
}