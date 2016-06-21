package nl.dke12.bot;

import com.badlogic.gdx.math.Vector3;
import nl.dke12.controller.GameController;
import nl.dke12.controller.InputProcessor;
import nl.dke12.game.SimulationData;
import nl.dke12.game.GameWorld;
import nl.dke12.game.TestChamber;
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
    private TestChamber simulator;

    public RandomAI(GameWorld gameWorld, InputProcessor processor)
    {
        super(gameWorld, processor);
        rng = new Random(System.currentTimeMillis());
        this.gameController = gameWorld.getGameController();
        this.simulator = new TestChamber(gameWorld);
    }

    @Override
    public void calculateBestMove()
    {
        ArrayList<SimulationData> simData = new ArrayList<SimulationData>();
        super.calculateBestMove();
        Vector3 baseVector = super.distance;

        int degree = -90 + rng.nextInt(180);
        Vector3 shotdir = new Vector3(super.distance);
        shotdir.rotate((float) degree, 0,0, 1);
        shotdir.scl(2.1540658f / shotdir.len());

        float force = rng.nextFloat();
        float height = rng.nextFloat();

        System.out.println(String.format("added: vector:%s height: %f force: %f", shotdir, 0.5, 0.3));

        this.distance = shotdir;
        gameController.setForceMultiplier(force);
        gameController.setHeightMultiplier(height);
    }

    private SimulationData extractBestShot(ArrayList<SimulationData> simData)
    {
        SimulationData bestShot = simData.get(0);
        for(int i = 1; i<simData.size(); i++)
        {
            SimulationData data = simData.get(i);
            Log.log(data.toString());
            if(data.isGotBallInHole())
            {
                return data;
            }
            if(bestShot.absDistFromHole() > data.absDistFromHole())
            {
                bestShot = simData.get(i);
            }
        }
        return bestShot;
    }

}
