/**
 * Purpose: This file holds the functionality for the Player. Lerp is applied
 *          to the movement so that it appears more smooth, and less jumpy at faster
 *          speeds. The health, movement, and image is changed depending on what ship
 *          the player decided to play as. The death, health, and score is also updated
 *          accordingly.
 *
 * Authors: Camila Grubb, Federico Fernandez, Katelyn Rohrer, Lydia Dufek
 */

package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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

    public void setX(double x) {
        this.x = x;
        updateAABB();
        drawFrame(gc);
    }

    public void moveLeft(GraphicsContext gc) {
        if (x - 1 < 0) {
            return;
        }
        x = lerp(x, x - xVelocity);
        drawFrame(gc);
        updateAABB();
    }

    public void moveRight(GraphicsContext gc) {
        if (x + 1 + width > WW) {
            return;
        }
        x = lerp(x, x + xVelocity);
        drawFrame(gc);
        updateAABB();
    }

    private double lerp(double start, double end) {
        return start + 0.7 * (end - start);
    }

    public void setDead() { lifeAmount = 0; }

    public void updateLives() { lifeAmount--; }

    public boolean isDead() { return lifeAmount==0; }

    public void updateScore(int amount) {
        score += amount;
        scoreReset += amount;
    }

    public int getLives() { return lifeAmount; }

    public boolean getNewLife() {
        if (scoreReset >= 1500) {
            scoreReset = 0;
            lifeAmount++;
            return true;
        } else {
            return false;
        }
    }

    public long getDelay() { return shootDelay; }

    @Override
    public String toString() { return "Player"; }
}
