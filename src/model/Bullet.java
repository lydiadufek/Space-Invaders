package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Bullet extends Sprite {
    private int damage;
    private boolean playerShot;

    public Bullet(Image image, double x, double y) {
        super(image, x, y);
        this.playerShot = playerShot;
    }

    public void drawFrame(GraphicsContext gc) {
        gc.drawImage(image, x, y);
        updateAABB();
    }

    public void setPlayerShot() {
        playerShot = true;
    }

    public boolean getPlayerShot() {
        return playerShot;
    }

    public void move(GraphicsContext gc) {
        if (playerShot) {
            yVelocity = -10;  // Player's bullets move upward
        } else {
            yVelocity = 10;   // Alien's bullets move downward
        }
        y += yVelocity;
    }

    public void updateAABB() {
        this.AABB = new Rectangle(x, y, this.width, this.height);
    }
}
