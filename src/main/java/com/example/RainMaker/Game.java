package com.example.RainMaker;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.util.*;

class Game extends Pane {

    static final int GAME_WIDTH = 800;
    static final int GAME_HEIGHT = 800;

    static final double WIND_SPEED = .3;

    Set<KeyCode> keysDown = new HashSet<>();

    int key(KeyCode k) {
        return keysDown.contains(k) ? 1 : 0;
    }

    Helipad helipad;

    Helicopter helicopter;


    GameText gt;

    BackGroundImage bg;


    Pond[] ponds = new Pond[]{new Pond(),new Pond(), new Pond()};

    //Cloud[] clouds = new Cloud[]{new Cloud(), new Cloud(), new Cloud(), new Cloud()};
    ArrayList<Cloud> clouds = new ArrayList<>(Arrays.asList(new Cloud(), new Cloud(),
            new Cloud(), new Cloud()));

    Iterator<Cloud> it = clouds.iterator();

    Blimp blimp;


    Distance[] distances = new Distance[]{
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

    double totalSaturation;


    public Game() throws FileNotFoundException {
        bg = new BackGroundImage();
        helipad = new Helipad();
        helicopter = new Helicopter();
        blimp = new Blimp();
        getChildren().addAll(bg, blimp, helipad);
        for(Pond p: ponds){
            getChildren().addAll(p);
        }
        for(Cloud c: clouds){
            getChildren().addAll(c);
        }
        for(Distance d: distances){
            getChildren().addAll(d);
        }
        getChildren().add(helicopter);
    }

    public void handleKeyPressed(KeyEvent e) {
        keysDown.add(e.getCode());
    }

    public void handleKeyReleased(KeyEvent e) {
        keysDown.remove(e.getCode());
    }

    public void checkGameStatus(){
        if(helicopter.getFuel() == 0){
            helicopter.alive = false;
            gt = new GameText("Game Over! \n Press 'R' to restart");
            gt.translate((GAME_WIDTH / 2) - 80, GAME_HEIGHT / 2 + 30);
            gt.changeColor(Color.RED);
            getChildren().removeAll(helicopter, helipad);
            getChildren().add(gt);
        }
        if(helicopter.getFuel() > 0 && helicopter.alive && totalSaturation >= 300 * 0.8 &&
                helicopter.intersects(helipad) && !helicopter.engineOn){
            gt = new GameText("You won! \n 'R' to continue");
            gt.translate((GAME_WIDTH / 2) - 120, (GAME_HEIGHT / 2) - 30);
            gt.changeColor(Color.BLACK);
            getChildren().add(gt);
        }
        totalSaturation = ponds[0].getRadius() + ponds[1].getRadius() + ponds[2].getRadius();
    }

    public void updateClouds(){

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

//    public void regenerate(){
//        getChildren().add(blimp);
//    }

    public void init() throws FileNotFoundException {
        getChildren().removeAll(helicopter, helipad, blimp);
        for(Cloud c: clouds){
            getChildren().remove(c);
        }
        for(Distance d: distances){
            getChildren().remove(d);
        }
        for(Pond p: ponds){
            getChildren().remove(p);
        }
        getChildren().remove(gt);
        clouds.clear();
        ponds = new Pond[]{new Pond(),new Pond(), new Pond()};
        //clouds = new Cloud[]{new Cloud(), new Cloud(), new Cloud(), new Cloud()};
        clouds = new ArrayList<>(Arrays.asList(new Cloud(), new Cloud(),
                new Cloud(), new Cloud()));
        helicopter = new Helicopter();
        helipad = new Helipad();
        blimp = new Blimp();
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
        getChildren().addAll(blimp, helipad);
        for(Pond p: ponds){
            getChildren().add(p);
        }
        for(Cloud c: clouds){
            getChildren().add(c);
        }
        for(Distance d: distances){
            getChildren().add(d);
        }
        getChildren().add(helicopter);
    }


}
