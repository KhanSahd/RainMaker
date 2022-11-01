package com.example.RainMaker;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.util.Random;

interface Updatable {
    void update();
}

class GameText {

    private Label s;
    GameText(int percent){
        s = new Label(percent + "%");
        s.setScaleY(-1);
    }

    public Label getString(){
        return s;
    }

    public void setString(int percent){
        s.setText(percent + "%");
    }
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
    GameText perc;

    Random r;
    int low = 800 / 3;
    int lowW;
    int high;
    int highW;
    int result;
    int resultW;

    Cloud(){
        ellipse = new Ellipse(60, 60);
        r = new Random();
        high = 800 - (int)ellipse.getRadiusY();
        lowW = (int)ellipse.getRadiusX();
        highW = 400 - (int)ellipse.getRadiusX();
        result = r.nextInt(high-low) + low;
        resultW = r.nextInt(highW - lowW) + lowW;
        ellipse.setFill(Color.WHITE);
        add(ellipse);
        translate(resultW, result);

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
        translate(150, 10);
    }
}

class Helicopter extends Group {
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

//        helipad.setTranslateX(150);
//        helipad.setTranslateY(10);
        //helipad.translate(150, 10);
        //cloud.translate(300, 500);
//        cloud.setTranslateY(result);
//        cloud.setTranslateX((Math.random() * 400) - cloud.getWidth());
        getChildren().addAll(cloud, helipad);
        setPrefSize(400, 800);

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

    public int getGameWidth(){
        return GAME_WIDTH;
    }

    public int getGameHeight(){
        return GAME_HEIGHT;
    }

    public static void main(String[] args) {
        Application.launch();
    }


}
