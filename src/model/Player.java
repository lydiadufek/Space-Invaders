package model;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Player extends Sprite {
    private int width;
    private int height;
    private int x;
    private int y;
    private double xVelocity;
    private double yVelocity;

    private Image image;
    private int lifeAmount;

    public Player(Image image, double x, double y) {
        super(image, x, y);
        lifeAmount = 3; //3 total
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

    public void updateLives() {
        lifeAmount--;
    }
}
