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
    private ArrayList<SubBarrier> pieces;
    private GraphicsContext gc;
    private double x;
    private double y;

    private static SubBarrier barrier;
    private static SubBarrier barrier1;
    private static SubBarrier barrier2;
    private static SubBarrier barrier3;
    private static SubBarrier barrier4;
    private static SubBarrier barrier5;
    private static SubBarrier barrier6;
    private static SubBarrier barrier7;

    public Barrier(double x, double y, Canvas canvas, ArrayList<Sprite> objects, GraphicsContext gc) {
        this.x = x;
        this.y = y;
        this.canvas = canvas;
        this.objects = objects;
        this.gc = gc;

        pieces = new ArrayList<>();
    }

    public void draw(){
        Image image = null;
        Image image2 = null;
        Image image3 = null;
        Image image4 = null;
        Image image5 = null;
        Image[] temp = null;

        //-------------------------------------------------
        image = readImage("BottomLeftCorner-1.png");
        image2 = readImage("BottomLeftCorner-2.png");
        image3 = readImage("BottomLeftCorner-3.png");
        image4 = readImage("BottomLeftCorner-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        barrier = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - x, canvas.getHeight() - image.getHeight()-y);
        objects.add(barrier);
        barrier.drawFrame(gc);
        pieces.add(barrier);

        //----------------------------------------------
        image = readImage("TopLeftCorner-1.png");
        image2 = readImage("TopLeftCorner-2.png");
        image3 = readImage("TopLeftCorner-3.png");
        image4 = readImage("TopLeftCorner-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        barrier1 = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - x, canvas.getHeight() - image.getHeight()- ((y-9) + image.getHeight()));
        objects.add(barrier1);
        barrier1.drawFrame(gc);
        pieces.add(barrier1);

        //----------------------------------------
        image = readImage("TopFill-1.png");
        image2 = readImage("TopFill-2.png");
        image3 = readImage("TopFill-3.png");
        image4 = readImage("TopFill-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        barrier2 = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - (x+1 - image.getWidth()), canvas.getHeight() - image.getHeight()- (y+15 + image.getHeight()));
        objects.add(barrier2);
        barrier2.drawFrame(gc);
        pieces.add(barrier2);

        //----------------------------------------
        image = readImage("TopFill-1.png");
        image2 = readImage("TopFill-2.png");
        image3 = readImage("TopFill-3.png");
        image4 = readImage("TopFill-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        barrier3 = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - ((x+1) - image.getWidth()*2), canvas.getHeight() - image.getHeight()- ((y+15) + image.getHeight()));
        objects.add(barrier3);
        barrier3.drawFrame(gc);
        pieces.add(barrier3);

        //-------------------------------------------
        image = readImage("BottomFill-1.png");
        image2 = readImage("BottomFill-2.png");
        image3 = readImage("BottomFill-3.png");
        image4 = readImage("BottomFill-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        barrier4 = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - (x-21), canvas.getHeight() - image.getHeight()-((y-7)+image.getWidth()));
        objects.add(barrier4);
        barrier4.drawFrame(gc);
        pieces.add(barrier4);

        //-------------------------------------------
        image = readImage("BottomFill-1.png");
        image2 = readImage("BottomFill-2.png");
        image3 = readImage("BottomFill-3.png");
        image4 = readImage("BottomFill-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        barrier5 = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - ((x-21)  - image.getWidth() ), canvas.getHeight() - image.getHeight()-((y-7)+image.getWidth()));
        objects.add(barrier5);
        barrier5.drawFrame(gc);
        pieces.add(barrier5);

        //-----------------------------------------------
        image = readImage("TopRightCorner-1.png");
        image2 = readImage("TopRightCorner-2.png");
        image3 = readImage("TopRightCorner-3.png");
        image4 = readImage("TopRightCorner-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        barrier6 = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - ((x-25) - image.getWidth()*2), canvas.getHeight() - image.getHeight()- ((y-9) + image.getHeight()));
        objects.add(barrier6);
        barrier6.drawFrame(gc);
        pieces.add(barrier6);

        //--------------------------------------------------
        image = readImage("BottomRightCorner-1.png");
        image2 = readImage("BottomRightCorner-2.png");
        image3 = readImage("BottomRightCorner-3.png");
        image4 = readImage("BottomRightCorner-4.png");
        temp = new Image[]{image, image2, image3, image4, image5};

        barrier7 = new SubBarrier(temp, (canvas.getWidth() / 2) - (image.getWidth() / 2) - ((x-43)  - image.getWidth()), canvas.getHeight() - image.getHeight()-((y-20)+image.getWidth()));
        objects.add(barrier7);
        barrier7.drawFrame(gc);
        pieces.add(barrier7);
    }

    public void updateGC(ArrayList<Sprite> objects, GraphicsContext gc ) {
        this.objects = objects;
        this.gc = gc;
    }

    public void staticDraw(ArrayList<Sprite> objects, GraphicsContext gc){
        for (int i = 0; i < pieces.size(); i++) {
            objects.add(pieces.get(i));
        }
    }

    public ArrayList<SubBarrier> getPieces() {
        return pieces;
    }

    @Override
    public String toString() {
        return "Barrier";
    }
}