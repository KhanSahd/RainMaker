package com.example.RainMaker;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Blimp extends GameObject {


    private Ellipse ellipse;
    private int fuel;
    private Label fuelText;

    private boolean offScreen, inView, exited;

    public Blimp() throws FileNotFoundException {
        alive = true;
        offScreen = true;
        FileInputStream file = new FileInputStream("images/blimp.png");
        Image blimp = new Image(file);
        ImageView img = new ImageView(blimp);
        img.setFitWidth(500);
        fuel = (int)Math.floor(Math.random() * (10000 - 5000) + 5000);
        fuelText = new Label(String.valueOf(fuel));
        ellipse = new Ellipse(130, 30);
        ellipse.setTranslateX(247);
        ellipse.setTranslateY(180);
        img.setPreserveRatio(true);
        fuelText.setTranslateY(170);
        fuelText.setTranslateX(230);
        fuelText.setScaleY(-1);
        add(ellipse);
        add(img);
        add(fuelText);
        setTranslateX(-Game.GAME_WIDTH / 2);
    }

    public void update(){
        if(offScreen){
            setTranslateX(-Game.GAME_WIDTH / 2);
            setTranslateY(Math.random() * Game.GAME_HEIGHT);
        }
        if(inView){
            setTranslateX(getTranslateX() + 2);
        }
    }

    public void fuelHelicopter(){
        fuel -= fuel * 0.01;
        fuelText.setText(String.valueOf(fuel));
    }

    @Override
    Shape getShapeBounds() {
        return ellipse;
    }

    int getFuel(){
        return fuel;
    }

    public boolean isExited(){
        return exited;
    }

    public void setInView() {
        offScreen = false;
        this.inView = !inView;
    }

    public void setExited() {
        this.exited = !exited;
        inView = !inView;
        offScreen = !offScreen;
    }
}
