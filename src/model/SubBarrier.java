package model;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class SubBarrier extends Sprite {

    private int health;
    private Image[] playerDamageImages;
    private Image[] alienDamageImages;

    public SubBarrier(Image[] playerDamageImages, double x, double y) {
        super(playerDamageImages[0], x, y);
        this.playerDamageImages = playerDamageImages;
        this.health = 2; //i think
    }

    public void receiveDamage() {
        if (health > 0 || health <= 2) {
            health--;
        }
    }

    public Image[] getPlayerDamageImages() {
        return playerDamageImages;
    }

    public Image[] getAlienDamageImages() {
        return alienDamageImages;
    }

    public int getHealth() {
        return health;
    }
}

