package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Bullet extends Sprite {
    private int damage;

    //moves forward, player, or downward, alien
    public Bullet(Image image, double x, double y) {
        super(image, x, y);
        xVelocity = 0;
        yVelocity = 10;
    }

    public void drawFrame(GraphicsContext gc) {
        gc.drawImage(image, x, y);
        updateAABB();
    }

    public void moveUp(GraphicsContext gc) {
        y-= yVelocity;
    }

    public void moveDown(GraphicsContext gc) {
        y += yVelocity;
    }

    public void updateAABB() {
        this.AABB = new Rectangle(x, y, this.width, this.height);
    }

}

