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
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

interface Updatable {
    void update();
}

class GameText extends GameObject {

    private Label s;
    Color color;
    Boolean isPercent;

    GameText(int percent, Boolean isPercentage) {
        isPercent = isPercentage;
        if (isPercent) {
            s = new Label(percent + "%");
        } else {
            s = new Label("F:" + percent);
        }
        color = Color.BLUE;
        s.setTextFill(color);
        s.setScaleY(-1);
        add(s);
    }

    GameText(String text){
        isPercent = false;
        s = new Label(text);
        color = Color.BLUE;
        s.setTextFill(color);
        s.setScaleY(-1);
        s.setFont(new Font("Robot", 30));
        add(s);
    }

    public void setText(int percent) {
        if(isPercent){
            s.setText(percent + "%");
        } else {
            s.setText("F: " + percent);
        }
    }

    public void changeColor(Color c) {
        color = c;
        s.setTextFill(color);
    }

    @Override
    Shape getShapeBounds() {
        return null;
    }
}

abstract class GameObject extends Group {
    protected Translate myTranslation;
    protected Rotate myRotation;
    protected Scale myScale;
    Boolean alive;

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

    abstract Shape getShapeBounds();

    public boolean intersects(GameObject obj) {
        return alive && obj.alive &&
                !Shape.intersect(getShapeBounds(), obj.getShapeBounds()).getBoundsInLocal().isEmpty();
    }

}

class Pond extends GameObject implements Updatable {

    Ellipse ellipse;
    GameText percent;
    Random r;
    int low = 800 / 3;
    int lowW;
    int high;
    int highW;
    int result;
    int resultW;
    double radius;

    Pond() {
        alive = true;
        radius = Math.random() * 60;
        ellipse = new Ellipse(radius, radius);
        r = new Random();
        high = 800 - (int) ellipse.getRadiusY();
        lowW = (int) ellipse.getRadiusX();
        highW = 400 - (int) ellipse.getRadiusX();
        result = r.nextInt(high - low) + low;
        resultW = r.nextInt(highW - lowW) + lowW;
        ellipse.setFill(Color.BLUE);
        add(ellipse);
        translate(resultW, result);
        percent = new GameText((int) radius, true);
        percent.changeColor(Color.WHITE);
        add(percent);
        percent.translate(-8, -5);
    }

    public void update() {
        result = r.nextInt(high - low) + low;
        resultW = r.nextInt(highW - lowW) + lowW;
        translate(resultW, result);
    }

    @Override
    Shape getShapeBounds() {
        return ellipse;
    }

}

class Cloud extends GameObject implements Updatable {

    Ellipse ellipse;
    GameText percent;
    Random r;
    int low = 800 / 3;
    int lowW;
    int high;
    int highW;
    int result;
    int resultW;
    int rgbColor = 255;
    int saturation = 0;
    Boolean isRaining = false;

    Cloud() {
        alive = true;
        ellipse = new Ellipse(60, 60);
        r = new Random();
        high = 800 - (int) ellipse.getRadiusY();
        lowW = (int) ellipse.getRadiusX();
        highW = 400 - (int) ellipse.getRadiusX();
        result = r.nextInt(high - low) + low;
        resultW = r.nextInt(highW - lowW) + lowW;
        ellipse.setFill(Color.rgb(rgbColor, rgbColor, rgbColor));
        add(ellipse);
        translate(resultW, result);
        percent = new GameText(saturation, true);
        add(percent);
        percent.translate(-8, -5);

    }

    public void update() {
        if (saturation < 100) {
            rgbColor -= 1;
            ellipse.setFill(Color.rgb(rgbColor, rgbColor, rgbColor));
            saturation++;
            percent.setText(saturation);
            if (saturation == 30) {
                isRaining = true;
            }
        }
    }

    @Override
    Shape getShapeBounds() {
        return ellipse;
    }

}

class Helipad extends GameObject {

    Ellipse ellipse;
    Rectangle rectangle;

