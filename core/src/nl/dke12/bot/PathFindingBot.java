package nl.dke12.bot;

import com.badlogic.gdx.math.Vector3;
import nl.dke12.bot.maze.MazeMapNode;
import nl.dke12.bot.pathfinding.AStar;
import nl.dke12.bot.pathfinding.MapNode;
import nl.dke12.controller.GameController;
import nl.dke12.controller.InputProcessor;
import nl.dke12.game.GameMap;
import nl.dke12.game.GameWorld;
import nl.dke12.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ajki on 20/06/2016.
 */
public class PathFindingBot extends SimpleAI
{

    private GameController gameController;
    private Random rng;
    private ArrayList<MazeMapNode> path;
    private int nodeInPath;

    public PathFindingBot(GameWorld gameWorld, InputProcessor processor)
    {
        super(gameWorld, processor);
        rng = new Random(System.currentTimeMillis());
        this.gameController = gameWorld.getGameController();
        this.path = new ArrayList<>();

        GameMap gameMap = gameWorld.getGameMap();
        try
        {
            ArrayList<MapNode> path = new AStar().calculatePath(gameMap.getGridBasedMapGraph());
            Log.log("Path of AI: ");
            System.out.println("Path of AI: ");
            for(MapNode n : path)
            {
                Log.log(n.getIdentifier());
                System.out.println(n.getIdentifier());
                this.path.add((MazeMapNode) n);
            }

            Log.log("size of path:" + path.size());
            Log.log(path.toString());


            nodeInPath = 0;
            if(path.size() <= 0)
            {
                throw new Exception("path length was 0");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void calculateBestMove()
    {
        int count = 1;
        if(nodeInPath + 1 >= path.size())
        {
            super.calculateBestMove();
            return;
        }
        MazeMapNode startNode = path.get(nodeInPath);   nodeInPath++;
        MazeMapNode nextNode = path.get(nodeInPath);    nodeInPath++;

        float startX = startNode.getX();
        float startY = startNode.getY();
        float nextX  = nextNode.getX();
        float nextY  = nextNode.getY();

        float absX = Math.abs(startX - nextX);
        float absY = Math.abs(startY - nextY);

        Vector3 shotVector = null;
        if(absX == 0 || absY == 0) //dealing with straight line
        {
            boolean x = absX == 0;
            boolean y = absY == 0;
            //finding collinear points in path
            for (int i = nodeInPath; i < path.size(); i++, nodeInPath++)
            {
                count++;

                nextNode = path.get(i);
                nextX  = nextNode.getX();
                nextY  = nextNode.getY();

                absX = Math.abs(startX - nextX);
                absY = Math.abs(startY - nextY);

                if (x && Math.abs(absX - 0) > 0.01)
                {
                    MazeMapNode prevNode = path.get(i - 1);
                    shotVector = new Vector3(0, Math.abs(startNode.getY() - prevNode.getY()), 0.8f);
                    System.out.println("shotVector length " + shotVector.len());
                    shotVector.scl(2.1540658f / shotVector.len());
                    break;
                }
                else if(y && Math.abs(absY - 0) > 0.01)
                {
                    MazeMapNode prevNode = path.get(i - 1);
                    shotVector = new Vector3(Math.abs(startNode.getX() - prevNode.getX()), 0, 0.8f);
                    System.out.println("shotVector length " + shotVector.len());
                    shotVector.scl(2.1540658f / shotVector.len());
                    break;
                }
            }
        }
        else //dealing with diagonal line
        {

            float m = (nextNode.getY() - startNode.getY()) / (nextNode.getX() - startNode.getX());
            float c = nextNode.getY() - m * nextNode.getX();

            //finding collinear points in path
            for (int i = nodeInPath; i < path.size(); i++, nodeInPath++)
            {
                count++;

                nextNode = path.get(i);
                float actualNextX = nextNode.getX();
                float actualNextY = nextNode.getY();
                float possibleNextY = m * actualNextX + c;

                if (Math.abs(actualNextY - possibleNextY) > 0.01)
                {
                    MazeMapNode prevNode = path.get(i - 1);
                    shotVector = new Vector3(Math.abs(startNode.getX() - prevNode.getX()), Math.abs(startNode.getY() - prevNode.getY()), 0.8f);
                    System.out.println("shotVector length " + shotVector.len());
                    shotVector.scl(2.1540658f / shotVector.len());
                    break;
                }
            }
        }

        if(shotVector != null)
        {
            gameController.setForceMultiplier(count * 0.02f);
            gameController.setHeightMultiplier(count * 0.01f);

            System.out.println("Pathfinding AI is shooting with vector: " + shotVector.toString() + "." +
                    "currently located at location " + nodeInPath + " in the path.");
            this.distance = shotVector;
        }
        else
        {
            System.out.println("not able to calculate a shotvector in the PathFindingBot");
            Log.log("not able to calculate a shotvector in the PathFindingBot");
        }

    }
}
