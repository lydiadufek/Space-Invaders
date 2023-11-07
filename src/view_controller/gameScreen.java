package view_controller;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Player;
import model.Sprite;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class gameScreen extends Application {
    private BorderPane root;
    private Label scoreNum;
    private BorderPane topBar;
    private VBox livesBox;
    private GamePane gamePane;
    private int MAX_LIVES = 2;
    private Scene scene;
    private boolean isStarted;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        root = new BorderPane();
        scene = new Scene(root, 500, 700);

        setBackground();
        setupTopBar();

        stage.setScene(scene);
        stage.show();
    }

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

        if(isStarted)
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

    private Font getFont() {
        FileInputStream fontInputStream;
        try {
            fontInputStream = new FileInputStream("lib/pixeboy-font.ttf");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return Font.loadFont(fontInputStream, 25);
    }

    private void setupTopBar() {
        topBar = new BorderPane();
        HBox scoreBox = new HBox();
        scoreBox.setSpacing(10);

        livesBox = new VBox();
        livesBox.setSpacing(5);

        Font font = getFont();

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

        setLivesDisplay(MAX_LIVES);

        root.setTop(topBar);
    }

    public void updateScore(int score) {
        int originalScore = Integer.parseInt(scoreNum.getText());
        scoreNum.setText(String.valueOf(originalScore + score));
    }

    private void setLivesDisplay(int num) {
        //TODO: make method that updates the save lives
        ArrayList<HBox> paneList = new ArrayList<>();
        for (int i = 0; i < MAX_LIVES; i += 4) {
            HBox row = new HBox();
            row.setSpacing(5);
            paneList.add(row);
            // lining up second row of ships
            //TODO on ships <3, the score label shifts. want it to be stationary
            if (i != 0) {
                row.getChildren().add(new Label("\t\t\t"));
            }
            livesBox.getChildren().add(row);
        }
        ArrayList<ImageView> extraShips = new ArrayList<>();

        Font font = getFont();

        Label livesLabel = new Label("LIVES ");
        //TODO: KATIE FIX IT
        // label.setLayoutX(100); // Set the X coordinate
        // label.setLayoutY(50);  // Set the Y coordinate
        livesLabel.setFont(font);
        livesLabel.setTextFill(Color.WHITE);

        // Thinking I might remove this line and add the label some other way
        paneList.get(0).getChildren().add(livesLabel);

        FileInputStream shipImagePath;
        try {
            shipImagePath = new FileInputStream("lib/heart2.png");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Image shipImageObj = new Image(shipImagePath);
        for (int i = 0; i < num; i++) {
            extraShips.add(new ImageView(shipImageObj));
        }

        for (int i = 0; i < extraShips.size(); i++) {
            paneList.get(i/4).getChildren().add(extraShips.get(i));
        }

        topBar.setRight(livesBox);
    }

    private void setupGameScreen() {
//        gamePane = new GamePane();
//        root.setCenter(gamePane.getCanvas());
        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.SPACE) {
                gamePane.shoot();
            } else if (keyEvent.getCode() == KeyCode.LEFT) {
                gamePane.moveLeft();
            } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                gamePane.moveRight();
            }
        });
    }
}