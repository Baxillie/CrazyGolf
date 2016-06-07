package nl.dke12.bot;

import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.math.Vector3;
import nl.dke12.controller.InputProcessor;
import nl.dke12.game.GameWorld;
import nl.dke12.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ajki on 23/05/2016.
 */
public class RandomAI extends SimpleAI {

    private Random rng;

    public RandomAI(GameWorld gameWorld, InputProcessor processor){
        super(gameWorld, processor);
        rng = new Random(System.currentTimeMillis());
    }

    @Override
    protected void calculateBestMove() {
        Vector3 hole = this.holePosition;
        Vector3 ball = new Vector3(this.ballPosition);

        ArrayList<Vector3> randomVectors = new ArrayList<Vector3>(10);

        //create 10 random vectors between the possible
        for (int i = 0; i < 10; i++) {
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
