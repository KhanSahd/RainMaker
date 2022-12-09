package com.example.RainMaker;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
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
    Boolean isFuel;

    GameText(int percent, Boolean isPercentage) {
        isPercent = isPercentage;
        if (isPercent) {
            s = new Label(percent + "%");
            isFuel = false;
        } else {
            s = new Label("F:" + percent);
            isFuel = true;
        }
        color = Color.BLUE;
        s.setTextFill(color);
        s.setScaleY(-1);
        add(s);
    }

    GameText(String text){
        isPercent = false;
        isFuel = false;
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
        }
        if (isFuel){
            s.setText("F: " + percent);
        } else {
            s.setText(String.valueOf(percent));
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
    Point2D result;
    double radius;

    Pond() {
        alive = true;
        radius = Math.random() * 60;
        ellipse = new Ellipse(radius, radius);
        r = new Random();
        high = 800 - (int) ellipse.getRadiusY();
        lowW = (int) ellipse.getRadiusX();
        highW = Game.GAME_WIDTH - (int) ellipse.getRadiusX();
        result = new Point2D((r.nextInt(highW - lowW) + lowW), (r.nextInt(high - low) + low) );
        ellipse.setFill(Color.BLUE);
        add(ellipse);
        translate(result.getX(), result.getY());
        percent = new GameText((int) radius, true);
        percent.changeColor(Color.WHITE);
        add(percent);
        percent.translate(-8, -5);
    }

    public void update() {
        result = new Point2D((r.nextInt(highW - lowW) + lowW), (r.nextInt(high - low) + low));
        translate(result.getX(), result.getY());
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

    public Point2D getLoc(){
        return result;
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
    double result;
    double resultW;

    Point2D loc;
    int rgbColor = 255;
    double saturation = 0;
    Boolean isRaining = false;

    Pond[] ponds;

    Cloud() {
        alive = true;
        ellipse = new Ellipse(60,  60);
        r = new Random();
        high = Game.GAME_HEIGHT - (int) ellipse.getRadiusY();
        lowW = (int) ellipse.getRadiusX();
        highW = Game.GAME_WIDTH - (int) ellipse.getRadiusX();
        result = r.nextInt(high - low) + low;
        resultW = r.nextInt(highW - lowW) + lowW;
        loc = new Point2D(resultW, result );
        ellipse.setFill(Color.rgb(rgbColor, rgbColor, rgbColor));
        add(ellipse);
        translate(loc.getX(), loc.getY());
        percent = new GameText((int)saturation, true);
        add(percent);
        percent.translate(-8, -5);

    }

    public void update() {
        resultW += Game.WIND_SPEED;
        result += Game.WIND_SPEED;
        loc = new Point2D(resultW, result);
        translate(loc.getX(), loc.getY());
        if (result >= Game.GAME_HEIGHT){
            resultW = resultW - resultW;
            result = result - result ;
            loc = new Point2D(resultW, result);
        }
    }




    public void seed(Boolean seeding){
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


    public Point2D getLoc(){
        return loc;
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
    Boolean isShowing = false;


    Helipad() throws FileNotFoundException {
        alive = true;
        FileInputStream file = new FileInputStream("images/helipad.png");
        Image img = new Image(file);
        ImageView helipad = new ImageView(img);
        helipad.setFitWidth(80);
        helipad.setFitHeight(80);
        helipad.preserveRatioProperty();
        Rectangle rct = new Rectangle(helipad.getFitWidth() + 15, helipad.getFitWidth() + 15, Color.WHITE);
        rct.setTranslateX(-7);
        rct.setTranslateY(-7);
        shapeBound = new Rectangle(rct.getWidth() + 10, rct.getHeight() + 10);
        shapeBound.setStroke(Color.YELLOW);
        shapeBound.setFill(Color.rgb(255, 255, 255, 0.1));
        shapeBound.setStrokeWidth(3);
        shapeBound.setTranslateX(-12);
        shapeBound.setTranslateY(-12);

        shapeBound.setVisible(false);
        add(shapeBound);
        add(rct);
        add(helipad);
        translate(150, 30);
    }

    public void update(){
        if(isShowing == false){
            shapeBound.setVisible(false);
        }
        if(isShowing == true){
            shapeBound.setVisible(true);
        }
    }

    public void toggle(){
        isShowing = !isShowing;
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
            if(speed < 10.1){
                speed += 0.1;
            }
        }
    }
    public void decreaseSpeed(){
        if(engineOn){
            if(speed > -2){
                speed -= 0.1;
            }
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
        FileInputStream file = new FileInputStream("images/bg2.jpg");
        Image img = new Image(file);
        ImageView map = new ImageView(img);
        setScaleY(-1.5);
        setScaleX(1.5);
        add(map);
    }

    @Override
    Shape getShapeBounds() {
        return null;
    }
}

class Distance extends GameObject implements Updatable {

    Line line;
    int distance;
    Label l;
    Point2D loc;
    Boolean isShowing = false;

    public Distance(Point2D c, Point2D p){
        line = new Line(c.getX(), c.getY(), p.getX(), p.getY());
        loc = new Point2D(line.getTranslateX(), line.getTranslateY());
        line.setStroke(Color.RED);
        distance = (int)Math.sqrt(Math.pow((p.getX() - c.getX()), 2) + Math.pow((p.getY() - c.getY()), 2));
        l = new Label(String.valueOf(distance));
        l.setFont(new Font(15));
        l.setTextFill(Color.WHITE);
        l.setScaleY(-1);
        l.setTranslateX(loc.getX() + (p.getX() - c.getX()) / 2);
        l.setTranslateY(loc.getY() + (p.getY() - c.getY()) / 2);
        getChildren().addAll(line, l);
    }

    public void update(Cloud c, Pond p){
        loc = new Point2D(c.getLoc().getX(), c.getLoc().getY());
        line.setStartX(loc.getX());
        line.setStartY(loc.getY());
        distance = (int)Math.sqrt(Math.pow((p.getLoc().getX() - c.getLoc().getX()), 2) +
                Math.pow((p.getLoc().getY() - c.getLoc().getY()), 2));
        l.setTranslateX(loc.getX() + (p.getLoc().getX() - c.getLoc().getX()) / 2);
        l.setTranslateY(loc.getY() + (p.getLoc().getY() - c.getLoc().getY()) / 2);
        l.setText(String.valueOf(distance));
        if(isShowing){
            line.setVisible(true);
            l.setVisible(true);
        } else {
            line.setVisible(false);
            l.setVisible(false);
        }
    }

    public int getDistance(){
        return distance;
    }

    public void toggle(){
        isShowing = !isShowing;
    }


    @Override
    Shape getShapeBounds() {
        return line;
    }
}

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

public class GameApp extends Application {


    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Game game = new Game();
        Scene scene = new Scene(game, game.GAME_WIDTH, game.GAME_HEIGHT);
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

        scene.setOnKeyReleased(e -> {
            game.handleKeyReleased(e);
        });

        primaryStage.setTitle("RainMaker");
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
                for(Cloud c: game.clouds){
                    if (c.getSaturation() >= 30){
                        game.ponds[0].makeRain((int) game.clouds[0].getSaturation());
                        game.ponds[1].makeRain((int) game.clouds[1].getSaturation());
                        game.ponds[2].makeRain((int) game.clouds[2].getSaturation());
                    }
                }

                if(game.key(KeyCode.SPACE) == 0){
                    game.clouds[0].seed(false);
                    game.clouds[1].seed(false);
                    game.clouds[2].seed(false);
                    game.clouds[3].seed(false);
                }

                for(Cloud c: game.clouds){
                    c.update();
                }
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
