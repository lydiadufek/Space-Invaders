package view_controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Alien;
import model.Bullet;
import model.Player;
import model.Sprite;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class GamePane {
    private Player player;
    private ArrayList<Sprite> objects;
    private Canvas canvas;
    private GraphicsContext gc;
    private boolean isShooting = false;
    private boolean notAlien = false;
    private Scene scene;
    private gameScreen gameScreen;
    private boolean playerShot;

    private Timer alienShootingTimer;


    public GamePane(Scene scene, gameScreen gameScreen) {
        this.scene = scene;
        this.gameScreen = gameScreen;

        canvas = new Canvas(500, 650);
        gc = canvas.getGraphicsContext2D();
        objects = new ArrayList<Sprite>();

        drawPlayer();
        drawAliens();

        alienShootingTimer = new Timer();
        // Schedule the task to run periodically (adjust the delay and interval as needed)
        alienShootingTimer.scheduleAtFixedRate(new AlienShootingTask(), 5000, 3000); // Delay: 5 seconds, Interval: 3 seconds
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

    private void drawPlayer() {
        Image image = readImage("ship.png");
        player = new Player(image, (canvas.getWidth() / 2) - (image.getWidth() / 2), canvas.getHeight() - image.getHeight()-10);
        objects.add(player);
        player.drawFrame(gc);
    }

    private void drawAliens() {
        int numRows = 5; // Number of rows of aliens
        int spacingX = 20; // Spacing between aliens in the X-direction
        int spacingY = 20; // Spacing between aliens in the Y-direction

        for (int i = 0; i < numRows; i++) {
            Image image;
            int scoreAmount;

            // Determine the image and score amount based on the row index (i)
            if (i == 0) {
                image = readImage("alien3.png");
                scoreAmount = 50;
            } else if (i == 1 || i == 2) {
                image = readImage("alien2.png");
                scoreAmount = 25;
            } else {
                image = readImage("alien.png");
                scoreAmount = 1000;
            }

            int numAliensInRow = 7; // Number of aliens in each row
            double totalWidth = numAliensInRow * image.getWidth();

            // Calculate the starting X-coordinate for the current row
            double startX = (canvas.getWidth() - totalWidth - (numAliensInRow - 1) * spacingX) / 2;

            // Create aliens for the current row
            for (int j = 0; j < numAliensInRow; j++) {
                // Calculate the X-coordinate for the current alien
                double x = startX + j * (image.getWidth() + spacingX);
                double y = i * (image.getHeight() + spacingY);

                Alien alien = new Alien(image, (int) x, (int) y, 1, scoreAmount);
                objects.add(alien);
                alien.drawFrame(gc);
            }
        }
    }

    public void shoot() {
        isShooting = true;
        Image image = readImage("bullet.png");
        Bullet bullet = new Bullet(image, player.getX() + player.getWidth() / 2 - (image.getWidth() / 2), player.getY()-10);
        bullet.setPlayerShot();
        objects.add(bullet);
    }

    public void alienShoot(Sprite object) {
        Image image = readImage("bullet.png");
        Bullet bullet = new Bullet(image, object.getX() + object.getWidth() / 2 - (image.getWidth() / 2), object.getY()-10);
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
                            ((Bullet) object).move(gc);
                        }
                    }
                }

                for (int i = objects.size() - 1; i >= 0; i--) {
                    Sprite object = objects.get(i);
                    if (object instanceof Bullet) {
                        if (object.getX() < 0 || object.getX() > canvas.getWidth() || object.getY() < 0 || object.getY() > canvas.getHeight()) {
                            objects.remove(i);
                        }
                    }
                }

                detectAndHandleCollisions();

                //Rendering
                drawFrame();

                lastNanoTime = currentNanoTime;
            }
        }.start();
    }

    private class AlienShootingTask extends TimerTask {
        @Override
        public void run() {
            Random random = new Random();
            List<Alien> aliensToShoot = new ArrayList<>();

            for (Sprite object : objects) {
                if (object instanceof Alien && random.nextDouble() < 0.3) {
                    aliensToShoot.add((Alien) object);
                }
            }

            for (Alien alien : aliensToShoot) {
                alienShoot(alien);
            }
        }
    }

    public void drawAABB(Sprite object) {
        gc.setStroke(Color.WHITE);
        gc.strokeRect(object.getAABB().getX(), object.getAABB().getY(), object.getAABB().getWidth(), object.getAABB().getHeight());
    }

    public void drawFrame() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Sprite object : objects) {
            object.drawFrame(gc);
            drawAABB(object); //draw aabb
        }
    }

    private void detectAndHandleCollisions() {
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                Sprite object1 = objects.get(i);
                Sprite object2 = objects.get(j);

                //Player hitting the Alien
                if (isCollided(object1.getAABB(), object2.getAABB())) {
                    if ((object1 instanceof Alien && object2 instanceof Bullet && ((Bullet) object2).getPlayerShot())
                            || (object1 instanceof Bullet && object2 instanceof Alien && ((Bullet) object1).getPlayerShot())) {
                        objects.remove(object1);
                        objects.remove(object2);
                        if(object1 instanceof Alien) {
                            gameScreen.updateScore(((Alien) object1).getScore());
                            player.updateScore(((Alien) object1).getScore());
                            //this isnt working an im too lazy to fix rn
                            if(player.newLife()) {
                                System.out.println("new life");
                            }
                        }
                        if(object2 instanceof Alien) {
                            gameScreen.updateScore(((Alien) object2).getScore());
                            player.updateScore(((Alien) object2).getScore());
                            if(player.newLife()) {
                                System.out.println("new life");
                            }
                        }
                    }

                    if ((object1 instanceof Bullet && object2 instanceof Player && !((Bullet) object1).getPlayerShot())
                            || (object1 instanceof Player && object2 instanceof Bullet && ! ((Bullet) object2).getPlayerShot())) {
                        if(object1 instanceof Bullet) {
                            objects.remove(object1);
                            ((Player) object2).updateLives();
                            //TODO: update heart bar, save score to display?
                            if(((Player) object2).isDead()) {
                                objects.remove(object2);
                                System.out.println("dead");
                            }
                        }
                        if(object2 instanceof Bullet) {
                            objects.remove(object2);
                            ((Player) object1).updateLives();
                            //TODO: update heart bar, save score to display?
                            if(((Player) object1).isDead()) {
                                objects.remove(object1);
                                System.out.println("dead");
                            }
                        }
                    }
                }
            }
        }
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