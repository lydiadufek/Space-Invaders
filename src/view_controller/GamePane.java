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
import model.*;

import java.util.*;

import static model.Utils.readImage;

public class GamePane {
    private Player player;
    private AlienShip alienShip;
    private ArrayList<Sprite> objects;
    private boolean isShooting;
    private boolean notAlien;
    private boolean playerIsInvincible;
    private boolean playerShot;
    private Timer alienShootingTimer;
    private Timer alienShipTimer;
    private Timer alienMovingTimer;
    private Timer invincibilityTimer;
    private Alien[][] aliens;
    private Random random;
    private int shotInterval;
    private int levelNum;
    private ArrayList<Timer> timers;

    private int delay;

    private Scene scene;
    private gameScreen gameScreen;
    private Canvas canvas;
    private GraphicsContext gc;

    private Double coordTrack = 250.0;
    private String direction = "right";

    private final int WW = startScreen.getWW();
    private final int WH = startScreen.getWH();

    private boolean alienShipHit;

    public GamePane(Scene scene, gameScreen gameScreen) {
        this.scene = scene;
        this.gameScreen = gameScreen;
        random = new Random();
        levelNum = 1;

        canvas = new Canvas(WW, WH*0.929);
        gc = canvas.getGraphicsContext2D();
        objects = new ArrayList<>();
        aliens = new Alien[5][7];
        timers = new ArrayList<>();

        drawPlayer();
        drawAliens();

        //these are not getting deleted properly

        alienShootingTimer = new Timer();
        generateShotInterval();
        alienShootingTimer.scheduleAtFixedRate(new RandomAlienShots(), 1000, shotInterval); //delay: 5 sec interval: 3 sec

        //it needs to move across the screen first
        alienShipTimer = new Timer();
        delay = generateRandomAlienShipDelay();
        alienShipTimer.scheduleAtFixedRate(new AlienShipTimer(), 10000, delay);


        alienMovingTimer = new Timer();
        alienMovingTimer.scheduleAtFixedRate(new moveAllAliens(), 500, 1000);

        timers.add(alienShootingTimer);
        timers.add(alienShipTimer);
        timers.add(alienMovingTimer);
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
                        //TODO: restrict how many the player can shoot
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

                //checking the alienship
                if (alienShip != null && alienShip.isActive()) {
                    alienShip.moveAcrossScreen(gc);

                    double newX = alienShip.getX() - 1;
                    if (newX - (alienShip.getWidth()/2) >= canvas.getWidth()) {
                        System.out.println("hello");

                        alienShip.setActive(false);
                        objects.remove(alienShip);
//                        alienShipHit = false;
                    }
                }

                lastNanoTime = currentNanoTime;
            }
        }.start();
    }

    private void detectAndHandleCollisions() {
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                Sprite object1 = objects.get(i);
                Sprite object2 = objects.get(j);

// ^^^^^^^^^^^^^EXPERIMENTAL CLEANUP, DOESN'T WORK^^^^^^^^^^^^^^^^^^^
//                if (isCollided(object1.getAABB(), object2.getAABB())) {
//                    Sprite[] orderedObjs = bulletFirst(object1, object2);
//                    Bullet bullet = (Bullet) orderedObjs[0];
//                    Sprite otherObj = orderedObjs[1];
//
//                    if (bullet != null && otherObj instanceof Alien && bullet.getPlayerShot()) {
//                        objects.remove(object1);
//                        objects.remove(object2);
//                        gameScreen.updateScore(((Alien) otherObj).getScore());
//                        player.updateScore(((Alien) otherObj).getScore());
//                        if (player.newLife()) {
//                            System.out.println("new life");
//                        }
//                    }
//
//                    //Bullet hitting the Player
//                    if (!playerIsInvincible) {
//                        if (bullet != null && otherObj instanceof Player && !bullet.getPlayerShot()) {
//                            handlePlayerBeingShot((Player) otherObj, bullet);
//                        }
//                    }
//
//                    //Bullets hitting each other
//                    if (bullet != null && object2 instanceof Bullet) {
//                        System.out.println("bullets collided");
//                        objects.remove(object1);
//                        objects.remove(object2);
//                    }
//                }
                if (isCollided(object1.getAABB(), object2.getAABB())) {
                    //Player hitting the Alien
                    if ((object1 instanceof Alien && object2 instanceof Bullet && ((Bullet) object2).getPlayerShot())
                            || (object1 instanceof Bullet && object2 instanceof Alien && ((Bullet) object1).getPlayerShot())) {
                        objects.remove(object1);
                        objects.remove(object2);
                        if (object1 instanceof Alien) {
                            gameScreen.updateScore(((Alien) object1).getScore());
                            player.updateScore(((Alien) object1).getScore());
                            if (player.newLife()) {
                                gameScreen.addLifeIcon();
                            }
                            ((Alien) object1).kill();
                        }
                        if (object2 instanceof Alien) {
                            gameScreen.updateScore(((Alien) object2).getScore());
                            player.updateScore(((Alien) object2).getScore());
                            if (player.newLife()) {
                                gameScreen.addLifeIcon();
                            }
                            ((Alien) object2).kill();
                        }
                    }

                    //Bullet hitting the Player
                    if (!playerIsInvincible) {
                        if ((object1 instanceof Bullet && object2 instanceof Player && !((Bullet) object1).getPlayerShot())
                                || (object1 instanceof Player && object2 instanceof Bullet && !((Bullet) object2).getPlayerShot())) {
                            if (object1 instanceof Bullet) {
                                handlePlayerBeingShot((Player) object2, (Bullet) object1);
                            }
                            if (object2 instanceof Bullet) {
                                handlePlayerBeingShot((Player) object1, (Bullet) object2);
                            }
                        }
                    }

                    //Bullets hitting each other
                    if (object1 instanceof Bullet && object2 instanceof Bullet) {
                        System.out.println("bullets collided");
                        objects.remove(object1);
                        objects.remove(object2);
                    }

                    //Player hitting the AlienShip
                    if ((object1 instanceof AlienShip && object2 instanceof Bullet && ((Bullet) object2).getPlayerShot())
                            || (object1 instanceof Bullet && object2 instanceof AlienShip && ((Bullet) object1).getPlayerShot())) {

                        alienShip.setActive(false);
                        objects.remove(object1);
                        objects.remove(object2);

                        if (object1 instanceof AlienShip) {
                            gameScreen.updateScore(((AlienShip) object1).getScore());
                            player.updateScore(((AlienShip) object1).getScore());
                            if (player.newLife()) {
                                gameScreen.addLifeIcon();
                            }
                        }
                        if (object2 instanceof AlienShip) {
                            gameScreen.updateScore(((AlienShip) object2).getScore());
                            player.updateScore(((AlienShip) object2).getScore());
                            if (player.newLife()) {
                                gameScreen.addLifeIcon();
                            }
                        }
                    }
                }
            }
        }
    }

    private Sprite[] bulletFirst(Sprite object1, Sprite object2) {
        Sprite[] retVal = new Sprite[2];
        if (object1 instanceof Bullet) {
            retVal[0] = object1;
            retVal[1] = object2;
        } else if (object2 instanceof Bullet) {
            retVal[0] = object2;
            retVal[1] = object1;
        } else {
            retVal[0] = null;
            retVal[1] = null;
        }
        System.out.print(retVal[0] + " ");
        System.out.println(retVal[1]);
        return retVal;
    }

    private void handlePlayerBeingShot(Player player, Bullet bullet) {
        objects.remove(bullet);

        //update the life counter
        player.updateLives();
        gameScreen.removeLifeIcon();

        //reset the player
        double middleX = canvas.getWidth() / 2 - player.getImage().getWidth() / 2;
        player.setX(middleX);
        player.drawFrame(gc);

        //invicibility time frame
        startInvincibilityTimer();
        playerIsInvincible = true;

        if (player.isDead()) {
            objects.remove(player);
            System.out.println("dead");
        }
    }

    private void generateShotInterval() {
        if (levelNum < 10) {
            int maxTime = (-110*levelNum) + 2100;
            shotInterval = random.nextInt(300, maxTime);
        } else {
            shotInterval = random.nextInt(300, 1000);
        }
    }

    private class RandomAlienShots extends TimerTask {
        @Override
        public void run() {
            generateShotInterval();

            // getting the bottom row of aliens (the ones that can shoot)
            ArrayList<Alien> bottomRowAliens = new ArrayList<>();
            for (int i = 0; i < aliens[0].length; i++) {
                int j;
                for (j = aliens.length-1; j >= 0; j--) {
                    if (aliens[j][i].stillAlive()) {
                        bottomRowAliens.add(aliens[j][i]);
                        break;
                    }
                }
            }

            // randomly getting which aliens will shoot in the next frame
            ArrayList<Alien> aliensToShoot = new ArrayList<>();
            for (Alien object : bottomRowAliens) {
                if (random.nextDouble() < 0.15) {
                    aliensToShoot.add(object);
                }
            }

            // have them shoot later
            Platform.runLater(() -> {
                for (Alien alien : aliensToShoot) {
                    alienShoot(alien);
//                    updateAlienSprites(alien);
                }
            });
        }
    }

    private class moveAllAliens extends TimerTask {
    	@Override
        public void run() {
            for (Sprite object : new ArrayList<>(objects)) {
                if (object instanceof Alien) {
                    Alien alien = (Alien) object;
                    updateAlienSprites(alien);
                    alien.changeVelocity(3, 10);
	    			if(direction.equals("left")) {
	    				alien.moveLeft(gc);
                    }
	    			if(direction.equals("right")) {
	    				alien.moveRight(gc);
                    }

	    			if (coordTrack > 290.0) {
	    				alien.moveDown(gc);
                        direction = "left";
                    }
	    			if (coordTrack < 230.0) {
	    				alien.moveDown(gc);
                        direction = "right";
                    }
                    
                    if(alien.getY() >= player.getY()) {
                        player.setDead();
                        objects.remove(player);
                    }
	    		}
	    	}
	    	if (direction.equals("left")) {
	    		coordTrack -= 3;
	    	} else {
	    		coordTrack += 3;
	    	}

        }
    }

    private int generateRandomAlienShipDelay() {
        return random.nextInt(30000, 46000);
    }

    private class AlienShipTimer extends TimerTask {
        @Override
        public void run() {
            if (alienShip == null || alienShip.getX() > canvas.getWidth() || !alienShip.isActive()) {
                spawnAlienShip();
                delay = generateRandomAlienShipDelay();
                alienShipTimer.scheduleAtFixedRate(new AlienShipTimer(), 30000, delay);
                timers.add(alienShipTimer);
            }
        }
    }

    private void spawnAlienShip() {
        System.out.println("hello");
        Image image = readImage("AlienShip.png");
        alienShip = new AlienShip(image, ((int) -image.getWidth()), 10);
        alienShip.getScore();
        alienShip.setActive(true);
        objects.add(alienShip);
        alienShip.drawFrame(gc);
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
            //drawAABB(object);
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

            //update the image and score dependin on the alien type
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
                aliens[i][j] = alien;
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
        }, 2000); //invicibility length
        timers.add(timer);
    }

    private void updateAlienSprites(Sprite object) {
        if (object instanceof Alien) {
            Alien alien = (Alien) object;
            Image oldImage = alien.getImage();
            Image newImage = null;

            // Different image depending on the type of alien
            if (alien.getType() == 1) {
                newImage = readImage("alien1-2.png");
            } else if (alien.getType() == 2) {
                newImage = readImage("alien2-2.png");
            } else if (alien.getType() == 3) {
                newImage = readImage("alien3-2.png");
            }

            alien.updateSprite(newImage);
            alien.updateAABB();

            //this might prevent internal error 66
            new AnimationTimer() {
                long startTime = System.currentTimeMillis();

                @Override
                public void handle(long now) {
                    long elapsedTime = System.currentTimeMillis() - startTime;

                    if (elapsedTime < 400) {
                        Platform.runLater(() -> {
                            alien.drawFrame(gc);
                        });
                    } else if (elapsedTime >= 400) {
                        alien.updateSprite(oldImage);
                        alien.updateAABB();

                        Platform.runLater(() -> {
                            alien.drawFrame(gc);
                        });

                        stop();
                    }
                }
            }.start();
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

    public Canvas getCanvas() {
        return canvas;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Sprite> getObjects() {
        return objects;
    }

    public ArrayList<Timer> getTimers() {
        return timers;
    }

}