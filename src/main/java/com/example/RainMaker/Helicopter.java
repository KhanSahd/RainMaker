package com.example.RainMaker;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

class Helicopter extends GameObject implements Updatable {

    Ellipse e;
    Line l;
    Color color = Color.YELLOW;

    private int fuel;
    GameText fText;
    double speed = 0.0;

    private Point2D loc = new Point2D(190, 60);
    Boolean engineOn;
    ImageView img1;
    HeliBlades blades;

    Helicopter() throws FileNotFoundException {
        alive = true;
        engineOn = false;
        e = new Ellipse(10, 10);
        l = new Line(0, 0, 0, 25);
        fuel = 25000;
        /* HELICOPTER */
        FileInputStream file = new FileInputStream("images/helicopter.png");
        Image heli = new Image(file);
        img1 = new ImageView(heli);
        img1.setFitWidth(80);
        img1.setPreserveRatio(true);
        img1.setRotate(180);
        img1.setTranslateX(-40);
        img1.setTranslateY(-35);

        /* PROPELLERS  */
        blades = new HeliBlades();
        /* END OF PROPELLERS */

        fText = new GameText(fuel, false);
        e.setFill(color);
        l.setStroke(color);
        add(l);
        add(img1);
        add(blades);
        add(fText);
        fText.translate(-25, -45);
        fText.changeColor(Color.BLACK);
        translate(loc.getX(), loc.getY());

    }

    @Override
    Shape getShapeBounds() {
        return l;
    }

    public void update() {
        loc = loc.add(speed * Math.sin(-1*Math.PI*getMyRotation()/180), speed *
                Math.cos(-1*Math.PI*getMyRotation()/180));
        translate(loc.getX(), loc.getY());
        if(fuel > 0){
            fuel -= 5;
            fText.setText(fuel);
        }
        if(engineOn){
            blades.rotate(blades.getMyRotation() - 5);

        }

    }

    public void handleKeyPress(KeyEvent evt) {
        if (evt.getCode() == KeyCode.RIGHT) {
            rotate(getMyRotation() - 15);
        }

        if (evt.getCode() == KeyCode.LEFT) {
            rotate(getMyRotation() + 15);
        }
    }

    public void startEngine() {
        if(speed < 0.1 && speed > -0.01){
            engineOn = !engineOn;
        }
    }

    public void increaseSpeed(){
        if(engineOn){
            if(speed < 10.1){
                speed += 0.1;
            }
        }
    }
    public void decreaseSpeed(){
        if(engineOn){
            if(speed > -2){
                speed -= 0.1;
            }
        }

    }

    public int getFuel(){
        return fuel;
    }

    public Point2D getLoc() {
        return loc;
    }



}
