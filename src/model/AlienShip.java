/**
 * Purpose: This file holds the functionality for the Alien Ship that
 *          flys by in a randomized interval. When the player hits the
 *          ship, a random score is determined. There are also attributes
 *          to determine when the ship is active or not so we can tell when
 *          to spawn a new one or not.
 *
 * Authors: Camila Grubb, Federico Fernandez, Kateyln Rohrer, Lydia Dufek
 */
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
    private boolean isActive;

    public AlienShip(Image image, int x, int y) {
        super(image, x, y);
        random = new Random();

        this.health = health;
        this.startingPositionX = x;
        this.startingPositionY = y;
        this.scoreAmount = scoreAmount;

        xVelocity = 2;

    }

    public void updateAABB() {
        this.AABB = new Rectangle(x, y, this.width, this.height);
    }

    public void moveAcrossScreen(GraphicsContext gc) {
        x += xVelocity;
        drawFrame(gc);
        updateAABB();
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
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

        return scoreAmount;
    }

    @Override
    public String toString() {
        return "AlienShip";
    }
}
