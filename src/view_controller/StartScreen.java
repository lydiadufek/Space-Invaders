package view_controller;

import java.util.ArrayList;
import java.util.Timer;

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

public class StartScreen {

    // instance variables
    private Stage stage;
    private Scene scene;

    private GameScreen game;
    private HelpScreen helpPane;

    private GridPane pane;
    private Font font;
    private Label scoreLabel;
    private Hyperlink startLink;
    private Hyperlink helpLink;

    // static constants
    private static final int WW = Window.getWidth();
    private static final int WH = Window.getHeight();

    public StartScreen(Stage stage) {
        this.stage = stage;
        System.out.println("space invaders!!");
        pane = new GridPane();
        helpPane = new HelpScreen(this);

        layoutGUI();
        registerHandlers();

        scene = new Scene(pane, WW, WH);

        stage.setOnCloseRequest(event -> {
        	if (game != null) {
        		ArrayList<Timer> timers = game.getTimers();
        		for (Timer timer: timers) timer.cancel();
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
            game = new GameScreen(stage, this);
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