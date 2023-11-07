package view_controller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import model.Player;
import model.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;

public class gameScreen {
    private BorderPane root;
    private Label scoreNum;
    private BorderPane topBar;
    private HBox livesBox;
    private GamePane gamePane;
    private int MAX_LIVES = 2;
    private int currentLives;
    private Scene scene;
    private boolean isStarted;

    public gameScreen(boolean isStarted) {
        this.isStarted = isStarted;
        root = new BorderPane();
        scene = new Scene(root, 500, 700);

        setBackground();
        setupTopBar();

        gamePane = new GamePane(scene, this);
        root.setCenter(gamePane.getCanvas());

        Canvas canvas = gamePane.getCanvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Player player = gamePane.getPlayer();
        currentLives = MAX_LIVES;

        if (isStarted)
            gamePane.gameLoop();
    }

    public Scene getScene() {
        return scene;
    }

    private void setBackground() {
        try {
            InputStream inStream = new FileInputStream("lib/game-background.jpg");
            Image image = new Image(inStream);
            BackgroundImage bgImage = new BackgroundImage(image,
                    BackgroundRepeat.REPEAT,
                    BackgroundRepeat.REPEAT,
                    BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            Background bg = new Background(bgImage);
            root.setBackground(bg);
        } catch (FileNotFoundException e) {
            System.out.println("Background image not found. Setting to default black.");
            Paint c = Color.BLACK;
            Background bg = new Background(new BackgroundFill(c, null, null));
            root.setBackground(bg);
        }
    }

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

    private void setLivesDisplay() {
        Font font = Utils.getFont(25);
        Label livesLabel = new Label("LIVES ");
        livesLabel.setFont(font);
        livesLabel.setTextFill(Color.WHITE);
        livesBox.getChildren().add(livesLabel);

        FileInputStream shipImagePath;
        try {
            shipImagePath = new FileInputStream("lib/heart2.png");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Image shipImageObj = new Image(shipImagePath);
        for (int i = 0; i <= MAX_LIVES; i++) {
            livesBox.getChildren().add(new ImageView(shipImageObj));
        }
        livesBox.getChildren().get(MAX_LIVES+1).setVisible(false);
        topBar.setRight(livesBox);
    }

    public void updateScore(int score) {
        int originalScore = Integer.parseInt(scoreNum.getText());
        scoreNum.setText(String.valueOf(originalScore + score));
    }
    
    public ArrayList<Timer> getTimers() {
    	return gamePane.getTimers();
    }

    protected void removeLifeIcon() {
        livesBox.getChildren().get(currentLives+1).setVisible(false);
        currentLives--;
    }

}