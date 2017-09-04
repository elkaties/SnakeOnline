package Client.model;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class GameInformation implements Serializable {
    private ArrayList<Point> snake1;
    private ArrayList<Point> snake2;
    private int score1;
    private int score2;
    private Point food;
    private String winner;
    private boolean isOver;

    public GameInformation(ArrayList<Point> snake1, ArrayList<Point> snake2,
                           int score1, int score2, Point food) {
        this.snake1 = snake1;
        this.snake2 = snake2;
        this.score1 = score1;
        this.score2 = score2;
        this.food = food;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setOver(boolean over) {
        isOver = over;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public ArrayList<Point> getSnake1() {
        return snake1;
    }

    public ArrayList<Point> getSnake2() {
        return snake2;
    }

    public int getScore1() {
        return score1;
    }

    public int getScore2() {
        return score2;
    }

    public Point getFood() {
        return food;
    }

    public String getWinner() {
        return winner;
    }

    public boolean isOver() {
        return isOver;
    }
}
