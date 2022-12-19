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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

class Helicopter extends GameObject implements Updatable {


    private Ellipse e;
    private Line l;
    private Color color = Color.YELLOW;

    private int fuel;
    private GameText fText;
    private double speed = 0.0;

    private Point2D loc = new Point2D(190, 60);
    private Boolean engineOn;
    private ImageView img1;
    private HeliBlades blades;
    private Boolean off, starting, ready, stopping;
    private Media sound;
    private MediaPlayer mediaPlayer;

    boolean losingFuel;

    Helicopter() throws FileNotFoundException {

        /* INSTANTIATING OBJECTS */
            alive = true;
            engineOn = false;
            losingFuel = false;
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
            /* END OF HELICOPTER */

            /* PROPELLERS  */
                blades = new HeliBlades();
            /* END OF PROPELLERS */

            fText = new GameText(fuel, false);
            e.setFill(color);
            l.setStroke(color);
        /* END OF INSTANTIATION */

        /* ADDING TO GROUP */
            add(l);
            add(img1);
            add(blades);
            add(fText);
        /* END OF ADDING TO GROUP */
        /* TRANSLATION */
            fText.translate(-25, -45);
            fText.changeColor(Color.BLACK);
            translate(loc.getX(), loc.getY());
        /* END OF TRANSLATION */

        /* Setting up sound */
        sound = new Media(new File("sounds/helicopter.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.2);
        /* END OF SOUND */

    }

    @Override
    Shape getShapeBounds() {
        return l;
    }

    public void update() {
        loc = loc.add(speed * Math.sin(-1*Math.PI*getMyRotation()/180), speed *
                Math.cos(-1*Math.PI*getMyRotation()/180));
        translate(loc.getX(), loc.getY());
        if(fuel > 0 && losingFuel){
            fuel -= 5;
            fText.setText(fuel);
        }
        if(engineOn){
            blades.rotate(blades.getMyRotation() - 5);

        }

    }

    public void playSound(){
        if(engineOn){
            mediaPlayer.play();
        } else {
            mediaPlayer.stop();
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
            losingFuel = true;
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

    public void reFuel(int f){
        fuel += f * 0.02;
    }

    public Point2D getLoc() {
        return loc;
    }

    /* GETTERS */
    public Ellipse getE() {
        return e;
    }

    public Line getL() {
        return l;
    }

    public Color getColor() {
        return color;
    }

    public GameText getfText() {
        return fText;
    }

    public double getSpeed() {
        return speed;
    }

    public Boolean getEngineOn() {
        return engineOn;
    }

    public ImageView getImg1() {
        return img1;
    }

    public HeliBlades getBlades() {
        return blades;
    }

    public Boolean getOff() {
        return off;
    }

    public Boolean getStarting() {
        return starting;
    }

    public Boolean getReady() {
        return ready;
    }

    public Boolean getStopping() {
        return stopping;
    }

    public Media getSound() {
        return sound;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }


}
