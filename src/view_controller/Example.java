package view_controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import view_controller.GamePane;

public class Example extends Application {

    private double x = 50;
    private double y = 50;
    private double velocityX = 2;
    private double velocityY = 2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game Loop Example");

        Pane root = new Pane();
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);

        Canvas canvas = new Canvas(400, 400);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Game character's position
        x = 50;
        y = 50;
        velocityX = 5;
        velocityY = 5;

        new AnimationTimer() {
            long lastNanoTime = System.nanoTime();

            @Override
            public void handle(long currentNanoTime) {
                double elapsedTime = (currentNanoTime - lastNanoTime) / 1e9; // Convert nanoseconds to seconds

                // Clear the canvas
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                // Update game logic here
                scene.setOnKeyPressed(keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.SPACE) {
                        System.out.println("shoot");;
                    } else if (keyEvent.getCode() == KeyCode.LEFT) {
                        x -= velocityX;
                    } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                        x += velocityX;
                    }
                });

                // Draw the game character
                gc.setFill(Color.BLUE);
                gc.fillRect(x, y, 20, 20);

                lastNanoTime = currentNanoTime;
            }
        }.start();

        primaryStage.show();
    }
}
