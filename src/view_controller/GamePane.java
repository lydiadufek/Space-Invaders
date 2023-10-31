package view_controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import model.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GamePane {
    private Player player;
    private Canvas canvas;
    private GraphicsContext gc;

    public GamePane() {
        canvas = new Canvas(500, 650);
        player = new Player(readImage(), 230, 550);
        gc = canvas.getGraphicsContext2D();

        // Top left coordinates!!
        player.drawFrame(gc);
    }


    public Canvas getCanvas() {
        return canvas;
    }

    public void shoot() {
        player.shoot();
    }

    public void moveLeft() {
        player.moveLeft(gc);
    }

    public void moveRight() {
        player.moveRight(gc);
    }

    private Image readImage() {
        FileInputStream shipImagePath;
        try {
            shipImagePath = new FileInputStream("lib/ship.png");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new Image(shipImagePath, 40, 40, true, false);
    }

}
