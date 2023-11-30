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

    /**
     * Constructor for the player class
     * @param image the image of the player
     * @param x the player's x coordinate
     * @param y the player's y coordinate
     * @param xVelocity the player's speed
     * @param shootDelay the player's shooting speed
     * @param health the player's number of lives
     */
    public Player(Image image, double x, double y, int xVelocity, long shootDelay, int health) {
        super(image, x, y);
        this.lifeAmount = health;
        this.shootDelay = shootDelay;
        this.xVelocity = xVelocity;
        score = 0;
        scoreReset = 0;
    }

    /**
     * Draws the player image
     */
    public void drawFrame(GraphicsContext gc) {
        this.gc = gc;
        gc.drawImage(image, x, y);
    }

    /**
     * Sets the player's x value
     * @param x the new x value
     */
    public void setX(double x) {
        this.x = x;
        updateAABB();
        drawFrame(gc);
    }

    /**
     * Moves the player left
     */
    public void moveLeft(GraphicsContext gc) {
        if (x - 1 < 0) {
            return;
        }
        x = lerp(x, x - xVelocity);
        drawFrame(gc);
        updateAABB();
    }

    /**
     * Moves the player right
     */
    public void moveRight(GraphicsContext gc) {
        if (x + 1 + width > WW) {
            return;
        }
        x = lerp(x, x + xVelocity);
        drawFrame(gc);
        updateAABB();
    }

    /**
     * Makes the movements smoother using linear interpolation
     * @param start the starting position
     * @param end the ending position
     * @return the new end position
     */
    private double lerp(double start, double end) {
        return start + 0.7 * (end - start);
    }

    /**
     * Sets the player's lives to 0
     */
    public void setDead() { lifeAmount = 0; }

    /**
     * Decreases the player's lives by 1
     */
    public void updateLives() { lifeAmount--; }

    /**
     * Checks if the player is dead
     * @return true if the player is dead, false otherwise
     */
    public boolean isDead() { return lifeAmount==0; }

    /**
     * Increases the player's score
     * @param amount the amount to increase the score by
     */
    public void updateScore(int amount) {
        score += amount;
        scoreReset += amount;
    }

    /**
     * @return the player's number of lives
     */
    public int getLives() { return lifeAmount; }

    /**
     * Gives the player an extra life
     * @return true if the player got a new life, false otherwise
     */
    public boolean getNewLife() {
        if (scoreReset >= 1500) {
            scoreReset = 0;
            lifeAmount++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the player's shot delay
     */
    public long getDelay() { return shootDelay; }

    /**
     * Overrides the toString method
     */
    @Override
    public String toString() { return "Player"; }
}
