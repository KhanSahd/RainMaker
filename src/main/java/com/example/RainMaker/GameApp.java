package com.example.RainMaker;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.util.EventListener;
import java.util.Random;

interface Updatable {
    void update();
}

class GameText extends GameObject {

    private Label s;
    Color color;
    Boolean isPercent;

    GameText(int percent, Boolean isPercentage){
        isPercent = isPercentage;
        if (isPercent){
        s = new Label(percent + "%");
        } else {
            s = new Label("F:" + percent);
        }
        color = Color.BLUE;
        s.setTextFill(color);
        s.setScaleY(-1);
        add(s);
    }



    public void changeColor(Color c){
        color = c;
        s.setTextFill(color);
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
    GameText percent;
    Random r;
    int low = 800 / 3; int lowW;
    int high; int highW;
    int result; int resultW;

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
        percent = new GameText(0, true);
        add(percent);
        percent.translate(-8, -5);

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
        translate(150, 20);
    }
}

class Helicopter extends GameObject {

    Ellipse e;
    Line l;
    Color color = Color.YELLOW;
    int fuel;
    GameText fText;

    int y = 60;
    int x = 190;
    Point2D loc = new Point2D(x, y);

    Helicopter(){
        e = new Ellipse(10, 10);
        l = new Line(0, 0, 0, 25);
        fuel = 25000;
        fText = new GameText(fuel, false);
        e.setFill(color);
        l.setStroke(color);
        add(e);
        add(l);
        add(fText);
        fText.translate(-25, -30);
        fText.changeColor(Color.YELLOW);
        translate(loc.getX(), loc.getY());
    }

    public void handleKeyPress(KeyEvent evt){
        if(evt.getCode() == KeyCode.UP){
            if(getMyRotation() == 0.0){
            y += 5;
            translate(x, y);
            }
            if(getMyRotation() == 90.0){
                x -= 5;
                translate(x, y);
            }
            if(getMyRotation() < 0 ){
                y+= 5;
                x += 5;
                translate(x,y);
            }
            if(getMyRotation() > 0 && getMyRotation() < 90.0){
                y += 5;
                x -= 5;
                translate(x,y);
            }
            if(getMyRotation() > 90.0 && getMyRotation() < 180.0){
                y -= 5;
                x -= 5;
                translate(x, y);
            }
            if (getMyRotation() == 180.0){
                y -= 5;
                translate(x, y);
            }
            if(getMyRotation() > 180.0 && getMyRotation() < -90.0){
                y -= 5;
                x += 5;
                translate(x, y);
            }

        }

        if(evt.getCode() == KeyCode.RIGHT){
            rotate(getMyRotation() - 3);
            System.out.println(getMyRotation());

        }
        if(evt.getCode() == KeyCode.LEFT){
            rotate(getMyRotation() + 3);
            System.out.println(getMyRotation());
        }
    }

}

class Game extends Pane {

    Helipad helipad;
    Helicopter helicopter;
    Cloud cloud;
    public Game() {
        helipad = new Helipad();
        cloud = new Cloud();
        helicopter = new Helicopter();
        getChildren().addAll(cloud, helipad, helicopter);
        setPrefSize(400, 800);

    }



}

public class GameApp extends Application {
    private static final int GAME_WIDTH = 400;
    private static final int GAME_HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Game game = new Game();
        root.getChildren().add(game);
        Scene scene = new Scene(root, GAME_WIDTH, GAME_HEIGHT, Color.BLACK);
        scene.setOnKeyPressed(e -> game.helicopter.handleKeyPress(e));
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
