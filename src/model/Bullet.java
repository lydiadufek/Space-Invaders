package model;

import javafx.scene.image.Image;

public class Bullet extends Sprite {
    private int damage;

    //moves forward, player, or downward, alien

    public Bullet(Image image, double x, double y) {
        super(image, x, y);
    }
}
