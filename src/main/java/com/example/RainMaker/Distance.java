package com.example.RainMaker;

import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;

class Distance extends GameObject implements Updatable {

    Line line;
    int distance;
    Label l;
    Point2D loc;
    Boolean isShowing = false;

    public Distance(Point2D c, Point2D p){
        line = new Line(c.getX(), c.getY(), p.getX(), p.getY());
        loc = new Point2D(line.getTranslateX(), line.getTranslateY());
        line.setStroke(Color.RED);
        distance = (int)Math.sqrt(Math.pow((p.getX() - c.getX()), 2) + Math.pow((p.getY() - c.getY()), 2));
        l = new Label(String.valueOf(distance));
        l.setFont(new Font(15));
        l.setTextFill(Color.WHITE);
        l.setScaleY(-1);
        l.setTranslateX(loc.getX() + (p.getX() - c.getX()) / 2);
        l.setTranslateY(loc.getY() + (p.getY() - c.getY()) / 2);
        getChildren().addAll(line, l);
    }

    public void update(Cloud c, Pond p){
        loc = new Point2D(c.getLoc().getX(), c.getLoc().getY());
        line.setStartX(loc.getX());
        line.setStartY(loc.getY());
        distance = (int)Math.sqrt(Math.pow((p.getLoc().getX() - c.getLoc().getX()), 2) +
                Math.pow((p.getLoc().getY() - c.getLoc().getY()), 2));
        l.setTranslateX(loc.getX() + (p.getLoc().getX() - c.getLoc().getX()) / 2);
        l.setTranslateY(loc.getY() + (p.getLoc().getY() - c.getLoc().getY()) / 2);
        l.setText(String.valueOf(distance));
        if(isShowing){
            line.setVisible(true);
            l.setVisible(true);
        } else {
            line.setVisible(false);
            l.setVisible(false);
        }
    }

    public int getDistance(){
        return distance;
    }

    public void toggle(){
        isShowing = !isShowing;
    }


    @Override
    Shape getShapeBounds() {
        return line;
    }
}