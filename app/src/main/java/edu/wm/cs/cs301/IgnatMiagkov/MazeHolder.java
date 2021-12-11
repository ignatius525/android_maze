package edu.wm.cs.cs301.IgnatMiagkov;

import edu.wm.cs.cs301.IgnatMiagkov.generation.Maze;

public class MazeHolder {

    private static Maze maze;

    public static Maze getMaze(){return maze;}

    public static void setMaze(Maze mazeConfig){maze = mazeConfig;}
}
