package nl.dke12.bot;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.sun.org.apache.xerces.internal.parsers.CachingParserPool;
import com.sun.org.apache.xml.internal.serializer.utils.SystemIDResolver;
import com.sun.org.omg.CORBA.ExcDescriptionSeqHelper;
import nl.dke12.controller.AIInputProcessor;
import nl.dke12.controller.InputProcessor;
import nl.dke12.game.GameWorld;
import nl.dke12.util.Logger;

/**
 * Created by nik on 5/20/16.
 */
public class SimpleAI implements Runnable {

    public static final int WAIT_TIME = 1000; //ms

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
        loop = true;
        loop();
    }

    private boolean ballStoppedMoving(float directionLength) {
        logger.log(String.format("deciding if ball stopped moving with current vector length: %f and" +
                " previous vector length: %f and counter: %d", directionLength, lastVectorLength, counter));
        if (Math.abs(directionLength - lastVectorLength) < 0.1)
        {
            counter++;
            if(counter < 2)
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
            float directionVectorLength = gameWorld.getBallDirection().len();
            //if(directionVectorLength < 0.46 /*&& !makingDecision*/) // ball doesnt move, make a decision
            //long currentTime = System.currentTimeMillis();
            //if((currentTime - 5000) > lastTimeMoved)
            if(ballStoppedMoving(directionVectorLength))
            {
                //makingDecision = true;
                logger.log("Ball isn't moving.");
                //sleep to make it seem it the ai thinks
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                makeDecision();
            }
            //if getMove is true, the decision is made but not yet retrieved by the gameController.
//            if(makingDecision && processor.getMove())
//            {
//
//            }
            else
            {
                try
                {
                    logger.log("Ball is still moving because vector length is:" + directionVectorLength +
                            ". Sleeping for " + WAIT_TIME +" ms");
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

//        try {
//            Thread.sleep(1000);
//            System.out.println("Waiting in make decision");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        loadGameWorld();
        calculateBestMove();
        makeMove();
    }

    //loads the current game wold into a format which is readable by the AI
    protected void loadGameWorld()
    {
        this.ballPosition = new Vector3(gameWorld.getBallPosition());
        logger.log("ball position: " + ballPosition.toString());
        this.holePosition = new Vector3(gameWorld.getHolePosition());
        logger.log("hole position: " + holePosition.toString());
    }

    //decides which move the bot should make based on the current world situation
    protected void calculateBestMove()
    {
        distance = new Vector3(new Vector3(holePosition).sub(ballPosition));
        logger.log("The calculated AI vector:" + distance);

    }

    //gives the move the ai wants to make to the inputprocessor
    protected void makeMove()
    {
        //set direction vector
        //distance = distance.nor();
        processor.setDirectionVector(distance);
        processor.setMove(true);
        //set move in ai intput processor to true

    }

}
