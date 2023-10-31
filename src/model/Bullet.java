package model;

import javafx.scene.image.ImageView;

public class Bullet extends Sprite {
    private int damage;

    //moves forward, player, or downward, alien

    public Bullet(ImageView image, double x, double y) {
        super(image, x, y);
    }
}
