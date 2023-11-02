package view_controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Alien;
import model.Bullet;
import model.Player;
import model.Sprite;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class GamePane {
    private Player player;
    private ArrayList<Sprite> objects;
    private Canvas canvas;
    private GraphicsContext gc;
    private boolean isShooting = false; // Flag to track if the spacebar is pressed
    private Scene scene; // Reference to the scene

    public GamePane(Scene scene) {
        this.scene = scene; // Store a reference to the scene
        canvas = new Canvas(500, 650);
        objects = new ArrayList<>();
        Image image = readImage("realShip.png");
        player = new Player(image, (canvas.getWidth() / 2) - (image.getWidth() / 2), canvas.getHeight() - image.getHeight() * 2.8);
        objects.add(player);
        drawAliens();
        gc = canvas.getGraphicsContext2D();
        player.drawFrame(gc);
        for (int i = 1; i < 56; i++) {
        	objects.get(i).drawFrame(gc);
        }
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Sprite> getObjects() {
        return objects;
    }

    private void drawAliens() {
    	for (int i = 0; i < 5; i++) {
    		for (int j = 0; j < 11; j++) {
    			Image image = readImage("bullet.png");
    			Alien alien = new Alien(image, 25 + 40*j, 100 + 50*i, 1);
    			objects.add(alien);
    		}
    	}
    }

    public void shoot() {
        isShooting = true;
        Image image = readImage("bullet.png");
        Bullet bullet = new Bullet(image, player.getX() + player.getWidth() / 2 - (image.getWidth() / 2), player.getY()-20);
        objects.add(bullet);
    }

    public void moveLeft() {
        player.moveLeft(gc);
    }

    public void moveRight() {
        player.moveRight(gc);
    }

    public void gameLoop() {
        new AnimationTimer() {
            long lastNanoTime = System.nanoTime();

            @Override
            public void handle(long currentNanoTime) {
                double elapsedTime = (currentNanoTime - lastNanoTime) / 1e9;

                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                scene.setOnKeyPressed(keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.SPACE) {
                        // The user pressed the spacebar, handle shooting
                        shoot();
                    } else if (keyEvent.getCode() == KeyCode.LEFT) {
                        moveLeft();
                    } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                        moveRight();
                    }
                });

                if (isShooting) {
                    for (Sprite object : objects) {
                        if (object instanceof Bullet) {
                            ((Bullet) object).moveUp(gc);
                        }
                    }
                }

                //Rendering
                for (Sprite object : objects) {
                    object.drawFrame(gc);
                }

                lastNanoTime = currentNanoTime;
            }
        }.start();
    }

    public Image readImage(String filePath) {
        FileInputStream imagePath;
        try {
            imagePath = new FileInputStream("lib/" + filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new Image(imagePath);
    }


    public boolean isCollided(Rectangle obj1, Rectangle obj2) {
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