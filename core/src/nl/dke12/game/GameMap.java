package nl.dke12.game;

import com.badlogic.gdx.math.Vector3;
import nl.dke12.bot.pathfinding.MapGraph;
import nl.dke12.util.GameWorldLoader;
import nl.dke12.util.Log;

import java.util.ArrayList;

/**
 * Created by nik on 13/06/16.
 */
public class GameMap
{
    /**
     * The radius of the golf ball hole.
     */
    private final static int HOLE_RADIUS = 1; //// TODO: 13/06/16 determine actual radius

    /**
     * the amount of cells get created for every unit the game world spans
     */
    private final static int UNIT_TO_CELL_RATIO = 4;

    /**
     * stores the spawning location of the golf ball
     */
    private Vector3 startPosition;

    /**
     * Stores the position of the hole as x, y, z
     */
    private Vector3 holePosition;

    /**
     * list of all obstacles (eg walls, floor, slopes) in the game world.
     */
    private ArrayList<SolidObject> gameObject;

    /**
     * Creates a game map object which can translate the hole to the ai
     * @param loader gameWorldLoader object which has been used to create the game world
     */
    public GameMap(GameWorldLoader loader)
    {
        Log.log("Created GameMap object");

        //get information from the GameWorldLoader
        this.startPosition = loader.getStartPosition(); //currently just returns a new Vector at 0,0,0
        this.holePosition = loader.getHolePosition();
        this.gameObject = loader.getSolidObjects();

        //calculate grid-based view of the golf course
        preMakeGrid();

        //calculate graph-based view of the golf course with the help of physics simulations
        preMakeGraph();
    }

    /**
     * determines the grid of the game world so when the information is requested the computation has already been done
     */
    private void preMakeGrid()
    {
        int[][] grid = determineGridDimensions(); //calculates the grid dimensions
    }

    /**
     * loops over all objects in the golf course to determine how large the grid has to be
     */
    private int[][] determineGridDimensions()
    {
        //initialise max and min values
        SolidObject o = gameObject.get(0); Vector3 pos = o.getPosition();

        //largest  x and y found
        float maxX = o.getWidth() / 2 - pos.x;
        float maxY = o.getWidth() / 2 + pos.x;
        //smallest x and y found
        float minX = o.getDepth() / 2 - pos.y;
        float minY = o.getDepth() / 2 + pos.y;

        //loop
        float width, depth;         //used in loop to store dimensions of every object
        Vector3 position;           //these dimensions have to be divided by 2 from the center to get max and min
        float temp;                 //center of object
        for(SolidObject object : gameObject)
        {
            //get data from object
            width = object.getWidth() / 2;       //x
            depth = object.getDepth() / 2;       //y
            position = object.getPosition();

            //determine if this object exceeds the current dimensions
            if((temp = position.x + width) > maxX)
            {
                maxX = temp;
            }
            if((temp = position.x - width) < minX)
            {
                minX = temp;
            }
            if((temp = position.y + depth) > maxY)
            {
                maxY = temp;
            }
            if((temp = position.y - depth) < minY)
            {
                minY = temp;
            }
        }

        //calculate actual grid size and return it
        float absoluteX = Math.abs(maxX - minX);
        float absoluteY = Math.abs(maxY - minY);

        int gridLength = Math.round(absoluteY);
        int gridWidth  = Math.round(absoluteX);

        return new int[gridLength * UNIT_TO_CELL_RATIO][gridWidth * UNIT_TO_CELL_RATIO];
    }

    /**
     * determines the graph of the game world based on physics simulations (e.g every possible shot from a certain point)
     */
    private void preMakeGraph()
    {

    }

    /**
     * get a simple grid/tile based view of the game course for a* and floodfill algorithm
     * @return a MapGraph holding the game course
     */
    public MapGraph getGridBasedMapGraph()
    {
        return null;
    }

    /**
     * get a graph based view of the game course based on simulating every shot in the golf course.
     * @return A MapGraph holding every shot
     */
    public MapGraph getGraphBasedMapGraph()
    {
        return null;
    }

}
