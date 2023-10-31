package model;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Barrier extends Sprite {
    public Barrier(Image image, double x, double y) {
        super(image, x, y);
        //add the barrier images for decay
    }

    public void updateHealth(int damage) {
        health -= damage;
        //check for checkpoints to update sprite
        //add sprite changes to show decay
    }

    public boolean stillAlive() {
        return health != 0;
        //removes the barrier from the game
    }
}
