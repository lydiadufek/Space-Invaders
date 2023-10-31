package model;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Alien extends Sprite {
    private int width, height;
    private int x, y;

    private int startingPositionX, startingPositionY;

    private double xVelocity;
    private double yVelocity;

    private Image image;
    private int health;
    private int scoreAmount;

    public Alien(Image image, int x, int y, int health) {
        super(image, x, y);

        this.health = health;
        this.startingPositionX = x;
        this.startingPositionY = y;
    }

    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(x, y, image.getWidth(), image.getHeight());
    }

    @Override
    public void changeVelocity(double velocityX, double velocityY) {
        this.xVelocity = velocityX;
        this.yVelocity = velocityY;
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
