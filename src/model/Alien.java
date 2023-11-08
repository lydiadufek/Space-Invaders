package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Alien extends Sprite {
    private int startingPositionX, startingPositionY;
    private int health;
    private int scoreAmount;
    private boolean isShooting;
    private int type;

    public Alien(Image image, int x, int y, int health, int scoreAmount, int type) {
        super(image, x, y);

        this.health = health;
        this.startingPositionX = x;
        this.startingPositionY = y;
        this.scoreAmount = scoreAmount;
        this.isShooting = false;
        this.type = type;

        xVelocity = 5;
        yVelocity = 5;

    }

    public void moveDown(GraphicsContext gc) {
        y += yVelocity;
        drawFrame(gc);
        updateAABB();
    }

    public void moveUp(GraphicsContext gc) {
        y -= yVelocity;
        drawFrame(gc);
        updateAABB();
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

    public void updateAABB() {
        this.AABB = new Rectangle(x, y, this.width, this.height);
    }

    public int getScore() {
        return scoreAmount;
    }

    public int getType() {
        return type;
    }

    public void isShooting(boolean status) {
        isShooting = status;
    }

    public boolean stillAlive() {
        return health != 0;
    }

    public void kill() {
        health = 0;
    }
}
