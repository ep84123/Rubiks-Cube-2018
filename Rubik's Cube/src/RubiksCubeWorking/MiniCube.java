package RubiksCubeWorking;

import javafx.scene.paint.Color;

public class MiniCube {

    private Color color;
    private Color firstLean;
    private Color secondLean;

    public MiniCube (Color color, Color firstLean){
        this (color,firstLean,null);
    }

    public MiniCube(Color color, Color firstLean, Color secondLean){
        this.color = color;
        this.firstLean = firstLean;
        this.secondLean = secondLean;
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getFirstLean() {
        return firstLean;
    }

    public void setFirstLean(Color firstLean) {
        this.firstLean = firstLean;
    }

    public Color getSecondLean() {
        return secondLean;
    }

    public void setSecondLean(Color secondLean) {
        this.secondLean = secondLean;
    }
}
