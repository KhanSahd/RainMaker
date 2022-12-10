package com.example.RainMaker;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

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
