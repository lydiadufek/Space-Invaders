package view_controller;

import java.util.ArrayList;
import java.util.Timer;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.Player;
import model.Utils;

public class ShipScreen {
    private Scene scene;
    private StartScreen home;
    private GameScreen game;
    
    private Hyperlink defaultLink;
    private Hyperlink retroLink;
    private Hyperlink otherLink;
    private Hyperlink otherLink2;
    
    private GridPane pane;
    private BorderPane root;

    private static final int WW = Window.getWidth();
    private static final int WH = Window.getHeight();

    private Player player;

    public ShipScreen(StartScreen home) {
        this.home = home;
        pane = new GridPane();
        root = new BorderPane();
        
        scene = new Scene(root, WW, WH);
        
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.CENTER);
        pane.getColumnConstraints().add(columnConstraints);
        
        setBackground();
        setupGUI();
        registerHandlers();
        
    }

    public Scene getScene() {
        return scene;
    }

    private void setupGUI() {
        Label label = new Label("Select a Ship!");
        Font font = Utils.getFont(30);
        label.setFont(font);
        label.setTextFill(Color.WHITE);
        label.setAlignment(Pos.CENTER);
        pane.add(label, 1, 0);
        
        ImageView defaultShip = new ImageView(Utils.readImage("purpleShip.png"));
        defaultLink = new Hyperlink();
        defaultLink.setGraphic(defaultShip);
        
        pane.add(defaultLink, 0, 1);
        Label defaultLabel = new Label("Default Ship: 3 Lives");
        defaultLabel.setTextFill(Color.color(1, 1, 1));
        defaultLabel.setFont(font);
        defaultLabel.setAlignment(Pos.CENTER);
        pane.add(defaultLabel, 1, 1);
        
        ImageView retroShip = new ImageView(Utils.readImage("greenShip.png"));
        retroLink = new Hyperlink();
        retroLink.setGraphic(retroShip);
        
        pane.add(retroLink, 0, 2);
        Label retroLabel = new Label("Original Ship: Slower Speed");
        retroLabel.setTextFill(Color.color(1, 1, 1));
        retroLabel.setFont(font);
        retroLabel.setAlignment(Pos.CENTER);
        pane.add(retroLabel, 1, 2);
        
        ImageView otherShip = new ImageView(Utils.readImage("blueShip.png"));
        otherLink = new Hyperlink();
        otherLink.setGraphic(otherShip);
        
        pane.add(otherLink, 0, 3);
        Label otherLabel = new Label("Other Ship 1: New Attributes");
        otherLabel.setTextFill(Color.color(1, 1, 1));
        otherLabel.setFont(font);
        otherLabel.setAlignment(Pos.CENTER);
        pane.add(otherLabel, 1, 3);
        
        ImageView otherShip2 = new ImageView(Utils.readImage("redShip.png"));
        otherLink2 = new Hyperlink();
        otherLink2.setGraphic(otherShip2);
        
        pane.add(otherLink2, 0, 4);
        Label otherLabel2 = new Label("Other Ship 2: New Attributes");
        otherLabel2.setTextFill(Color.color(1, 1, 1));
        otherLabel2.setFont(font);
        otherLabel2.setAlignment(Pos.CENTER);
        pane.add(otherLabel2, 1, 4);
        
        pane.setHgap(10);
        pane.setVgap(25);
        pane.setAlignment(Pos.CENTER);
        root.setCenter(pane);
    }

    private void setBackground() {
        Image image = Utils.readImage("game-background.jpg");
        BackgroundImage bgImage = new BackgroundImage(image,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background bg = new Background(bgImage);
        pane.setBackground(bg);
    }

    private void registerHandlers() {
        defaultLink.setOnAction(event -> {
        	home.setShipImage("purpleShip.png");
            home.startGame();
        });
        retroLink.setOnAction(event -> {
        	home.setShipImage("greenShip.png");
        	home.startGame();
        });
        otherLink.setOnAction(event -> {
        	home.setShipImage("redShip.png");
        	home.startGame();
        });
        otherLink2.setOnAction(event -> {
        	home.setShipImage("blueShip.png");
        	home.startGame();
        });
    }
}
