package view_controller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.Utils;

public class helpScreen {
    private BorderPane root;
    private Scene scene;
    private startScreen home;
    private Hyperlink backButton;

    private static final int WW = startScreen.getWW();
    private static final int WH = startScreen.getWH();

    public helpScreen(startScreen home) {
        this.home = home;
        root = new BorderPane();
        scene = new Scene(root, WW, WH);

        setBackground();
        setupGUI();
        registerHandlers();
    }

    public Scene getScene() {
        return scene;
    }

    private void setupGUI() {
        Label label = new Label("Move Ship Left - Left Arrow Key\n\nMove Ship Right - Right"
                + " Arrow Key\n\nShoot Bullet - SPACE BAR\n\nPause Game - ESC Key");
        Font font = Utils.getFont(25);
        label.setFont(font);
        label.setTextFill(Color.WHITE);
        root.setCenter(label);

        backButton = new Hyperlink("Back");
        backButton.setFont(font);
        backButton.setTextFill(Color.WHITE);
        backButton.setPadding(new Insets(20));
        root.setBottom(backButton);
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

    private void registerHandlers() {
        backButton.setOnAction(event -> {
            home.getStage().setScene(home.getScene());
            home.getStage().show();
        });
    }
}
