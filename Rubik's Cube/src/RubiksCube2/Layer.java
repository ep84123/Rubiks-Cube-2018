package RubiksCube2;

public class Layer {

    protected Place [][] pieces;
    protected int index;
    protected String name;

    public Layer(Place [][] pieces, int index, String name){
        this.pieces = pieces;
        this.index = index;
        this.name = name;
    }

    public void rotate(boolean direction){
        int constant = -1;
        if (direction)
            constant = 1;
        Piece[][] copy = new Piece[pieces.length][pieces.length];
        for (int i = 0; i < pieces.length; i++){
            for(int n = 0; n < pieces.length; n++){
                pieces[i][n].getPiece().rotate(index, constant);
                copy[i][n] = pieces[i][n].getPiece().getPiece();
            }
        }
        for (int i = 0; i < pieces.length; i++){
            for(int n = 0; n < pieces.length; n++){
                int[] indexes = getNextIndexes(constant, i - 1, n -1);
                pieces[indexes[0]+1][indexes[1]+1].setPiece(copy[i][n]);
            }
        }
    }

    public static double calAngle(int x, int y) {
        if (x == 0) {
            if (y < 0)
                return -90.0;
            else if (y > 0)
                return 90.0;
        } else
            return Math.atan(((double) y) / x)/Math.PI*180.0;
        return 0.0;
    }

    public  static double calDistance(int x, int y){
        return Math.sqrt((double) (x*x + y*y));
    }

    public static int[] getNextIndexes(int constant, int i, int n){
        double d = calDistance(i, n);
        double a = (calAngle(i, n) - constant* 90.0)*Math.PI/180.0;
        return new int[]{(int) Math.round(d*Math.cos(a)), (int) Math.round(d*Math.sin(a))};
    }

    public String getName() {
        return name;
    }
}
