package RubiksCube2;

import javafx.scene.paint.Color;

public class Front {

    private final Color color;
    private int index;

    public Color getColor() {
        return color;
    }

    public Front(Color color, int index){
        this.color = color;
        this.index = index;
    }

    public void rotate(int index, int constant) {
        if (index % 2 == 0)
            constant *= -1;
        if ((this.index + constant - index) % 3 == 0)
            constant *= 2;
        this.index = (this.index + constant + 6) % 6;
    }

    public Front getFront(){
        return new Front(color, index);
    }

    public boolean isOnFace(int index){
        return this.index == index;
    }
}
