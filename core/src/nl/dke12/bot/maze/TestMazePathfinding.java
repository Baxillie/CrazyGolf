package nl.dke12.bot.maze;

import nl.dke12.bot.pathfinding.AStar;
import nl.dke12.bot.pathfinding.MapGraph;
import nl.dke12.bot.pathfinding.MapNode;

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
        //System.out.println(mapGraph);
        mapGraph.printFullInformation();
    }

}
