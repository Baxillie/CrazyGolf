package nl.dke12.bot.maze;

import nl.dke12.bot.maze.Maze;
import nl.dke12.bot.pathfinding.MapGraph;
import nl.dke12.bot.pathfinding.MapGraphFactory;
import nl.dke12.bot.pathfinding.MapNode;
import nl.dke12.bot.pathfinding.Node;

/**
 * Created by Ajki on 07/06/2016.
 */
public class MazeTranslator extends MapGraphFactory<Maze>
{
    private final boolean debug = true;

    public MazeTranslator()
    {
        super();
    }

    @Override
    public MapGraph makeMapGraph(Maze maze)
    {
        char[][] grid =  maze.getMaze();
        int endNodeX = maze.endCoords[0];
        int endNodeY = maze.endCoords[1];
        int startNodeX = maze.beginCoords[0];
        int startNodeY = maze.beginCoords[1];
        if(debug) {
            System.out.println("making a map graph of the following maze:");
            maze.printMaze();
            System.out.printf("Start node is at x: %d\ty: %d\nEnd node is at x: %d\ty: %d\n",
                    startNodeX, startNodeY, endNodeX, endNodeY);
        }



        MapGraph theMapGraph = new MapGraph(new MazeMapNode(startNodeX, startNodeY),
                new MazeMapNode(endNodeX, endNodeY));

        return theMapGraph;
    }

}
