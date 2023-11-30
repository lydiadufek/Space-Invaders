/**
 * Purpose: This file holds the functionality for the bullets. A bullet can be
 *          spawned by 3 different entities: player, alien, and boss (alien). The boss
 *          has a different movement (moveHoming) whereas the others just use move. A different
 *          velocity is applied depending on if a player shot or an alien shot so that the bullet
 *          can either move up or down. There are booleans attached to who shot.
 *
 * Authors: Camila Grubb, Federico Fernandez, Katelyn Rohrer, Lydia Dufek
 */

package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Bullet extends Sprite {
    private boolean playerShot;
    private boolean bossShot;
    private GraphicsContext gc;

    /**
     * Creates a bullet object with set image, and positions. A boolean is set to determine
     * if the player activated this class or not
     * @param image
     * @param x
     * @param y
     */
    public Bullet(Image image, double x, double y) {
        super(image, x, y);
        this.playerShot = false;
    }

    /**
     * Draws the bullet onto the Gc. Updates the AABB as well
     * @param gc
     */
    public void drawFrame(GraphicsContext gc) {
        this.gc = gc;
        gc.drawImage(image, x, y);
        updateAABB();
    }

    /**
     * Sets the playerShot boolean to true. The player shot the bullet
     */
    public void setPlayerShot() { playerShot = true; }

    /**
     * Sets the bossShot to true. The boss shot the bullet
     */
    public void setBossShot() { bossShot = true; }

    /**
     * Gets the player shot boolean. Used for moving the bullet accordingly
      * @return boolean
     */
    public boolean getPlayerShot() { return playerShot; }

    /**
     * Gets the boss shot boolean. Used to determine if the homingShot method
     * needs to be used.
     * @return boolean
     */
    public boolean getBossShot() { return bossShot; }

    /**
     * Moves the bullet. Up if the player shot, and down if the alien shot it.
     * At a constant rate.
     */
    public void move() {
        if (playerShot) yVelocity = -10;  // Player's bullets move upward
        else yVelocity = 5;   // Alien's bullets move downward

        y += yVelocity;
    }

    /**
     * Creates a homing shot from the boss that follows the player until it makes
     * contact.
     * @param gc
     * @param player
     */
    public void moveHoming(GraphicsContext gc, Player player) {
        this.gc = gc;
        double directionX = player.getX() - x;
        double directionY = player.getY() - y;

        double distanceToPlayer = Math.sqrt(directionX * directionX + directionY * directionY);

        if (distanceToPlayer > 0) {
            directionX /= distanceToPlayer;

            x = x + directionX * 2;
            y += (yVelocity*2);
        }
    }

    /**
     * Returns string value of the object
     * @return String
     */
    @Override
    public String toString() { return "Bullet"; }
}
