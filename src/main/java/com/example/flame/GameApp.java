package com.example.flame;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

interface Updatable {

}

class GameText {

}

abstract class GameObject extends Group {

}

class Pond {

}

class Cloud {

}

class Helipad {

}

class Helicopter {

}

class Game {

    public Game() {

    }

}

public class GameApp extends Application {
    private static final int GAME_WIDTH = 400;
    private static final int GAME_HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, GAME_WIDTH, GAME_HEIGHT, Color.BLACK);
        primaryStage.setTitle("GAME_WINDOW_TITLE");
        primaryStage.setScene(scene);
        root.setScaleY(-1);
        primaryStage.setResizable(false);

        primaryStage.show();

    }

    public static void main(String[] args) {
        Application.launch();
    }
}
