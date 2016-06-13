package nl.dke12.game;

import com.badlogic.gdx.math.Vector3;
import nl.dke12.controller.GameController;
import nl.dke12.controller.InputProcessor;
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
    private Vector3 holePosition;

    public GameWorld(boolean multiplayer, boolean isHumanPlayer)
    {
        this.multiplayer = multiplayer;
        player1Turn = true;

        this.worldLoader = new GameWorldLoader("core/assets/level1.txt");
        this.instances = worldLoader.getModelInstances();
        this.mapOfWorld = worldLoader.getMapOfWorld();
        this.solidObjects = worldLoader.getSolidObjects();
        holePosition = worldLoader.getHolePosition();

        //this.gameDisplay = new GameDisplay(multiplayer, this);
        //gameDisplay.setInstances(instances);

        createBalls();
        createPhysics();
        createController(isHumanPlayer);
    }

    public GameController getGameController()
    {return gameController;}

    public void setDisplay(GameDisplay display)
    {
        this.gameDisplay = display;
        gameDisplay.setInstances(instances, mapOfWorld);
    }

    private void createController(boolean isHumanPlayer)
    {
        if(multiplayer)
        {
            this.gameController = new GameController(physics,physics2,false);
        }
        else
        {
            if(isHumanPlayer)
                this.gameController = new GameController(physics, true);
            else
                this.gameController = new GameController(physics, false);
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
            this.ball = new Ball(0,0,0f);
            this.ball2 = new Ball(0,0,0);
        }
        else
        {
            this.ball = new Ball(0,0,0f);
        }
    }

    public boolean ballIsInHole()
    {
        Vector3 ballPosition = ball.position;
        Vector3 holePosition = getHolePosition();
        if(ballPosition.z < -10)
        {
            return true;
        }
        return false;
    }

    public void render()
    {
        gameController.moveCamera(gameDisplay.getCamera());


       // if(ball.direction.isZero(0.001f))
            gameController.move();

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
    }

    public void updatePosition(Physics physics, Ball ball)
    {
        //System.out.println(ball.direction.z);
        if (physics.collides())
        {
            if (physics.bounceVector != null)
            {
                if(physics.bounceVector.z > 0.08)
                {
                    //physics.gravit = false;
                    ball.direction.set(physics.bounceVector);
                }
                else
                {
                    //we should not be setting the gravity here, but oh well
                    //physics.gravit = false;
                    /*ball.direction.x=physics.bounceVector.x;
                    ball.direction.y=physics.bounceVector.y;


                    ball.direction.z=0;*/
                    ball.direction.set(physics.bounceVector);
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

    public Vector3 getBallPosition()
    {
        return ball.position;
    }

    public Vector3 getBallDirection()
    {
        return ball.direction;
    }

    public Vector3 getHolePosition()
    {
        return holePosition;
    }
}