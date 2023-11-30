/**
 * Purpose: This file holds the functionality for the Sub barriers. The damage
 *          is changed here so that we can determine what image to change to. The
 *          health is set to 4 as there are 4 different images each sub barrier cycles
 *          through.
 *
 * Authors: Camila Grubb, Federico Fernandez, Kateyln Rohrer, Lydia Dufek
 */
package model;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class SubBarrier extends Sprite {
    private int playerHealth;
    private Image[] playerDamageImages;

    public SubBarrier(Image[] playerDamageImages, double x, double y) {
        super(playerDamageImages[0], x, y);
        this.playerDamageImages = playerDamageImages;
        this.playerHealth = 0;
    }

    public void receiveDamagePlayer() {
        if (playerHealth < 4 ) {
            playerHealth++;
        }
    }

    public Image[] getPlayerDamageImages() {
        return playerDamageImages;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
//        updateAABB();
    }

    @Override
    public String toString() {
        return "SubBarrier";
    }
}

