package view_controller;

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
    private Canvas canvas;
    private GraphicsContext gc;
    private ArrayList<Sprite> objects; //this could be fine for now, if not lets use a r tree

    public GamePane() {
        canvas = new Canvas(500, 650);
        objects = new ArrayList<>();

        Image image = readImage("realShip.png");
        player = new Player(image, (canvas.getWidth() / 2) - (image.getWidth() / 2), canvas.getHeight() - image.getHeight()*2.5);
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

    public Player getPlayer() { return player;}
    
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
        Image image = readImage("bullet.png");
        Bullet bullet = new Bullet(image, player.getX()+player.getWidth()/2 - (image.getWidth() / 2), player.getY()-image.getHeight());
        objects.add(bullet);
        
        player.shoot(bullet, gc, player, this);

        //testing image/aabb
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.TRANSPARENT);
        gc.setLineWidth(2.0);
//        gc.strokeRect((int) bullet.getX(), (int) bullet.getY(), (int) bullet.getWidth(), (int) bullet.getHeight());
//        gc.strokeRect((int) player.getX(), (int) player.getY(), (int) player.getWidth(), (int) player.getHeight());
    }

    public void moveLeft() {
        player.moveLeft(gc);
    }

    public void moveRight() {
        player.moveRight(gc);
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
