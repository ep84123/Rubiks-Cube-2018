package RubiksCubeWorking;

import javafx.scene.paint.Color;
import java.util.Random;

public class Cube {

    private Face[] Faces;
    // Color [0] stands for the front face
    // Color [1] stands for the up face
    private Color[] State;
    private Color rightFace;
    private Random r = new Random();


    public Cube (){
        Faces = new Face[6];
        for (int i = 0; i < 6; i++)
            Faces [i] = new Face(SixColors.sixColors[i], i % 2 != 0);
        State = new Color[]{Color.ORANGE, Color.WHITE};
        calcRightFace();

    }

    private void rotate (Color c, boolean direction){
        int constant = -1;
        if (direction == Faces[SixColors.searchColor(c)].getDirection())
            constant = 1;
        Faces[SixColors.searchColor(c)].rotate(direction);
        int[] indexes = new int[4];
        for(int i = 0; i < 4; i++)
            indexes[i] = SixColors.searchColor(Faces[SixColors.searchColor(c)].getFaces()[(i*constant + 4) % 4]);
        //Color[] fourFaces = Faces[RubiksCubeWorking.SixColors.searchColor(c)].getFaces();
        //Faces[RubiksCubeWorking.SixColors.searchColor(fourFaces[0])].getRow(c);
        Faces[indexes[0]]
                .getAndSetRow(Faces[indexes[3]]
                .getAndSetRow(Faces[indexes[2]]
                .getAndSetRow(Faces[indexes[1]]
                .getAndSetRow(Faces[indexes[0]]
                        .getRow(c),c, Faces[indexes[0]] ),c, Faces[indexes[1]] )
                        ,c, Faces[indexes[2]]),c, Faces[indexes[3]] );
    }

    private void rotate (Color c, boolean direction, int times){
        for (int i = 0; i < times; i++)
        this.rotate(c, direction);
    }

    public void rotate (Twist move){
        boolean direction = move.direction;
        int times = move.times;
        switch(move.act){
            case F:
                this.rotate(State[0], direction, times);
                break;
            case B:
                this.rotate(SixColors.getOpposite(State[0]), direction, times);
                break;
            case U:
                this.rotate(State[1], direction, times);
                break;
            case D:
                this.rotate(SixColors.getOpposite(State[1]), direction, times);
                break;
            case R:
                this.rotate(rightFace, direction, times);
                break;
            case L:
                this.rotate(SixColors.getOpposite(rightFace), direction, times);
        }
    }

    public Color[] getState() {
        return State;
    }

    public void setState(Color state0, Color state1) {
        State[0] = state0;
        State[1] = state1;
        calcRightFace();
    }

    public void mixCube(){
        for(int i = 0; i < 500; i++)
            rotate(Twist.OptionalMoves[r.nextInt(11)]);
    }

    public boolean isSolved(){
        //RubiksCubeWorking.Cube m = new RubiksCubeWorking.Cube();
        //m.setState(this.State);
        //return this .equals(m);
        for(int i = 0; i < 6; i++){
            if (!Faces[i].isSolve())
                return false;
        }
        return true;
    }

    private void calcRightFace(){
        int constant = -1;
        if (Faces[SixColors.searchColor(State[0])].getDirection())
            constant = 1;
        rightFace = Faces[SixColors.searchColor(State[0])].getFaces()[(Faces[SixColors.searchColor(State[0])].searchFaceOutOf4(State[1]) + constant + 4) % 4];
    }

    public Color[][][] getCubeLook (){
        return new Color[][][]{Faces[SixColors.searchColor(State [0])].getVisualLook(State [1]),
                Faces[SixColors.searchColor(State [1])].getVisualLook(SixColors.getOpposite(State [0])),
                Faces[SixColors.searchColor(rightFace)].getVisualLook(State [1])};
        }

    public Face[] getFaces() {
        return Faces;
    }

    public Color getRightFace() {
        return rightFace;
    }
}
