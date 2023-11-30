/**
 * Purpose: This file holds the parent class for all of the sprites in the game, which
 *          includes the player, bullet, barriers, aliens, and alienship. The methods
 *          that all sprites inherent are created here
 *
 * Authors: Camila Grubb, Federico Fernandez, Katelyn Rohrer, Lydia Dufek
 */

package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;


public abstract class Sprite {
    protected Image image;
    protected double x, y;
    protected double xVelocity, yVelocity;
    protected double width, height;
    protected Rectangle AABB;
    protected int health;

    /**
     * Creates a sprite object. This stores all basic information used across
     * all sprite instances.
     * @param image
     * @param x
     * @param y
     */
    public Sprite(Image image, double x, double y) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.xVelocity = 1;
        this.yVelocity = 1;
        this.health = 0;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.AABB = new Rectangle(x, y, this.width, this.height);
    }

    /**
     * This is used to get the image the object currently is using
     * @return Image
     */
    public Image getImage() { return image; }

    /**
     * This returns the current x position of the object
     * @return double (position)
     */
    public double getX() { return x; }

    /**
     * This returns the current y position of the object
     * @return double (position)
     */
    public double getY() { return y; }

    /**
     * This returns the width of the object, determined by width of sprite image
     * @return double (width)
     */
    public double getWidth() { return width; }

    /**
     * This returns the height of the object, determined by height of sprite image
     * @return double (height)
     */
    public double getHeight() { return height; }

    /**
     * This creates an AABB which is the bounding box of the object. Used
     * for collision detection.
     * @return Rectangle (borders of the image)
     */
    public Rectangle getAABB() { return AABB; }

    /**
     * Updates the AABB if the sprite changes during gameplay
     */
    public void updateAABB() {
        this.AABB = new Rectangle(x, y, this.width, this.height);
    }

    /**
     * This changes the velocity, x and y, based on the paramaters.
     * @param velocityX
     * @param velocityY
     */
    public void changeVelocity(double velocityX, double velocityY) {
        this.xVelocity = velocityX;
        this.yVelocity = velocityY;
    }

    /**
     * This draws the object onto the gc, the screen.
     * @param gc
     */
    public void drawFrame(GraphicsContext gc) {
        gc.drawImage(image, x, y);
    }

    /**
     * This update is used to update the image if changed during gameplay
     * @param newImage
     */
    public void updateSprite(Image newImage) {
        image = newImage;
    }

    /**
     * This moves the object to the left by a constant rate. Also updates
     * the collider and draws the object onto the screen
     * @param gc
     */
    public void moveLeft(GraphicsContext gc) {
        x -= xVelocity;
        drawFrame(gc);
        updateAABB();
    }

    /**
     * This moves the object to the right by a constant rate. Also updates
     * the collider and draws the object onto the screen
     * @param gc
     */
    public void moveRight(GraphicsContext gc) {
        x += xVelocity;
        drawFrame(gc);
        updateAABB();
    }

    /**
     * This moves the object downward by a constant rate. Also updates
     * the collider and draws the object onto the screen
     * @param gc
     */
    public void moveDown(GraphicsContext gc) {
        y += yVelocity;
        drawFrame(gc);
        updateAABB();
    }

    /**
     * Returns a string value of the object type, Sprite
     * @return String
     */
    @Override
    public String toString() { return "Sprite"; }

}
