package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import view_controller.GamePane;

public class Player extends Sprite {
    private int lifeAmount;
    private int score;
    private GraphicsContext gc;

    public boolean isInvincible;
    private long invincibilityStartTime;
    private long invincibilityDuration; // Specify the duration of invisibility in milliseconds


    public Player(Image image, double x, double y) {
        super(image, x, y);
        lifeAmount = 3; //3 total
        score = 0;
        xVelocity = 20;
        yVelocity = 0;

        isInvincible = false;
    }

    public void drawFrame(GraphicsContext gc) {
        this.gc = gc;
        gc.drawImage(image, x, y);
    }

    public void shoot(Bullet bullet, GraphicsContext gc, Player player, GamePane pane) {
        bullet.move(gc);
//        System.out.println("PEW PEW PEW!!");
    }

    public void setInvincible(boolean update) {
        isInvincible = update;
    }

    public boolean isInvincible() {
        return isInvincible && (System.currentTimeMillis() - invincibilityStartTime >= invincibilityDuration);
    }

    public void setX(double x) {
        this.x = x;
        updateAABB();
        drawFrame(gc);
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

    public void updateLives() {
        lifeAmount--;
    }

    public void updateAABB() {
        this.AABB = new Rectangle(x, y, this.width, this.height);
    }

    public boolean isDead() {
        return lifeAmount==0;
    }

    public void updateScore(int amount) {
        score += amount;
    }

    public int getLives() {
        return lifeAmount;
    }

    public boolean newLife() {
        if(score == 1500) {
            lifeAmount++;
            return true;
        } else {
            return false;
        }
    }
}
