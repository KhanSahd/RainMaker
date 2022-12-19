package com.example.RainMaker;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;

public class BezierOval extends Group {

    CubicCurve cubic1, cubic2, cubic3, cubic4;
    private int rgbColor = 255;

    public BezierOval(double x, double y) {
        cubic1 = new CubicCurve();
        cubic2 = new CubicCurve();
        cubic3 = new CubicCurve();
        cubic4 = new CubicCurve();

//        double point1 = x * Math.sin(90);
//        double point2 = y * Math.cos(90);


        /* Top left */
        cubic1.setStartX(0);
        cubic1.setStartY(y);
        cubic1.setControlX1(-(x * Math.sin(-60)));
        cubic1.setControlY1(-(y * Math.cos(-60)));
        cubic1.setControlX2(-x);
        cubic1.setControlY2(y);
        cubic1.setEndX(-x);
        cubic1.setEndY(0);
        cubic1.setFill(Color.rgb(rgbColor, rgbColor, rgbColor));
        cubic1.setStroke(Color.BLACK);

        /* Bottom left */
        cubic2.setStartX(0);
        cubic2.setStartY(-y);
        cubic2.setControlX1((x * Math.sin(180)));
        cubic2.setControlY1((y * Math.sin(180)));
        cubic2.setControlX2(-100);
        cubic2.setControlY2(-50);
        cubic2.setEndX(-x);
        cubic2.setEndY(0);
        cubic2.setFill(Color.rgb(rgbColor, rgbColor, rgbColor));
        cubic2.setStroke(Color.BLACK);
        /* Bottom right */
        cubic4.setStartX(0);
        cubic4.setStartY(-y);
        cubic4.setControlX1((x * Math.sin(-180)));
        cubic4.setControlY1((y * Math.cos(-185)));
        cubic4.setControlX2(100);
        cubic4.setControlY2(-60);
        cubic4.setEndX(x);
        cubic4.setEndY(0);
        cubic4.setFill(Color.rgb(rgbColor, rgbColor, rgbColor));
        cubic4.setStroke(Color.BLACK);

        /* Top Right */
        cubic3.setStartX(0);
        cubic3.setStartY(y);
        cubic3.setControlX1(-(x * Math.sin(60)));
        cubic3.setControlY1(-(y * Math.cos(60)));
        cubic3.setControlX2(x + Math.random() * 30);
        cubic3.setControlY2(y + Math.random() * 30);
        cubic3.setEndX(x);
        cubic3.setEndY(0);
        cubic3.setFill(Color.rgb(rgbColor, rgbColor, rgbColor));
        cubic3.setStroke(Color.BLACK);


        getChildren().addAll(cubic1, cubic2, cubic3, cubic4);

    }

    public void changeColor(int c){
        cubic1.setFill(Color.rgb(c, c, c));
        cubic2.setFill(Color.rgb(c, c, c));
        cubic3.setFill(Color.rgb(c, c, c));
        cubic4.setFill(Color.rgb(c, c, c));
    }

}
