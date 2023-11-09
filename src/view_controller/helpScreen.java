package view_controller;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;

public class helpScreen extends Application {
    private BorderPane root;
    private Scene scene;
    private startScreen home;
    private Hyperlink backButton;

    private final int WW = startScreen.getWW();
    private final int WH = startScreen.getWH();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        root = new BorderPane();
        scene = new Scene(root, WW, WH);

        setBackground();
        setupGUI();
        registerHandlers();

        stage.setScene(scene);
        stage.show();
    }
    public helpScreen(startScreen home) {
        this.home = home;
        root = new BorderPane();
//        System.out.println("space invaders!!");
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
