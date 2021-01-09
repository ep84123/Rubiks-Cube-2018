package RubiksCubeWorking;

import javafx.scene.paint.Color;

public class AutoSolver {
    private Twist[] output;
    private Cube cube;
    private int phase = 9;
    private Color lastFace = Color.YELLOW;
    private Twist[] last = new Twist[]{Twist.R,Twist.dU,Twist.nR,Twist.nU,Twist.R,Twist.nU,Twist.nR,
        Twist.nL,Twist.dU,Twist.L,Twist.U,Twist.nL,Twist.U,Twist.L};

    public AutoSolver(Cube cube){
        this.cube = cube;
    }
    public Twist[] getNext(){
        switch (phase){
            case 9:
                phase9();
        }
        return output;
    }
    public void phase9(){
        boolean isFound = false;
        Color rightFace = Color.GREEN;
        Color topFace = Color.YELLOW;
        for (Face f: cube.getFaces()){
            for (Color c: f.getFaces()){
                if (f.getRow(c)[0] == f.getRow(c)[2] && f.getRow(c)[1] != f.getRow(c)[0]){
                    rightFace = f.getFaceColor();
                    topFace = c;
                    isFound = true;
                    break;
                }
            }
            if (isFound)
                break;
        }
        if (!isFound){
            for (Color c: cube.getFaces()[SixColors.searchColor(lastFace)].getFaces()){
                Color[] row = cube.getFaces()[SixColors.searchColor(c)].getRow(lastFace);
                if (row[0] != row[2] && row[0] != c && row[2] != c){
                    rightFace = c;
                    topFace = lastFace;
                    break;
                }
            }
        }
        cube.setState(calFrontFace(topFace,rightFace), topFace);
        output = last;
    }

    public Color calFrontFace(Color topFace, Color rightface){
        Face rightFace = cube.getFaces()[SixColors.searchColor(rightface)];
        int constant = 1;
        if(rightFace.getDirection())
            constant = -1;
        return rightFace.getFaces()[(rightFace.searchFaceOutOf4(topFace)+constant +4) % 4];

    }

    public Twist[] getLast(){
        return last;
    }

}
