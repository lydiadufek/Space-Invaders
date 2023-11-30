/**
 * Purpose: This file holds the functionality for the Sub barriers. The damage
 *          is changed here so that we can determine what image to change to. The
 *          health is set to 4 as there are 4 different images each sub barrier cycles
 *          through.
 *
 * Authors: Camila Grubb, Federico Fernandez, Katelyn Rohrer, Lydia Dufek
 */

package model;

import javafx.scene.image.Image;

public class SubBarrier extends Sprite {
    private int playerHealth;
    private Image[] playerDamageImages;

    /**
     * This creates a sub barrier object. Used to form an entire barrier.
     * Makes it easier to show damage decay depending on where a bullet shot
     * on the barrier. Health stats are updated in this class.
     *
     * @param playerDamageImages
     * @param x
     * @param y
     */
    public SubBarrier(Image[] playerDamageImages, double x, double y) {
        super(playerDamageImages[0], x, y);
        this.playerDamageImages = playerDamageImages;
        this.playerHealth = 0;
    }

    /**
     * This indicates damage was done, so the health decreases. Used for indexing the
     * image array in barriers.
     */
    public void receiveDamagePlayer() {
        if (playerHealth < 4 ) { playerHealth++; }
    }

    /**
     * This returns the image array used to store the damage images that are used to cycle
     * through to show damage being done on the barrier.
     *
     * @return Image[]
     */
    public Image[] getPlayerDamageImages() { return playerDamageImages; }

    /**
     * This returns the player health int, that is used for indexing
     * @return int
     */
    public int getPlayerHealth() {
        return playerHealth;
    }

    /**
     * This returns a string value of the object
     * @return String
     */
    @Override
    public String toString() { return "SubBarrier"; }
}

