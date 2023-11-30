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

    public Alien(Image image, int x, int y, int health, int scoreAmount, int type) {
        super(image, x, y);

        this.health = health;
        this.scoreAmount = scoreAmount;
        this.type = type;

        xVelocity = 15;
        yVelocity = 15;
        isBoss = false;
    }

    public int getScore() { return scoreAmount; }

    public int getType() { return type; }

    public boolean stillAlive() { return health > 0; }

    public void hit() { health--; }

    public void iAmBoss() { isBoss = true; }

    public boolean getBoss() { return isBoss; }

    @Override
    public String toString() { return "Alien"; }
}
