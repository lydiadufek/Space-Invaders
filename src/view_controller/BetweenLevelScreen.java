package view_controller;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Utils;

public class BetweenLevelScreen {

    private static BetweenLevelScreen INSTANCE;
    private static final Stage stage = Window.getStage();
    private static final GameScreen gameScreen = GameScreen.getInstance();;
    private static final int WW = Window.getWidth();
    private static final int WH = Window.getHeight();

    private BorderPane root;
    private Scene scene;

    public static BetweenLevelScreen getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BetweenLevelScreen();
        }
        return INSTANCE;
    }

    private BetweenLevelScreen() {
        root = new BorderPane();
        scene = new Scene(root, WW, WH);

        setBackground();
        setupGUI();
    }

    public void runTransition() {
        Window.changeScene(scene);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Window.changeScene(gameScreen.getScene());
    }

    public Scene getScene() {
        return scene;
    }

    private void setupGUI() {
        Label label = new Label("Level " + GamePane.getLevelNum());
        Font font = Utils.getFont(25);
        label.setFont(font);
        label.setTextFill(Color.WHITE);
        root.setCenter(label);
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

}
