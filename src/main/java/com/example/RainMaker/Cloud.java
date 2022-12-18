package com.example.RainMaker;

import javafx.geometry.Point2D;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

import java.io.File;
import java.util.Random;

class Cloud extends GameObject implements Updatable {

    Ellipse ellipse;
    GameText percent;
    Random r;
    int low = 800 / 3;
    int lowW;
    int high;
    int highW;
    double result;
    double resultW;

    Point2D loc;
    int rgbColor = 255;
    double saturation = 0;
    Boolean isRaining = false;
    BezierOval bez;

    Media sound;
    MediaPlayer mediaPlayer;



    Cloud() {
        alive = true;
        ellipse = new Ellipse(60,  60);
        r = new Random();
        high = Game.GAME_HEIGHT - (int) ellipse.getRadiusY();
        lowW = (int) ellipse.getRadiusX();
        highW = Game.GAME_WIDTH - (int) ellipse.getRadiusX();
        result = r.nextInt(high - low) + low;
        resultW = r.nextInt(highW - lowW) + lowW;
        loc = new Point2D(resultW, result );
        ellipse.setFill(Color.rgb(rgbColor, rgbColor, rgbColor));
        bez = new BezierOval(ellipse);
        add(bez);
        add(ellipse);
        translate(loc.getX(), loc.getY());
        percent = new GameText((int)saturation, true);
        add(percent);
        percent.translate(-8, -5);

        /* Setting up sound */
        sound = new Media(new File("sounds/thunder.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(sound);

    }

    public void updateClosestPond(Pond p){

    }

    public void update() {
        resultW += Game.WIND_SPEED;
        result += Game.WIND_SPEED;
        loc = new Point2D(resultW, result);
        translate(loc.getX(), loc.getY());
        if (result >= Game.GAME_HEIGHT || resultW >= Game.GAME_WIDTH){
            resultW = -30;
            result = 0 - (Math.random() * 400);
            loc = new Point2D(resultW, result);
        }
    }




    public void seed(Boolean seeding){
        if(seeding == true){
            if (saturation < 100) {
                rgbColor -= 1;
                ellipse.setFill(Color.rgb(rgbColor, rgbColor, rgbColor));
                saturation++;
                percent.setText((int)saturation);
                if (saturation == 30) {
                    isRaining = true;
                    mediaPlayer.play();
                }
            }
        }
        if(seeding == false){
            if (saturation > 0){
                if (rgbColor < 255){
                    rgbColor += 0.04;
                }
                ellipse.setFill(Color.rgb(rgbColor,rgbColor,rgbColor));
                saturation -= 0.04;
                percent.setText((int) saturation);
                if (saturation < 30){
                    isRaining = false;
                    mediaPlayer.stop();
                }
            }
        }
    }


    public Point2D getLoc(){
        return loc;
    }


    public double getSaturation(){
        return saturation;
    }



    @Override
    Shape getShapeBounds() {
        return ellipse;
    }

}
