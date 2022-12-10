package com.example.RainMaker;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

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