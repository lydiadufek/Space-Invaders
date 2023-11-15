package model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.ArrayList;

import static model.Utils.readImage;

public class Barrier {
    private Canvas canvas;
    private ArrayList<Sprite> objects;
    private GraphicsContext gc;
    private double x;
    private double y;

    public Barrier(double x, double y, Canvas canvas, ArrayList<Sprite> objects, GraphicsContext gc) {
        this.x = x;
        this.y = y;

        this.canvas = canvas;
        this.objects = objects;
        this.gc = gc;
    }

    public void draw(){
        Image image = null;
        Image image2 = null;
        Image image3 = null;
        Image[] temp = null;
        SubBarrier barrier = null;

        //-------------------------------------------------
        image = readImage("BottomLeftCorner-1.png");
        image2 = readImage("originalShip.png");
        image3 = readImage("originalShip.png");
        temp = new Image[]{image, image2, image3};

        barrier = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - x, canvas.getHeight() - image.getHeight()-y);
        objects.add(barrier);
        barrier.drawFrame(gc);

        //----------------------------------------------
        image = readImage("TopLeftCorner-1.png");
        image2 = readImage("originalShip.png");
        image3 = readImage("originalShip.png");
        temp = new Image[]{image, image2, image3};

        barrier = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - x, canvas.getHeight() - image.getHeight()- ((y-9) + image.getHeight()));
        objects.add(barrier);
        barrier.drawFrame(gc);

        //----------------------------------------
        image = readImage("TopFill-1.png");
        image2 = readImage("originalShip.png");
        image3 = readImage("originalShip.png");
        temp = new Image[]{image, image2, image3};

        barrier = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - (x+1 - image.getWidth()), canvas.getHeight() - image.getHeight()- (y+15 + image.getHeight()));
        objects.add(barrier);
        barrier.drawFrame(gc);

        //----------------------------------------
        image = readImage("TopFill-1.png");
        image2 = readImage("originalShip.png");
        image3 = readImage("originalShip.png");
        temp = new Image[]{image, image2, image3};

        barrier = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - ((x+1) - image.getWidth()*2), canvas.getHeight() - image.getHeight()- ((y+15) + image.getHeight()));
        objects.add(barrier);
        barrier.drawFrame(gc);

        //-------------------------------------------
        image = readImage("BottomFill-1.png");
        image2 = readImage("originalShip.png");
        image3 = readImage("originalShip.png");
        temp = new Image[]{image, image2, image3};

        barrier = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - (x-21), canvas.getHeight() - image.getHeight()-((y-7)+image.getWidth()));
        objects.add(barrier);
        barrier.drawFrame(gc);

        //-------------------------------------------
        image = readImage("BottomFill-1.png");
        image2 = readImage("originalShip.png");
        image3 = readImage("originalShip.png");
        temp = new Image[]{image, image2, image3};

        barrier = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - ((x-21)  - image.getWidth() ), canvas.getHeight() - image.getHeight()-((y-7)+image.getWidth()));
        objects.add(barrier);
        barrier.drawFrame(gc);

        //-----------------------------------------------
        image = readImage("TopRightCorner-1.png");
        image2 = readImage("originalShip.png");
        image3 = readImage("originalShip.png");
        temp = new Image[]{image, image2, image3};

        barrier = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - ((x-25) - image.getWidth()*2), canvas.getHeight() - image.getHeight()- ((y-9) + image.getHeight()));
        objects.add(barrier);
        barrier.drawFrame(gc);

        //--------------------------------------------------
        image = readImage("BottomRightCorner-1.png");
        image2 = readImage("originalShip.png");
        image3 = readImage("originalShip.png");
        temp = new Image[]{image, image2, image3};

        barrier = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - ((x-43)  - image.getWidth()), canvas.getHeight() - image.getHeight()-((y-20)+image.getWidth()));
        objects.add(barrier);
        barrier.drawFrame(gc);
    }
}