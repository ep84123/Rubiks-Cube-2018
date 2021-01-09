package RubiksCubeWorking;

import javafx.scene.paint.Color;

public class Column {

    private Color[] column = new Color[3];
    private Face lean;

    public Column (Color[] column, Face lean){
        this.column = column;
        this.lean = lean;
    }

    public Column (Color one,Color two, Color three, Face lean){
        column[0] = one;
        column[1] = two;
        column[2] = three;
        this.lean = lean;
    }

    public void setCornerColor (int index, Color color){
        this.column[2-index] = color;
    }

    public Face getLean(){
        return this.lean;
    }

    public void setLean(Face lean){
        this.lean = lean;
    }
}
