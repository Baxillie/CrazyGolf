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
