package nl.dke12.bot;

import com.badlogic.gdx.math.Vector3;
import nl.dke12.bot.maze.MazeMapNode;
import nl.dke12.bot.pathfinding.AStar;
import nl.dke12.bot.pathfinding.FloodFill;
import nl.dke12.bot.pathfinding.MapNode;
import nl.dke12.bot.pathfinding.PathNotFoundException;
import nl.dke12.controller.GameController;
import nl.dke12.controller.InputProcessor;
import nl.dke12.game.GameMap;
import nl.dke12.game.GameWorld;
import nl.dke12.util.ArrayUtil;
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
    private GameMap gameMap;
    private boolean floodfill;

    public PathFindingBot(GameWorld gameWorld, InputProcessor processor, boolean floodfill)
    {
        super(gameWorld, processor);
        rng = new Random(System.currentTimeMillis());
        this.gameController = gameWorld.getGameController();
        this.path = new ArrayList<>();
        this.gameMap = gameWorld.getGameMap();
        this.floodfill = floodfill;

        generateNewPath();
    }

    @Override
    protected void calculateBestMove()
    {
        int count = 1;
        if(nodeInPath + 1 >= path.size())
        {
            nodeInPath = 0;
            generateNewPath();
            return;
        }

        MazeMapNode startNode = path.get(nodeInPath);   nodeInPath++;
        MazeMapNode nextNode = path.get(nodeInPath);    nodeInPath++;

        float startX = startNode.getX(), startY = startNode.getY();
        float nextX  = nextNode.getX() , nextY  = nextNode.getY();

        float absX = Math.abs(startX - nextX);
        float absY = Math.abs(startY - nextY);

        Vector3 shotVector = null;
        if(absX == 0 || absY == 0) //dealing with straight line
        {
            boolean x = absX == 0; boolean y = absY == 0;
            //finding collinear points in path
            for (; nodeInPath < path.size(); nodeInPath++)
            {
                count++;
                nextNode = path.get(nodeInPath);

                nextX  = nextNode.getX();
                nextY  = nextNode.getY();

                absX = Math.abs(startX - nextX);
                absY = Math.abs(startY - nextY);

                if (x && Math.abs(absX - 0) > 0.01)
                {
                    MazeMapNode prevNode = path.get(nodeInPath - 1);
                    shotVector = new Vector3(0, prevNode.getY() - startNode.getY(), 0.8f);
                    shotVector.scl(2.1540658f / shotVector.len());
                    //generateNewPath();
                    break;
                }
                else if(y && Math.abs(absY - 0) > 0.01)
                {
                    MazeMapNode prevNode = path.get(nodeInPath - 1);
                    shotVector = new Vector3(prevNode.getX() - startNode.getX(), 0, 0.8f);
                    shotVector.scl(2.1540658f / shotVector.len());
                    //generateNewPath();
                    break;
                }
            }
        }

        else //dealing with diagonal line
        {
            float m = (nextNode.getY() - startNode.getY()) / (nextNode.getX() - startNode.getX());
            float c = nextNode.getY() - (m * nextNode.getX());

            //finding collinear points in path
            for (; nodeInPath < path.size(); nodeInPath++)
            {
                count++;
                nextNode = path.get(nodeInPath);
                float actualNextX = nextNode.getX();
                float actualNextY = nextNode.getY();
                float possibleNextY = (m * actualNextX) + c;

                if (Math.abs(actualNextY - possibleNextY) > 0.01 || nextNode.equals(path.get(path.size() - 1))) // if next node == goal node
                {
                    MazeMapNode prevNode = path.get(nodeInPath - 1);
                    shotVector = new Vector3(prevNode.getX() - startNode.getX(), prevNode.getY() - startNode.getY(), 0.8f);
                    shotVector.scl(2.1540658f / shotVector.len());
                    //generateNewPath();
                    break;
                }
            }
        }

        /*
        if ball position is in path
        continue shooting from ball position node
        else
        create new path from current ball position
         */

        if(shotVector != null)
        {
            gameController.setForceMultiplier(count * 0.025f);
            gameController.setHeightMultiplier(count * 0.05f);

            System.out.println("Pathfinding AI is shooting with vector: " + shotVector.toString() + " currently located at location " + nodeInPath + " in the path.");
            this.distance = shotVector;
        }
        else
        {
            generateNewPath();

            System.out.println("not able to calculate a shotvector in the PathFindingBot, so creating a new path");
            Log.log("not able to calculate a shotvector in the PathFindingBot, so creating a new path");
        }

    }

    private void generateNewPath()
    {
        path.clear();
        try
        {
            //ballStoppedMoving();

            Vector3 ballPos = gameWorld.getBallSimPosition();
            gameMap.setStartNode(ballPos);
            if(floodfill)
            {
                ArrayList<MapNode> path = new FloodFill().calculatePath(gameMap.getGridBasedMapGraph());
            }
            else
            {
                ArrayList<MapNode> path = new AStar().calculatePath(gameMap.getGridBasedMapGraph());
            }
            for(MapNode n : path)
            {
                Log.log(n.getIdentifier());
                this.path.add((MazeMapNode) n);
            }

            nodeInPath = 0;

            System.out.println("New Path");
            System.out.println("pathsize= " + path.size());
            System.out.println("ballpos = " + ballPos);
            System.out.println("startpos= " + gameMap.getStartPosition());
            System.out.println("startnod= " + gameMap.getStartNode());
            Log.log("New Path ");
            Log.log(path.toString());
            Log.log(ArrayUtil.arrayToStringWithPath(gameMap.numgrid, this.path));
        }
        catch (PathNotFoundException e)
        {
            super.calculateBestMove();
            System.out.println("Calculating with simpleAI since path calculation not possible");
            Log.log("Calculating with simpleAI since path calculation not possible");
        }
    }

    private void ballStoppedMoving()
    {
        while(true)
        {
            if(gameWorld.ballIsInHole(gameWorld.getBallSim()))
            {
                return;
            }
            float directionLength = gameWorld.getBallDirection(gameWorld.getBallSim()).x + gameWorld.getBallDirection(gameWorld.getBallSim()).y;
            if (Math.abs(directionLength - lastVectorLength) < 0.01)
            {
                if(counter < 50)
                {
                    lastVectorLength = directionLength;
                    counter++;
                }
                else
                {
                    counter = 0;
                    lastVectorLength = directionLength;
                    return;
                }
            }
            else
            {
                lastVectorLength = directionLength;
            }
            try
            {
                Thread.sleep(100);
            }
            catch (Exception e) {e.printStackTrace();}
        }
    }
}
