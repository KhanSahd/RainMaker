package com.example.RainMaker;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;

public class BezierOval {

    Ellipse bez;
    QuadCurve quad;

    public BezierOval() {
        bez = new Ellipse();
        quad = new QuadCurve();
        quad.setStartX(0.0f);
        quad.setStartY(50.0f);
        quad.setEndX(50.0f);
        quad.setEndY(50.0f);
        quad.setControlX(25.0f);
        quad.setControlY(0.0f);
        quad.setFill(Color.RED);

    }

}
