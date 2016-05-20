package nl.dke12.game;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import nl.dke12.controller.GameController;
import nl.dke12.screens.GameDisplay;
import nl.dke12.util.GameWorldLoader;

import java.util.ArrayList;

/**
 * Created by Ajki on 12/05/2016.
 */
public class GameWorld
{
    private ArrayList<SolidObject> solidObjects;
    private ArrayList<InstanceModel> instances;
    private boolean multiplayer;
    private Ball ball;
    private Ball ball2;
    private GameWorldLoader worldLoader;
    private GameController gameController;
    private GameDisplay gameDisplay;
    private Physics physics;
    private Physics physics2;

    public GameWorld(boolean multiplayer)
    {
        this.multiplayer = multiplayer;

        this.worldLoader = new GameWorldLoader("core/assets/level1.txt");
        this.instances = worldLoader.getModelInstances();
        this.solidObjects = worldLoader.getSolidObjects();

        createBalls(multiplayer);
        createPhysics(multiplayer);

        this.gameDisplay = new GameDisplay(instances, multiplayer);
        this.gameController = new GameController(physics);
    }

    private void createPhysics(boolean multiplayer)
    {
        if(multiplayer)
        {
            this.physics = new Physics(solidObjects, ball);
            this.physics2 = new Physics(solidObjects, ball2);
        }
        else
        {
            this.physics = new Physics(solidObjects, ball);
        }
    }

    private void createBalls(boolean multiplayer)
    {
        if(multiplayer)
        {
            this.ball = new Ball(0,0,0);
            this.ball2 = new Ball(0,0,0);
        }
        else
        {
            this.ball = new Ball(0,0,0);
        }
    }

    public void doStuff()
    {
        gameController.moveCamera(gameDisplay.getCamera());
    }

    public void advance(Ball ball)
    {
        ball.position.add(ball.direction);
        //update graphics here :D
        gameDisplay.updateBall(ball.direction);
    }
}
