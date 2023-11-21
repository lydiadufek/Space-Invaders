package view_controller;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import java.util.*;

import static java.lang.Math.min;
import static model.Utils.readImage;


public class GamePane {
    // static variables
    private static final int WW = Window.getWidth();
    private static final int WH = Window.getHeight();

    private static Stage stage;
    private static Scene scene;
    private static StartScreen home;
    private static GameScreen gameScreen;

    private static int levelNum;
    private static Player player;
    private static Random random;
    
    // sounds
    private SoundEffect shootSound = new SoundEffect("shipShoot.mp3");
    private SoundEffect deathSound = new SoundEffect("deathExplosion.mp3");
    private SoundEffect ufoSound = new SoundEffect("ufoHit.mp3");
    private SoundEffect alienSound = new SoundEffect("alienDieNoise.mp3");

    // instance variables
    private ArrayList<Sprite> objects;
    private Alien[][] aliens;
    private ArrayList<Timer> timers;
    private AlienShip alienShip;
    private String shipImage;


    private Timer alienShootingTimer;
    private Timer alienShipTimer;
    private Timer alienMovingTimer;

    private int coordTrack;
    private static int shotInterval;
    private double alienVelocity;
    private long lastShotTime;

    private boolean playerIsInvincible;
    private boolean isPaused;
    private Set<KeyCode> pressedKeys;

    private String alienTravelDirection = "right";

    private Canvas canvas;
    private GraphicsContext gc;

    // constants
    private final int ALIENS_PER_ROW = 9;
    private final int ALIEN_ROWS = 5;
    private final long SHOT_COOLDOWN = 200000000;

    //subBarriers
    private static SubBarrier barrier;
    private static SubBarrier barrier1;
    private static SubBarrier barrier2;
    private static SubBarrier barrier3;
    private static SubBarrier barrier4;
    private static SubBarrier barrier5;
    private static SubBarrier barrier6;
    private static SubBarrier barrier7;


    public GamePane(Stage stage, Scene scene, StartScreen home, GameScreen gameScreen) {
        GamePane.stage = stage;
        GamePane.scene = scene;
        GamePane.home = home;
        GamePane.gameScreen = gameScreen;

        GamePane.levelNum = 0;
        GamePane.random = new Random();
        setupKeypress();

//        isPaused = false;
        alienVelocity = 3;

        canvas = new Canvas(WW, WH*0.929);
        gc = canvas.getGraphicsContext2D();

        objects = new ArrayList<>();
        aliens = new Alien[ALIEN_ROWS][ALIENS_PER_ROW];
        timers = new ArrayList<>();

        coordTrack = WW/2;
        shipImage = home.getShipImage();

        if(shipImage == "purpleShip.png") {
            drawPlayer(shipImage, 20, 200000000, 3); //purpleShip
        }else if(shipImage == "greenShip.png") {
            drawPlayer("greenShip.png", 15, 200000000, 4); //greenShip
        }else if(shipImage == "redShip.png") {
            drawPlayer("redShip.png", 50, 800000000, 3); //red
        } else {
            drawPlayer("blueShip.png", 20, -10, 1); //blue
        }

        drawAliens();
        drawBarriers();
        startTimers();
    }

    public GamePane() {
        GamePane.levelNum += 1;
        System.out.println(player.getLives());
        setupKeypress();

//        isPaused = false;
        regenerateAlienVelocity();

        canvas = new Canvas(WW, WH*0.929);
        gc = canvas.getGraphicsContext2D();

        objects = new ArrayList<>();
        aliens = new Alien[ALIEN_ROWS][ALIENS_PER_ROW];
        timers = new ArrayList<>();

        coordTrack = WW/2;
        shipImage = home.getShipImage();

        if(shipImage == "purpleShip.png") {
            drawPlayer(shipImage, 20, 200000000, 3); //purpleShip
        }else if(shipImage == "greenShip.png") {
            drawPlayer("greenShip.png", 15, 200000000, 4); //greenShip
        }else if(shipImage == "redShip.png") {
            drawPlayer("redShip.png", 50, 800000000, 3); //red
        } else {
            drawPlayer("blueShip.png", 20, -10, 1); //blue
        }

        drawAliens();
        drawBarriers();
        startTimers();
    }

