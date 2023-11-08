package model;

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

        //once hit it dies --> collider should just control this
        //random score amount
    }

    public void updateAABB() {
        this.AABB = new Rectangle(x, y, this.width, this.height);
    }

    public int getScore() {
        //roll a random score amount
        //50, 100, 150, 300
        return scoreAmount;
    }
}
