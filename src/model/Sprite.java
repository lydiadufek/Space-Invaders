package model;

import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public abstract class Sprite {
    protected Image image;
    protected double x, y;
    protected double xVelocity, yVelocity;
    protected double width, height;
    protected Rectangle AABB;
    protected int health;

    public Sprite(Image image, double x, double y) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.xVelocity = 1;
        this.yVelocity = 1;
        this.health = 0;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.AABB = new Rectangle(x, y, this.width, this.height);
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

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void updateAABB() {
        this.AABB = new Rectangle(x, y, this.width, this.height);
    }

    public Rectangle getAABB() {
        return AABB;
    }

    public void changeVelocity(double velocityX, double velocityY) {
        this.xVelocity = velocityX;
        this.yVelocity = velocityY;
    }

    public void drawFrame(GraphicsContext gc) {
        gc.drawImage(image, x, y);
    }

    public void update() {
        x += xVelocity;
        y += yVelocity;
    }

    public void updateSprite(Image newImage) {
        image = newImage;
    }
    
    public void moveLeft(GraphicsContext gc) {
        x -= xVelocity;
        drawFrame(gc);
        updateAABB();
    }
    
    public void moveRight(GraphicsContext gc) {
        x += xVelocity;
        drawFrame(gc);
        updateAABB();
    }
    
    public void moveDown(GraphicsContext gc) {
        y += yVelocity;
        drawFrame(gc);
        updateAABB();
    }

    @Override
    public String toString() {
        return "Sprite";
    }

}
