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
    private boolean isBoss;

    public Alien(Image image, int x, int y, int health, int scoreAmount, int type) {
        super(image, x, y);

        this.health = health;
        this.startingPositionX = x;
        this.startingPositionY = y;
        this.scoreAmount = scoreAmount;
        this.isShooting = false;
        this.type = type;

        xVelocity = 15;
        yVelocity = 15;
        isBoss = false;
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
        return health > 0;
    }

    public void kill() {
        health--;
    }

    public void iAmBoss() {
        isBoss = true;
    }

    public boolean getBoss() {
        return isBoss;
    }
}
