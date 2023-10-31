package view_controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GamePane {
    private Canvas canvas;
    private GraphicsContext gc;

    public GamePane() {
        canvas = new Canvas(500, 650);
        gc = canvas.getGraphicsContext2D();
        initialSetup();
    }


    public Canvas getCanvas() {
        return canvas;
    }


    private void initialSetup() {
        canvas.setOnMousePressed(mouseEvent -> {
            System.out.println("clicked!");
        });
        gc.setFill(Color.RED);
        gc.strokeRect(0, 0, 500, 630);
        gc.fill();
    }


}
