package Client.model;

import java.awt.*;
import java.io.Serializable;

public interface Game {
    enum Direction implements Serializable {
        UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

        private Point point;

        Direction(int x, int y) {
            point = new Point(x, y);
        }

        public int getX() {
            return point.x;
        }

        public int getY() {
            return point.y;
        }
    }

    void setDirection(Direction d);

    void start();

    void pause();

    void continueGame();

    void over();

    Point getFood();

    Direction getDirection();
}
