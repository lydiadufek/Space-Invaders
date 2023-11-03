package model;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Alien extends Sprite {
    private int startingPositionX, startingPositionY;
    private int health;
    private int scoreAmount;

    public Alien(Image image, int x, int y, int health, int scoreAmount) {
        super(image, x, y);

        this.health = health;
        this.startingPositionX = x;
        this.startingPositionY = y;
        this.scoreAmount = scoreAmount;
    }

    public void updateHealth(int damage) {
        health -= damage;
    }

    public int updateScore(int score) {
        return scoreAmount += score;
    }

    public boolean stillAlive() {
        return health != 0;
    }

    public void updateAABB() {
        this.AABB = new Rectangle(x, y, this.width, this.height);
    }

    public int getScore() {
        return scoreAmount;
    }

}
