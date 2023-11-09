package model;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Barrier extends Sprite {
    public Barrier(Image image, double x, double y) {
        super(image, x, y);

        ArrayList<Image> topLeftCorner = new ArrayList<>();
        ArrayList<Image> topRightCorner = new ArrayList<>();
        ArrayList<Image> bottomLeftCorner = new ArrayList<>();
        ArrayList<Image> bottomRightCorner = new ArrayList<>();
        ArrayList<Image> center = new ArrayList<>();
    }
}
