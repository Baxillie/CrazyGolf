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
        this.simulator = new TestChamber();
    }

    @Override
    public void calculateBestMove()
    {
        ArrayList<SimulationData> simData = new ArrayList<SimulationData>();

        super.calculateBestMove();
        Vector3 baseVector = super.distance;

        //create 10 random vectors between the possible
        for (int i = 0; i < 10; i++)
        {

            float x = rng.nextFloat() * 2 - 1;
            float y = rng.nextFloat() * 2 - 1;
            Vector3 shotdir = new Vector3(baseVector.x + x, baseVector.y + y, 0.8f );
            shotdir.scl(2.1540658f/shotdir.len());

            //float heightmult = Math.round(rng.nextFloat() * 10) / 10;
            float heightmult = rng.nextFloat();
            //float forcemult = Math.round(rng.nextFloat() * 10) / 10;
            float forcemult = rng.nextFloat();

            simData.add(new SimulationData(
                    super.ballPosition,
                    shotdir,
                    heightmult, forcemult,
                    super.holePosition
            ));
        }

        //simulate the 10 shots

        simulator.simulateShot(simData);

        //choose the best one

        SimulationData bestSimulation = extractBestShot(simData);

        this.distance = bestSimulation.getDirection();
        gameController.setForceMultiplier(bestSimulation.getForceModifier());
        gameController.setHeightMultiplier(bestSimulation.getHeightModifier());
    }

    private SimulationData extractBestShot(ArrayList<SimulationData> simData)
    {
        SimulationData bestShot = simData.get(0);
        for(int i = 1; i<simData.size(); i++)
        {
            SimulationData data = simData.get(i);
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
