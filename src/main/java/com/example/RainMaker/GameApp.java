package com.example.RainMaker;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.Key;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
    int pivot;

    public GameObject() {
        myTranslation = new Translate();
        myRotation = new Rotate();
        myScale = new Scale();
        pivot = 0;
        this.getTransforms().addAll(myTranslation, myRotation, myScale);
    }

    public void rotate(double degrees) {
        myRotation.setAngle(degrees);
        myRotation.setPivotX(pivot);
        myRotation.setPivotY(pivot);
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
        if (radius < 100){
            radius++;
            ellipse.setRadiusY(radius);
            ellipse.setRadiusY(radius);
        }
    }

    public double getRadius(){
        return radius;
    }

    public void makeRain(int sat){
        if (radius < 100){
            radius = radius + (sat * 0.001);
            ellipse.setRadiusX(radius);
            ellipse.setRadiusY(radius);
            percent.setText((int)radius);
        }
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
    double saturation = 0;
    Boolean isRaining = false;

    Cloud() {
        alive = true;
        ellipse = new Ellipse(60,  60);
        r = new Random();
        high = Game.GAME_HEIGHT - (int) ellipse.getRadiusY();
        lowW = (int) ellipse.getRadiusX();
        highW = Game.GAME_WIDTH - (int) ellipse.getRadiusX();
        result = r.nextInt(high - low) + low;
        resultW = r.nextInt(highW - lowW) + lowW;
        ellipse.setFill(Color.rgb(rgbColor, rgbColor, rgbColor));
        add(ellipse);
        translate(resultW, result);
        percent = new GameText((int)saturation, true);
        add(percent);
        percent.translate(-8, -5);

    }

    public void update(Boolean seeding) {
        if(seeding == true){
            if (saturation < 100) {
                rgbColor -= 1;
                ellipse.setFill(Color.rgb(rgbColor, rgbColor, rgbColor));
                saturation++;
                percent.setText((int)saturation);
                if (saturation == 30) {
                    isRaining = true;
                }
            }
        }
        if(seeding == false){
            if (saturation > 0){
                if (rgbColor < 255){
                    rgbColor += 0.04;
                }
                ellipse.setFill(Color.rgb(rgbColor,rgbColor,rgbColor));
                saturation -= 0.04;
                percent.setText((int) saturation);
                if (saturation < 30){
                    isRaining = false;
                }
            }
        }
    }


    public double getSaturation(){
        return saturation;
    }



    @Override
    Shape getShapeBounds() {
        return ellipse;
    }

}

class Helipad extends GameObject {

    Rectangle shapeBound;
    Boolean isShowing;


    Helipad() throws FileNotFoundException {
        alive = true;
        isShowing = false;
        FileInputStream file = new FileInputStream("images/helipad.png");
        Image img = new Image(file);
        ImageView helipad = new ImageView(img);
        helipad.setFitWidth(80);
        helipad.setFitHeight(80);
        helipad.preserveRatioProperty();
        Rectangle rct = new Rectangle(helipad.getFitWidth() + 15, helipad.getFitWidth() + 15, Color.WHITE);
        rct.setTranslateX(-7);
        rct.setTranslateY(-7);
        shapeBound = new Rectangle(rct.getWidth() + 5, rct.getHeight() + 5);
        shapeBound.setFill(Color.TAN);
        //shapeBound.setStroke(Color.YELLOW);
        shapeBound.setTranslateX(-9);
        shapeBound.setTranslateY(-9);
        add(shapeBound);
        add(rct);
        add(helipad);
        translate(150, 30);
    }





    @Override
    Shape getShapeBounds() {
        return shapeBound;
    }
}

class Helicopter extends GameObject implements Updatable {

    Ellipse e;
    Line l;
    Color color = Color.YELLOW;

    private int fuel;
    GameText fText;
    double speed = 0.0;

    private Point2D loc = new Point2D(190, 60);
    Boolean engineOn;
    ImageView img1;
    HeliBlades blades;

    Helicopter() throws FileNotFoundException {
        alive = true;
        engineOn = false;
        e = new Ellipse(10, 10);
        l = new Line(0, 0, 0, 25);
        fuel = 25000;
        /* HELICOPTER */
        FileInputStream file = new FileInputStream("images/helicopter.png");
        Image heli = new Image(file);
        img1 = new ImageView(heli);
        img1.setFitWidth(80);
        img1.setPreserveRatio(true);
        img1.setRotate(180);
        img1.setTranslateX(-40);
        img1.setTranslateY(-35);

        /* PROPELLERS  */
        blades = new HeliBlades();
        /* END OF PROPELLERS */

        fText = new GameText(fuel, false);
        e.setFill(color);
        l.setStroke(color);
        add(l);
        add(img1);
        add(blades);
        add(fText);
        fText.translate(-25, -45);
        fText.changeColor(Color.BLACK);
        translate(loc.getX(), loc.getY());

    }

    @Override
    Shape getShapeBounds() {
        return l;
    }

