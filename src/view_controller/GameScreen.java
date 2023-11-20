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

    public void newLevel() {
        transitionScreen.runTransition();
    }

    public void startNextLevel() {
        gamePane = new GamePane();
        root.setCenter(gamePane.getCanvas());
        gamePane.gameLoop();
    }

    public Scene getScene() {
        return scene;
    }

    private void setBackground() {
        Image image = Utils.readImage("game-background.jpg");
        BackgroundImage bgImage = new BackgroundImage(image,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background bg = new Background(bgImage);
        root.setBackground(bg);
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

        Image heartObj = Utils.readImage("heart2.png");
        for (int i = 0; i < MAX_LIVES; i++) {
            livesBox.getChildren().add(new ImageView(heartObj));
        }
        for (int i = starting_lives; i <= MAX_LIVES; i++) {
            livesBox.getChildren().get(i).setVisible(false);
        }
        topBar.setRight(livesBox);
    }

    public void updateScore(int score) {
        int originalScore = Integer.parseInt(scoreNum.getText());
        scoreNum.setText(String.valueOf(originalScore + score));
    }
    
    public int getScore() {
    	return Integer.parseInt(scoreNum.getText());
    }
    
    public ArrayList<Timer> getTimers() {
    	return gamePane.getTimers();
    }

    protected void removeLifeIcon() {
        livesBox.getChildren().get(currentLives-1).setVisible(false);
        currentLives--;
    }

    protected void addLifeIcon() {
        livesBox.getChildren().get(currentLives-1).setVisible(true);
        currentLives++;
    }

}