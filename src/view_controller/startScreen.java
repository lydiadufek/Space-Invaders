package view_controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class startScreen extends Application {
	
	private Button instruct;
	private BorderPane pane = new BorderPane();
	
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        System.out.println("space invaders!!");
        Scene scene = new Scene(pane, 700, 500);
        
        instruct = new Button("Instructions");
        pane.setTop(instruct);
        
        registerHandlers();

        stage.setScene(scene);
        stage.show();
    }
    
    
    
    private void registerHandlers() {
  	  instruct.setOnMousePressed((event) -> {
  			Alert alert = new Alert(AlertType.CONFIRMATION);
  			Image image = null;
  			try {
  	            InputStream inStream = new FileInputStream("lib/game-background.jpg");
  	            image = new Image(inStream, 480, 350, true, false);
  	        } catch (FileNotFoundException e) {
  	            System.out.println("Background image not found.");
  	        }
  			ImageView imageView = new ImageView(image);  			
  			Label label = new Label("Move Ship Left - Left Arrow Key\n\nMove Ship Right - Right"
  					+ " Arrow Key\n\nShoot Bullet - SPACE BAR\n\nPause Game - ESC Key");
  			Font font = getFont();
  			label.setFont(font);
  		    label.setTextFill(Color.web("#FFFFFF"));
  			StackPane alertContext = new StackPane(imageView, label);
  			alert.setGraphic(alertContext);
  			alert.getDialogPane().setMaxWidth(0);
  			alert.setHeaderText("S.I.");
    		Optional<ButtonType> result = alert.showAndWait();
    		 if (result.get() == ButtonType.OK) {
     			  return;
     		  } else {
     			  return;
     		  }
  	  	});
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
    
}
