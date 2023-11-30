/**
 * Purpose: This file essentially holds all of our "utility" methods such as
 *          reading an image, font reading, checking AABB collisions, and random
 *          intervals. Methods that do not to be so apparent essentially.
 *
 * Authors: Camila Grubb, Federico Fernandez, Katelyn Rohrer, Lydia Dufek
 */

package model;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.min;
import static view_controller.GamePane.getLevelNum;

public class Utils {
    public static final Random random = new Random();

    /**
     * Loads the font from the file
     * @param size size of the font
     * @return Font object representing the loaded font
     */
    public static Font getFont(int size) {
        FileInputStream fontInputStream;
        try {
            fontInputStream = new FileInputStream("lib/pixeboy-font.ttf");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return Font.loadFont(fontInputStream, size);
    }

    /**
     * Creates an image object given a file containing the image
     * @param filePath the path to the image file
     * @return an Image object
     */
    public static Image readImage(String filePath) {
        FileInputStream imagePath;
        try {
            imagePath = new FileInputStream("lib/" + filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new Image(imagePath);
    }

    /**
     * Generates a random interval of time between alien shots
     * @return an integer representing the time interval
     */
    public static int generateShotInterval() {
        int levelNum = getLevelNum();
        if (levelNum < 10) {
            int maxTime = (-110 * levelNum) + 2100;
            return random.nextInt(300, maxTime);
        } else {
            return random.nextInt(300, 1000);
        }
    }

    /**
     * Sets the time between appearances of the bonus ship
     * @return an integer representing the time interval
     */
    public static long generateRandomAlienShipDelay() {
        return random.nextInt(60000, 100000);
    }

    /**
     * Increases the alien velocity based on which level it is
     * @return the velocity of the aliens as an integer
     */
    public static int regenerateAlienVelocity() {
        return min(getLevelNum() + 5, 15);
    }

    /**
     * Checks if all aliens are dead
     * @param objects an ArrayList containing all the aliens
     * @return true if all aliens are dead, false otherwise
     */
    public static boolean allAliensDead(ArrayList<Sprite> objects) {
        for (Sprite sprite: objects) {
            if (sprite instanceof Alien) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if two objects have collided
     * @param obj1 the first object
     * @param obj2 the second object
     * @return true if the objects collided, false otherwise
     */
    public static boolean isCollided(Rectangle obj1, Rectangle obj2) {
        double obj1Top = obj1.getY();
        double obj1Bottom = obj1Top + obj1.getHeight();
        double obj1Left = obj1.getX();
        double obj1Right = obj1Left + obj1.getWidth();

        double obj2Top = obj2.getY();
        double obj2Bottom = obj2Top + obj2.getHeight();
        double obj2Left = obj2.getX();
        double obj2Right = obj2Left + obj2.getWidth();

        boolean comp1 = obj1Bottom > obj2Top;
        boolean comp2 = obj1Top < obj2Bottom;
        boolean comp3 = obj1Right > obj2Left;
        boolean comp4 = obj1Left < obj2Right;

        return comp1 && comp2 && comp3 && comp4;
    }
}
