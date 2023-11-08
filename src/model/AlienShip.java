package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import java.util.*;


public class AlienShip extends Sprite {
    private Random random;
    private int startingPositionX, startingPositionY;
    private int health;
    private int scoreAmount;
    private int type;

    public AlienShip(Image image, int x, int y) {
        super(image, x, y);
        random = new Random();

        this.health = health;
        this.startingPositionX = x;
        this.startingPositionY = y;
        this.scoreAmount = scoreAmount;

        xVelocity = 1;
    }

    public void updateAABB() {
        this.AABB = new Rectangle(x, y, this.width, this.height);
    }

    public void moveAcrossScreen(GraphicsContext gc) {
        x += xVelocity;
        drawFrame(gc);
        updateAABB();
    }

    public int getScore() {
        int x = random.nextInt(1, 5);

        if(x == 1) {
            scoreAmount = 50;
        }
        if(x == 2) {
            scoreAmount = 100;
        }
        if(x == 3) {
            scoreAmount = 150;
        }
        if(x == 4) {
            scoreAmount = 300;
        }

        System.out.println(scoreAmount);
        return scoreAmount;
    }
}
