/**
 * Purpose: This main purpose is to store the game's main mechanics. It handles the game loop,
 *          collision detection, key presses, and animation states. This class also draws all objects
 *          on to the screen adn deals with the interactions with each object. There is also next level
 *          capabilites in which the aliens increase in difficulty, movement spedd increases, and there
 *          is a boss level every 5 levels. This pane redirects the player to the correct pages when they die.
 *
 * Authors: Camila Grubb, Federico Fernandez, Katelyn Rohrer, Lydia Dufek
 */
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
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import java.util.*;

public class GamePane {
    // static variables
    private static final int WW = Window.getWidth();
    private static final int WH = Window.getHeight();

    private static Stage stage;
    private static Scene scene;
    private static StartScreen home;
    private static GameScreen gameScreen;
    private static Player player;
    private static int levelNum;

    // sounds
    private static final SoundEffect shootSound = new SoundEffect("shipShoot.mp3");
    private static final SoundEffect deathSound = new SoundEffect("deathExplosion.mp3");
    private static final SoundEffect ufoSound = new SoundEffect("ufoHit.mp3");
    private static final SoundEffect alienSound = new SoundEffect("alienDieNoise.mp3");
    private static final SoundEffect bossSound = new SoundEffect("eenWailsNew.mp3");

    // instance variables
    private final ArrayList<Sprite> objects;
    private final Alien[][] aliens;
    private final ArrayList<Timer> timers;
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final int alienVelocity;

    private Set<KeyCode> pressedKeys;
    private AlienShip alienShip;
    private Timer alienShipTimer;

    private int coordTrack;
    private long lastShotTime;
    private char alienTravelDirection;

    private boolean playerIsInvincible;
    private boolean isPaused;
    private boolean notStarted;
    private boolean transitioning;
    private boolean allDead;

    // constants
    private final int ALIENS_PER_ROW = 9;
    private final int ALIEN_ROWS = 5;

    // subBarriers
    private static Barrier totalBarrier1;
    private static Barrier totalBarrier2;
    private static Barrier totalBarrier3;
    private static Barrier totalBarrier4;

    /**
     * Constructor for the start of a new game. Sets up references to the stage and scene,
     * as well as starting from level 0. Runs the game.
     * @param stage The stage of the window
     * @param scene The current scene the pane exist on
     * @param home The home screen pane object
     * @param gameScreen The game screen pane object
     */
    public GamePane(Stage stage, Scene scene, StartScreen home, GameScreen gameScreen) {
        GamePane.stage = stage;
        GamePane.scene = scene;
        GamePane.home = home;
        GamePane.gameScreen = gameScreen;

        GamePane.levelNum = 0;
        setupKeypress();

        alienVelocity = 4;

        notStarted = true;
        transitioning = false;
        alienTravelDirection = 'r';

        canvas = new Canvas(WW, WH * 0.929);
        gc = canvas.getGraphicsContext2D();

        objects = new ArrayList<>();
        aliens = new Alien[ALIEN_ROWS][ALIENS_PER_ROW];
        timers = new ArrayList<>();

        coordTrack = WW / 2;
        String shipImage = home.getShipImage();

        switch (shipImage) {
            case "purpleShip.png" ->
                    drawPlayer(shipImage, 20, 350000000, 3); //purpleShip
            case "greenShip.png" ->
                    drawPlayer(shipImage, 15, 350000000, 4); //greenShip
            case "redShip.png" ->
                    drawPlayer(shipImage, 50, 800000000, 3); //red
            default -> drawPlayer(shipImage, 20, 200000000, 1); //blue
        }

        drawAliens();
        drawBarriers();
        startTimers();
    }

    /**
     * Constructor for a new level in the game. One is added to the level and
     * the game restarts.
     */
    public GamePane() {
        GamePane.levelNum += 1;
        setupKeypress();

        alienVelocity = Utils.regenerateAlienVelocity();
        notStarted = true;
        alienTravelDirection = 'r';

        canvas = new Canvas(WW, WH*0.929);
        gc = canvas.getGraphicsContext2D();

        objects = new ArrayList<>();
        aliens = new Alien[ALIEN_ROWS][ALIENS_PER_ROW];
        timers = new ArrayList<>();
        
        if ((levelNum + 1) % 5 == 0 && !player.isDead()) {
        	bossSound.playSound();
        } else {
        	bossSound.stopSound();
        }

        coordTrack = WW/2;

        drawPlayer();
        drawAliens();
        drawStaticBarrier();
        startTimers();
    }

