/*
 * Purpose: Window.java sets up the stage and window size. The start screen is called
 *          and displayed - essentially the Space Invaders launchpad.
 *
 * Authors: Camila Grubb, Federico Fernandez, Katelyn Rohrer, Lydia Dufek
 */
package view_controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Timer;

public class Window extends Application {
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 700;
    private static Stage stage;

    private static GameScreen game;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Window.stage = stage;

        StartScreen StartScreen = new StartScreen(stage);
        Window.stage.setScene(StartScreen.getScene());
        Window.stage.show();
    }

    public static void setGame(GameScreen game) {
        Window.game = game;
        Window.stage.setOnCloseRequest(event -> {
            if (Window.game != null) {
                ArrayList<Timer> timers = Window.game.getTimers();
                for (Timer timer: timers) timer.cancel();
            }
        });
    }

    public static GameScreen getGame() {
        return game;
    }

    public static int getWidth() {
        return WINDOW_WIDTH;
    }

    public static int getHeight() {
        return WINDOW_HEIGHT;
    }

    public static Stage getStage() {
        return stage;
    }

    public synchronized static void changeScene(Scene scene) {
        stage.setScene(scene);
        stage.show();
    }

}
