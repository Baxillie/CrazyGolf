package nl.dke12.bot;

import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import nl.dke12.controller.GameController;
import nl.dke12.controller.InputProcessor;
import nl.dke12.game.Ball;
import nl.dke12.game.BallSimData;
import nl.dke12.game.GameWorld;
import nl.dke12.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ajki on 23/05/2016.
 */
public class RandomAI extends SimpleAI
{
    private GameController gameController;
    private Random rng;

    public RandomAI(GameWorld gameWorld, InputProcessor processor)
    {
        super(gameWorld, processor);
        rng = new Random(System.currentTimeMillis());
        this.gameController = gameWorld.getGameController();
    }

    @Override
    public void calculateBestMove()
    {
        ArrayList<Vector3> randomVectors = new ArrayList<Vector3>(10);
        ArrayList<BallSimData> simData = new ArrayList<BallSimData>();

        super.calculateBestMove();
        Vector3 baseVector = super.distance;

        boolean flag = false;
        //create 10 random vectors between the possible
        for (int i = 0; i < 10; i++)
        {

            float x = rng.nextFloat() * 2 - 1;
            float y = rng.nextFloat() * 2 - 1;
            Vector3 shotdir = new Vector3(baseVector.x + x, baseVector.y + y, 0.8f );
            shotdir.scl(2.1540658f/shotdir.len());

            //float heightmult = Math.round(rng.nextFloat() * 10) / 10;
            float heightmult = rng.nextFloat();
            gameController.setHeightMultiplier(heightmult);
            //float forcemult = Math.round(rng.nextFloat() * 10) / 10;
            float forcemult = rng.nextFloat();
            gameController.setForceMultiplier(forcemult);

            gameController.pushBallSim(shotdir);
            //gameWorld.getPhysics().push(shotdir);

            float lastVectorLength = gameWorld.getBallDirection(gameWorld.getBallSim()).x + gameWorld.getBallDirection(gameWorld.getBallSim()).y;
            //System.out.println(gameWorld.isMoving + " is game world moving ? ");
            int counter = 0;
            flag = false;
            while(true)
            {
              //  System.out.println("in loooooooooooooooooooooooooooooooooooooooooop");
                if(gameWorld.ballIsInHole(gameWorld.getBallSim()))
                {
                    flag = true;
                    break;
                }
                float directionLength = gameWorld.getBallDirection(gameWorld.getBallSim()).x + gameWorld.getBallDirection(gameWorld.getBallSim()).y;
                if (Math.abs(directionLength - lastVectorLength) < 0.01)
                {
                    //counter++;
                    if(counter < 50)
                    {
                        lastVectorLength = directionLength;
                        counter++;
                    }
                    else
                    {
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
                    Thread.sleep(100);
                }
                catch (Exception e) {e.printStackTrace();}
            }

            simData.add(new BallSimData(shotdir, heightmult, forcemult, gameWorld.getBallSimPosition(), holePosition));
            gameWorld.resetBall(gameWorld.getBallSim(), new Vector3(0,0,0));
            if (flag)
            {
                break;
            }
        }
        if(flag)
        {
            Log.log(simData.toString());
            BallSimData bestShot = simData.get(simData.size()-1);
            gameController.setForceMultiplier(bestShot.getForceModifier());
            gameController.setHeightMultiplier(bestShot.getHeightModifier());
            super.distance = bestShot.getDirection();
            System.out.println("THIS IS THE BEST SHOT because ball is in hole ");
            Log.log("it's taking entry " + bestShot);
            Log.log("hole position is  " + holePosition);
        }
        else
        {
            Log.log(simData.toString());
            BallSimData bestShot = extractBestShot(simData);
            gameController.setForceMultiplier(bestShot.getForceModifier());
            gameController.setHeightMultiplier(bestShot.getHeightModifier());
            super.distance = bestShot.getDirection();
            System.out.println("THIS IS THE BEST SHOT ");
            Log.log("it's taking entry " + bestShot);
            Log.log("hole position is  " + holePosition);
        }
    }

    private BallSimData extractBestShot(ArrayList<BallSimData> simData)
    {
        BallSimData bestShot = simData.get(0);
        for(int i = 1; i<simData.size(); i++)
        {
            if(bestShot.absDistFromHole() > simData.get(i).absDistFromHole())
            {
                bestShot = simData.get(i);
            }
        }
        return bestShot;
    }


    protected void calc()
    {
        Vector3 hole = this.holePosition;
        Vector3 ball = new Vector3(this.ballPosition);

        ArrayList<Vector3> randomVectors = new ArrayList<Vector3>(10);

        //create 10 random vectors between the possible
        for (int i = 0; i < 10; i++)
        {
            float x = rng.nextFloat() * 2 - 1;
            float y = rng.nextFloat() * 2 - 1;
            float z = 0.1f; //dont push it up or down
            Vector3 randomVector = new Vector3(x, y, z);
            Log.log(String.format("Random vector %d: %s", i, randomVector.toString()));
            randomVectors.add(randomVector);
        }

        //determine which random vector is the closest to the hole.
        Vector3 bestVector = randomVectors.get(0);
        Vector3 distance = new Vector3(new Vector3(holePosition).sub(bestVector));
        float bestDist = distance.len();
        float distanceFromHole;

        for (int i = 1; i < randomVectors.size(); i++)
        {
            distance = new Vector3(new Vector3(holePosition).sub(randomVectors.get(i)));
            distanceFromHole = distance.len();
            if(distanceFromHole < bestDist)
            {
                bestVector = randomVectors.get(i);
                bestDist = distanceFromHole;
            }
        }

        //set the vector which is going to be given to the physics
        this.distance = new Vector3(bestVector); //// TODO: 23/05/2016
        Log.log("decided on vector: " + distance.toString());
    }
}
