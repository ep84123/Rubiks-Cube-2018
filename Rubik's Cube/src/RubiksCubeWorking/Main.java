package RubiksCubeWorking;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    Stage window;
    String[] colors = new String[]{"White", "Orange", "Green", "Yellow", "Red", "Blue"};
    Cube cube;
    AutoSolver solver;
    Polygon [][][] cubeLook = new Polygon[3][3][3];
    Pane g = new Pane();
    ArrayList<Twist> moves = new ArrayList<>();
    int index = 0;
    int undoGap = 0;
    Button R;
    Button nR;
    Button dR;
    Button L;
    Button nL;
    Button dL;
    Button F;
    Button nF;
    Button dF;
    Button D;
    Button nD;
    Button dD;
    Button B;
    Button nB;
    Button dB;
    Button U;
    Button nU;
    Button dU;
    Button pause;
    boolean isMixed = false;
    Label timer;
    //Thread stopWatch;
    String time;
    long startTime;
    boolean isPaused = false;
    long timeTaken = 0;
    Cube cube2;


    //RubiksCubeWorking.Tuple<Color,String>[] tuples;

    //new RubiksCubeWorking.Tuple<Color, String>[]{new RubiksCubeWorking.Tuple<>(Color.WHITE,"White")

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //for(int i = 0; i< 6; i++)
        //    tuples[i] = new RubiksCubeWorking.Tuple<>(RubiksCubeWorking.SixColors.sixColors[i],colors[i]);

        cube = new Cube();
        solver = new AutoSolver(cube);

        window = primaryStage;
        window.setTitle("Rubik's RubiksCubeWorking.Cube");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        int x = 25;

        Button solve = new Button("solve");
        solve.setOnAction(e-> solve(solver));
        GridPane.setConstraints(solve, x+3, 30);

        Button alg = new Button("alg");
        alg.setOnAction(e-> alg());
        GridPane.setConstraints(alg, x+3, 31);


        timer = new Label("00:00.00");
        GridPane.setConstraints(timer, x+3, 25);

        pause = new Button("Pause");
        pause.setOnAction(e -> pause());
        GridPane.setConstraints(pause,x+3, 26);


        Button mixCube = new Button("MIX CUBE");
        mixCube.setOnAction(e -> mixCube());
        GridPane.setConstraints(mixCube,x-5,15);

        Button undo = new Button("UNDO");
        undo.setOnAction(e-> undo());
        GridPane.setConstraints(undo,x-4, 10);

        Button redo = new Button("REDO");
        redo.setOnAction(e-> redo());
        GridPane.setConstraints(redo,x-3, 10);

        ChoiceBox<String> frontFace = new ChoiceBox<>();
        frontFace.getItems().addAll("White", "Orange", "Green", "Yellow", "Red", "Blue" );
        frontFace.setValue("Orange");

        ChoiceBox<String> topFace = new ChoiceBox<>();
        topFace.setItems(calcItems(frontFace.getValue()));
        topFace.setValue("White");

        //frontFace.setOnAction(e-> {
          // topFace.setItems(calcItems(frontFace.getValue()));
        //});
        //topFace.setOnAction(e-> setState(frontFace.getValue(), topFace.getValue()));

        GridPane.setConstraints(frontFace,x ,1);
        GridPane.setConstraints(topFace,x ,2);

        Label topface = new Label("Top RubiksCubeWorking.Face:");
        Label frontface = new Label("Front RubiksCubeWorking.Face:");
        Label cubePosition = new Label("RubiksCubeWorking.Cube Position");
        Label moves = new Label ("Moves:");
        GridPane.setConstraints(moves, x-2,8);
        GridPane.setConstraints(frontface, x-1, 1);
        GridPane.setConstraints(topface, x-1, 2);
        GridPane.setConstraints(cubePosition, x-1, 0);

        createCube(50, 100,100);
        setButtons(x,8);
        grid.getChildren().addAll( mixCube, undo, redo, timer, pause, solve,alg,
                moves, R, nR, dR, L, nL, dL, F, nF, dF, B, nB, dB, U, nU, dU, D, nD, dD, g);

        //frontFace,topFace, topface, frontface, cubePosition,

        HBox layout = new HBox(10);
        layout.getChildren().addAll(g,grid);
        Scene scene = new Scene(layout, 2000,1000);

        scene.setOnKeyPressed(e-> press(e.getCode(), cube, false) );

        window.setScene(scene);
        window.show();
    }

    public ObservableList<String> calcItems (String s){
        ChoiceBox<String> copy = new ChoiceBox<>();
        String[] faces = new String[4];
        for (int i = 0; i < 4; i++)
            faces[i] = colors[SixColors.searchColor(cube.getFaces()[searchColor(s)].getFaces()[i])];
        copy.getItems().addAll(faces[0],faces[1],faces[2],faces[3]);
        return copy.getItems();
    }

    public int searchColor (String s){
        int i = 0;
        for (;i < 6; i++){
            if (colors[i] == s)
                break;
        }
        return i;
    }

    //public void setState(String front, String top){
    //    cube.setState(RubiksCubeWorking.SixColors.sixColors[searchColor(front)], RubiksCubeWorking.SixColors.sixColors[searchColor(top)]);
    //    System.out.println(top + "\n" + front);
    //    setLook();
    //}

    public void move(Twist move){
        if (!isPaused) {
            if (undoGap > 0) {
                for (int i = moves.size() - 1; i >= index; i--)
                    moves.remove(i);
                undoGap = 0;
                index = moves.size();
            }
            cube.rotate(move);
            moves.add(move);
            index++;
            setLook(cube);
            if (cube.isSolved() && isMixed) {
                isMixed = false;
                AlertBox.display(":)", "Congratulations!!! YOU SOLVED IT IN " + time);
            }
        }
    }

    public void setButtons(int x, int y){

        R = new Button("R");
        R.setOnAction(e -> move(Twist.R));
        GridPane.setConstraints(R, x-1, y);

        nR = new Button("R'");
        nR.setOnAction(e -> move(Twist.nR));
        GridPane.setConstraints(nR, x, y);

        dR = new Button("R^2");
        dR.setOnAction(e -> move(Twist.dR));
        GridPane.setConstraints(dR, x+1, y);

        L = new Button("L");
        L.setOnAction(e -> move(Twist.L));
        GridPane.setConstraints(L, x-1, y+1);

        nL = new Button("L'");
        nL.setOnAction(e -> move(Twist.nL));
        GridPane.setConstraints(nL, x, y+1);

        dL = new Button("L^2");
        dL.setOnAction(e -> move(Twist.dL));
        GridPane.setConstraints(dL, x+1, y+1);

        F = new Button("F");
        F.setOnAction(e -> move(Twist.F));
        GridPane.setConstraints(F, x-1, y+2);

        nF = new Button("F'");
        nF.setOnAction(e -> move(Twist.nF));
        GridPane.setConstraints(nF, x, y+2);

        dF = new Button("F^2");
        dF.setOnAction(e -> move(Twist.dF));
        GridPane.setConstraints(dF, x+1, y+2);

        B = new Button("B");
        B.setOnAction(e -> move(Twist.B));
        GridPane.setConstraints(B, x-1, y+3);

        nB = new Button("B'");
        nB.setOnAction(e -> move(Twist.nB));
        GridPane.setConstraints(nB, x, y+3);

        dB = new Button("B^2");
        dB.setOnAction(e -> move(Twist.dB));
        GridPane.setConstraints(dB, x+1, y+3);

        U = new Button("U");
        U.setOnAction(e -> move(Twist.U));
        GridPane.setConstraints(U, x-1, y+4);

        nU = new Button("U'");
        nU.setOnAction(e -> move(Twist.nU));
        GridPane.setConstraints(nU, x, y+4);

        dU = new Button("U^2");
        dU.setOnAction(e -> move(Twist.dU));
        GridPane.setConstraints(dU, x+1, y+4);

        D = new Button("D");
        D.setOnAction(e -> move(Twist.D));
        GridPane.setConstraints(D, x-1, y+5);

        nD = new Button("D'");
        nD.setOnAction(e -> move(Twist.nD));
        GridPane.setConstraints(nD, x, y+5);

        dD = new Button("D^2");
        dD.setOnAction(e -> move(Twist.dD));
        GridPane.setConstraints(dD, x+1, y+5);
    }

    public void createCube(double size, double vSpacing, double hSpacing){
        for (int i = 0; i < 3; i ++){
            for (int r = 0; r < 3; r++){
                cubeLook[0][i][r] = new Polygon(hSpacing +2*r*size, vSpacing+(3+2*i)*size, hSpacing +2*r*size,vSpacing+(5+2*i)*size,
                        hSpacing +(2*r+2)*size, vSpacing+(5+2*i)*size, hSpacing +(2*r+2)*size, vSpacing+(3+2*i)*size);
                cubeLook[1][i][r] = new Polygon(hSpacing + (3+2*r - i)*size,vSpacing + i*size, hSpacing + (2+2*r - i)*size,vSpacing + (i+1)*size,
                        hSpacing + (4+2*r - i)*size,vSpacing + (i+1)*size, hSpacing + (5+2*r - i)*size,vSpacing + i*size);
                cubeLook[2][i][r] = new Polygon(hSpacing+ (6+r)*size,  vSpacing+(3+2*i - r)*size, hSpacing+ (6+r)*size, vSpacing+(5+2*i - r)*size,
                        hSpacing+ (7+r)*size, vSpacing+(4+2*i - r)*size, hSpacing+ (7+r)*size, vSpacing+(2+2*i - r)*size );
                for (int t = 0; t <3; t++) {
                    cubeLook[t][i][r].setStroke(Color.BLACK);
                    g.getChildren().add(cubeLook[t][i][r]);
                }

            }
        }
        setLook(cube);


    }

    public void setLook(Cube cube){
        for (int i = 0; i < 3; i ++) {
            for (int r = 0; r < 3; r++) {
                for (int t = 0; t < 3; t++ )
                    cubeLook[i][r][t].setFill(cube.getCubeLook()[i][r][t]);
            }
        }
    }

    public void undo(){
        if(!isPaused) {
            if (index > 0) {
                index--;
                cube.rotate(new Twist(moves.get(index).act, !moves.get(index).direction, moves.get(index).times));
                setLook(cube);
                undoGap++;
            } else System.out.println("index out of bound");
        }
    }

    public void redo(){
        if(!isPaused) {
            if (index < moves.size() && undoGap > 0) {
                cube.rotate(moves.get(index));
                index++;
                setLook(cube);
                undoGap--;
            } else System.out.println("index out of bound");
        }
    }

    public void press (KeyCode typed, Cube cube, boolean override) {
        if (!isPaused || override) {
            switch (typed) {
                case DOWN:
                    cube.setState(SixColors.getOpposite(cube.getState()[1]), cube.getState()[0]);
                    break;
                case UP:
                    cube.setState(cube.getState()[1], SixColors.getOpposite(cube.getState()[0]));
                    break;
                case LEFT:
                    cube.setState(SixColors.getOpposite(cube.getRightFace()), cube.getState()[1]);
                    break;
                case RIGHT:
                    cube.setState(cube.getRightFace(), cube.getState()[1]);
            }
            if(!isPaused)
                setLook(cube);
        }
    }

    public void mixCube(){
        if (!isPaused) {
            timeTaken = 0;
            cube.mixCube();
            startTime = System.currentTimeMillis();
            setLook(cube);
            if (!isMixed)
                createStopWatch();
        }
    }

    public String format (long l){

        double d = (double) (l+timeTaken);
        if (isPaused){
            timeTaken += l;
            isMixed = false;
        }
        double t = Math.round(d/10.0)/100.0;
        double seconds = Math.round((t % 60)*100.0)/100.0;
        String second;
        int minutes = (int) Math.round((t-seconds) / 60.0);
        String minute;
        if(seconds < 10)
            second = "0" + seconds;
        else
            second = "" + seconds;
        if (Math.round(seconds*10.0) == seconds*10)
            second = second + "0";
        if(minutes < 10)
            minute = "0" + minutes;
        else
            minute = "" + minutes;
        time = minute + ":" + second;
        return  time;
    }

    public void pause(){
        if(isMixed){
            isPaused = !isPaused;
            if (!isPaused)
                resume();
            else {
                pause.setText("Continue");
                createScreenSaver();
            }
        }
    }

    public void resume(){
        setLook(cube);
        pause.setText("Pause");
        startTime = System.currentTimeMillis();
        createStopWatch();
    }

    public void createStopWatch(){
        Thread stopWatch = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isMixed) {
                    try {
                        final String s = format(System.currentTimeMillis() - startTime);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                timer.setText(s);
                            }
                        });
                        Thread.sleep(25);
                    }
                    catch (InterruptedException e) {
                        System.out.println("lol");
                        break;
                    }
                }
                if (isPaused)
                    isMixed = true;
            }
        });
        isMixed = true;
        stopWatch.setDaemon(true);
        stopWatch.start();
    }

    public void createScreenSaver(){
        cube2 = new Cube();
        Thread screenSaver = new Thread(new Runnable() {
            @Override
            public void run() {

                int i = 0;
                while (isPaused) {
                    if(i % 3 == 1)
                        press(KeyCode.RIGHT, cube2, true);
                    if(i % 3 == 2)
                        press(KeyCode.LEFT, cube2, true);
                    press(KeyCode.DOWN, cube2, true);
                    i++;
                    setLook(cube2);
                    try {
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException e) {
                        System.out.println("lol");
                        break;
                    }
                }
            }
        });
        screenSaver.setDaemon(true);
        screenSaver.start();
    }

    public void solve(AutoSolver solver){
        while(!cube.isSolved()) {
            Twist[] moves = solver.getNext();
            for (Twist t : moves)
                move(t);
        }
    }
    public void alg(){
        Twist[] moves = solver.getLast();
        for (Twist r: moves)
            move(r);
    }

    /*
    public String convert(Color c){
        for(int i = 0; i < 6; i++){
            if (tuples[i].c.equals(c))
                return tuples[i].s;
            break;
        }
        System.out.println("The color is'nt one of the six, no match String");
        return null;
    }

    public Color convert(String s){
        for(int i = 0; i < 6; i++){
            if (tuples[i].s.equals(s))
                return tuples[i].c;
            break;
        }
        System.out.println("The String is'nt one of the six, no match String");
        return null;
    }
    **/


}
