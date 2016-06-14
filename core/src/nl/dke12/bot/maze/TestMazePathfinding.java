package nl.dke12.bot.maze;

import nl.dke12.bot.pathfinding.*;

import java.util.ArrayList;

/**
 * Created by nik on 09/06/16.
 */
public class TestMazePathfinding {

    public static void main(String[] args)
    {
        Maze maze = new Maze(10, 10);
        maze.printMaze();

        MazeTranslator mazeTranslator = new MazeTranslator();
        MapGraph mapGraph = mazeTranslator.makeMapGraph(maze);

        AStar astar = new AStar();
        FloodFill floodFill = new FloodFill();

        try
        {
            //ASTAR

            System.out.println();
            System.out.println("AStar");
            ArrayList<MapNode> path = astar.calculatePath(mapGraph);

            System.out.println();
            System.out.println(path);
            maze.addPath(path);
            maze.printMaze();

            maze.deletePath(path);

            //FloodFill

            System.out.println();
            System.out.println("FloodFill");
            path = floodFill.calculatePath(mapGraph);

            System.out.println();
            System.out.println(path);
            maze.addPath(path);
            maze.printMaze();

        }
        catch(PathNotFoundException e)
        {
            System.out.println("no path found");
        }

    }

}
