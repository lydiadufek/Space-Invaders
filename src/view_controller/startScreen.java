package view_controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLOutput;
import java.util.Optional;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class startScreen extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private GridPane pane;
    private Font font;
    private Hyperlink start;
    private Hyperlink help;
    private Label score;
    private Scene scene;
    private gameScreen game;
    private helpScreen helpPane;
    private Stage stage;

    @Override
    public void start(Stage stage) {
        System.out.println("space invaders!!");
        pane = new GridPane();
        game = new gameScreen();
        helpPane = new helpScreen(this);

        layoutGUI();
        registerHandlers();

        scene = new Scene(pane, 500, 700);
        this.stage = stage;
        stage.setScene(scene);
        stage.show();
    }

    public Scene getScene() {
        return scene;
    }

    public Stage getStage() {
        return stage;
    }

    private void registerHandlers() {
        start.setOnAction(event -> {
//            System.out.println("Started!");
            stage.setScene(game.getScene());
            stage.show();
        });

        help.setOnAction((event) -> {
//            System.out.println("help screen!");
            stage.setScene(helpPane.getScene());
            stage.show();
        });
    }

    private void layoutGUI() {
        try {
            InputStream inStream = new FileInputStream("lib/game-background.jpg");
            Image image = new Image(inStream);
            BackgroundImage bgImage = new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            Background bg = new Background(bgImage);
            pane.setBackground(bg);
        } catch (FileNotFoundException e) {
            System.out.println("Background image not found. Setting to default black.");
            Paint c = Color.BLACK;
            Background bg = new Background(new BackgroundFill(c, null, null));
            pane.setBackground(bg);
        }
        font = getFont();

        start = new Hyperlink("Start game");
        start.setFont(font);
        start.setTextFill(Color.color(0, 0, 1));
        help = new Hyperlink("How to Play");
        help.setFont(font);
        help.setTextFill(Color.color(1, 0, 0));

        score = new Label("High Score: 000"); //Temporary
        score.setFont(font);
        score.setTextFill(Color.color(1, 1, 1));
        pane.add(score, 0, 1);
        Label label = new Label("Space Invaders!!");
        label.setFont(font);
        label.setTextFill(Color.color(1, 0, 1));
        pane.add(label, 0, 3);
        pane.add(start, 0, 5);
        pane.add(help, 0, 6);
        pane.add(new ImageView(getImage()), 0, 8);
        pane.setAlignment(Pos.BASELINE_CENTER);
        pane.setVgap(50);
    }

    private Font getFont() {
        FileInputStream fontInputStream;
        try {
            fontInputStream = new FileInputStream("lib/pixeboy-font.ttf");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return Font.loadFont(fontInputStream, 50);
    }

    private Image getImage() {
        try {
            Image img = new Image(new FileInputStream("lib/ship.png"), 20, 40, true, false);
            return img;
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
