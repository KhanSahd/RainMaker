package com.example.RainMaker;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class Game extends Pane {
    static final int GAME_WIDTH = 800;
    static final int GAME_HEIGHT = 800;
    static final double WIND_SPEED = .3;
    Set<KeyCode> keysDown = new HashSet<>();
    int key() {
        return keysDown.contains(KeyCode.SPACE) ? 1 : 0;
    }


    private Helipad helipad;
    private Helicopter helicopter;
    private GameText gt;
    private BackGroundImage bg;
    private Pond[] ponds = new Pond[]{new Pond(),new Pond(), new Pond()};
    private ArrayList<Cloud> clouds = new ArrayList<>(Arrays.asList(new Cloud(), new Cloud(),
            new Cloud(), new Cloud()));

    private Blimp blimp;
    private Distance[] distances = new Distance[]{
            new Distance(clouds.get(0).getLoc(), ponds[0].getLoc()), // index 0
            new Distance(clouds.get(0).getLoc(), ponds[1].getLoc()), // index 1
            new Distance(clouds.get(0).getLoc(), ponds[2].getLoc()), // index 2
            new Distance(clouds.get(1).getLoc(), ponds[0].getLoc()), // index 3
            new Distance(clouds.get(1).getLoc(), ponds[1].getLoc()), // index 4
            new Distance(clouds.get(1).getLoc(), ponds[2].getLoc()), // index 5
            new Distance(clouds.get(2).getLoc(), ponds[0].getLoc()), // index 6
            new Distance(clouds.get(2).getLoc(), ponds[1].getLoc()), // index 7
            new Distance(clouds.get(2).getLoc(), ponds[2].getLoc()), // index 8
            new Distance(clouds.get(3).getLoc(), ponds[0].getLoc()), // index 9
            new Distance(clouds.get(3).getLoc(), ponds[1].getLoc()), // index 10
            new Distance(clouds.get(3).getLoc(), ponds[2].getLoc())  // index 11
    };
    private double totalSaturation;
    private int cloudCounter = 4;
    private Pane ground, sky, cloudCont;

    private Media sound, sound2;
    private MediaPlayer mediaPlayer, mediaPlayer2;


    public Game() throws FileNotFoundException {

        /* Instantiating Game Objects */
            bg = new BackGroundImage();
            helipad = new Helipad();
            helicopter = new Helicopter();
            blimp = new Blimp();
            ground = new Pane();
            sky = new Pane();
            cloudCont = new Pane();
            sound = new Media(new File("sounds/background.mp3").toURI().toString());
            mediaPlayer = new MediaPlayer(sound);
            sound2 = new Media(new File("sounds/victory.mp3").toURI().toString());
            mediaPlayer2 = new MediaPlayer(sound2);
            mediaPlayer2.setStartTime(new Duration(2700));
        /* End of Instantiation */

        /* Adding objects to rightful pane */
            /* Ground objects */
                ground.getChildren().addAll(bg, helipad);
                for(Pond p: ponds){
                    ground.getChildren().add(p);
                }
                getChildren().add(ground);
            /* End of Ground objects */

            /* Sky objects */
                clouds.forEach((c) -> {
                    cloudCont.getChildren().add(c);
                    //sky.getChildren().add(cloudCont);
                });
                sky.getChildren().add(cloudCont);
                for(Distance d: distances){
                    sky.getChildren().add(d);
                }
                getChildren().add(sky);
                getChildren().add(blimp);
                getChildren().add(helicopter);
            /* End of Sky Objects */
    }

    public void handleKeyPressed(KeyEvent e) {
        keysDown.add(e.getCode());
    }

    public void handleKeyReleased(KeyEvent e) {
        keysDown.remove(e.getCode());
    }

    public void checkGameStatus(){
        totalSaturation = ponds[0].getRadius() + ponds[1].getRadius() + ponds[2].getRadius();
        if(helicopter.getFuel() == 0){
            helicopter.alive = false;
            gt = new GameText("Game Over! \n Press 'R' to restart");
            gt.translate((GAME_WIDTH / 2) - 80, GAME_HEIGHT / 2 + 30);
            gt.changeColor(Color.RED);
            getChildren().removeAll(helicopter, helipad);
            getChildren().add(gt);
        }
        if(helicopter.getFuel() > 0 && helicopter.alive && totalSaturation >= 300 * 0.8 &&
                helicopter.intersects(helipad) && !helicopter.getEngineOn()){
            gt = new GameText("You won! \n 'R' to continue");
            gt.translate((GAME_WIDTH / 2) - 120, (GAME_HEIGHT / 2) - 30);
            gt.changeColor(Color.BLACK);
            mediaPlayer2.play();
            getChildren().add(gt);
        }

    }


    public void updateDistance(){
        distances[0].update(clouds.get(0), ponds[0]);
        distances[1].update(clouds.get(0), ponds[1]);
        distances[2].update(clouds.get(0), ponds[2]);
        distances[3].update(clouds.get(1), ponds[0]);
        distances[4].update(clouds.get(1), ponds[1]);
        distances[5].update(clouds.get(1), ponds[2]);
        distances[6].update(clouds.get(2), ponds[0]);
        distances[7].update(clouds.get(2), ponds[1]);
        distances[8].update(clouds.get(2), ponds[2]);
        distances[9].update(clouds.get(3), ponds[0]);
        distances[10].update(clouds.get(3), ponds[1]);
        distances[11].update(clouds.get(3), ponds[2]);
    }

    public void regenerateBlimp() throws FileNotFoundException {
        if(blimp.isExited()){
            getChildren().remove(blimp);
            blimp = new Blimp();
            getChildren().add(blimp);
        }
    }

    public void generateClouds(){
        clouds.forEach(e -> {
            if(e.getLoc().getX() >= GAME_WIDTH || e.getLoc().getY() >= GAME_HEIGHT){
                e.destroy();
                cloudCont.getChildren().remove(e);
            }

        });
    }

    public void init() throws FileNotFoundException {

        /* Removing everything from game */
            getChildren().clear();
            clouds.clear();
            ground.getChildren().clear();
            cloudCont.getChildren().clear();
            sky.getChildren().clear();

            /* stopping any game sounds */
            helicopter.getMediaPlayer().stop();
            clouds.forEach(c -> c.getMediaPlayer().stop());
            for(Pond p: ponds){
                p.getMediaPlayer().stop();
            }
            mediaPlayer.stop();
            mediaPlayer2.stop();

        /* End of Removal */

        /* Re-instantiating Game Objects */
            ponds = new Pond[]{new Pond(),new Pond(), new Pond()};
            clouds = new ArrayList<>(Arrays.asList(new Cloud(), new Cloud(),
                    new Cloud(), new Cloud()));
            helicopter = new Helicopter();
            helipad = new Helipad();
            blimp = new Blimp();
            bg = new BackGroundImage();
            distances = new Distance[]{
                    new Distance(clouds.get(0).getLoc(), ponds[0].getLoc()), // index 0
                    new Distance(clouds.get(0).getLoc(), ponds[1].getLoc()), // index 1
                    new Distance(clouds.get(0).getLoc(), ponds[2].getLoc()), // index 2
                    new Distance(clouds.get(1).getLoc(), ponds[0].getLoc()), // index 3
                    new Distance(clouds.get(1).getLoc(), ponds[1].getLoc()), // index 4
                    new Distance(clouds.get(1).getLoc(), ponds[2].getLoc()), // index 5
                    new Distance(clouds.get(2).getLoc(), ponds[0].getLoc()), // index 6
                    new Distance(clouds.get(2).getLoc(), ponds[1].getLoc()), // index 7
                    new Distance(clouds.get(2).getLoc(), ponds[2].getLoc()), // index 8
                    new Distance(clouds.get(3).getLoc(), ponds[0].getLoc()), // index 9
                    new Distance(clouds.get(3).getLoc(), ponds[1].getLoc()), // index 10
                    new Distance(clouds.get(3).getLoc(), ponds[2].getLoc())  // index 11
            };
            gt = new GameText("");
        /* End of Re instantiation */

        /* Adding to panes */
            /* Ground Objects */
                ground.getChildren().addAll(bg, helipad);
                for(Pond p: ponds){
                    ground.getChildren().add(p);
                }
                getChildren().add(ground);
            /* End of Ground Objects */

            /* Sky objects */
                clouds.forEach((c) -> {
                    cloudCont.getChildren().add(c);
                });
                sky.getChildren().add(cloudCont);
                for(Distance d: distances){
                    sky.getChildren().add(d);
                }
                getChildren().add(sky);
                getChildren().add(helicopter); // HELICOPTER WILL BE ABOVE EVERYTHING
            /* End of sky objects */

        /* End of adding to panes */
    }

    /* GETTERS FOR PRIVATE VARIABLES */

    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    public Helipad getHelipad() {
        return helipad;
    }

    public Helicopter getHelicopter() {
        return helicopter;
    }

    public GameText getGt() {
        return gt;
    }

    public BackGroundImage getBg() {
        return bg;
    }

    public Pond[] getPonds() {
        return ponds;
    }

    public ArrayList<Cloud> getClouds() {
        return clouds;
    }

    public Blimp getBlimp() {
        return blimp;
    }

    public Distance[] getDistances() {
        return distances;
    }

    public double getTotalSaturation() {
        return totalSaturation;
    }

    public int getCloudCounter() {
        return cloudCounter;
    }

    public Pane getGround() {
        return ground;
    }

    public Pane getSky() {
        return sky;
    }

    public Pane getCloudCont() {
        return cloudCont;
    }

}
