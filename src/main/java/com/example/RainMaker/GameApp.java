package com.example.RainMaker;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import java.io.FileNotFoundException;

import static com.example.RainMaker.Game.GAME_HEIGHT;
import static com.example.RainMaker.Game.GAME_WIDTH;


public class GameApp extends Application {


    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Game game = new Game();
        Scene scene = new Scene(game, GAME_WIDTH, GAME_HEIGHT);
        scene.setOnKeyPressed(e -> {
            game.handleKeyPressed(e);
            game.getHelicopter().handleKeyPress(e);
            for(Cloud c: game.getClouds()){
                if(c.intersects(game.getHelicopter())){
                    if(game.key() == 1){
                        c.seed(true);
                    }
                }
            }

            if(e.getCode() == KeyCode.I){
                game.getHelicopter().startEngine();
            }
            if(e.getCode() == KeyCode.UP){
                game.getHelicopter().increaseSpeed();
            }
            if(e.getCode() == KeyCode.DOWN){
                game.getHelicopter().decreaseSpeed();
            }
            if(e.getCode() == KeyCode.R){
                try {
                    game.init();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if(e.getCode() == KeyCode.T){
                game.getHelipad().toggle();
            }
            if(e.getCode() == KeyCode.D){
                for(Distance d: game.getDistances()){
                    d.toggle();
                }
            }
        });

        scene.setOnKeyReleased(game::handleKeyReleased);

        primaryStage.setTitle("RainMaker");
        primaryStage.setScene(scene);
        game.setScaleY(-1);
        primaryStage.setResizable(false);
        primaryStage.show();

        AnimationTimer gameLoop = new AnimationTimer() {
            double old = -1;
            double elapsedTime = 0;
            @Override
            public void handle(long nano) {

                /* Getting seconds */
                    if(old < 0) {
                        old = nano;
                    }
                    double delta = (nano - old) / 1e9;
                    old = nano;
                    elapsedTime += delta;
                /* End of seconds */

                if(game.getHelicopter().getEngineOn()){
                    game.getHelicopter().update();
                }
                for(Cloud c: game.getClouds()){
                    if (c.getSaturation() >= 30){
                        game.getPonds()[0].makeRain((int) c.getSaturation());
                        game.getPonds()[1].makeRain((int) c.getSaturation());
                        game.getPonds()[2].makeRain((int) c.getSaturation());
                    }
                    c.update();
                }

                if(game.key() == 0){
                    game.getClouds().forEach((c) -> c.seed(false));
                }

                if(game.getHelicopter().intersects(game.getBlimp())){
                    game.getBlimp().fuelHelicopter();
                    game.getHelicopter().reFuel(game.getBlimp().getFuel());
                }
                game.generateClouds();
                game.getClouds().removeIf(e -> !e.alive);
                if(game.getHelicopter().getFuel() < 16000){
                    game.getBlimp().update();
                }
                try {
                    game.regenerateBlimp();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                if(game.getClouds().size() < game.getCloudCounter()){
                    game.getClouds().add(new Cloud());
                    game.getCloudCont().getChildren().add(game.getClouds().get(game.getClouds().size() - 1));
                }

                game.checkGameStatus();
                game.getHelipad().update();
                game.updateDistance();
                game.getHelicopter().playSound();
                game.getMediaPlayer().play();
            }
        };

        gameLoop.start();

    }

    public static void main(String[] args) {
        Application.launch();
    }

}
