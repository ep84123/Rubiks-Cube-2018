package RubiksCubeWorking;

public class Twist {

    public final Actions act;
    public  final boolean direction;
    public final int times;

    public Twist(Actions act, boolean direction, int times){
        this.act = act;
        this.direction = direction;
        this.times = times;
    }

    public static final Twist R = new Twist(Actions.R, true, 1);
    public static final Twist nR = new Twist(Actions.R, false, 1);
    public static final Twist dR = new Twist(Actions.R, true, 2);
    public static final Twist L = new Twist(Actions.L, true, 1);
    public static final Twist nL = new Twist(Actions.L, false, 1);
    public static final Twist dL = new Twist(Actions.L, true, 2);
    public static final Twist F = new Twist(Actions.F, true, 1);
    public static final Twist nF = new Twist(Actions.F, false, 1);
    public static final Twist dF = new Twist(Actions.F, true, 2);
    public static final Twist B = new Twist(Actions.B, true, 1);
    public static final Twist nB = new Twist(Actions.B, false, 1);
    public static final Twist dB = new Twist(Actions.B, true, 2);
    public static final Twist U = new Twist(Actions.U, true, 1);
    public static final Twist nU = new Twist(Actions.U, false, 1);
    public static final Twist dU = new Twist(Actions.U, true, 2);
    public static final Twist D = new Twist(Actions.D, true, 1);
    public static final Twist nD = new Twist(Actions.D, false, 1);
    public static final Twist dD = new Twist(Actions.D, true, 2);

    public static final Twist[] OptionalMoves= new Twist[]{R,nR,dR,L,nL,dL,F,nF,dF,B,nB,dB,U,nU,dU,D,nD,dD};
    

}
