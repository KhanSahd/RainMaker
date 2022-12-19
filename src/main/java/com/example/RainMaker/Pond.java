package com.example.RainMaker;

import javafx.geometry.Point2D;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

import java.io.File;
import java.util.Random;

class Pond extends GameObject implements Updatable {

    private Ellipse ellipse;
    private GameText percent;
    private Random r;
    private int low = 800 / 3;
    private int lowW;
    private int high;
    private int highW;
    private Point2D result;
    private double radius;

    private Media sound;
    private MediaPlayer mediaPlayer;

    Pond() {
        alive = true;
        radius = Math.random() * 60;
        ellipse = new Ellipse(radius, radius);
        r = new Random();
        high = 800 - (int) ellipse.getRadiusY();
        lowW = (int) ellipse.getRadiusX();
        highW = Game.GAME_WIDTH - (int) ellipse.getRadiusX();
        result = new Point2D((r.nextInt(highW - lowW) + lowW), (r.nextInt(high - low) + low) );
        ellipse.setFill(Color.BLUE);
        add(ellipse);
        translate(result.getX(), result.getY());
        percent = new GameText((int) radius, true);
        percent.changeColor(Color.WHITE);
        add(percent);
        percent.translate(-8, -5);

        /* Setting up sound */
        sound = new Media(new File("sounds/rain.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(sound);

    }

    public void update() {
        result = new Point2D((r.nextInt(highW - lowW) + lowW), (r.nextInt(high - low) + low));
        translate(result.getX(), result.getY());
        if (radius < 100){
            radius++;
            ellipse.setRadiusY(radius);
            ellipse.setRadiusY(radius);
        }
    }

    public double getRadius(){
        return radius;
    }

    public void makeRain(int sat){
        if (radius < 100){
            radius = radius + (sat * 0.001);
            ellipse.setRadiusX(radius);
            ellipse.setRadiusY(radius);
            percent.setText((int)radius);
        }
        mediaPlayer.play();
    }

    public Point2D getLoc(){
        return result;
    }

    @Override
    Shape getShapeBounds() {
        return ellipse;
    }

    /* GETTERS */

    public Ellipse getEllipse() {
        return ellipse;
    }

    public GameText getPercent() {
        return percent;
    }

    public Random getR() {
        return r;
    }

    public int getLow() {
        return low;
    }

    public int getLowW() {
        return lowW;
    }

    public int getHigh() {
        return high;
    }

    public int getHighW() {
        return highW;
    }

    public Point2D getResult() {
        return result;
    }

    public Media getSound() {
        return sound;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
