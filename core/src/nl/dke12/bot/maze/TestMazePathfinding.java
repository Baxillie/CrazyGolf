package nl.dke12.bot.maze;

import com.badlogic.gdx.utils.Array;
import nl.dke12.bot.pathfinding.AStar;
import nl.dke12.bot.pathfinding.MapGraph;
import nl.dke12.bot.pathfinding.MapNode;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by nik on 09/06/16.
 */
public class TestMazePathfinding {

    public static void main(String[] args)
    {
        Maze maze = new Maze(10, 10);
        maze.printMaze();
        char[][] grid;
        grid = maze.getMaze();

        MazeTranslator mazeTranslator = new MazeTranslator();
        MapGraph mapGraph = mazeTranslator.makeMapGraph(maze);

        AStar algorithm = new AStar();
        try
        {
            ArrayList<MapNode> path = algorithm.calculatePath(mapGraph);

            System.out.println();
            System.out.println(path);

            String identifier;
            int x; int y;
            for (MapNode node : path)
            {
                identifier = node.getIdentifier();

                x = Character.getNumericValue(identifier.charAt(1));
                y = Character.getNumericValue(identifier.charAt(4));

                grid[y][x] = '*';
            }
            System.out.println();
            for(int i = 0; i < grid.length; i++)
            {
                for(int j = 0; j < grid[i].length; j++)
                {
                    System.out.print(" " + grid[i][j]);
                }
                System.out.println();
            }
        }
        catch(AStar.PathNotFoundException e)
        {
            System.out.println("no path found");
        }
        //mapGraph.printFullInformation();

    }

}
