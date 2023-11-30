/**
 * Purpose: This file holds the functionality for the Aliens. Each alien has
 *          a different type that determines score amount, health, sprite image,
 *          and starting positions. The boss is also controlled here and has
 *          attributes that makes it easier to find and differentiate between
 *          the different aliens.
 *
 * Authors: Camila Grubb, Federico Fernandez, Katelyn Rohrer, Lydia Dufek
 */

package model;

import javafx.scene.image.Image;

public class Alien extends Sprite {
    private int health;
    private final int scoreAmount;
    private final int type;
    private boolean isBoss;

    /**
     * Creates an alien. With set velocity of 15 (both x and y).
     * @param image
     * @param x
     * @param y
     * @param health
     * @param scoreAmount
     * @param type
     */
    public Alien(Image image, int x, int y, int health, int scoreAmount, int type) {
        super(image, x, y);

        this.health = health;
        this.scoreAmount = scoreAmount;
        this.type = type;

        xVelocity = 15;
        yVelocity = 15;
        isBoss = false;
    }

    /**
     * This returns the score attached to each alien
     * @return int: score
     */
    public int getScore() { return scoreAmount; }

    /**
     * Each alien ahs a type 1-3 to indicate what kind of alien they are
     * @return int type
     */
    public int getType() { return type; }

    /**
     * This returns if an alien is alive or not. If the health
     * is greater than 0
     * @return boolean
     */
    public boolean stillAlive() { return health > 0; }

    /**
     * This decreases the alien's health by one point. Mostly here
     * for the boss since it has more than 1 health
     */
    public void hit() { health--; }

    /**
     * Used to set a boolean indicated for the boss
     */
    public void iAmBoss() { isBoss = true; }

    /**
     * Returns a boolean for the boss, if an alien is the boss
     * @return boolean
     */
    public boolean getBoss() { return isBoss; }

    /**
     * Tostring method. Returns type of object in a string
     * @return String
     */
    @Override
    public String toString() { return "Alien"; }
}
