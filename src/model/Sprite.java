package model;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

class Sprite {
    private ImageView image;
    private double x, y;
    private double xVelocity, yVelocity;
    private Rectangle boundingBox;
    private int health;

    public Sprite(ImageView image, double x, double y) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.xVelocity = 0;
        this.yVelocity = 0;
        this.health = 0; //maybe i should not have this
    }

    public ImageView getImageView() {
        return image;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Rectangle getBoundingBox() {
        return new Rectangle(x, y, image.getFitWidth(), image.getFitHeight());
    }

    public void changeVelocity(double velocityX, double velocityY) {
        this.xVelocity = velocityX;
        this.yVelocity = velocityY;
    }

    public void update() {
        x += xVelocity;
        y += yVelocity;

        //getBoundingBox();
        //update collider
    }

    public void updateSprite(ImageView newImage) {
        image = newImage;
        //actually update the sprite eventually
    }
//
//    public void updateHealth(int damage) {
//        health -= damage;
//    }
}
