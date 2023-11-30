/**
 * Authors: Camila Grubb, Federico Fernandez, Kateyln Rohrer, Lydia Dufek
 * Purpose: GameOver is the screen where players insert the three letter names for
 * the top 5 highest scores (serialized).
 */
package view_controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.Utils;

public class GameOver {
	private GridPane root;
	private Scene scene;
	private StartScreen home;
	private Label gameOver;
	private int score;
	private String[] scores;
	private Text newText;
	private int scoreIndex;
	private FadeTransition fadeTransition;
	private Hyperlink backButton;

	private static final int WW = Window.getWidth();
	private static final int WH = Window.getHeight();

	public GameOver(StartScreen home, int score) {
		this.home = home;
		this.score = score;
		root = new GridPane();
		scene = new Scene(root, WW, WH);

		try {
			FileInputStream rawBytes = new FileInputStream("scores.ser");
			ObjectInputStream inFile = new ObjectInputStream(rawBytes);
			scores = (String[]) inFile.readObject();
			inFile.close();
		} catch (Exception e) {
			scores = new String[5];
			scores[0] = "LYD : 500";
			scores[1] = "KAT : 300";
			scores[2] = "CAM : 250";
			scores[3] = "FED : 100";
			scores[4] = "RIC : 005";
		}

		setBackground();
		setupGUI();
		registerHandlers();

	}

	public Scene getScene() {
		return scene;
	}

	private void setupGUI() {
		gameOver = new Label("HIGH SCORES");
		Font font = Utils.getFont(100);
		gameOver.setFont(font);
		gameOver.setTextFill(Color.color(0.5, 1, 0.5));
		root.add(gameOver, 0, 1);
		font = Utils.getFont(60);
		setScores();
		newText = new Text();

		for (int i = 0; i < scores.length; i++) {
			Text label = new Text(scores[i]);
			label.setFont(font);
			label.setFill(Color.WHITE);
			root.add(label, 0, i + 4);
			if (i == scoreIndex)
				newText = label;
		}

		backButton = new Hyperlink("Home");
		backButton.setFont(font);
		backButton.setTextFill(Color.color(0.5, 1, 0.5));
		root.add(backButton, 0, 9);
		root.setAlignment(Pos.BASELINE_CENTER);
		root.setVgap(35);
	}

	private void setScores() {
		int i = 0;
		while (i < 5 && score <= Integer.parseInt(scores[i].substring(6)))
			i++;
		scoreIndex = i;
		if (i == 5)
			return;
		String newScore = "_AA : " + score;
		while (i < 5) {
			String tmpString = scores[i];
			scores[i] = newScore;
			newScore = tmpString;
			i++;
		}
	}

	private void setBackground() {
		Image image = Utils.readImage("game-background.jpg");
		BackgroundImage bgImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
				BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background bg = new Background(bgImage);
		root.setBackground(bg);
	}

	private void registerHandlers() {
		newText.requestFocus();
		fadeTransition = new FadeTransition(Duration.seconds(0.9), newText);
		fadeTransition.setCycleCount(Animation.INDEFINITE);
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0.0);
		fadeTransition.play();
		newText.setOnKeyPressed(event -> {
			String code = event.getCode().getName();
			if (code.length() == 1 && Character.isAlphabetic(code.charAt(0))) {
				int i = 0;
				while (newText.getText().charAt(i) != '_' && i < 3)
					i++;
				if (i == 0) {
					String str = event.getText() + "_" + newText.getText().substring(2);
					newText.setText(str);
				} else if (i == 1) {
					String str = newText.getText().substring(0, 1) + event.getText() + "_"
							+ newText.getText().substring(3);
					newText.setText(str);
				} else if (i == 2) {
					String str = newText.getText().substring(0, 2) + event.getText() + newText.getText().substring(3);
					newText.setText(str);
				}
			}
		});

		backButton.setOnAction(event -> {
			fadeTransition.stop();
			newText.setText(newText.getText().replace('_', 'A'));
			if (scoreIndex < 5)
				scores[scoreIndex] = newText.getText();
			try {
				FileOutputStream bytesToDisk = new FileOutputStream("scores.ser");
				ObjectOutputStream outFile = new ObjectOutputStream(bytesToDisk);
				outFile.writeObject(scores);
				outFile.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			home.setScore(Integer.parseInt(scores[0].substring(6)));
			home.getStage().setScene(home.getScene());
			home.getStage().show();
		});
	}
}
