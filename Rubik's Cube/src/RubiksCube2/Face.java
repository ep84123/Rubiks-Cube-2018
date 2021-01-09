package RubiksCube2;

import javafx.scene.paint.Color;

public class Face extends Layer {

    private Color faceColor;

    public Face(Color faceColor, Place[][] pieces, int index, String name){
        super(pieces, index, name);
        this.faceColor = faceColor;
    }

    public void addItems(){
        for (Place[] pieces: pieces) {
            for (Place piece : pieces)
                piece.getPiece().addFront(new Front(faceColor, index));
        }
    }

    //the Array starts at the corner between index 1 & 2 faces and continues with along index2 face
    public Color[][] getLook(int index1, int index2){
        System.out.println("starting memes");
        System.out.println(pieces[1][1].getPiece().getLook(index) == Color.WHITE);
        int[] indexes = new int[4];
        Color[][] look = new Color[pieces.length][pieces.length];
        for (int i = 0; i < look.length; i++) {
            for (int n = 0; n < look.length; n++) {
                if(pieces[i][n].getPiece().isPiece(new int[]{this.index, index1, index2})){
                    indexes[0] = i;
                    indexes[1] = n;
                }
                if(pieces[i][n].getPiece().isPiece(new int[]{this.index, index2, (index1 + 3) % 6})){
                    indexes[2] = i;
                    indexes[3] = n;
                }
            }
        }
        for (int i = 0; i < look.length; i++) {
            for (int n = 0; n < look.length; n++) {
                if (indexes[0] == indexes[2])
                    look[i][n] = pieces[Math.abs(indexes[0] - i)][indexes[1] + n*(indexes[3] - indexes[1])/(look.length - 1)].getPiece().getLook(this.index);
                else
                    look[i][n] = pieces[indexes[0] + n*(indexes[2] - indexes[0])/(look.length - 1)][Math.abs(indexes[1] - i)].getPiece().getLook(this.index);
            }
        }
        return look;
    }

    public boolean isSolved(){
        for (Place[] pieces: this.pieces) {
            for (Place piece: pieces) {
                if(piece.getPiece().getLook(index) != this.pieces[0][0].getPiece().getLook(index))
                    return false;
            }
        }
        return true;
    }
}
