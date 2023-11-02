package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Bullet extends Sprite {
    private int damage;

    //moves forward, player, or downward, alien
    public Bullet(Image image, double x, double y) {
        super(image, x, y);
        xVelocity = 10;
        yVelocity = 0;
    }

    public void drawFrame(GraphicsContext gc) {
        gc.drawImage(image, x, y);
    }

    public void moveUp(GraphicsContext gc) {
        y-= yVelocity;
        drawFrame(gc);
    }

    public void moveDown(GraphicsContext gc) {
        y += yVelocity;
        drawFrame(gc);
    }
}