    private void setupKeypress() {
        pressedKeys = new HashSet<>();
        // user input
        scene.setOnKeyPressed(keyEvent -> {
            pressedKeys.add(keyEvent.getCode());
            handleKeyPress();
        });

        scene.setOnKeyReleased(keyEvent -> {
            pressedKeys.remove(keyEvent.getCode());
        });
    }

    private void handleKeyPress() {
        if (!isPaused) {
            // Handle key presses only if the game is not paused

            if (pressedKeys.contains(KeyCode.ESCAPE)) {
                isPaused = true;
                pauseGame();
                showPausePopup();
            }
            if (pressedKeys.contains(KeyCode.SPACE)) {
                shoot();
            }
            if (pressedKeys.contains(KeyCode.LEFT)) {
                moveLeft();
            }
            if (pressedKeys.contains(KeyCode.RIGHT)) {
                moveRight();
            }
        }
    }

    private void regenerateAlienVelocity() {
        alienVelocity = min( (0.3) * ((double) levelNum) + 3, 10 );
    }

    private void startTimers() {
        alienShootingTimer = new Timer();
        GamePane.shotInterval = generateShotInterval();
        alienShootingTimer.scheduleAtFixedRate(new RandomAlienShots(), 1000, shotInterval);
        timers.add(alienShootingTimer);

        //it needs to move across the screen first
        alienShipTimer = new Timer();
        alienShipTimer.scheduleAtFixedRate(new AlienShipTimer(), 10000, generateRandomAlienShipDelay());
        timers.add(alienShipTimer);

        alienMovingTimer = new Timer();
        alienMovingTimer.scheduleAtFixedRate(new moveAllAliens(), 500, 1000);
        timers.add(alienMovingTimer);
    }

	public void gameLoop() {
        new AnimationTimer() {
            long lastNanoTime = System.nanoTime();

            @Override
            public void handle(long currentNanoTime) {
//                double elapsedTime = (currentNanoTime - lastNanoTime) / 1e9;

                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

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

                //checking the alien ship
                if (alienShip != null && alienShip.isActive()) {
                    alienShip.moveAcrossScreen(gc);

                    double newX = alienShip.getX() - 1;
                    if (newX - (alienShip.getWidth()/2) >= canvas.getWidth()) {
                        System.out.println("hello");

                        alienShip.setActive(false);
                        objects.remove(alienShip);
                    }
                }

                //gameover screen
                if (player.isDead()) {
                	for (Timer timer: timers) timer.cancel();
                	this.stop();
                	GameOver endScreen = new GameOver(home, gameScreen.getScore());
                	home.getStage().setScene(endScreen.getScene());
                    home.getStage().show();
                }

                //next level
                if (allAliensDead()) {
                    for (Timer timer: timers){
                        timer.cancel();
                    }
                    this.stop();
                    gameScreen.newLevel();
                }

                if(isPaused)
                    stop();

//                start(); //uhhhhhhhhhhh

//                lastNanoTime = currentNanoTime;
            }
        }.start();
    }

    private boolean allAliensDead() {
        for (Sprite sprite: objects) {
            if (sprite instanceof Alien) {
                return false;
            }
        }
        return true;
    }

    public static int getLevelNum() {
        return levelNum;
    }