    Helipad() {
        alive = true;
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

    @Override
    Shape getShapeBounds() {
        return ellipse;
    }
}

class Helicopter extends GameObject implements Updatable {

    Ellipse e;
    Line l;
    Color color = Color.YELLOW;
    int fuel;
    GameText fText;
    int speed = 3;
    // double angle =
    int y = 60;
    int x = 190;
    Point2D loc = new Point2D(x, y);
    double rotation;

    Helicopter() {
        alive = true;
        e = new Ellipse(10, 10);
        l = new Line(0, 0, 0, 25);
        fuel = 25000;
        //rotation = Math.toRadians(getMyRotation());
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

    @Override
    Shape getShapeBounds() {
        return e;
    }

    public void update() {
        loc = loc.add(speed * Math.sin(-1*Math.PI*getMyRotation()/180), speed *
        Math.cos(-1*Math.PI*getMyRotation()/180));
        translate(loc.getX(), loc.getY());
        if(fuel > 0){
            fuel -= 10;
            fText.setText(fuel);
        }
    }

    public void handleKeyPress(KeyEvent evt) {
        if (evt.getCode() == KeyCode.RIGHT) {
            rotate(getMyRotation() - 15);
        }

        if (evt.getCode() == KeyCode.LEFT) {
            rotate(getMyRotation() + 15);
        }
    }

}

class Game extends Pane {

    static final int GAME_WIDTH = 400;
    static final int GAME_HEIGHT = 800;

    Set<KeyCode> keysDown = new HashSet<>();

    int key(KeyCode k) {
        return keysDown.contains(k) ? 1 : 0;
    }

    Helipad helipad;
    Helicopter helicopter;
    Cloud cloud;
    Pond pond;
    GameText gameOver;

    public Game() {
        helipad = new Helipad();
        cloud = new Cloud();
        helicopter = new Helicopter();
        pond = new Pond();
        if (pond.resultW > cloud.resultW && pond.resultW < cloud.resultW + cloud.ellipse.getRadiusX()) {
            if (pond.result < cloud.result && pond.result > cloud.result + cloud.ellipse.getRadiusY()) {
                pond.update();
            }
        }
        getChildren().addAll(pond, cloud, helipad, helicopter);
        setPrefSize(400, 800);

    }

    public void handleKeyPressed(KeyEvent e) {
        keysDown.add(e.getCode());
    }

    public void handleKeyReleased(KeyEvent e) {
        keysDown.remove(e.getCode());
    }

    public void checkGameStatus(){
        if(helicopter.fuel == 0){
            gameOver = new GameText("Game Over!");
            gameOver.translate((GAME_WIDTH / 2) - 80, GAME_HEIGHT / 2 + 30);
            gameOver.changeColor(Color.RED);
            getChildren().removeAll(pond, cloud, helicopter, helipad);
            getChildren().add(gameOver);
        }
    }

}

public class GameApp extends Application {

    // private static final int GAME_WIDTH = 400;
    // private static final int GAME_HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Game game = new Game();
        root.getChildren().add(game);
        Scene scene = new Scene(root, game.GAME_WIDTH, game.GAME_HEIGHT, Color.BLACK);
        scene.setOnKeyPressed(e -> {
            game.handleKeyPressed(e);
            game.helicopter.handleKeyPress(e);
            if (game.helicopter.intersects(game.cloud)) {
                if (game.key(KeyCode.SPACE) == 1) {
                    game.cloud.update();
                }
            }
        });

        scene.setOnKeyReleased(e -> {
            game.handleKeyReleased(e);
        });

        primaryStage.setTitle("GAME_WINDOW_TITLE");
        primaryStage.setScene(scene);
        game.setScaleY(-1);
        primaryStage.setResizable(false);
        primaryStage.show();

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (game.key(KeyCode.UP) == 1) {
                    game.helicopter.update();
                }

                game.checkGameStatus();
            }
        };

        gameLoop.start();

    }

    public static void main(String[] args) {
        Application.launch();
    }

}
