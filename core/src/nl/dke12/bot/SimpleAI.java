package nl.dke12.bot;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.sun.org.apache.xerces.internal.parsers.CachingParserPool;
import com.sun.org.apache.xml.internal.serializer.utils.SystemIDResolver;
import com.sun.org.omg.CORBA.ExcDescriptionSeqHelper;
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
        testPathFinding();
        loop();
    }

    private void testPathFinding()
    {
        FloodFill astar = new FloodFill();

        MapGraph mapGraph = gameWorld.getGameMap().getGridBasedMapGraph();
        try
        {
            ArrayList<MapNode> list = astar.calculatePath(mapGraph);
            logger.log("size of path:" + list.size());
            logger.log(list.toString());
        }
        catch(PathNotFoundException e)
        {
            System.out.println("WE fucked this .... ");
        }
    }

    private boolean ballStoppedMoving(float directionLength)
    {
        logger.log(String.format("deciding if ball stopped moving with current vector length: %f and" +
                " previous vector length: %f and counter: %d", directionLength, lastVectorLength, counter));
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
            logger.createBreak("----AI loop begins----");
            //check if ball is moving
            float directionVectorLength = gameWorld.getBallDirection(gameWorld.getBallSim()).len();
            //if(directionVectorLength < 0.46 /*&& !makingDecision*/) // ball doesnt move, make a decision
            //long currentTime = System.currentTimeMillis();
            //if((currentTime - 5000) > lastTimeMoved)
            if(ballStoppedMoving(directionVectorLength))
            {
                //makingDecision = true;
                logger.log("Ball isn't moving.");
                //sleep to make it seem it the ai thinks
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                makeDecision();
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
        loadGameWorld();
        if(ballIsInHole()) // done
        {
            return;
        }
        calculateBestMove();
        makeMove();
    }

    //loads the current game wold into a format which is readable by the AI
    protected void loadGameWorld()
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

    protected boolean ballIsInHole()
    {
        distance = new Vector3(new Vector3(holePosition).sub(ballPosition));
        logger.log("calculating if ball is in hole with distance to hole: " + distance.len());
        System.out.println("Distance to hole: " + distance);
        float distanceWithoutZ = Math.abs(distance.x + distance.y);
        if(distanceWithoutZ < 0.5)
        {
            return true;
        }
        return false;
    }

    //gives the move the ai wants to make to the inputprocessor
    protected void makeMove()
    {
        processor.setDirectionVector(distance);
        processor.setMove(true);
    }

}
