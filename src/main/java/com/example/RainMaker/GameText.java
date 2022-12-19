package com.example.RainMaker;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;

class GameText extends GameObject {

    private Label s;
    private Color color;
    private Boolean isPercent;
    private Boolean isFuel;

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
