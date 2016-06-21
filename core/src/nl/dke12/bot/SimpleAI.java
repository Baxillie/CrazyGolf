package nl.dke12.bot;

import com.badlogic.gdx.math.Vector3;
import nl.dke12.bot.pathfinding.*;
import nl.dke12.controller.AIInputProcessor;
import nl.dke12.controller.InputProcessor;
import nl.dke12.game.GameWorld;
import nl.dke12.util.Log;
import nl.dke12.util.Logger;

import java.util.ArrayList;

/**
 * Created by nik on 5/20/16.
 */
public class SimpleAI implements Runnable {

    public static final int WAIT_TIME = 100; //ms
    private static Logger logger = Logger.getInstance();
    protected boolean loop;
    protected boolean makingDecision;
    protected AIInputProcessor processor;
    protected Vector3 ballPosition;
    protected Vector3 holePosition;
    protected Vector3 distance;
    protected GameWorld gameWorld;

    protected long lastTimeMoved = 0;

    //for the method ballStoppedMoving
    int counter = 0;
    float lastVectorLength;

    public SimpleAI(GameWorld world, InputProcessor processor)
    {
        this.processor = (AIInputProcessor) processor;
        this.gameWorld = world;
    }

    @Override
    public void run() {
        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        loop = true;
        //testPathFinding();
        loop();
    }

    private void testPathFinding()
    {
        //FloodFill astar = new FloodFill();
        AStar astar = new AStar();

        MapGraph mapGraph = gameWorld.getGameMap().getGridBasedMapGraph();
        try
        {
            ArrayList<MapNode> list = astar.calculatePath(mapGraph);
            logger.log("size of path:" + list.size());
            logger.log(list.toString());
        }
        catch(PathNotFoundException e)
        {
            System.out.println("Path not found in simple ai testPathFinding()");
        }
    }

    private boolean ballStoppedMoving(float directionLength)
    {
        logger.log(String.format("deciding if ball stopped moving with current vector length: %f and previous vector length: %f and counter: %d",
                                                                        directionLength, lastVectorLength, counter));
        if (Math.abs(directionLength - lastVectorLength) < 0.01)
        {
            counter++;
            if(counter < 20)
            {
                lastVectorLength = directionLength;
                return false;
            }
            else
            {
                counter = 0;
                lastVectorLength = directionLength;
                return true;
            }
        }
        else
        {
            lastVectorLength = directionLength;
            return false;
        }
    }

    protected void loop()
    {
        while(loop)
        {
            logger.createBreak("-AI loop-");
            //check if ball is moving
            float directionVectorLength = gameWorld.getBallDirection(gameWorld.getBallSim()).len();

            if(ballStoppedMoving(directionVectorLength))
            {
                logger.log("Ball isn't moving.");
                //sleep to make it seem like the ai thinks
                try
                {
                    Thread.sleep(100);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                long start = System.currentTimeMillis();
                makeDecision();
                long end = System.currentTimeMillis();
                System.out.println("Run time for decision: " + (end - start) + " ms");
            }
            else
            {
                try
                {
                    logger.log("Ball is still moving because vector length is:" + directionVectorLength + ". Sleeping for " + WAIT_TIME + " ms");
                    Thread.sleep(WAIT_TIME);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void kill()
    {
        loop = false;
    }

    //gets called by gamecontroller when AI needs to make a move
    protected void makeDecision()
    {
        loadHoleAndBalPosition();
        if(gameWorld.ballIsInHole(gameWorld.getBallSim())) // done
        {
            System.out.println("stopping because in hole ");
            logger.log("ball is in hole");
            return;
        }
        calculateBestMove();
        makeMove();
    }

    //loads the current game wold into a format which is readable by the AI
    protected void loadHoleAndBalPosition()
    {
        this.ballPosition = new Vector3(gameWorld.getBallSimPosition());
        logger.log("ball position: " + ballPosition.toString());
        this.holePosition = new Vector3(gameWorld.getHolePosition());
        logger.log("hole position: " + holePosition.toString());
    }

    //decides which move the bot should make based on the current world situation
    protected void calculateBestMove()
    {
        distance = new Vector3(new Vector3(holePosition).sub(ballPosition));
        distance.z += 0.1;
        logger.log("The calculated AI vector:" + distance);
    }

    //gives the move the ai wants to make to the inputprocessor
    protected void makeMove()
    {
        if (distance != null)
        {
            processor.setDirectionVector(distance);
            processor.setMove(true);
        }
        else
        {
            Log.log("failed to make a move");
        }
    }

    protected boolean isCloseToHole()
    {
        Vector3 ballPosition = gameWorld.getBallSimPosition();
        Vector3 holePosition = this.holePosition;

        int distance = 0;
        return false;
    }
}
