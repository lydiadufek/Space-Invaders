package model;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

abstract class Sprite {
    protected Image image;
    protected double x, y;
    protected double xVelocity, yVelocity;
    protected double width, height;
    protected Rectangle boundingBox;
    protected int health;

    public Sprite(Image image, double x, double y) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.xVelocity = 1;
        this.yVelocity = 1;
        this.health = 0; //maybe i should not have this
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.boundingBox = new Rectangle(x, y, this.width, this.height);
    }

    public Image getImage() {
        return image;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // does it make more sense to do it this way?? idk
    public void updateBoundingBox() {
        this.boundingBox = new Rectangle(x, y, this.width, this.height);
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
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

    public void updateSprite(Image newImage) {
        image = newImage;
        //actually update the sprite eventually
    }
//
//    public void updateHealth(int damage) {
//        health -= damage;
//    }
}
