import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Example extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create two circles
        Circle sourceCircle = new Circle(50, Color.BLUE);
        Circle targetCircle = new Circle(50, Color.RED);

        // Set initial positions
        sourceCircle.setTranslateX(50);
        sourceCircle.setTranslateY(150);
        targetCircle.setTranslateX(300);
        targetCircle.setTranslateY(300);

        // Create a TranslateTransition for the source circle
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), sourceCircle);
        translateTransition.setToX(targetCircle.getTranslateX());
        translateTransition.setToY(targetCircle.getTranslateY());

        // Set up the Pane
        Pane root = new Pane();
        root.getChildren().addAll(sourceCircle, targetCircle);

        // Set up the Scene
        Scene scene = new Scene(root, 400, 600);

        // Set up the Stage
        primaryStage.setTitle("Move Object Example");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Play the translation animation when the application starts
        translateTransition.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
