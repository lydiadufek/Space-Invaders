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
        Image image4 = null;
        Image image5 = null;
        Image[] temp = null;
        SubBarrier barrier = null;

        //-------------------------------------------------
        image = readImage("BottomLeftCorner-1.png");
        image2 = readImage("BottomLeftCorner-2.png.");
        image3 = readImage("BottomLeftCorner-3.png.");
        image4 = readImage("BottomLeftCorner-4.png.");
        temp = new Image[]{image, image2, image3, image4, image5};

        barrier = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - x, canvas.getHeight() - image.getHeight()-y);
        objects.add(barrier);
        barrier.drawFrame(gc);

        //----------------------------------------------
        image = readImage("TopLeftCorner-1.png");
        image2 = readImage("TopLeftCorner-2.png");
        image3 = readImage("TopLeftCorner-3.png");
        image4 = readImage("TopLeftCorner-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        barrier = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - x, canvas.getHeight() - image.getHeight()- ((y-9) + image.getHeight()));
        objects.add(barrier);
        barrier.drawFrame(gc);

        //----------------------------------------
        image = readImage("TopFill-1.png");
        image2 = readImage("TopFill-2.png");
        image3 = readImage("TopFill-3.png");
        image4 = readImage("TopFill-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        barrier = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - (x+1 - image.getWidth()), canvas.getHeight() - image.getHeight()- (y+15 + image.getHeight()));
        objects.add(barrier);
        barrier.drawFrame(gc);

        //----------------------------------------
        image = readImage("TopFill-1.png");
        image2 = readImage("TopFill-2.png");
        image3 = readImage("TopFill-3.png");
        image4 = readImage("TopFill-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        barrier = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - ((x+1) - image.getWidth()*2), canvas.getHeight() - image.getHeight()- ((y+15) + image.getHeight()));
        objects.add(barrier);
        barrier.drawFrame(gc);

        //-------------------------------------------
        image = readImage("BottomFill-1.png");
        image2 = readImage("BottomFill-2.png");
        image3 = readImage("BottomFill-3.png");
        image4 = readImage("BottomFill-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        barrier = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - (x-21), canvas.getHeight() - image.getHeight()-((y-7)+image.getWidth()));
        objects.add(barrier);
        barrier.drawFrame(gc);

        //-------------------------------------------
        image = readImage("BottomFill-1.png");
        image2 = readImage("BottomFill-2.png");
        image3 = readImage("BottomFill-3.png");
        image4 = readImage("BottomFill-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        barrier = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - ((x-21)  - image.getWidth() ), canvas.getHeight() - image.getHeight()-((y-7)+image.getWidth()));
        objects.add(barrier);
        barrier.drawFrame(gc);

        //-----------------------------------------------
        image = readImage("TopRightCorner-1.png");
        image2 = readImage("TopRightCorner-2.png");
        image3 = readImage("TopRightCorner-3.png");
        image4 = readImage("TopRightCorner-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};


        barrier = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - ((x-25) - image.getWidth()*2), canvas.getHeight() - image.getHeight()- ((y-9) + image.getHeight()));
        objects.add(barrier);
        barrier.drawFrame(gc);

        //--------------------------------------------------
        image = readImage("BottomRightCorner-1.png");
        image2 = readImage("BottomRightCorner-2.png");
        image3 = readImage("BottomRightCorner-3.png");
        image4 = readImage("BottomRightCorner-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        barrier = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - ((x-43)  - image.getWidth()), canvas.getHeight() - image.getHeight()-((y-20)+image.getWidth()));
        objects.add(barrier);
        barrier.drawFrame(gc);
    }
}