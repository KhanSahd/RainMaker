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
            game.helicopter.handleKeyPress(e);
            for(Cloud c: game.clouds){
                if(c.intersects(game.helicopter)){
                    if(game.key(KeyCode.SPACE) == 1){
                        c.seed(true);
                    }
                }
            }

            if(e.getCode() == KeyCode.I){
                game.helicopter.startEngine();
            }
            if(e.getCode() == KeyCode.UP){
                game.helicopter.increaseSpeed();
            }
            if(e.getCode() == KeyCode.DOWN){
                game.helicopter.decreaseSpeed();
            }
            if(e.getCode() == KeyCode.R){
                try {
                    game.init();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if(e.getCode() == KeyCode.T){
                game.helipad.toggle();
            }
            if(e.getCode() == KeyCode.D){
                for(Distance d: game.distances){
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
            @Override
            public void handle(long nano) {

                if(game.helicopter.engineOn){
                    game.helicopter.update();
                }
                for(Cloud c: game.clouds){
                    if (c.getSaturation() >= 30){
                        game.ponds[0].makeRain((int) game.clouds.get(0).getSaturation());
                        game.ponds[1].makeRain((int) game.clouds.get(1).getSaturation());
                        game.ponds[2].makeRain((int) game.clouds.get(2).getSaturation());
                    }
                    c.update();
                }

                if(game.key(KeyCode.SPACE) == 0){
                    game.clouds.forEach((c) -> c.seed(false));
                }

                game.blimp.update();
                game.checkGameStatus();
                game.helipad.update();
                game.updateDistance();
            }
        };

        gameLoop.start();

    }

    public static void main(String[] args) {
        Application.launch();
    }

}
