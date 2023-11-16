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

public class StartScreen  {

    private static StartScreen INSTANCE;
    // instance variables
    private static Scene scene;

    private static GameScreen game; // <<<<< trying to remove

    private static GridPane pane;
    private static Font font;
    private static Label scoreLabel;
    private static Hyperlink startLink;
    private static Hyperlink helpLink;

    // static constants
    private static final int WW = Window.getWidth();
    private static final int WH = Window.getHeight();
    private static final HelpScreen helpScreen = HelpScreen.getInstance();


    public static StartScreen getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StartScreen();
        }
        return INSTANCE;
    }
    private StartScreen() {
        StartScreen.pane = new GridPane();
        StartScreen.scene = new Scene(pane, WW, WH);

        layoutGUI();
        registerHandlers();
    }

    public static Scene getScene() {
        return scene;
    }

    private void registerHandlers() {
        startLink.setOnAction(event -> {
            game = GameScreen.getInstance();
            Window.setGame(game);
            Window.changeScene(game.getScene());
        });

        helpLink.setOnAction((event) -> {
            Window.changeScene(helpScreen.getScene());
        });
    }

    private static void layoutGUI() {
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
