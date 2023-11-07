package view_controller;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
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
    private Timer invincibilityTimer;
    private boolean playerIsInvincible = false; // Add this variable to track player invincibility state



    public GamePane(Scene scene, gameScreen gameScreen) {
        this.scene = scene;
        this.gameScreen = gameScreen;

        canvas = new Canvas(500, 650);
        gc = canvas.getGraphicsContext2D();
        objects = new ArrayList<Sprite>();

        drawPlayer();
        drawAliens();

        alienShootingTimer = new Timer();
        alienShootingTimer.scheduleAtFixedRate(new RandomAlienShots(), 1000, 3000); //delay: 5 sec interval: 3 sec
    }

    public void gameLoop() {
        new AnimationTimer() {
            long lastNanoTime = System.nanoTime();

            @Override
            public void handle(long currentNanoTime) {
                double elapsedTime = (currentNanoTime - lastNanoTime) / 1e9;

                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                //user input
                scene.setOnKeyPressed(keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.SPACE) {
                        shoot();
                    } else if (keyEvent.getCode() == KeyCode.LEFT) {
                        moveLeft();
                    } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                        moveRight();
                    }
                });

                //bullet mechanics
                for (int i = objects.size() - 1; i >= 0; i--) {
                    Sprite object = objects.get(i);
                    if (object instanceof Bullet) {
                        ((Bullet) object).move(gc);
                        if (object.getX() < 0 || object.getX() > canvas.getWidth() || object.getY() < 0 || object.getY() > canvas.getHeight()) {
                            objects.remove(i);
                        }
                    }
                }

                //collisions
                detectAndHandleCollisions();

                //rendering
                drawFrame();

                lastNanoTime = currentNanoTime;
            }
        }.start();
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
                            //TODO: fix this because right now its not checking properly
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

                    //Bullet hitting the player
                        if (!playerIsInvincible) {
                        if ((object1 instanceof Bullet && object2 instanceof Player && !((Bullet) object1).getPlayerShot())
                                || (object1 instanceof Player && object2 instanceof Bullet && ! ((Bullet) object2).getPlayerShot())) {
                            if(object1 instanceof Bullet) {
                                objects.remove(object1);

                                ((Player) object2).updateLives();
                                double middleX = canvas.getWidth() / 2 - player.getImage().getWidth() / 2;
                                ((Player) object2).setX(middleX);
                                player.drawFrame(gc);

                                startInvincibilityTimer();
                                playerIsInvincible = true;

                                if (player.isDead()) {
                                    objects.remove(player);
                                    System.out.println("dead");
                                }
                            }

                            if(object2 instanceof Bullet) {
                                //TODO: Make into a method
                                objects.remove(object2);

                                ((Player) object1).updateLives();
                                double middleX = canvas.getWidth() / 2 - player.getImage().getWidth() / 2;
                                ((Player) object1).setX(middleX);
                                player.drawFrame(gc);

                                startInvincibilityTimer();
                                playerIsInvincible = true;

                                if (player.isDead()) {
                                    objects.remove(player);
                                    System.out.println("dead");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private class RandomAlienShots extends TimerTask {
        @Override
        public void run() {
            Random random = new Random();
            List<Alien> aliensToShoot = new ArrayList<>();

            for (Sprite object : objects) {
                if (object instanceof Alien && random.nextDouble() < 0.2) {
                    aliensToShoot.add((Alien) object);
                }
            }

            Platform.runLater(() -> {
                for (Alien alien : aliensToShoot) {
                    alienShoot(alien);
                    updateAlienSprites(alien);
                }
            });
        }
    }

    public void shoot() {
        if(!player.isDead()) {
            isShooting = true;
            Image image = readImage("bullet.png");
            Bullet bullet = new Bullet(image, player.getX() + player.getWidth() / 2 - (image.getWidth() / 2), player.getY() - 10);
            bullet.setPlayerShot();
            objects.add(bullet);
        }
    }

    public void alienShoot(Sprite object) {
        Image image = readImage("bullet.png");
        Bullet bullet = new Bullet(image, object.getX() + object.getWidth() / 2 - (image.getWidth() / 2), object.getY()-10);
        objects.add(bullet);
    }

    public void moveLeft() {
        double newX = player.getX() - 1;
        if (newX > 0) {
            player.moveLeft(gc);
        }
    }

    public void moveRight() {
        double newX = player.getX() + 1;
        if (newX + player.getWidth() < canvas.getWidth()) {
            player.moveRight(gc);
        }
    }

    public void drawFrame() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Sprite object : objects) {
            object.drawFrame(gc);
//            drawAABB(object); //draw aabb
        }
    }

    private void drawPlayer() {
        Image image = readImage("ship.png");
        player = new Player(image, (canvas.getWidth() / 2) - (image.getWidth() / 2), canvas.getHeight() - image.getHeight()-10);
        objects.add(player);
        player.drawFrame(gc);
    }

    private void drawAliens() {
        int spacingX = 20;
        int spacingY = 20;

        for (int i = 0; i < 5; i++) {
            Image image;
            int scoreAmount;
            int type;

            //update the iamge and score dependin on the alien type
            if (i == 0) {
                image = readImage("alien3-1.png");
                scoreAmount = 50;
                type = 3;
            } else if (i == 1 || i == 2) {
                image = readImage("alien2-1.png");
                scoreAmount = 25;
                type = 2;
            } else {
                image = readImage("alien1-1.png");
                scoreAmount = 10;
                type = 1;
            }

            double totalWidth = 7 * image.getWidth();
            double startX = (canvas.getWidth() - totalWidth - 7 * spacingX) / 2;

            for (int j = 0; j < 7; j++) {
                double x = startX + j * (image.getWidth() + spacingX);
                double y = i * (image.getHeight() + spacingY);

                Alien alien = new Alien(image, (int) x, (int) y, 1, scoreAmount, type);
                objects.add(alien);
                alien.drawFrame(gc);
            }
        }
    }


    private void startInvincibilityTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                playerIsInvincible = false;
            }
        }, 4000); // Invincibility for 3 seconds
    }

    public void updateAlienSprites(Sprite object) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (object instanceof Alien) {
            Alien alien = (Alien) object;
            Image oldImage = alien.getImage();
            Image newImage = null;

            //Different image depending on the type of alien
            if (alien.getType() == 1) {
                newImage = readImage("alien1-2.png");
            } else if (alien.getType() == 2) {
                newImage = readImage("alien2-2.png");
            } else if (alien.getType() == 3) {
                newImage = readImage("alien3-2.png");
            }

            alien.updateSprite(newImage);
            alien.updateAABB();

            Platform.runLater(() -> {
                alien.drawFrame(gc);
            });

            //create a timer to draw the "animations" for the aliens shooting
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    alien.isShooting(false);
                    alien.updateSprite(oldImage);
                    alien.updateAABB();

                    Platform.runLater(() -> {
                        alien.drawFrame(gc);
                    });
                }
            }, 1000);
        }
    }

    public void drawAABB(Sprite object) {
        gc.setStroke(Color.WHITE);
        gc.strokeRect(object.getAABB().getX(), object.getAABB().getY(), object.getAABB().getWidth(), object.getAABB().getHeight());
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

    public Image readImage(String filePath) {
        FileInputStream imagePath;
        try {
            imagePath = new FileInputStream("lib/" + filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new Image(imagePath);
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

}