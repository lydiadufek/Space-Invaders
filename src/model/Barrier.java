package model;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class Barrier extends Sprite {
    private int width;
    private int height;
    private int x;
    private int y;
    private double xVelocity;
    private double yVelocity;

    private ImageView image;
    private int health;

    public Barrier(ImageView image, double x, double y) {
        super(image, x, y);
        //add the barrier images for decay
    }

    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(x, y, image.getFitWidth(), image.getFitHeight());
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
