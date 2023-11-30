/**
 * Purpose: This file holds the functionality for the Alien Ship that
 *          files by in a randomized interval. When the player hits the
 *          ship, a random score is determined. There are also attributes
 *          to determine when the ship is active or not so we can tell when
 *          to spawn a new one or not.
 *
 * Authors: Camila Grubb, Federico Fernandez, Katelyn Rohrer, Lydia Dufek
 */

package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.*;

public class AlienShip extends Sprite {
    private final Random random;
    private boolean isActive;

    public AlienShip(Image image, int x, int y) {
        super(image, x, y);
        random = new Random();
        xVelocity = 2;
    }

    public void moveAcrossScreen(GraphicsContext gc) {
        x += xVelocity;
        drawFrame(gc);
        updateAABB();
    }

    public void setActive(boolean active) { isActive = active; }

    public boolean isActive() { return isActive; }

    public int getScore() {
        int x = random.nextInt(1, 5);

        int scoreAmount;
        switch (x) {
            case 1 -> scoreAmount = 50;
            case 2 -> scoreAmount = 100;
            case 3 -> scoreAmount = 150;
            default -> scoreAmount = 300;
        }

        return scoreAmount;
    }

    @Override
    public String toString() { return "AlienShip"; }
}
