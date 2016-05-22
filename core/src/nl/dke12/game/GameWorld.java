package nl.dke12.game;

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
    private ArrayList<InstanceModel> mapOfWorld;
    private boolean multiplayer;
    private Ball ball;
    private Ball ball2;
    private GameWorldLoader worldLoader;
    private GameController gameController;
    private GameDisplay gameDisplay;
    private Physics physics;
    private Physics physics2;
    private boolean player1Turn;

    public GameWorld(boolean multiplayer)
    {
        this.multiplayer = multiplayer;
        player1Turn = true;

        this.worldLoader = new GameWorldLoader("core/assets/level1.txt");
        this.instances = worldLoader.getModelInstances();
        this.mapOfWorld = worldLoader.getMapOfWorld();
        this.solidObjects = worldLoader.getSolidObjects();

        //this.gameDisplay = new GameDisplay(multiplayer, this);
        //gameDisplay.setInstances(instances);

        createBalls();
        createPhysics();
        createController();
    }

    public void setDisplay(GameDisplay display)
    {
        this.gameDisplay = display;
        gameDisplay.setInstances(instances, mapOfWorld);
    }

    private void createController()
    {
        if(multiplayer)
        {
            this.gameController = new GameController(physics,physics2);
        }
        else
        {
            this.gameController = new GameController(physics);
        }
    }

    private void createPhysics()
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

    private void createBalls()
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

    public void render()
    {
        gameController.moveCamera(gameDisplay.getCamera());
        gameController.move();
        //updatePosition();
        if(player1Turn)
        {
            updatePosition(physics,ball);
            if(multiplayer)
                player1Turn = false;
        }
        else
        {
            updatePosition(physics2, ball2);
            player1Turn = true;
        }

        /*dont advance here*/
    }

//    public void advance(Ball ball)
//    {
//        ball.position.add(ball.direction);
//        gameDisplay.updateBall(ball.direction);
//    }

    public void updatePosition(Physics physics, Ball ball)
    {
        //System.out.println(ball.direction.z);
        if (physics.collides())
        {
            if (physics.bounceVector!=null)
            {
                if(physics.bounceVector.z>0.08)
                {
                    physics.gravit = true;
                    ball.direction.set(physics.bounceVector);
                }
                else
                {
                    //we should not be setting the gravity here, but oh well
                    physics.gravit = false;
                    ball.direction.x=physics.bounceVector.x;
                    ball.direction.y=physics.bounceVector.y;

                    //ball.direction.z=0;
                }
            }
        }
        else
        {
            ball.position.add(ball.direction);

            if(this.ball == ball)
                gameDisplay.updateBall(ball.direction);
            else
                gameDisplay.updateBall2(ball.direction);

            physics.updateVelocity(ball.direction);
        }
    }
}