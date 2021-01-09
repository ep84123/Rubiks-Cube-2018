package RubiksCube2;

import javafx.scene.paint.Color;

import java.util.Random;

public class Cube {

    private Place[][][] pieces;
    private Face[] faces;
    private Layer[][] layers;
    private int rightFaceIndex = 2;
    private int frontFaceIndex = 1;
    private int topFaceIndex = 0;
    private Random random = new Random();

    public Cube(int size){
        pieces = new Place[size][size][size];
        for(int i = 0; i < size; i++){
            for(int n = 0; n < size; n++){
                for(int r = 0; r < size; r++){
                    pieces[i][n][r] = new Place();
                }
            }
        }
        layers = new Layer[3][size];
        faces = new Face[]{
                new Face(Color.WHITE, createFace(0, true), 0, "U"),
                new Face(Color.ORANGE, pieces[0], 1, "F"),
                new Face(Color.GREEN, createFace(0,false),2,"R"),
                new Face(Color.YELLOW, createFace(size - 1, true),3, "D"),
                new Face(Color.RED, pieces[size -1 ],4, "B"),
                new Face(Color.BLUE, createFace(size - 1, false), 5, "L")
        };
        for(int i = 0; i < 3; i++){
            layers[i][0] = faces[i];
            layers[i][size - 1] = faces[i + 3];
        }
        for (int i = 1; i < size -1; i++){
            layers[0][i] = new Layer(createFace(i, true), 0, "U"+ i);
            layers[1][i] = new Layer(pieces[i], 1, "F" + i);
            layers[2][i] = new Layer(createFace(i,false),2, "R" + i);
        }
        for (Face face:faces)
            face.addItems();
    }

    public void rotate( int i, int n){
        layers[i/2][n].rotate(i % 2 == 0);
    }

    public boolean isSolved(){
        for (Face face:faces) {
            if(!face.isSolved())
                return false;
        }
        return true;
    }

    public Color[][][] getCubeLook(){
        System.out.println("dammmnvnvn");
        return new Color[][][]{
                faces[frontFaceIndex].getLook((rightFaceIndex + 3) % 6, topFaceIndex),
                faces[topFaceIndex].getLook((rightFaceIndex + 3) % 6, (frontFaceIndex + 3) % 6),
                faces[rightFaceIndex].getLook(frontFaceIndex,topFaceIndex)
        };
    }

    public void setState(int frontFaceIndex, int topFaceIndex){
        this.frontFaceIndex = frontFaceIndex;
        this.topFaceIndex = topFaceIndex;
        calRightFaceIndex();
    }

    public void mixCube(){
        for(int i = 0; i < 500; i++)
            rotate(random.nextInt(6), random.nextInt(pieces.length));
    }

    private void calRightFaceIndex(){
        int constant = 1;
        if (frontFaceIndex % 2 == 0)
            constant = -1;
        if ((topFaceIndex + constant - frontFaceIndex) % 3 == 0)
            constant *= 2;
        rightFaceIndex = (topFaceIndex + constant + 6) % 6;
    }

    private Place[][] createFace(int layer, boolean b){
        Place[][] pieces = new Place[this.pieces.length][this.pieces.length];
        for (int i = 0; i < pieces.length; i++) {
            for (int n = 0; n < pieces.length; n++){
                if (b)
                    pieces[i][n] = this.pieces[i][layer][n];
                else
                    pieces[i][n] = this.pieces[i][n][layer];
            }
        }
        return pieces;
    }

    public String getLayerName(int i, int n){
        return layers[i][n].getName();
    }

    public int getRightFaceIndex() {
        return rightFaceIndex;
    }

    public int getFrontFaceIndex() {
        return frontFaceIndex;
    }

    public int getTopFaceIndex() {
        return topFaceIndex;
    }
}
