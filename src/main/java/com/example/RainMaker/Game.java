package com.example.RainMaker;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

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

    Cloud[] clouds = new Cloud[]{new Cloud(), new Cloud(), new Cloud(), new Cloud()};


    Distance[] distances = new Distance[]{
            new Distance(clouds[0].getLoc(), ponds[0].getLoc()), // index 0
            new Distance(clouds[0].getLoc(), ponds[1].getLoc()), // index 1
            new Distance(clouds[0].getLoc(), ponds[2].getLoc()), // index 2
            new Distance(clouds[1].getLoc(), ponds[0].getLoc()), // index 3
            new Distance(clouds[1].getLoc(), ponds[1].getLoc()), // index 4
            new Distance(clouds[1].getLoc(), ponds[2].getLoc()), // index 5
            new Distance(clouds[2].getLoc(), ponds[0].getLoc()), // index 6
            new Distance(clouds[2].getLoc(), ponds[1].getLoc()), // index 7
            new Distance(clouds[2].getLoc(), ponds[2].getLoc()), // index 8
            new Distance(clouds[3].getLoc(), ponds[0].getLoc()), // index 9
            new Distance(clouds[3].getLoc(), ponds[1].getLoc()), // index 10
            new Distance(clouds[3].getLoc(), ponds[2].getLoc())  // index 11
    };

    double totalSaturation;


    public Game() throws FileNotFoundException {
        bg = new BackGroundImage();
        helipad = new Helipad();
        helicopter = new Helicopter();
        getChildren().add(bg);
        for(Cloud c: clouds){
            getChildren().add(c);
        }
        for(Pond p: ponds){
            getChildren().add(p);
        }
        for(Distance d: distances){
            getChildren().add(d);
        }
        getChildren().addAll(helipad, helicopter);
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
            gt.translate((GAME_WIDTH / 2) + 120, (GAME_HEIGHT / 2) + 30);
            gt.changeColor(Color.BLACK);
            getChildren().add(gt);
        }
        totalSaturation = ponds[0].getRadius() + ponds[1].getRadius() + ponds[2].getRadius();
    }

    public void updateDistance(){
        distances[0].update(clouds[0], ponds[0]);
        distances[1].update(clouds[0], ponds[1]);
        distances[2].update(clouds[0], ponds[2]);
        distances[3].update(clouds[1], ponds[0]);
        distances[4].update(clouds[1], ponds[1]);
        distances[5].update(clouds[1], ponds[2]);
        distances[6].update(clouds[2], ponds[0]);
        distances[7].update(clouds[2], ponds[1]);
        distances[8].update(clouds[2], ponds[2]);
        distances[9].update(clouds[3], ponds[0]);
        distances[10].update(clouds[3], ponds[1]);
        distances[11].update(clouds[3], ponds[2]);
    }

    public void init() throws FileNotFoundException {
        getChildren().removeAll(helicopter, helipad, gt);
        for(Cloud c: clouds){
            getChildren().remove(c);
        }
        for(Distance d: distances){
            getChildren().remove(d);
        }
        for(Pond p: ponds){
            getChildren().removeAll(p);
        }
        ponds = new Pond[]{new Pond(),new Pond(), new Pond()};
        clouds = new Cloud[]{new Cloud(), new Cloud(), new Cloud(), new Cloud()};
        helicopter = new Helicopter();
        helipad = new Helipad();
        distances = new Distance[]{
                new Distance(clouds[0].getLoc(), ponds[0].getLoc()), // index 0
                new Distance(clouds[0].getLoc(), ponds[1].getLoc()), // index 1
                new Distance(clouds[0].getLoc(), ponds[2].getLoc()), // index 2
                new Distance(clouds[1].getLoc(), ponds[0].getLoc()), // index 3
                new Distance(clouds[1].getLoc(), ponds[1].getLoc()), // index 4
                new Distance(clouds[1].getLoc(), ponds[2].getLoc()), // index 5
                new Distance(clouds[2].getLoc(), ponds[0].getLoc()), // index 6
                new Distance(clouds[2].getLoc(), ponds[1].getLoc()), // index 7
                new Distance(clouds[2].getLoc(), ponds[2].getLoc()), // index 8
                new Distance(clouds[3].getLoc(), ponds[0].getLoc()), // index 9
                new Distance(clouds[3].getLoc(), ponds[1].getLoc()), // index 10
                new Distance(clouds[3].getLoc(), ponds[2].getLoc())  // index 11
        };
        gt = new GameText("");
        for(Cloud c: clouds){
            getChildren().add(c);
        }
        for(Distance d: distances){
            getChildren().add(d);
        }
        for(Pond p: ponds){
            getChildren().add(p);
        }
        getChildren().addAll(helipad, helicopter);
    }


}
