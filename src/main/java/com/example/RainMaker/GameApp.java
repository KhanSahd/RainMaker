package com.example.RainMaker;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

interface Updatable {
    void update();
}

class GameText {

}

abstract class GameObject extends Group {
    protected Translate myTranslation;
    protected Rotate myRotation;
    protected Scale myScale;

    public GameObject() {
        myTranslation = new Translate();
        myRotation = new Rotate();
        myScale = new Scale();
        this.getTransforms().addAll(myTranslation, myRotation, myScale);
    }

    public void rotate(double degrees) {
        myRotation.setAngle(degrees);
        myRotation.setPivotX(0);
        myRotation.setPivotY(0);
    }

    public void scale(double sx, double sy) {
        myScale.setX(sx);
        myScale.setY(sy);
    }

    public void translate(double tx, double ty) {
        myTranslation.setX(tx);
        myTranslation.setY(ty);
    }

    public double getMyRotation() {
        return myRotation.getAngle();
    }

    public void update() {
        for (Node n : getChildren()) {
            if (n instanceof Updatable)
                ((Updatable) n).update();
        }
    }

    void add(Node node) {
        this.getChildren().add(node);
    }

}

class Pond {

}

class Cloud extends GameObject {

    Ellipse ellipse;
    int count = 0;
    Cloud(){
        ellipse = new Ellipse(60, 60);
        ellipse.setFill(Color.WHITE);
        getChildren().add(ellipse);
    }
}

class Helipad extends GameObject {

    Ellipse ellipse;
    Rectangle rectangle;
    Helipad(){
        ellipse = new Ellipse(30, 30);
        ellipse.setStroke(Color.GRAY);
        rectangle = new Rectangle(80, 80);
        rectangle.setStroke(Color.GRAY);
        add(rectangle);
        add(ellipse);
        ellipse.setTranslateX(40);
        ellipse.setTranslateY(40);
        //translate(150, 10);
    }
}

class Helicopter {
    Helicopter(){

    }
}

class Game extends Pane {

    Helipad helipad;
    Helicopter helicopter;
    Cloud cloud;
    public Game() {
        helipad = new Helipad();
        cloud = new Cloud();
        helipad.translate(150, 10);
        cloud.translate(300, 500);
        getChildren().addAll(cloud, helipad);

    }

}

public class GameApp extends Application {
    private static final int GAME_WIDTH = 400;
    private static final int GAME_HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) {
        Game game = new Game();
        Scene scene = new Scene(game, GAME_WIDTH, GAME_HEIGHT, Color.BLACK);
        primaryStage.setTitle("GAME_WINDOW_TITLE");
        primaryStage.setScene(scene);
        game.setScaleY(-1);
        primaryStage.setResizable(false);

        primaryStage.show();

    }

    public static void main(String[] args) {
        Application.launch();
    }
}
