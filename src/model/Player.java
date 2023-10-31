package model;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class Player extends Sprite {
    private int width;
    private int height;
    private int x;
    private int y;
    private double xVelocity;
    private double yVelocity;

    private ImageView image;
    private int lifeAmount;

    public Player(ImageView image, double x, double y) {
        super(image, x, y);
        lifeAmount = 3; //3 total
    }

    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(x, y, image.getFitWidth(), image.getFitHeight());
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
