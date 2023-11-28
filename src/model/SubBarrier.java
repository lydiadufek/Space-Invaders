package model;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class SubBarrier extends Sprite {

    private int playerHealth;
    private int alienHealth;
    private Image[] playerDamageImages;
    private Image[] alienDamageImages;

    public SubBarrier(Image[] playerDamageImages, double x, double y) {
        super(playerDamageImages[0], x, y);
        this.playerDamageImages = playerDamageImages;
        this.playerHealth = 0;
        this.alienHealth = 0;
    }

    public void receiveDamagePlayer() {
        if (playerHealth < 4 ) {
            playerHealth++;
        }
    }

    public void receiveDamageAlien() {
        if (playerHealth < 4 ) {
            playerHealth++;
        }
    }

    public Image[] getPlayerDamageImages() {
        return playerDamageImages;
    }

    public Image[] getAlienDamageImages() {
        return alienDamageImages;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }
    public int getAlienHealth() {
        return alienHealth;
    }

    public void setPosition(double x, double y) {
        System.out.println(x + " " + y);
        this.x = x;
        this.y = y;
//        updateAABB();
    }

    @Override
    public String toString() {
        return "SubBarrier";
    }
}