    public void update() {
        loc = loc.add(speed * Math.sin(-1*Math.PI*getMyRotation()/180), speed *
        Math.cos(-1*Math.PI*getMyRotation()/180));
        translate(loc.getX(), loc.getY());
        if(fuel > 0){
            fuel -= 5;
            fText.setText(fuel);
        }
        if(engineOn){
            //img2.setRotate(img2.getRotate() - 5);
            blades.rotate(blades.getMyRotation() - 5);

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

    public void startEngine() {
        if(speed < 0.1 && speed > -0.01){
            engineOn = !engineOn;
        }
    }

    public void increaseSpeed(){
        if(engineOn){
            speed += 0.1;
        }
    }
    public void decreaseSpeed(){
        if(engineOn){
            speed -= 0.1;
        }

    }

    public int getFuel(){
        return fuel;
    }

    public Point2D getLoc() {
        return loc;
    }



}

class HeliBlades extends GameObject {

    private Rectangle line1, line2;

    HeliBlades() {
        alive = true;
        line1 = new Rectangle(3, 30);
        line1.setScaleY(2);
        line1.setTranslateY(-15);
        line1.setTranslateX(-1);
        add(line1);
        line2 = new Rectangle(3, 30);
        line2.setRotate(90);
        line2.setScaleY(2);
        line2.setTranslateY(-15);
        translate(0, 5);
        add(line2);
    }



    @Override
    Shape getShapeBounds() {
        return line1;
    }
}


class BackGroundImage extends GameObject {

    BackGroundImage() throws FileNotFoundException {
        FileInputStream file = new FileInputStream("images/bg.png");
        Image img = new Image(file);
        ImageView map = new ImageView(img);
        setScaleY(-1);
        add(map);
    }

    @Override
    Shape getShapeBounds() {
        return null;
    }
}

class Game extends Pane {

    static final int GAME_WIDTH = 600;
    static final int GAME_HEIGHT = 800;

    Set<KeyCode> keysDown = new HashSet<>();

    int key(KeyCode k) {
        return keysDown.contains(k) ? 1 : 0;
    }

    Helipad helipad;
    Helicopter helicopter;
    Cloud cloud;
    //Pond pond;
    Pond pond;
    GameText gt;
    BackGroundImage bg;


    public Game() throws FileNotFoundException {
        bg = new BackGroundImage();
        helipad = new Helipad();
        cloud = new Cloud();
        helicopter = new Helicopter();
        pond = new Pond();
//        FileInputStream file = new FileInputStream("images/background.jpeg");
//        Image img = new Image(file);
//        ImageView bg = new ImageView(img);
//        bg.setFitWidth(GAME_WIDTH);
//        bg.setFitHeight(GAME_HEIGHT);
//        bg.setTranslateY(GAME_HEIGHT - GAME_HEIGHT);
        getChildren().addAll(pond, bg, cloud, helipad, helicopter);
        setBackground(new Background(new BackgroundFill(Color.TAN, CornerRadii.EMPTY, Insets.EMPTY)));
        setPrefSize(GAME_WIDTH, GAME_HEIGHT);

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
            getChildren().removeAll( cloud, helicopter, helipad);
            getChildren().add(gt);
        }
        if(helicopter.getFuel() > 0 && helicopter.alive && pond.getRadius() >= 100 &&
                helicopter.intersects(helipad) && !helicopter.engineOn){
            gt = new GameText("You won! \n 'R' to continue");
            gt.translate((GAME_WIDTH / 2) - 120, (GAME_HEIGHT / 2) + 30);
            gt.changeColor(Color.GREEN);
            getChildren().add(gt);
        }
        
    }

    public void init() throws FileNotFoundException {
        getChildren().removeAll(gt, pond, cloud, helicopter, helipad);
        helipad = new Helipad();
        helicopter = new Helicopter();
        cloud = new Cloud();
        pond = new Pond();
        gt = new GameText("");
        getChildren().addAll(gt, pond, cloud, helipad, helicopter);
    }

}

public class GameApp extends Application {


    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Game game = new Game();
        Scene scene = new Scene(game, game.GAME_WIDTH, game.GAME_HEIGHT);
        scene.setOnKeyPressed(e -> {
            game.handleKeyPressed(e);
            game.helicopter.handleKeyPress(e);
            if (game.helicopter.intersects(game.cloud)) {
                if (game.key(KeyCode.SPACE) == 1) {
                    game.cloud.update(true);
                }

            }

//            if(game.cloud.getSaturation() >= 30){
//                game.pond.makeRain((int) game.cloud.getSaturation());
//            }

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
            if(e.getCode() == KeyCode.B){
                game.helipad.update();
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
                if(game.helicopter.engineOn){
                    game.helicopter.update();
                }
                if(game.cloud.getSaturation() >= 30){
                    game.pond.makeRain((int) game.cloud.getSaturation());
                }
                if(game.key(KeyCode.SPACE) == 0){
                    game.cloud.update(false);
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
