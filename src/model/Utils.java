package model;

import javafx.scene.image.Image;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

import static view_controller.GamePane.getLevelNum;

public class Utils {
    public static final Random random = new Random();

    public static Font getFont(int size) {
        FileInputStream fontInputStream;
        try {
            fontInputStream = new FileInputStream("lib/pixeboy-font.ttf");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return Font.loadFont(fontInputStream, size);
    }


    public static Image readImage(String filePath) {
        FileInputStream imagePath;
        try {
            imagePath = new FileInputStream("lib/" + filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new Image(imagePath);
    }

    public static int generateShotInterval() {
        int levelNum = getLevelNum();
        if (levelNum < 10) {
            int maxTime = (-110 * levelNum) + 2100;
            return random.nextInt(300, maxTime);
        } else {
            return random.nextInt(300, 1000);
        }
    }

    public static long generateRandomAlienShipDelay() {
        return random.nextInt(60000, 100000);
    }

}
