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
    //render method that renders every object in the gameWorld  and also calls render from physics
    public ArrayList<SolidObject> solidObjects;
    private ArrayList<ModelInstance> instances;
    protected boolean multiplayer;
    private Ball ball;
    private Ball ball2;
    private GameWorldLoader worldLoader;
    private GameController gameController;
    private GameDisplay gameDisplay;


    public GameWorld(boolean multiplayer)
    {
        this.solidObjects = gameObjects;
        this.multiplayer = multiplayer;

        this.worldLoader = new GameWorldLoader();
        this.instances = worldLoader.getModelInstances();
        this.solidObjects = worldLoader.getSolidObjects();

        this.gameDisplay = new GameDisplay();
        this.gameController = new GameController();
        createBalls(multiplayer);
    }

    public void createBalls(boolean multiplayer)
    {
        if(multiplayer)
        {
            this.ball = new Ball(0,0,0,0,0,0,this.solidObjects);
            this.ball2 = new Ball(0,0,0,0,0,0,this.solidObjects);
        }
        else
        {
            this.ball = new Ball(0,0,0,0,0,0,this.solidObjects);
        }
    }

    public void setBall(Ball ball)
    {
        this.ball=ball;
        advance(ball);
    }

    public void setBall2(Ball ball)
    {
        this.ball2=ball;
        advance(ball2);
    }

    public void advance(Ball ball)
    {
        ball.position.add(ball.direction);
        ball.getModelInstance().transform.translate(ball.direction);
        ball.getPhysics().update();
    }
}
