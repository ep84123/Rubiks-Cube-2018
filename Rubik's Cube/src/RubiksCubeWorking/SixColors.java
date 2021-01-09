package RubiksCubeWorking;

import javafx.scene.paint.Color;

public class SixColors {

    public static final Color[] sixColors = {Color.WHITE, Color.ORANGE,
            Color.GREEN, Color.YELLOW, Color.RED, Color.BLUE};

    public static int searchColor(Color c){
        int i = 0;
        for(; i < 6; i++) {
            if (c == sixColors[i])
                break;
        }
        if (i == 6)
            System.out.println("The color isn't one of the six colors of the cube");
        return i;
    }

    public static Color getNext (Color c) {
        return sixColors[(searchColor(c)+1) % 6];
    }

    public static Color getOpposite(Color c) {
        return getNext(getNext(getNext(c)));
    }

}


