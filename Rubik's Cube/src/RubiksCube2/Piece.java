package RubiksCube2;

import javafx.scene.paint.Color;
import java.util.ArrayList;

public class Piece {

    private ArrayList<Front> fronts = new ArrayList<>();

    public Piece(){
    }

    public void addFront(Front front){
        if(fronts.size() < 3)
            fronts.add(front);
    }

    public void rotate(int index, int constant){
        for (Front front: fronts){
            if(!front.isOnFace(index))
                front.rotate(index, constant);
        }
    }

    public Color getLook(int index){
        for (Front front: fronts) {
            if (front.isOnFace(index))
                return front.getColor();
        }
        return null;
    }

    public boolean isPiece(int[] indexes){
        if (indexes.length != fronts.size())
            return false;
        indexes = arrangeBySize(indexes);
        for(int i = 0; i < indexes.length; i++){
            if(!fronts.get(i).isOnFace(indexes[i]))
                return false;
        }
        return true;
    }

    public Piece getPiece(){
        Piece piece = new Piece();
        for (int i = 0; i < fronts.size(); i++ )
            piece.addFront(fronts.get(i).getFront());
        return piece;
    }

    private int[] arrangeBySize(int[] ints){
        boolean arranged = true;
        int copy;
        while(!arranged){
            arranged = true;
            for(int i = 1; i < ints.length; i++){
                if (ints[i] < ints[i - 1]){
                    copy = ints[i];
                    ints[i] = ints[i-1];
                    ints[i-1] = copy;
                    arranged = false;
                }
            }
        }
        return ints;
    }
}
