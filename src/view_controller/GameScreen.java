/**
 * Purpose: GameScreen displays the player, alien, and barriers. The animations
 * 			of the items are controlled by GamePane.
 *
 * Authors: Camila Grubb, Federico Fernandez, Kateyln Rohrer, Lydia Dufek
 */
package view_controller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Utils;

import java.util.ArrayList;
import java.util.Timer;

public class GameScreen {

	// static variables
	private static final int WW = Window.getWidth();
	private static final int WH = Window.getHeight();
	private static Transition transitionScreen;

	// instance variables
	private BorderPane root;
	private BorderPane topBar;

	private Label scoreNum;
	private HBox livesBox;

	private GamePane gamePane;
	private Scene scene;

	private int currentLives;

	// constants
	private final int MAX_LIVES = 5;
	private int starting_lives;

	/**
	 * GameScreen() constructor method creates setup for the playing menu
	 */
	public GameScreen(Stage stage, StartScreen home) {

		root = new BorderPane();
		scene = new Scene(root, WW, WH);
		transitionScreen = new Transition(this);

		gamePane = new GamePane(stage, scene, home, this);
		starting_lives = GamePane.getPlayer().getLives();

		setBackground();
		setupTopBar();

		root.setCenter(gamePane.getCanvas());

		currentLives = starting_lives;

		gamePane.gameLoop();
	}

	/**
	 * newLevel() runs the transition animation screen for a new level
	 * calling the transitionScreen method
	 */
	public void newLevel() {
		transitionScreen.runTransition();
	}

	/**
	 * startNextLevel() opens a new GamePane with new aliens and level
	 * parameters - essentially starts the next level
	 */
	public void startNextLevel() {
		gamePane = new GamePane();
		root.setCenter(gamePane.getCanvas());
		gamePane.gameLoop();
	}

	/**
	 * getScene() getter for the GameScreen scene
	 */
	public Scene getScene() {
		return scene;
	}

	/**
	 * setBackground() reads in the background image file and sets it to the pane
	 */
	private void setBackground() {
		Image image = Utils.readImage("game-background.jpg");
		BackgroundImage bgImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
				BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background bg = new Background(bgImage);
		root.setBackground(bg);
	}

	/*
	 * setupTopBar() sets up the GUI look of the score at the top of the screen
	 */
	private void setupTopBar() {
		topBar = new BorderPane();
		HBox scoreBox = new HBox();
		scoreBox.setSpacing(10);

		livesBox = new HBox();
		livesBox.setSpacing(5);

		Font font = Utils.getFont(25);

		// initialize score label
		Label scoreLabel = new Label("  SCORE");
		scoreLabel.setFont(font);
		scoreLabel.setTextFill(Color.WHITE);

		// initialize score number label
		scoreNum = new Label("0");
		scoreNum.setFont(font);
		scoreNum.setTextFill(Color.LIMEGREEN);

		scoreBox.getChildren().addAll(scoreLabel, scoreNum);
		topBar.setLeft(scoreBox);
		topBar.setPadding(new Insets(10, 10, 10, 10));

		setLivesDisplay();

		root.setTop(topBar);
	}

	/**
	 * setLivesDisplay() sets up the GUI look of the lives at the top of the screen
	 * plus the number of heart images representing the current life status
	 */
	private void setLivesDisplay() {
		Font font = Utils.getFont(25);
		Label livesLabel = new Label("LIVES ");
		livesLabel.setFont(font);
		livesLabel.setTextFill(Color.WHITE);
		livesBox.getChildren().add(livesLabel);

		Image heartObj = Utils.readImage("heart2.png");
		for (int i = 0; i < MAX_LIVES; i++) {
			livesBox.getChildren().add(new ImageView(heartObj));
		}
		for (int i = starting_lives + 1; i <= MAX_LIVES; i++) {
			livesBox.getChildren().get(i).setVisible(false);
		}
		topBar.setRight(livesBox);
	}

	/**
	 * updateScore() sets the score label to the numeric counter of the score value
	 */
	public void updateScore(int score) {
		int originalScore = Integer.parseInt(scoreNum.getText());
		scoreNum.setText(String.valueOf(originalScore + score));
	}

	/**
	 * getScore() is a getter for the current score
	 */
	public int getScore() {
		return Integer.parseInt(scoreNum.getText());
	}

	/**
	 * getTimers() is a getter for the gameloop timers that keep the game running until
	 * interrupted
	 */
	public ArrayList<Timer> getTimers() {
		return gamePane.getTimers();
	}

	/**
	 * removeLifeIcon() handles when the player dies, loses a life from their life count
	 * and updates the image
	 */
	protected void removeLifeIcon() {
		livesBox.getChildren().get(currentLives).setVisible(false);
		currentLives--;
	}

	/**
	 * addLifeIcon() handles when the player gets an extra life 
	 */
	protected void addLifeIcon() {
		livesBox.getChildren().get(currentLives).setVisible(true);
		currentLives++;
	}

}