package nl.dke12.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import nl.dke12.util.Logger;

import java.util.ArrayList;

import static nl.dke12.controller.GameController.SCALING_SPEED_CONSTANT;


/**
 * Simulation class using multithreading for simulating shots.
 *
 * Created by nik on 20/06/16.
 */
public class TestChamber
{

    /**
     * the gameworld needed to do simulations due to opengl contect
     */
    private GameWorld gameworld;

    /**
     * Constructor for the TestChamber
     */
    public TestChamber(GameWorld gw)
    {
        this.gameworld = gw;
    }


    /**
     * Simulate the given amount of shotvectors in the game world.
     *
     * @param dataList the list of SimulationData needed to be simulated
     * @return a list of the positions of the ball after the shots have been taken
     */
    public void simulateShot(ArrayList<SimulationData> dataList)
    {
        //create list of all threads
        ArrayList<Thread> threads = new ArrayList<>();

        //create new thread based on data and start it immediately
        int count = 1;
        for(SimulationData data: dataList)
        {
            System.out.println("Started thread " + count);
            Thread t = new Thread(new Simulator(data));
            threads.add(t);
            t.start();
            count++;
        }

        //block until all threads are done
        while(true)
        {
            boolean someoneIsNotDone = false;
            for(Thread t: threads)
            {
                if(!this.isSimulatorDone(t))
                {
                    someoneIsNotDone = true;
                }
            }
            if(!someoneIsNotDone)
            {
                break;
            }
        }
        System.out.println("all are done");
    }

    /**
     * Runnable class which will be placed in a thread to simulate a shot. When the run method is done,
     * the thread will enter the TERMINATED state, which will make getting the endPosition safe.
     */
    private class Simulator implements Runnable
    {
        /**
         * The object holding the data for the shot
         */
        private SimulationData data;

        /**
         * The location where the ball will be placed before getting shot
         */
        private Vector3 initialPosition;
        /**
         * The vector with which the ball is going to get shot
         */
        private Vector3 pushVector;

        /**
         * The force multiplier applied to the shotVector
         */
        private float forceMultiplier;

        /**
         * the height multiplier applied to the shotVector
         */
        private float heightMultiplier;

        /**
         * the location where the ball will end up
         */
        private Vector3 endPosition = null;

        /**
         * whether the ball got in the hole with this shotVector
         */
        private boolean gotBallInHole = false;

        private SpriteBatch batch;

        /**
         * Constructor for a simulation
         * @param data the simulation data holding the shotVector, multipliers and initial position
         */
        private Simulator(SimulationData data)
        {
            this.data = data;
            this.initialPosition = data.getInitialPostion();
            this.pushVector = data.getDirection();
            this.forceMultiplier = data.getForceModifier();
            this.heightMultiplier = data.getHeightModifier();
        }

        /**
         * Creates a gameWorld and pushes the ball in that game world. After the ball has stopped moving,
         * store the location and terminate the thread
         */
        @Override
        public void run()
        {
            //create the GameWorld and place the ball at the correct location
            GameWorld gameWorld = gameworld;

            Ball ball = new Ball(initialPosition.x, initialPosition.y, initialPosition.z, "ball1");
            Physics physics = new Physics(gameWorld.getSolidObjects(), ball, gameWorld);
            //gameWorld.resetBall(simulationBall, initialPosition); //"reset it" to be correct position


            //shoot the ball with correct the correct force
            Vector3 finalDirectionVector = new Vector3(pushVector);
            finalDirectionVector = finalDirectionVector.nor();
            finalDirectionVector.x *= (forceMultiplier * SCALING_SPEED_CONSTANT);
            finalDirectionVector.y *= (forceMultiplier * SCALING_SPEED_CONSTANT);
            finalDirectionVector.z *= (heightMultiplier * SCALING_SPEED_CONSTANT);

            Logger.getInstance().log("Going to push the ball with vector: " + finalDirectionVector.toString());
            physics.push(new Vector3(
                    finalDirectionVector.x,
                    finalDirectionVector.y,
                    finalDirectionVector.z)
            );

            //wait until ball has stopped moving
            boolean gotBallInHole = false;
            int counter = 0;

            float lastVectorLength = ball.direction.x + ball.direction.y;

            while(true)
            {
                //  System.out.println("in loooooooooooooooooooooooooooooooooooooooooop");
                if(gameWorld.ballIsInHole(ball))
                {
                    gotBallInHole = true;
                    break;
                }
                float directionLength = ball.direction.x + ball.direction.y;
                if (Math.abs(directionLength - lastVectorLength) < 0.01)
                {
                    //System.out.println("Direction length: " + directionLength + ". Last direction length: " + lastVectorLength);
                    //counter++;
                    if(counter < 5)
                    {
                        lastVectorLength = directionLength;
                        counter++;
                        //System.out.println("Increasing counter by 1. now is: " + counter);
                    }
                    else
                    {
                        //System.out.println("count reset to 0");
                        counter = 0;
                        lastVectorLength = directionLength;
                        break;
                    }
                }
                else
                {
                    lastVectorLength = directionLength;
                }
                try
                {
                    //System.out.println("sleeping for 100ms");
                    Thread.sleep(100);
                }
                catch (Exception e) {e.printStackTrace();}
            }

            //add the end location to the SimulationData and set if ball got in hole
            data.setEndPosition(new Vector3(ball.position));
            if(gotBallInHole)
            {
                data.setGotBallInHole(true);
            }
        }

    }

    /**
     * Returns true when a thread is not running anymore
     *
     * @param t the thread which is like schrodinger cat, not alive nor death
     * @return whether the thread is death (returns true) or alive(returns false)
     */
    private boolean isSimulatorDone(Thread t)
    {
        return t.getState().equals(Thread.State.TERMINATED);
    }
}
