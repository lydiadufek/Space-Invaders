/**
 * Purpose: This file holds the functionality for the Player. Lerp is applied
 *          to the movement so that it appears more smooth, and less jumpy at faster
 *          speeds. The health, movement, and image is changed depending on what ship
 *          the player decided to play as. The death, health, and score is also updated
 *          accordingly.
 *
 * Authors: Camila Grubb, Federico Fernandez, Kateyln Rohrer, Lydia Dufek
 */

package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import view_controller.GamePane;
import view_controller.Window;

public class Player extends Sprite {
    private int lifeAmount;
    private int score;
    private int scoreReset; //once the user hits 1500
    private long shootDelay;

    private GraphicsContext gc;

    private final int WH = Window.getHeight();
    private final int WW = Window.getWidth();

    public Player(Image image, double x, double y, int xVelocity, long shootDelay, int health) {
        super(image, x, y);
        this.lifeAmount = health;
        this.shootDelay = shootDelay;
        this.xVelocity = xVelocity;
        score = 0;
        scoreReset = 0;
    }

    public void drawFrame(GraphicsContext gc) {
        this.gc = gc;
        gc.drawImage(image, x, y);
    }

    public void shoot(Bullet bullet, GraphicsContext gc, Player player, GamePane pane) {
        bullet.move(gc);
    }

    public void setX(double x) {
        this.x = x;
        updateAABB();
        drawFrame(gc);
    }

    public void moveLeft(GraphicsContext gc) {
        if (x - 1 < 0) {
            return;
        }
        x = lerp(x, x - xVelocity, 0.7);
        drawFrame(gc);
        updateAABB();
    }

    public void moveRight(GraphicsContext gc) {
        if (x + 1 + width > WW) {
            return;
        }
        x = lerp(x, x + xVelocity, 0.7);
        drawFrame(gc);
        updateAABB();
    }

    private double lerp(double start, double end, double t) {
        return start + t * (end - start);
    }

    public void setDead() {
        lifeAmount = 0;
    }
    
    public void setAlive() {
    	lifeAmount = 3;
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
        scoreReset += amount;
    }

    public int getLives() {
        return lifeAmount;
    }

    public int getScore() {
        return score;
    }

    public boolean getNewLife() {
        if(scoreReset >= 1500) {
            scoreReset = 0;
            lifeAmount++;
            return true;
        } else {
            return false;
        }
    }

    public long getDelay() {
        return shootDelay;
    }

    @Override
    public String toString() {
        return "Player";
    }
}