    //------MECHANICS------

    /**
     * The main loop of the game. Handles moving bullets, detecting collisions,
     * drawing the frame, and changing the level when necessary.
     */
    public void gameLoop() {
        new AnimationTimer() {

            /**
             * The main loop of the game. Handles moving bullets, detecting collisions,
             * drawing the frame, and changing the level when necessary.
             * @param currentNanoTime The current time. Necessary for gameLoop override
             */
            @Override
            public void handle(long currentNanoTime) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                moveBullets();
                detectAndHandleCollisions();
                drawFrame();

                moveOrDespawnAlienShip();

                // next level
                if (Utils.allAliensDead(objects) || allDead) {
                    transitioning = true;
                    for (Timer timer: timers){
                        timer.cancel();
                    }
                    this.stop();
                    gameScreen.newLevel();
                } else {
                    transitioning = false;
                }

                // game over screen
                if (player.isDead()) {
                    for (Timer timer : timers) timer.cancel();
                    this.stop();
                    transitioning = true;
                    GameOver endScreen = new GameOver(home, gameScreen.getScore());
                    home.getStage().setScene(endScreen.getScene());
                    home.getStage().show();
                }

                // boss battle
                if ((levelNum + 1) % 5 == 0 && notStarted) {
                    startBossBattle();
                }

                if (isPaused) stop();
                if (!transitioning) start();

            }
        }.start();
    }

    /**
     * Handles all collisions between objects. Checks whether they have collided
     * and takes the correct action when they do.
     */
    private void detectAndHandleCollisions() {
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                Sprite[] orderedSprites = orderSprites(objects.get(i), objects.get(j));
                Sprite object1 = orderedSprites[0];
                Sprite object2 = orderedSprites[1];

                if (Utils.isCollided(object1.getAABB(), object2.getAABB())) {

                    // Bullet hitting the Alien
                    if ((object1 instanceof Alien
                            && object2 instanceof Bullet
                            && ((Bullet) object2).getPlayerShot())) {

                        alienSound.playSound();
                        ((Alien) object1).hit();

                        if (!((Alien) object1).stillAlive()) {
                            objects.remove(object1);
                            gameScreen.updateScore(((Alien) object1).getScore());
                            player.updateScore(((Alien) object1).getScore());

                            if (player.getNewLife()) gameScreen.addLifeIcon();
                        }
                        objects.remove(object2);
                    }

                    // Bullet hitting the Player
                    if (!playerIsInvincible) {
                        if ((object1 instanceof Bullet
                                && object2 instanceof Player
                                && !((Bullet) object1).getPlayerShot())) {

                            playerDeathAnimation();
                            handlePlayerBeingShot((Player) object2, (Bullet) object1);
                            deathSound.playSound();
                        }
                    }

                    // Bullets hitting each other
                    if (object1 instanceof Bullet
                            && object2 instanceof Bullet) {

                        objects.remove(object1);
                        objects.remove(object2);
                    }

                    // Bullet hitting the AlienShip
                    if ((object1 instanceof AlienShip
                            && object2 instanceof Bullet
                            && ((Bullet) object2).getPlayerShot())) {

                        ufoSound.playSound();
                        alienShip.setActive(false);
                        objects.remove(object1);
                        objects.remove(object2);

                        gameScreen.updateScore(((AlienShip) object1).getScore());
                        player.updateScore(((AlienShip) object1).getScore());
                        if (player.getNewLife()) {
                            gameScreen.addLifeIcon();
                        }
                    }

                    // Bullet hitting the barrier
                    if ((object1 instanceof Bullet
                            && object2 instanceof SubBarrier)) {

                        objects.remove(object1);
                        ((SubBarrier) object2).receiveDamagePlayer();
                        Image[] temp = ((SubBarrier) object2).getPlayerDamageImages();
                        int health = ((SubBarrier) object2).getPlayerHealth();
                        if(health == 4)
                            objects.remove(object2);

                        object2.updateSprite(temp[health]);
                    }
                }
            }
        }
    }

    /**
     * Orders the two sprites in alphabetical order so that they are in a
     * known order for detectAndHandleCollisions.
     * @param object1 The first sprite to be ordered
     * @param object2 The second sprite to be ordered
     * @return Array of the two aliens in alphabetical order
     */
    public static Sprite[] orderSprites(Sprite object1, Sprite object2) {
        Sprite[] retVal = new Sprite[2];
        if (object1.toString().compareTo(object2.toString()) < 0) {
            retVal[0] = object1;
            retVal[1] = object2;
        } else {
            retVal[0] = object2;
            retVal[1] = object1;
        }
        return retVal;
    }

    /**
     * Lets the player shoot bullets. Checks that enough time has passed and
     * shoots if it has.
     */
    public void shoot() {
        if (!player.isDead()) {
            long currentTime = System.nanoTime();
            long elapsedSinceLastShot = currentTime - lastShotTime;

            if (elapsedSinceLastShot > player.getDelay()) {
                shootSound.playSound();
                Image image = Utils.readImage("bullet.png");
                Bullet bullet = new Bullet(image, player.getX() + player.getWidth() / 2 - (image.getWidth() / 2), player.getY() - 10);
                bullet.setPlayerShot();
                objects.add(bullet);

                lastShotTime = currentTime;
            }
        }
    }

    /**
     * Lets the aliens shoot bullets. Spawns a bullet at the alien's location.
     * @param object The alien that is shooting
     */
    public void alienShoot(Sprite object) {
        Image image = Utils.readImage("bullet.png");
        Bullet bullet = new Bullet(image, object.getX() + object.getWidth() / 2 - (image.getWidth() / 2), object.getY()-10);
        objects.add(bullet);
    }

    /**
     * Lets the boss shoot bullets. Spawns a bullet at the boss's location.
     * @param object The boss object that is shooting
     */
    public void bossShoot(Sprite object) {
        Image image = Utils.readImage("bossBullet.png");
        Bullet bullet = new Bullet(image, object.getX() + object.getWidth() / 2 - (image.getWidth() / 2), object.getY() + 200);
        bullet.setBossShot();
        objects.add(bullet);
    }

    /**
     * Handles when the player is shot by a bullet. Removes a life, restarts
     * player at the center of the screen, and starts invincibility.
     * @param player The player object
     * @param bullet The bullet that shot the player
     */
    private void handlePlayerBeingShot(Player player, Bullet bullet) {
        objects.remove(bullet);

        // update the life counter
        player.updateLives();
        gameScreen.removeLifeIcon();

        // invincibility time frame
        startInvincibilityTimer();
        playerIsInvincible = true;

        if (player.isDead()) {
            objects.remove(player);
        }
    }

    /**
     * Moves the alien ship across the screen if it exists, or removes it if
     * it's gone too far.
     */
    private void moveOrDespawnAlienShip() {
        // checking the alien ship
        if (alienShip != null && alienShip.isActive()) {
            alienShip.moveAcrossScreen(gc);

            double newX = alienShip.getX() - 1;
            if (newX - (alienShip.getWidth() / 2) >= canvas.getWidth()) {
                alienShip.setActive(false);
                objects.remove(alienShip);
            }
        }
    }

    /**
     * Moves all of the bullets one frame. Player bullets move up and alien
     * bullets move down.
     */
    private void moveBullets() {
        for (int i = objects.size() - 1; i >= 0; i--) {
            Sprite object = objects.get(i);
            if (object instanceof Bullet && !((Bullet) object).getBossShot()) {
                ((Bullet) object).move();
                if (object.getX() < 0 || object.getX() > canvas.getWidth() || object.getY() < 0 || object.getY() > canvas.getHeight()) {
                    objects.remove(object);
                }
            } else if (object instanceof Bullet && ((Bullet) object).getBossShot()) { //bullet from boss
                ((Bullet) object).moveHoming(gc, player);
            }
        }
    }

    /**
     * Starts the boss battle.
     */
    public void startBossBattle() {
        notStarted = true;
        objects.clear();

        drawPlayer();
        drawStaticBarrier();
        drawBossBattle();
    }

    /**
     * Sets up the key input
     */
    private void setupKeypress() {
        pressedKeys = new HashSet<>();

        // user input
        scene.setOnKeyPressed(keyEvent -> {
            pressedKeys.add(keyEvent.getCode());
            handleKeyPress();
        });

        scene.setOnKeyReleased(keyEvent ->
                pressedKeys.remove(keyEvent.getCode())
        );
    }

    /**
     * Handles what each key does on input.
     */
    private void handleKeyPress() {
        if (!isPaused) {
            // handle key presses only if the game is not paused
            if (pressedKeys.contains(KeyCode.ESCAPE)) {
                isPaused = true;
                pauseGame();
                showPausePopup();
            }
            if (pressedKeys.contains(KeyCode.SPACE)) {
                shoot();
            }
            if (pressedKeys.contains(KeyCode.LEFT)) {
                player.moveLeft(gc);
            }
            if (pressedKeys.contains(KeyCode.RIGHT)) {
                player.moveRight(gc);
            }
            // debug shortcuts
            if (pressedKeys.contains(KeyCode.D)) {
                allDead = true;
            }
//            if (pressedKeys.contains(KeyCode.F)) {
//                bossShoot(boss);
//            }
        }
    }

    //------TIMERS AND ANIMATIONS------

    /**
     * Starts timers that run the game in even increments
     */
    private void startTimers() {
        Timer alienShootingTimer = new Timer();
        alienShootingTimer.scheduleAtFixedRate(new RandomAlienShots(), 1000, Utils.generateShotInterval());
        timers.add(alienShootingTimer);

        alienShipTimer = new Timer();
        alienShipTimer.scheduleAtFixedRate(new AlienShipTimer(), 10000, Utils.generateRandomAlienShipDelay());
        timers.add(alienShipTimer);

        Timer alienMovingTimer = new Timer();
        alienMovingTimer.scheduleAtFixedRate(new moveAllAliens(), 500, 1000);
        timers.add(alienMovingTimer);

        Timer bossShootingTimer = new Timer();
        bossShootingTimer.scheduleAtFixedRate(new RandomBossShots(), 1000, 800);
        timers.add(bossShootingTimer);
    }

    /**
     * Class to handle running the random alien shooting.
     */
    private class RandomAlienShots extends TimerTask {
        /**
         * Runs random alien shooting
         */
        @Override
        public void run() {
            // getting the bottom row of aliens (the ones that can shoot)
            ArrayList<Alien> bottomRowAliens = new ArrayList<>();
            for (int i = 0; i < aliens[0].length; i++) {
                for (int j = aliens.length - 1; j >= 0; j--) {
                    if (aliens[j][i] != null) {
                        if (!aliens[j][i].getBoss()) {
                            if (aliens[j][i].stillAlive()) {
                                bottomRowAliens.add(aliens[j][i]);
                                break;
                            }
                        }
                    }
                }
            }

            // randomly getting which aliens will shoot in the next frame
            ArrayList<Alien> aliensToShoot = new ArrayList<>();
            for (Alien object : bottomRowAliens) {
                if (Utils.random.nextDouble() < 0.15) {
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

    /**
     * Class to move all aliens in regular intervals
     */
    private class moveAllAliens extends TimerTask {
        /**
         * Moves all of the aliens one time increment
         */
        @Override
        public void run() {
            for (Sprite object : new ArrayList<>(objects)) {
                if (object instanceof Alien alien) {
                    updateAlienSprites(alien);
                    alien.changeVelocity(alienVelocity, 10);

                    if (coordTrack > (WW/2 + 80)) {
                        alien.moveDown(gc);
                        alienTravelDirection = 'l';
                    }
                    else if (coordTrack < (WW/2 - 65)) {
                        alien.moveDown(gc);
                        alienTravelDirection = 'r';
                    }
                    else if (alienTravelDirection == 'l') alien.moveLeft(gc);
                    else alien.moveRight(gc);

                    if (alien.getY() >= player.getY()) {
                        player.setDead();
                        objects.remove(player);
                    }
                }
            }

            if (alienTravelDirection == 'l') coordTrack -= alienVelocity;
            else coordTrack += alienVelocity;
        }
    }

    /**
     * Class to handle the boss shooting at player
     */
    private class RandomBossShots extends TimerTask {
        /*
         * Makes the boss shooting the player
         */
        @Override
        public void run() {
            ArrayList<Alien> boss = new ArrayList<>();
            for (int i = 0; i < aliens[0].length; i++) {
                for (int j = aliens.length - 1; j >= 0; j--) {
                    if (aliens[j][i] != null) {
                        if (aliens[j][i].getBoss()) {
                            boss.add(aliens[j][i]);
                        }
                    }
                }
            }

            Platform.runLater(() -> {
                for (Alien alien : boss) {
                    if(alien.stillAlive()) {
                        bossShoot(alien);
                    }
                }
            });
        }
    }

    /**
     * Class to handle the alien ship flying across the screen
     */
    private class AlienShipTimer extends TimerTask {
        /**
         * Makes the alien ship fly across the screen
         */
        @Override
        public void run() {
            if (alienShip == null
                    || alienShip.getX() > canvas.getWidth()
                    || !alienShip.isActive()) {
                spawnAlienShip();
                alienShipTimer.scheduleAtFixedRate(
                        new AlienShipTimer(), 30000,
                        Utils.generateRandomAlienShipDelay()
                );
                timers.add(alienShipTimer);
            }
        }
    }

    /**
     * Starts the player invincibility after a player death
     */
    private void startInvincibilityTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                playerIsInvincible = false;
            }
        }, 2000); // invincibility length
        timers.add(timer);
    }

    /**
     * Handles the player death animation
     */
    private void playerDeathAnimation() {
        Image oldImage = player.getImage();
        Image newImage = Utils.readImage("shipDeath.png");
        new AnimationTimer() {
            final long startTime = System.currentTimeMillis();
            @Override
            public void handle(long now) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                player.updateSprite(newImage);

                if (elapsedTime >= 400) {
                    double middleX = canvas.getWidth() / 2 - player.getImage().getWidth() / 2;
                    player.setX(middleX);
                    player.drawFrame(gc);
                    player.updateSprite(oldImage);
                    player.updateAABB();
                    stop();
                }
                drawFrame();
            }
        }.start();
    }
    
    //------DRAWING TO FRAME------

    /**
     * Draws a frame of the game
     */
    public void drawFrame() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Sprite object : objects) {
            object.drawFrame(gc);
        }
    }

    /**
     * Draws the player when the player already exists
     */
    private void drawPlayer() {
        player.setX((canvas.getWidth() / 2) - (player.getImage().getWidth() / 2));
        objects.add(player);
        player.drawFrame(gc);
    }

    /**
     * Draws the player when the player doesn't already exists
     */
    private void drawPlayer(String imageName, int xVelocity, long shootDelay, int health) {
        Image image = Utils.readImage(imageName);
        player = new Player(image, (canvas.getWidth() / 2) - (image.getWidth() / 2), canvas.getHeight() - image.getHeight()-10, xVelocity, shootDelay, health);
        objects.add(player);
        player.drawFrame(gc);
    }

    /**
     * Draws all aliens on the screen
     */
    private void drawAliens() {
        for (int i = 0; i < ALIEN_ROWS; i++) {
            if (i == 0) {
                drawAlienRow(Utils.readImage("alien3-1.png"), 50, 3, 12, -47, 0, i, false);
            } else if (i == 1 || i == 2) {
                drawAlienRow(Utils.readImage("alien2-1.png"), 25, 2, 6, -23, -5, i, false);
            } else {
                drawAlienRow(Utils.readImage("alien1-1.png"), 10, 1, 0, 0, -15, i, false);
            }
        }
    }

    /**
     * Draws one row of aliens on the screen
     */
    private void drawAlienRow(Image image, int scoreAmount, int type, int interval, int shiftX, int shiftY, int i, boolean bossLevel) {
        Image bossImage = Utils.readImage("bossImage.png");

        int spacingX = 18;
        int spacingY = 20;

        double totalWidth = ALIENS_PER_ROW * image.getWidth();
        double startX = (canvas.getWidth() - totalWidth - ALIENS_PER_ROW * spacingX) / 2;

        for (int j = 0; j < ALIENS_PER_ROW; j++) {
            double x = startX + j * (image.getWidth() + spacingX + interval) + shiftX;
            double y = 60 + (i * (image.getHeight() + spacingY)) + shiftY;

            if (bossLevel) {
                if (j <= 2 || j >= ALIENS_PER_ROW - 3) {
                    Alien alien = new Alien(image, (int) x, (int) y, 1, scoreAmount, type);
                    objects.add(alien);
                    aliens[i][j] = alien;
                    alien.drawFrame(gc);
                } else {
                    aliens[i][j] = null;
                }

                if (i == 0 && j == 3) {
                    Alien boss = new Alien(bossImage, (int) x, (int) y, 20, 250, type);
                    boss.iAmBoss();
                    objects.add(boss);
                    aliens[i][j] = boss;
                    boss.drawFrame(gc);
                    notStarted = false;
                }
            } else {
                Alien alien = new Alien(image, (int) x, (int) y, 1, scoreAmount, type);
                objects.add(alien);
                aliens[i][j] = alien;
                alien.drawFrame(gc);
            }
        }
    }

    /**
     * Updates all of the aliens
     */
    private void updateAlienSprites(Sprite object) {
        if (object instanceof Alien alien) {
            Image oldImage = alien.getImage();
            Image newImage = null;

            // different image depending on the type of alien
            if (alien.getType() == 1) {
                newImage = Utils.readImage("alien1-2.png");
            } else if (alien.getType() == 2) {
                newImage = Utils.readImage("alien2-2.png");
            } else if (alien.getType() == 3) {
                newImage = Utils.readImage("alien3-2.png");
            }

            if (!alien.getBoss()) {
                alien.updateSprite(newImage);
                alien.updateAABB();

                new AnimationTimer() {
                    final long startTime = System.currentTimeMillis();

                    @Override
                    public void handle(long now) {
                        long elapsedTime = System.currentTimeMillis() - startTime;

                        if (elapsedTime >= 400) {
                            alien.updateSprite(oldImage);
                            alien.updateAABB();
                            stop();
                        }
                        drawFrame();
                    }
                }.start();
            }
        }
    }

    /**
     * Draws the boss battle screen
     */
    private void drawBossBattle() {
        for (int i = 0; i < ALIEN_ROWS; i++) {
            // update the image and score depending on the alien type and initiate boss battle
            if (i == 0) {
                drawAlienRow(Utils.readImage("alien3-1.png"),
                        50, 3, 12, -47, 0, i, true);

            } else if (i == 1 || i == 2) {
                drawAlienRow(Utils.readImage("alien2-1.png"),
                        25, 2, 6, -23, -5, i, true);
            } else {
                drawAlienRow(Utils.readImage("alien1-1.png"),
                        10, 1, 0, 0, -15, i, true);
            }
        }
    }

    /**
     * Spawns the alien ship at the top of the screen
     */
    private void spawnAlienShip() {
        Image image = Utils.readImage("AlienShip.png");
        alienShip = new AlienShip(image, ((int) -image.getWidth()), 10);
        alienShip.getScore();
        alienShip.setActive(true);
        objects.add(alienShip);
        alienShip.drawFrame(gc);
    }

    /**
     * Draws all of the barriers
     */
    private void drawBarriers() {
        totalBarrier1 = new Barrier(135, 80, canvas, objects, gc);
        totalBarrier1.draw();

        totalBarrier2 = new Barrier(300, 80, canvas, objects, gc);
        totalBarrier2.draw();

        totalBarrier3 = new Barrier(-70, 80, canvas, objects, gc);
        totalBarrier3.draw();

        totalBarrier4 = new Barrier(-235, 80, canvas, objects, gc);
        totalBarrier4.draw();
    }

    /**
     * Draws the static portion of the barriers
     */
    private void drawStaticBarrier() {
        totalBarrier1.staticDraw(objects);
        totalBarrier2.staticDraw(objects);
        totalBarrier3.staticDraw(objects);
        totalBarrier4.staticDraw(objects);

        for (int i = 0; i < objects.size(); i++) {
            Sprite object1 = objects.get(i);
            if (object1 instanceof SubBarrier) {
                if (object1.getImage() == null) {
                    objects.remove(object1);
                } else {
                    object1.drawFrame(gc);
                }
            }
        }
    }

    //------PAUSE SCREEN------

    /**
     * Handles the pause screen popup. Creates a new stage for it
     */
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

        Scene pauseScene = new Scene(pausePane, 550, 500);
        pauseStage.setScene(pauseScene);
        pauseStage.setTitle("Pause");
        pauseStage.initModality(Modality.APPLICATION_MODAL); // cannot touch other window stuff w this
        pauseStage.initOwner(stage); // makes our big stage the 'owner' of pause pop up
        pauseStage.showAndWait();
    }

    /**
     * Handles pausing the game
     */
    private void pauseGame() {
        scene.setOnKeyPressed(null);
        scene.setOnKeyReleased(null);
        isPaused = true;
        for (Timer timer : timers) {
            timer.cancel();
        }
    }

    /**
     * Handles resuming the game
     */
    private void resumeGame() {
        isPaused = false;
        timers.clear();
        startTimers();
        setupKeypress();
    }

    //------GETTERS------
    public Canvas getCanvas() { return canvas; }

    public ArrayList<Timer> getTimers() { return timers; }

    public static Player getPlayer() { return player; }

    public static int getLevelNum() { return levelNum; }

}