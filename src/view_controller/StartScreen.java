/**
 * Authors: Camila Grubb, Federico Fernandez, Kateyln Rohrer, Lydia Dufek
 * Purpose: StartScreen controls the main menu where the player can access the current
 * seralized highscores and start the game by selecting the start button. The points for
 * each alien and the instructions are displayed.
 */
package view_controller;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Timer;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Utils;

public class StartScreen {

	// instance variables
	private Stage stage;
	private Scene scene;

	private GameScreen game;
	private ShipScreen shipScreen;

	private GridPane pane;
	private GridPane scoreTable;
	private Font font;
	private Hyperlink scoreLabel;
	private Hyperlink startLink;
	private String playerImage;

	// static constants
	private static final int WW = Window.getWidth();
	private static final int WH = Window.getHeight();

	public StartScreen(Stage stage) {
		this.stage = stage;
		System.out.println("space invaders!!");
		pane = new GridPane();
		shipScreen = new ShipScreen(this);

		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setHalignment(HPos.CENTER);
		pane.getColumnConstraints().add(columnConstraints);

		layoutGUI();
		registerHandlers();

		scene = new Scene(pane, WW, WH);

		stage.setOnCloseRequest(event -> {
			if (game != null) {
				ArrayList<Timer> timers = game.getTimers();
				for (Timer timer : timers)
					timer.cancel();
			}
		});
	}

	public Scene getScene() {
		return scene;
	}

	public Stage getStage() {
		return stage;
	}

	private void registerHandlers() {
		startLink.setOnAction(event -> {
			stage.setScene(shipScreen.getScene());
			stage.show();
		});

		scoreLabel.setOnAction(event -> {
			GameOver scores = new GameOver(this, 0);
			stage.setScene(scores.getScene());
			stage.show();
		});
	}

	private void layoutGUI() {
		Image image = Utils.readImage("game-background.jpg");
		BackgroundImage bgImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background bg = new Background(bgImage);
		pane.setBackground(bg);

		font = Utils.getFont(40);

		scoreLabel = new Hyperlink();
		setScore(-1);
		scoreLabel.setFont(font);
		scoreLabel.setTextFill(Color.color(0.5, 1, 0.5));
		pane.add(scoreLabel, 0, 1);

		Label nameLabel = new Label("Space Invaders!!");
		nameLabel.setFont(font);
		nameLabel.setTextFill(Color.color(1, 1, 1));
		pane.add(nameLabel, 0, 3);

		startLink = new Hyperlink("Start game");
		startLink.setFont(font);
		startLink.setTextFill(Color.color(0.5, 1, 0.5));
		pane.add(startLink, 0, 5);

		Label instruct = new Label("Use Arrow Keys and Space Bar to Play");
		Label instruct2 = new Label("Press ESC to Pause the Game");
		instruct.setFont(Utils.getFont(35));
		instruct2.setFont(Utils.getFont(25));
		instruct.setTextFill(Color.color(1, 1, 1));
		instruct2.setTextFill(Color.color(1, 1, 1));
		instruct.setAlignment(Pos.CENTER);
		pane.add(instruct, 0, 6);
		pane.add(instruct2, 0, 7);

		// score table
		Font scoreFont = Utils.getFont(30);
		Label scoreNums = new Label("-- Score Advance Table --");
		scoreNums.setTextFill(Color.color(1, 1, 1));
		scoreNums.setFont(scoreFont);
		scoreNums.setAlignment(Pos.CENTER);
		pane.add(scoreNums, 0, 8);

		scoreTable = new GridPane();

		ColumnConstraints scoreConstraints = new ColumnConstraints();
		scoreConstraints.setHalignment(HPos.CENTER);
		scoreTable.getColumnConstraints().add(scoreConstraints);
		scoreTable.setAlignment(Pos.CENTER);

		scoreTable.add(new ImageView(Utils.readImage("AlienShip.png")), 0, 0);
		Label alienPts = new Label(" = ? Mystery");
		alienPts.setTextFill(Color.color(1, 1, 1));
		alienPts.setFont(scoreFont);
		alienPts.setAlignment(Pos.CENTER);
		scoreTable.add(alienPts, 1, 0);

		scoreTable.add(new ImageView(Utils.readImage("alien3-1.png")), 0, 1);
		Label alien3Pts = new Label(" = 30 Points");
		alien3Pts.setTextFill(Color.color(1, 1, 1));
		alien3Pts.setFont(scoreFont);
		alien3Pts.setAlignment(Pos.CENTER);
		scoreTable.add(alien3Pts, 1, 1);

		scoreTable.add(new ImageView(Utils.readImage("alien2-1.png")), 0, 2);
		Label alien2Pts = new Label(" = 20 Points");
		alien2Pts.setTextFill(Color.color(1, 1, 1));
		alien2Pts.setFont(scoreFont);
		alien2Pts.setAlignment(Pos.CENTER);
		scoreTable.add(alien2Pts, 1, 2);

		scoreTable.add(new ImageView(Utils.readImage("alien1-1.png")), 0, 3);
		Label alien1Pts = new Label(" = 10 Points");
		alien1Pts.setTextFill(Color.color(1, 1, 1));
		alien1Pts.setFont(scoreFont);
		alien1Pts.setAlignment(Pos.CENTER);
		scoreTable.add(alien1Pts, 1, 3);

		scoreTable.setVgap(10);

		pane.add(scoreTable, 0, 9);

		pane.setAlignment(Pos.BASELINE_CENTER);
		pane.setVgap(35);
	}

	public void setScore(int score) {
		if (score < 0) {
			try {
				FileInputStream rawBytes = new FileInputStream("scores.ser");
				ObjectInputStream inFile = new ObjectInputStream(rawBytes);
				String[] scores = (String[]) inFile.readObject();
				scoreLabel.setText("High Score: " + scores[0].substring(6));
				inFile.close();
			} catch (Exception e) {
				scoreLabel.setText("High Score: 000");
			}
		} else {
			scoreLabel.setText("High Score: " + score);
		}
	}

	public void setShipImage(String image) {
		playerImage = image;
	}

	public String getShipImage() {
		return playerImage;
	}

	public void startGame() {
		game = new GameScreen(stage, this);
		stage.setScene(game.getScene());
		stage.show();
	}

}
