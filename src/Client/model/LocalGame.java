package Client.model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Random;

public class LocalGame implements Game, ActionListener {

    public static final int GRID_SIZE = 50;

    private Random random;
    private ArrayList<Point> body;
    private int score;
    private Point food;
    private Point head;
    private Direction direction;
    private PropertyChangeSupport support;

    private Timer timer;

    public LocalGame() {
        support = new PropertyChangeSupport(this);
        random = new Random();
        body = new ArrayList<>();
        direction = Direction.RIGHT;
        head = new Point(GRID_SIZE / 2, GRID_SIZE / 2);
        body.add(head);
        timer = new Timer(100, this);
        refreshFood();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener){
        support.addPropertyChangeListener(listener);
    }

    @Override
    public void start() {
        timer.start();
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    private void move() {
        if (head.equals(food)) {
            score++;
            //Увеличиваем скорость каждые 5 оков
            if (score % 5 == 0) timer.setDelay(timer.getDelay() - 5);
            refreshFood();
        } else {
            for (Point p : body) {
                /**
                 * Здесь проверяем врезалась ли змейка в себя
                 * Так как голова входит в список "body",
                 * мы исключаем одинаковые ссылки, так как они
                 * обе будут указывать на голову
                 */
                if (head.equals(p) & head != p) over();
            }
            body.remove(0);
        }
        head = new Point((head.x + direction.getX() + GRID_SIZE) % GRID_SIZE,
                (head.y + direction.getY() + GRID_SIZE) % GRID_SIZE);
        body.add(head);
    }

    private void refreshFood() {
        /**
         * НУЖНО ОБНОВИТЬ ЗДЕСЬ ПОЛОЖЕНИЕ ЕДЫ
         */
        final int x = random.nextInt(GRID_SIZE);
        final int y = random.nextInt(GRID_SIZE);
        food = new Point(x, y);
        for (Point p : body) {
            if (p.equals(food)) refreshFood();
        }
    }

    public Timer getTimer() {
        return timer;
    }

    public ArrayList<Point> getSnake() {
        return body;
    }

    public int getScore() {
        return score;
    }

    @Override
    public void pause() {
        timer.stop();
    }

    @Override
    public void continueGame() {
        timer.start();
    }

    @Override
    public void over() {
        refreshFood();
        body = new ArrayList<>();
        head = new Point(GRID_SIZE / 2, GRID_SIZE / 2);
        body.add(head);
        timer.stop();
        support.firePropertyChange("over", false, true);
    }

    @Override
    public Point getFood() {
        return food;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
    }
}
