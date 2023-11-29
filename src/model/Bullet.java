package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.time.Instant;

public class Bullet extends Sprite {
    private int damage;
    private boolean playerShot;
    private boolean bossShot;
    private boolean toClose;
    private GraphicsContext gc;

    public Bullet(Image image, double x, double y) {
        super(image, x, y);
        this.playerShot = playerShot;
        toClose = false;
    }

    public void drawFrame(GraphicsContext gc) {
        this.gc = gc;
        gc.drawImage(image, x, y);
        updateAABB();
    }

    public void setPlayerShot() {
        playerShot = true;
    }

    public void setBossShot() {
        bossShot = true;
    }

    public boolean getPlayerShot() {
        return playerShot;
    }

    public boolean getBossShot() {
        return bossShot;
    }

    public void move(GraphicsContext gc) {
        if (playerShot) {
            yVelocity = -10;  // Player's bullets move upward
        } else {
            yVelocity = 5;   // Alien's bullets move downward
        }
        y += yVelocity;
    }

    public void moveHoming(GraphicsContext gc, Player player) {
        this.gc = gc;
        double directionX = player.getX() - x;
        double directionY = player.getY() - y;

        double distanceToPlayer = Math.sqrt(directionX * directionX + directionY * directionY);

        if (distanceToPlayer > 0) {
            directionX /= distanceToPlayer;
            directionY /= distanceToPlayer;

            double newX = x + directionX * 2; //speed
            double newY = y + directionY * 3; //speed

            x = newX;
            y = newY;

            double distanceToPlayerX = Math.abs(player.getX() - x);
            if (distanceToPlayerX <= 1) {
                toClose = true;
                return;
            }
        }
    }

    public boolean getToClose() {
        return toClose;
    }

    public void updateAABB(int add) {
        this.AABB = new Rectangle(x, y, this.width + add, this.height + add);
    }

    public void setX(double x) {
        this.x = x;
        updateAABB();
        drawFrame(gc);
    }

    public void setY(double y) {
        this.y = y;
        updateAABB();
        drawFrame(gc);
    }

    @Override
    public String toString() {
        return "Bullet";
    }
}
