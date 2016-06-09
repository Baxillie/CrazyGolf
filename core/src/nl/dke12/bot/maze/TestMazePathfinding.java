package nl.dke12.bot.maze;

import nl.dke12.bot.pathfinding.AStar;
import nl.dke12.bot.pathfinding.MapNode;

import java.util.ArrayList;

/**
 * Created by nik on 09/06/16.
 */
public class TestMazePathfinding {

    public static void main(String[] args)
    {
        Maze maze = new Maze(20, 20);
        maze.printMaze();

        AStar solver = new AStar(new MazeTranslator().makeMapGraph(maze));

        try
        {
            ArrayList<MapNode> path = solver.calculatePath();
            for (MazeMapNode n : path)
            {
                System.out.printf("Node x: %d Node y: %d\n", n.x, n.y);
            }
            maze.makePath(path);
            //maze.printMaze();
        } catch (AStar.PathNotFoundException e)
        {
            System.out.println("we're screwed");
        }
    }

}
