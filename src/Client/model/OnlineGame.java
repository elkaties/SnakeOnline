package Client.model;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;

public class OnlineGame implements Game {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private GameInformation gameInformation;
    private Direction direction;
    private boolean isOver;
    private PropertyChangeSupport support;

    public OnlineGame() {
        support  = new PropertyChangeSupport(this);
    }

    public boolean connect(String ipAddress, int port) throws IOException {
        try {
            socket = new Socket(ipAddress, port);
        } catch (IOException e) {
        }
        if (socket != null) {
            objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            objectInputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            Thread session = new Thread(() -> {
                while (!isOver && !socket.isClosed()) {
                    try {
                        gameInformation = (GameInformation) objectInputStream.readObject();
                        isOver = gameInformation.isOver();
                        support.firePropertyChange("gameInformation", null, gameInformation);
                    } catch (IOException | ClassNotFoundException e) {
                        try {
                            /**
                             * Здесь выводить сообщение о потере соединения с сервером.
                             */
                            socket.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        System.out.println("Какой-то эксепшн");
                    }
                }
            });
            session.start();
            return true;
        }
        return false;
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }


    private void gameOver() {
        try {
            isOver = true;
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();
        } catch (IOException e) {
        }
    }

    @Override
    public void setDirection(Direction d) {
        try {
            direction = d;
            objectOutputStream.writeObject(d);
            objectOutputStream.flush();
            objectOutputStream.reset();
        } catch (IOException e) {
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void continueGame() {

    }

    @Override
    public void over() {
        gameOver();
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public Point getFood() {
        return gameInformation.getFood();
    }

}
