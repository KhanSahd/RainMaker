package com.example.RainMaker;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Shape;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
