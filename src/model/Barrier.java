package model;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Barrier extends Sprite {

    private int health;
    private Image[] damageImages;

    public Barrier(Image[] damageImages, double x, double y) {
        super(damageImages[0], x, y);
        this.damageImages = damageImages;
        this.health = damageImages.length;
    }

    public void receiveDamage() {
        if (health > 0) {
            health--;
        }
    }

    public Image[] getDamageImages() {
        return damageImages;
    }
}