    private void detectAndHandleCollisions() {
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                Sprite object1 = objects.get(i);
                Sprite object2 = objects.get(j);

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
                                System.out.println("new life");
                                gameScreen.addLifeIcon();
                            }
                            ((Alien) object1).kill();
                            alienSound.playSound();
                        }
                        if (object2 instanceof Alien) {
                            gameScreen.updateScore(((Alien) object2).getScore());
                            player.updateScore(((Alien) object2).getScore());
                            if (player.newLife()) {
                                System.out.println("new life");
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
                            deathSound.playSound();
                        }
                    }

                    //Bullets hitting each other
                    if (object1 instanceof Bullet && object2 instanceof Bullet) {
                        objects.remove(object1);
                        objects.remove(object2);
                    }

                    //Player hitting the AlienShip
                    if ((object1 instanceof AlienShip && object2 instanceof Bullet && ((Bullet) object2).getPlayerShot())
                            || (object1 instanceof Bullet && object2 instanceof AlienShip && ((Bullet) object1).getPlayerShot())) {

                    	ufoSound.playSound();
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

                    //Player hitting the barrier
                    if ((object1 instanceof SubBarrier && object2 instanceof Bullet && ((Bullet) object2).getPlayerShot())
                            || (object1 instanceof Bullet && object2 instanceof SubBarrier && ((Bullet) object1).getPlayerShot())) {
                        objects.remove(object2);

                        ((SubBarrier) object1).receiveDamagePlayer();
                        Image[] temp = ((SubBarrier) object1).getPlayerDamageImages();
                        int health = ((SubBarrier) object1).getPlayerHealth();
                        if(health == 4)
                            objects.remove(object1);

                        object1.updateSprite(temp[health]);
                    }

                    //Alien hitting the barrier
                    if ((object1 instanceof Bullet && object2 instanceof SubBarrier && !((Bullet) object1).getPlayerShot())
                            || (object1 instanceof SubBarrier && object2 instanceof Bullet && !((Bullet) object2).getPlayerShot())) {
                        objects.remove(object2);

                        ((SubBarrier) object1).receiveDamagePlayer();
                        Image[] temp = ((SubBarrier) object1).getPlayerDamageImages();
                        int health = ((SubBarrier) object1).getPlayerHealth();
                        if(health == 4)
                            objects.remove(object1);

                        object1.updateSprite(temp[health]);
                    }
                }
            }
        }
    }

    private static Sprite[] bulletFirst(Sprite object1, Sprite object2) {
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

        //invincibility time frame
        startInvincibilityTimer();
        playerIsInvincible = true;

        if (player.isDead()) {
            objects.remove(player);
            System.out.println("dead");
        }
    }

    private static int generateShotInterval() {
        if (levelNum < 10) {
            int maxTime = (-110*levelNum) + 2100;
            return random.nextInt(300, maxTime);
        } else {
            return random.nextInt(300, 1000);
        }
    }

    private class RandomAlienShots extends TimerTask {
        @Override
        public void run() {
            GamePane.shotInterval = generateShotInterval();

            // getting the bottom row of aliens (the ones that can shoot)
            ArrayList<Alien> bottomRowAliens = new ArrayList<>();
            for (int i = 0; i < aliens[0].length; i++) {
                for (int j = aliens.length-1; j >= 0; j--) {
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
                }
            });
        }
    }

    private class moveAllAliens extends TimerTask {
    	@Override
        public void run() {
            for (Sprite object : new ArrayList<>(objects)) {
                if (object instanceof Alien alien) {
                    updateAlienSprites(alien);
                    alien.changeVelocity(alienVelocity, 10);
	    			if (alienTravelDirection.equals("left")) {
	    				alien.moveLeft(gc);
                    }
	    			if (alienTravelDirection.equals("right")) {
	    				alien.moveRight(gc);
                    }

	    			if (coordTrack > (WW/2 + 80)) {
	    				alien.moveDown(gc);
                        alienTravelDirection = "left";
                    }
	    			if (coordTrack < (WW/2 - 80)) {
	    				alien.moveDown(gc);
                        alienTravelDirection = "right";
                    }
                    
                    if(alien.getY() >= player.getY()) {
                        player.setDead();
                        objects.remove(player);
                    }
	    		}
	    	}
	    	if (alienTravelDirection.equals("left")) {
	    		coordTrack -= alienVelocity;
	    	} else {
	    		coordTrack += alienVelocity;
	    	}

        }
    }

    private long generateRandomAlienShipDelay() {
        return random.nextInt(30000, 46000);
    }

    private class AlienShipTimer extends TimerTask {
        @Override
        public void run() {
            if (alienShip == null || alienShip.getX() > canvas.getWidth() || !alienShip.isActive()) {
                spawnAlienShip();
                alienShipTimer.scheduleAtFixedRate(new AlienShipTimer(), 30000, generateRandomAlienShipDelay());
                timers.add(alienShipTimer);
            }
        }
    }

    private void spawnAlienShip() {
        Image image = Utils.readImage("AlienShip.png");
        alienShip = new AlienShip(image, ((int) -image.getWidth()), 10);
        alienShip.getScore();
        alienShip.setActive(true);
        objects.add(alienShip);
        alienShip.drawFrame(gc);
    }

    public void shoot() {
    	shootSound.playSound();
        if (!player.isDead()) {
            long currentTime = System.nanoTime();
            long elapsedSinceLastShot = currentTime - lastShotTime;

            if (elapsedSinceLastShot > player.getDelay()) {
                Image image = Utils.readImage("bullet.png");
                Bullet bullet = new Bullet(image, player.getX() + player.getWidth() / 2 - (image.getWidth() / 2), player.getY() - 10);
                bullet.setPlayerShot();
                objects.add(bullet);

                lastShotTime = currentTime; // Update last shot time
            }
        }
    }

    public void alienShoot(Sprite object) {
        Image image = Utils.readImage("bullet.png");
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
//            drawAABB(object);
        }
    }

    private void drawPlayer() {
        player.setX((canvas.getWidth() / 2) - (player.getImage().getWidth() / 2));
        objects.add(player);
        player.drawFrame(gc);
    }

    private void drawPlayer(String imageName, int xVelocity, long shootDelay, int health) {
        Image image = Utils.readImage(imageName);
        if (player == null) {
            player = new Player(image, (canvas.getWidth() / 2) - (image.getWidth() / 2), canvas.getHeight() - image.getHeight()-10, xVelocity, shootDelay, health);
        }
        objects.add(player);
        player.drawFrame(gc);
    }

    private void drawAliens() {
        int spacingX = 18;
        int spacingY = 20;

        for (int i = 0; i < ALIEN_ROWS; i++) {
            Image image;
            int scoreAmount;
            int type;
            int interval;
            int shiftX;
            int shiftY;

            //update the image and score dependin on the alien type
            if (i == 0) {
                image = Utils.readImage("alien3-1.png");
                scoreAmount = 50;
                type = 3;
                interval = 12;
                shiftX = -47; shiftY = 0;
            } else if (i == 1 || i == 2) {
                image = Utils.readImage("alien2-1.png");
                scoreAmount = 25;
                type = 2;
                interval = 6;
                shiftX = -23; shiftY = -5;
            } else {
                image = Utils.readImage("alien1-1.png");
                scoreAmount = 10;
                type = 1;
                interval = 0;
                shiftX = 0; shiftY = -15;
            }

            double totalWidth = ALIENS_PER_ROW * image.getWidth();
            double startX = (canvas.getWidth() - totalWidth - ALIENS_PER_ROW * spacingX) / 2;

            for (int j = 0; j < ALIENS_PER_ROW; j++) {
                double x = startX + j * (image.getWidth() + spacingX + interval) + shiftX;
                double y = 60 + (i * (image.getHeight() + spacingY)) + shiftY;

                Alien alien = new Alien(image, (int) x, (int) y, 1, scoreAmount, type);
                objects.add(alien);
                aliens[i][j] = alien;
                alien.drawFrame(gc);
            }
        }
    }

    private void drawBarriers() {
//        Barrier barrier = new Barrier(150, 80, canvas, objects, gc);
//        barrier.draw();
        draw(150, 80);

//        Barrier barrier2 = new Barrier(300, 80, canvas, objects, gc);
//        barrier2.draw();
        draw(300, 80);

//        Barrier barrier3 = new Barrier(-75, 80, canvas, objects, gc);
//        barrier3.draw();
        draw(-75, 80);

//        Barrier barrier4 = new Barrier(-245, 80, canvas, objects, gc);
//        barrier4.draw();
        draw(-245, 80);

    }

    public void draw(int x, int y){
        Image image = null;
        Image image2 = null;
        Image image3 = null;
        Image image4 = null;
        Image image5 = null;
        Image[] temp = null;
//        SubBarrier barrier = null;

        //-------------------------------------------------
        image = readImage("BottomLeftCorner-1.png");
        image2 = readImage("BottomLeftCorner-2.png");
        image3 = readImage("BottomLeftCorner-3.png");
        image4 = readImage("BottomLeftCorner-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        if(barrier == null) {
            barrier = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - x, canvas.getHeight() - image.getHeight() - y);
        }
        objects.add(barrier);
        barrier.drawFrame(gc);

        //----------------------------------------------
        image = readImage("TopLeftCorner-1.png");
        image2 = readImage("TopLeftCorner-2.png");
        image3 = readImage("TopLeftCorner-3.png");
        image4 = readImage("TopLeftCorner-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        if(barrier1 == null) {
            barrier1 = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - x, canvas.getHeight() - image.getHeight() - ((y - 9) + image.getHeight()));
        }
        objects.add(barrier1);
        barrier1.drawFrame(gc);

        //----------------------------------------
        image = readImage("TopFill-1.png");
        image2 = readImage("TopFill-2.png");
        image3 = readImage("TopFill-3.png");
        image4 = readImage("TopFill-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        if(barrier2 == null) {
            barrier2 = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - (x + 1 - image.getWidth()), canvas.getHeight() - image.getHeight() - (y + 15 + image.getHeight()));
        }
        objects.add(barrier2);
        barrier2.drawFrame(gc);

        //----------------------------------------
        image = readImage("TopFill-1.png");
        image2 = readImage("TopFill-2.png");
        image3 = readImage("TopFill-3.png");
        image4 = readImage("TopFill-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        if(barrier3 == null) {
            barrier3 = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - ((x + 1) - image.getWidth() * 2), canvas.getHeight() - image.getHeight() - ((y + 15) + image.getHeight()));
        }
        objects.add(barrier3);
        barrier3.drawFrame(gc);

        //-------------------------------------------
        image = readImage("BottomFill-1.png");
        image2 = readImage("BottomFill-2.png");
        image3 = readImage("BottomFill-3.png");
        image4 = readImage("BottomFill-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        if(barrier4 == null) {
            barrier4 = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - (x - 21), canvas.getHeight() - image.getHeight() - ((y - 7) + image.getWidth()));
        }
        objects.add(barrier4);
        barrier4.drawFrame(gc);

        //-------------------------------------------
        image = readImage("BottomFill-1.png");
        image2 = readImage("BottomFill-2.png");
        image3 = readImage("BottomFill-3.png");
        image4 = readImage("BottomFill-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        if(barrier5 == null) {
            barrier5 = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - ((x - 21) - image.getWidth()), canvas.getHeight() - image.getHeight() - ((y - 7) + image.getWidth()));
        }
        objects.add(barrier5);
        barrier5.drawFrame(gc);

        //-----------------------------------------------
        image = readImage("TopRightCorner-1.png");
        image2 = readImage("TopRightCorner-2.png");
        image3 = readImage("TopRightCorner-3.png");
        image4 = readImage("TopRightCorner-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        if(barrier6 == null) {
            barrier6 = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - ((x - 25) - image.getWidth() * 2), canvas.getHeight() - image.getHeight() - ((y - 9) + image.getHeight()));
        }
        objects.add(barrier6);
        barrier6.drawFrame(gc);

        //--------------------------------------------------
        image = readImage("BottomRightCorner-1.png");
        image2 = readImage("BottomRightCorner-2.png");
        image3 = readImage("BottomRightCorner-3.png");
        image4 = readImage("BottomRightCorner-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        if(barrier7 == null) {
            barrier7 = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - ((x - 43) - image.getWidth()), canvas.getHeight() - image.getHeight() - ((y - 20) + image.getWidth()));
        }
        objects.add(barrier7);
        barrier7.drawFrame(gc);
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
        if (object instanceof Alien alien) {
            Image oldImage = alien.getImage();
            Image newImage = null;

            // Different image depending on the type of alien
            if (alien.getType() == 1) {
                newImage = Utils.readImage("alien1-2.png");
            } else if (alien.getType() == 2) {
                newImage = Utils.readImage("alien2-2.png");
            } else if (alien.getType() == 3) {
                newImage = Utils.readImage("alien3-2.png");
            }

            alien.updateSprite(newImage);
            alien.updateAABB();

            new AnimationTimer() {
                long startTime = System.currentTimeMillis();

                @Override
                public void handle(long now) {
                    long elapsedTime = System.currentTimeMillis() - startTime;

                    if (elapsedTime < 400) {
                    } else if (elapsedTime >= 400) {
                        alien.updateSprite(oldImage);
                        alien.updateAABB();

                        stop();
                    }
                    drawFrame();

                }
            }.start();
        }
    }

    private void showPausePopup() {
        Stage pauseStage = new Stage();
        BorderPane pausePane = new BorderPane();

        Image image = Utils.readImage("game-background.jpg");
        BackgroundImage bgImage = new BackgroundImage(image,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background bg = new Background(bgImage);
        pausePane.setBackground(bg);

        Label label = new Label("Game Paused");
        Font font = Utils.getFont(30);
        label.setFont(font);
        label.setTextFill(Color.WHITE);
        pausePane.setCenter(label);
        
        Button resumeBtn = new Button("Resume");
        resumeBtn.setFont(font);
        resumeBtn.setOnAction(e -> {
            isPaused = false;
            pauseStage.close();
            resumeGame();
        });
        Button quitBtn = new Button("Main Menu");
        quitBtn.setFont(font);
        quitBtn.setOnAction(e -> {
            pauseStage.close();
            for (Timer timer : timers) {
                timer.cancel();
            }
            timers.clear();
            home.getStage().setScene(home.getScene());
            home.getStage().show();
        });
        

        pausePane.setLeft(resumeBtn);
        pausePane.setRight(quitBtn);
        BorderPane.setAlignment(resumeBtn, Pos.BOTTOM_LEFT);
        BorderPane.setAlignment(quitBtn, Pos.BOTTOM_RIGHT);
        BorderPane.setMargin(resumeBtn, new Insets(10, 10, 10, 10));
        BorderPane.setMargin(quitBtn, new Insets(0, 10, 10, 0));

        Scene pauseScene = new Scene(pausePane,550, 500);
        pauseStage.setScene(pauseScene);
        pauseStage.setTitle("Pause");
        pauseStage.initModality(Modality.APPLICATION_MODAL); // cannot touch other window stuff w this
        pauseStage.initOwner(this.stage); // makes our big stage the 'owner' of puse pop up

        pauseStage.showAndWait();
    }
    
    private void pauseGame() {
        scene.setOnKeyPressed(null);
        scene.setOnKeyReleased(null);
        isPaused = true;
        for (Timer timer : timers) {
            timer.cancel();
        }
    }
    
    private void resumeGame() {

        isPaused = false;
        timers.clear();

        alienShootingTimer = new Timer();
        GamePane.shotInterval = generateShotInterval();
        alienShootingTimer.scheduleAtFixedRate(new RandomAlienShots(), 1000, shotInterval);
        timers.add(alienShootingTimer);

        alienShipTimer = new Timer();
        alienShipTimer.scheduleAtFixedRate(new AlienShipTimer(), 10000, generateRandomAlienShipDelay());
        timers.add(alienShipTimer);

        alienMovingTimer = new Timer();
        alienMovingTimer.scheduleAtFixedRate(new moveAllAliens(), 500, 1000);
        timers.add(alienMovingTimer);

        setupKeypress();
    }

    public void drawAABB(Sprite object) {
        gc.setStroke(Color.WHITE);
        gc.strokeRect(object.getAABB().getX(), object.getAABB().getY(), object.getAABB().getWidth(), object.getAABB().getHeight());
    }

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

    public Canvas getCanvas() {
        return canvas;
    }

    public static Player getPlayer() {
        return player;
    }

    public ArrayList<Sprite> getObjects() {
        return objects;
    }

    public ArrayList<Timer> getTimers() {
        return timers;
    }

}