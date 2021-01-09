package RubiksCube2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application{

    Stage window;
    Pane pane = new Pane();
    GridPane grid = new GridPane();

    Button[][] buttons = new Button[6][3];
    Button pause;
    Label timer;

    Cube cube = new Cube(3);
    Cube cube2 = new Cube(3);

    Polygon[][][] cubeLook = new Polygon[3][3][3];
    ArrayList<int[]> moves = new ArrayList<>();
    String time;
    long startTime;
    long timeTaken = 0;
    int index = 0;
    int undoGap = 0;
    boolean isPaused = false;
    boolean isMixed = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        window.setTitle("Rubik's RubiksCubeWorking.Cube");

        int x = 25;

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

        Label moves = new Label ("Moves:");
        GridPane.setConstraints(moves, x-2,8);

        createCube(50, 100,100);
        setButtons(x,8, 3);


        grid.getChildren().addAll(mixCube, undo, redo, timer, pause, moves);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        HBox layout = new HBox(10);
        layout.getChildren().addAll(pane, grid);
        Scene scene = new Scene(layout, 2000,1000);

        scene.setOnKeyPressed(e-> press(e.getCode(), cube, false) );

        window.setScene(scene);
        window.show();
    }

    public void move(int i, int n){
        if (!isPaused) {
            if (undoGap > 0) {
                for (int r = moves.size() - 1; r >= index; r--)
                    moves.remove(r);
                undoGap = 0;
                index = moves.size();
            }
            cube.rotate(i, n);
            moves.add(new int[]{i,n});
            index++;
            setLook(cube);
            if (cube.isSolved() && isMixed) {
                isMixed = false;
                AlertBox.display(":)", "Congratulations!!! YOU SOLVED IT IN " + time);
            }
        }
    }

    public void setButtons(int x, int y, int size){
        String s;
        for(int i = 0; i < 6; i++){
            final int fi = i;
            if(i % 2 != 0)
                s = "'";
            else
                s = "";
            for(int n = 0; n < size; n++){
                final int fn = n;
                buttons[i][n] = new Button(cube.getLayerName(i/2, n) + s);
                buttons[i][n].setOnAction(e -> move(fi,fn));
                GridPane.setConstraints(buttons[i][n],x + n, y + i);
                grid.getChildren().add(buttons[i][n]);
            }
        }
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
                    pane.getChildren().add(cubeLook[t][i][r]);
                }

            }
        }
        setLook(cube);


    }

    public void setLook(Cube cube){
        Color[][][] look = cube.getCubeLook();
        for (int i = 0; i < 3; i ++) {
            for (int r = 0; r < 3; r++) {
                for (int t = 0; t < 3; t++ )
                    cubeLook[i][r][t].setFill(look[i][r][t]);
            }
        }
    }

    public void undo(){
        if(!isPaused) {
            if (index > 0) {
                index--;
                int constant = -1;
                if(moves.get(index)[0] % 2 == 0)
                    constant = 1;
                cube.rotate(moves.get(index)[0] + constant, moves.get(index)[1]);
                setLook(cube);
                undoGap++;
            } else System.out.println("index out of bound");
        }
    }

    public void redo(){
        if(!isPaused) {
            if (index < moves.size() && undoGap > 0) {
                cube.rotate(moves.get(index)[0], moves.get(index)[1]);
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
                    cube.setState((cube.getTopFaceIndex() + 3) % 6, cube.getFrontFaceIndex());
                    break;
                case UP:
                    cube.setState(cube.getTopFaceIndex(), (cube.getFrontFaceIndex() + 3) % 6);
                    break;
                case LEFT:
                    cube.setState((cube.getRightFaceIndex() + 3) % 6, cube.getTopFaceIndex());
                    break;
                case RIGHT:
                    cube.setState(cube.getRightFaceIndex(), cube.getTopFaceIndex());
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
        cube2 = new Cube(3);
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






}
