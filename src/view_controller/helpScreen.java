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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;

public class helpScreen extends Application {
    private BorderPane root;
    private Scene scene;
    private startScreen home;
    private Hyperlink backButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        root = new BorderPane();
        System.out.println("space invaders!!");
        scene = new Scene(root, 500, 700);

        setBackground();
        setupGUI();
        registerHandlers();

        stage.setScene(scene);
        stage.show();
    }
    public helpScreen(startScreen home) {
        this.home = home;
        root = new BorderPane();
        System.out.println("space invaders!!");
        scene = new Scene(root, 500, 700);

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
        Font font = getFont();
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

    private void registerHandlers() {
        backButton.setOnAction(event -> {
            System.out.println("back!");
            home.getStage().setScene(home.getScene());
            home.getStage().show();
        });


    }
}
