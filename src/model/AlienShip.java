package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class AlienShip extends Sprite {
    private int startingPositionX, startingPositionY;
    private int health;
    private int scoreAmount;
    private int type;

    public AlienShip(Image image, int x, int y) {
        super(image, x, y);

        this.health = health;
        this.startingPositionX = x;
        this.startingPositionY = y;
        this.scoreAmount = scoreAmount;

        xVelocity = 1;
        //once hit it dies --> collider should just control this
        //random score amount
    }

    public void updateAABB() {
        this.AABB = new Rectangle(x, y, this.width, this.height);
    }

    public void moveAcrossScreen(GraphicsContext gc, double canvasWidth) {
        x += xVelocity;
        drawFrame(gc);
    }

    public int getScore() {
        //roll a random score amount
        //50, 100, 150, 300
        return scoreAmount;
    }
}
