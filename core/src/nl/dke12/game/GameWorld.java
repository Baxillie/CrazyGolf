package nl.dke12.game;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;

/**
 * Created by Ajki on 12/05/2016.
 */
public class GameWorld
{
    //GameController

    //render method that renders every object in the gameWorld  and also calls render from physics
    public ArrayList<SolidObject> solidObjects;
    private Environment environment;
    private ArrayList<ModelInstance> instances;
    protected boolean multiplayer;
    private Ball ball;
    private Ball ball2;

    private ModelInstance golfBall;
    private ModelInstance golfBall2;

    public GameWorld(ArrayList<SolidObject> gameObjects, Environment environment, boolean multiplayer)
    {
        this.solidObjects = gameObjects;
        this.environment = environment;
        this.multiplayer = multiplayer;

    }


    public GameWorld()
    {
        this.solidObjects= new ArrayList<>();
        this.instances = new ArrayList<>();
        createBalls(false);
    }


    public void createBalls(boolean multiplayer)
    {
        if(multiplayer)
        {
            this.ball = new Ball(golfBall,0,0,0,0,0,0,this.solidObjects);
            this.ball2 = new Ball(golfBall2,0,0,0,0,0,0,this.solidObjects);

        }
        else
        {
            this.ball = new Ball(golfBall,0,0,0,0,0,0,this.solidObjects);
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



    public void addGameObject(SolidObject gameObject)
    {
        solidObjects.add(gameObject);
    }

    public void advance(Ball ball)
    {
        ball.position.add(ball.direction);
        ball.getModelInstance().transform.translate(ball.direction);
        ball.getPhysics().update();
    }
}
