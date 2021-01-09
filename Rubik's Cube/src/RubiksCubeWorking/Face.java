package RubiksCubeWorking;

import javafx.scene.paint.Color;

public class Face {

    private Color faceColor;
    // the colors of this face
    private Color[] face = new Color[8];

    // true -> rotating   the face in the faces[] direction is with the clock direction
    private boolean direction;
    private Color opposite;
    private Color[] faces;

    public Face (Color faceColor, boolean direction){
        this.direction = direction;
        this.faceColor = faceColor;
        this.opposite = SixColors.getOpposite(faceColor);
        faces = new Color[]{SixColors.getNext(faceColor), SixColors.getNext(SixColors.getNext(faceColor)),
                SixColors.getOpposite(SixColors.getNext(faceColor)), SixColors.getOpposite(SixColors.getNext(SixColors.getNext(faceColor)))};
        for (int i = 0; i < 8; i++)
            face[i] = faceColor;
    }

    //rotates the face by 90 degrees, true = with the clock
    public void rotate(boolean Direction) {
        int constant = -2;
        if (direction != Direction)
            constant = 2;

        Color[] copy = new Color[8];
        for (int i = 0; i<8; i++)
            copy[i] = face[i];
        for( int i = 0; i < 8; i++)
            face[i] = copy [(i+constant+8) % 8];
    }

    public Color[] getFaces (){
        return faces;
    }

    public int searchFaceOutOf4(Color c){
        int i = 0;
        for(; i < 4; i++){
            if (c == faces[i])
                break;
        }
        if (i == 4){
            System.out.println("Exception: the Color isn't in the array");
        }
        return i;
    }

    public int[] searchRow (Color c){
        int i = searchFaceOutOf4(c);
        return new int[]{2*i,2*i + 1, (2*i+2) % 8};
    }

    public Color[] getRow(Color c){
        Color[] row = new Color[3];
        for (int i = 0; i < 3; i++)
            row[i] = face[searchRow(c)[i]];
        return row;
    }

    public Color[] getAndSetRow (Color[] row, Color c, Face previous){

        Color[] Row = getRow(c);
        for (int i = 0; i < 3; i++){
            if (direction == previous.getDirection())
                face[searchRow(c)[i]] = row[i];
            else
                face[searchRow(c)[i]] = row[2-i];
        }
        return Row;
    }

    public boolean getDirection(){
        return direction;
    }

    public Color[][] getVisualLook(Color c){
        Color[][] face = new Color[3][3];
        face[1][1] = faceColor;

        for (int i = 0; i < 3; i++){
            if (direction) {
                face[0][i] = getRow(c)[i];
                face[2][i] = getRow(SixColors.getOpposite(c))[2 - i];
            }
            else {
                face[0][i] = getRow(c)[2 - i];
                face[2][i] = getRow(SixColors.getOpposite(c))[i];
            }
        }
        int[] v;
        if (direction)
            v = searchRow(c);
        else
            v = searchRow(SixColors.getOpposite(c));
        face[1][0] = this.face[(v[0] - 1 +8) % 8];
        face[1][2] = this.face[(v[2] + 1) % 8];

        return face;
    }

    public boolean isSolve(){
        for(int i = 0; i < 8; i++){
            if (faceColor != face[i])
                return false;
        }
        return true;
    }

    public Color getFaceColor(){
        return faceColor;
    }

    public Color[] getFace(){
        return face;
    }

}
