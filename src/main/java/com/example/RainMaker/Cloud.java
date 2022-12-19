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

    private Ellipse ellipse;
    private GameText percent;
    private Random r, r2;
    private int low = 800 / 3;
    private int lowW;
    private int high;
    private int highW;
    private double result;
    private double resultW;

    private Point2D loc;
    private int rgbColor = 255;
    private double saturation = 0;
    private Boolean isRaining = false;
    private BezierOval bez;

    private Media sound;
    private MediaPlayer mediaPlayer;



    Cloud() {
        alive = true;
        r2 = new Random();
        double radiusX = r2.nextInt(15, 60) + 15;
        double radiusY = r2.nextInt(15, 60) + 15;

        ellipse = new Ellipse(radiusX,  radiusY);
        r = new Random();
        high = Game.GAME_HEIGHT - (int) ellipse.getRadiusY();
        lowW = (int) ellipse.getRadiusX();
        highW = Game.GAME_WIDTH - (int) ellipse.getRadiusX();
        result = r.nextInt(high - low) + low;
        resultW = r.nextInt(highW - lowW) + lowW;
        loc = new Point2D(resultW, result );
        ellipse.setFill(Color.rgb(rgbColor, rgbColor, rgbColor));
        bez = new BezierOval(radiusX, radiusY);
        add(ellipse);
        add(bez);
        translate(loc.getX(), loc.getY());
        percent = new GameText((int)saturation, true);
        add(percent);
        percent.translate(-8, -5);

        /* Setting up sound */
        sound = new Media(new File("sounds/thunder.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(sound);

    }


    public void update() {
        resultW += Game.WIND_SPEED;
        result += Game.WIND_SPEED;
        loc = new Point2D(resultW, result);
        translate(loc.getX(), loc.getY());
    }




    public void seed(Boolean seeding){
        if(seeding == true){
            if (saturation < 100) {
                rgbColor -= 1;
                ellipse.setFill(Color.rgb(rgbColor, rgbColor, rgbColor));
                bez.changeColor(rgbColor);
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

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    Shape getShapeBounds() {
        return ellipse;
    }

}
