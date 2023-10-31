package view_controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class startScreen extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        System.out.println("space invaders!!");
        Scene scene = new Scene(new BorderPane(), 700, 500);

        stage.setScene(scene);
        stage.show();
    }
}
