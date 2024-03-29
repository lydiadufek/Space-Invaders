/**
 * Purpose: Transition is the script that runs in between switching levels - it
 * 			shows the level number the game is transitioning to and plays the ufo animation
 * 			before switching scenes
 *
 * Authors: Camila Grubb, Federico Fernandez, Kateyln Rohrer, Lydia Dufek
 */
package view_controller;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.AlienShip;
import model.Utils;

public class Transition {

	private static final int WW = Window.getWidth();
	private static final int WH = Window.getHeight();

	private BorderPane root;
	private Label label;

	private Scene scene;
	private GameScreen home;
	private AlienShip alienShip;

	private Canvas canvas;
	private GraphicsContext gc;

	/**
	 * Transition() is the constructor method for the transition screen between levels
	 */
	public Transition(GameScreen home) {
		this.home = home;
		root = new BorderPane();
		this.scene = new Scene(root, WW, WH);

		canvas = new Canvas(WW, WH);
		gc = canvas.getGraphicsContext2D();

		setBackground();
		setupGUI();
	}

	/**
	 * getScene() is the getter method for the transition scene
	 */
	public Scene getScene() {
		return scene;
	}

	/**
	 * setBackground() reads ijn the background image file and sets it to the scene
	 */
	private void setBackground() {
		Image image = Utils.readImage("game-background.jpg");
		BackgroundImage bgImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
				BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background bg = new Background(bgImage);
		root.setBackground(bg);
	}

	private void setupGUI() {
		// (2 = 0 based indexing + next level)
		label = new Label("Level " + GamePane.getLevelNum() + 2);
		label.setPadding(new Insets(200, 0, 0, 0));
		Font font = Utils.getFont(40);
		label.setFont(font);
		label.setTextFill(Color.WHITE);
		root.setCenter(label);
		root.setBottom(canvas);
	}

	/**
	 * spawnAlienShip() reads in the image file for the alien ship that will fly across
	 * the screen and adds it to the canvas
	 */
	private void spawnAlienShip() {
		Image image = Utils.readImage("AlienShip.png");
		alienShip = new AlienShip(image, ((int) -image.getWidth()), 200);
		alienShip.drawFrame(gc);
		alienShip.changeVelocity(4, 1);
	}

	/**
	 * runTransition() runs the animation sequence of the ship moving across the screen ad
	 * handles the incrementing of the level
	 */
	public void runTransition() {
		spawnAlienShip();
		label.setText("Level " + (GamePane.getLevelNum() + 2));

		Window.changeScene(scene);
		new AnimationTimer() {

			@Override
			public void handle(long currentNanoTime) {
				gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

				alienShip.moveRight(gc);
				alienShip.drawFrame(gc);

				if (alienShip.getX() > WW) {
					this.stop();
					Window.changeScene(home.getScene());
					home.startNextLevel();
				}

			}
		}.start();
	}

}
