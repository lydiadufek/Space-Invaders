package view_controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class gameScreen extends Application {
    BorderPane root;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        root = new BorderPane();
        System.out.println("space invaders!!");
        Scene scene = new Scene(root, 500, 700);
        setBackground();

        stage.setScene(scene);
        stage.show();
    }

    private void setBackground() {
        try {
            InputStream inStream = new FileInputStream("lib/game-background.jpg");
            Image image = new Image(inStream);
            BackgroundImage bgImage = new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
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

}
