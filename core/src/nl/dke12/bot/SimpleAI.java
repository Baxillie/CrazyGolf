package nl.dke12.bot;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
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

    protected void loop()
    {
        while(loop)
        {
            logger.createBreak("----AI loop begins----");
            //check if ball is moving
            if(gameWorld.getBallDirection().isZero(0.001f) /*&& !makingDecision*/) // ball doesnt move, make a decision
            {
                //makingDecision = true;
                logger.log("Ball isn't moving.");
                makeDecision();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
                    logger.log("Ball is still moving. Sleeping for " + WAIT_TIME +" ms");
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
        logger.log("setting distance to " + distance);

    }

    //gives the move the ai wants to make to the inputprocessor
    protected void makeMove()
    {
        //set direction vector
        distance = distance.nor();
        processor.setDirectionVector(distance);
        processor.setMove(true);
        //set move in ai intput processor to true

    }

}
