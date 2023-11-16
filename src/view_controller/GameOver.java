package view_controller;

import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Utils;

public class GameOver {
    private static final Stage stage = Window.getStage();

    private static BorderPane root;
    private static Scene scene;
    private static StartScreen home;
    private static Hyperlink gameOver;

    private static final int WW = Window.getWidth();
    private static final int WH = Window.getHeight();
    
    public GameOver(StartScreen home) {
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
    	gameOver = new Hyperlink("GAME OVER");
    	Font font = Utils.getFont(100);
        gameOver.setFont(font);
        gameOver.setTextFill(Color.WHITE);
        root.setCenter(gameOver);
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
        gameOver.setOnAction(event -> {
            Window.changeScene(home.getScene());
//            home.getStage().setScene(home.getScene());
//            home.getStage().show();
        });
    }
}
