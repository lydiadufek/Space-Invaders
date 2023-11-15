package view_controller;

import java.util.ArrayList;
import java.util.Timer;

import javafx.application.Application;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Utils;

public class startScreen extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private GridPane pane;
    private Font font;
    private Hyperlink startLink;
    private Hyperlink helpLink;
    private Label scoreLabel;
    private Scene scene;
    private gameScreen game;
    private helpScreen helpPane;
    private Stage stage;

    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 700;

    @Override
    public void start(Stage stage) {
        System.out.println("space invaders!!");
        pane = new GridPane();
        helpPane = new helpScreen(this);

        layoutGUI();
        registerHandlers();

        scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
        this.stage = stage;
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> {
        	if (game != null) {
        		ArrayList<Timer> timers = game.getTimers();
        		for (Timer timer: timers) timer.cancel();
        	}
        });
    }

    public static int getWW() {
        return WINDOW_WIDTH;
    }
    public static int getWH() {
        return WINDOW_HEIGHT;
    }

    public Scene getScene() {
        return scene;
    }

    public Stage getStage() {
        return stage;
    }

    private void registerHandlers() {
        startLink.setOnAction(event -> {
            game = new gameScreen(stage, this);
            stage.setScene(game.getScene());
            stage.show();
        });

        helpLink.setOnAction((event) -> {
            stage.setScene(helpPane.getScene());
            stage.show();
        });
    }

    private void layoutGUI() {
        Image image = Utils.readImage("game-background.jpg");
        BackgroundImage bgImage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background bg = new Background(bgImage);
        pane.setBackground(bg);

        font = Utils.getFont(50);

        scoreLabel = new Label("High Score: 000"); //Temporary
        scoreLabel.setFont(font);
        scoreLabel.setTextFill(Color.color(1, 1, 1));
        pane.add(scoreLabel, 0, 1);

        Label nameLabel = new Label("Space Invaders!!");
        nameLabel.setFont(font);
        nameLabel.setTextFill(Color.color(1, 0, 1));
        pane.add(nameLabel, 0, 3);

        startLink = new Hyperlink("Start game");
        startLink.setFont(font);
        startLink.setTextFill(Color.color(0, 0, 1));
        pane.add(startLink, 0, 5);

        helpLink = new Hyperlink("How to Play");
        helpLink.setFont(font);
        helpLink.setTextFill(Color.color(1, 0, 0));
        pane.add(helpLink, 0, 6);

        pane.add(new ImageView(Utils.readImage("ship.png")), 0, 8);
        pane.setAlignment(Pos.BASELINE_CENTER);
        pane.setVgap(50);
    }
}
