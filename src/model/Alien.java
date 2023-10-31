package model;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Alien extends Sprite {
    private int startingPositionX, startingPositionY;
    private int health;
    private int scoreAmount;

    public Alien(Image image, int x, int y, int health) {
        super(image, x, y);

        this.health = health;
        this.startingPositionX = x;
        this.startingPositionY = y;
        scoreAmount = 20;
    }

    public void updateHealth(int damage) {
        health -= damage;
    }

    public int updateScore(int score) {
        return score += scoreAmount;
    }

    public boolean stillAlive() {
        return health != 0;
    }
}
