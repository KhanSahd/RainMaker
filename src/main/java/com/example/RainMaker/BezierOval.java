package com.example.RainMaker;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;

public class BezierOval extends Group {

    Ellipse bez;
    QuadCurve quad;

    public BezierOval(Ellipse e) {
        double radiusX = Math.random() * 60;
        double radiusY = Math.random() * 60;
        //bez = new Ellipse(radiusX, radiusY);
        quad = new QuadCurve();
        quad.setStartX(0);
        quad.setStartY(0);
        quad.setEndX(20);
        quad.setEndY(20);
        quad.setControlX(200.0f);
        quad.setControlY(200.0f);
        quad.setFill(Color.WHITE);
        getChildren().addAll(quad);

    }

}
