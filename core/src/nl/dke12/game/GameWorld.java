package nl.dke12.game;

import com.badlogic.gdx.Game;
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
    private boolean isHumanPlayer;
    private boolean first = true;
    private Ball ballSim;
    private Ball ball;
    private Ball ball2;
    private GameWorldLoader worldLoader;
    private GameController gameController;
    private GameDisplay gameDisplay;
    private Physics physics;
    private Physics physics2;
    public static boolean player1Turn;
    private Vector3 holePosition;
    private GameMap gameMap;

    private Vector3 wind;
    private SolidObject solidBall;
    private SolidObject solidBall2;

    public GameWorld(boolean multiplayer, boolean isHumanPlayer)
    {
        this.multiplayer = multiplayer;
        this.isHumanPlayer = isHumanPlayer;
        player1Turn = true;

        this.worldLoader = new GameWorldLoader("core/assets/level1.txt");
        this.instances = worldLoader.getModelInstances();
        this.mapOfWorld = worldLoader.getMapOfWorld();
        this.solidObjects = worldLoader.getSolidObjects();
        holePosition = worldLoader.getHolePosition();

        gameMap = new GameMap(worldLoader);

        //this.gameDisplay = new GameDisplay(multiplayer, this);
        //gameDisplay.setInstances(instances);
        this.wind = new Vector3(wind());

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
        if (multiplayer)
        {
            gameDisplay.updateBall(ball.position);
            gameDisplay.updateBall2(ball2.position);
        }
        else
        {
            gameDisplay.updateBall(ball.position);
        }
    }

    public GameMap getGameMap()
    {
        return gameMap;
    }

    private void createController(boolean isHumanPlayer)
    {
        if(multiplayer)
        {
            this.gameController = new GameController(physics,physics2,false);
            this.gameController.multiplayer = true;
        }
        else
        {
            if(isHumanPlayer)
            {
                this.gameController = new GameController(physics, true);
            }

            else
            {
                this.gameController = new GameController(physics, false);
            }

            this.gameController.multiplayer = false;
        }

    }

    private void createPhysics()
    {
        if(!isHumanPlayer)
        {
            this.physics = new Physics(solidObjects, ballSim, this);
            physics.wind = this.wind;
            physics.noise = true;
        }
        else if(multiplayer)
        {
            try
            {
                solidBall = new SolidObject(ball.position.x, ball.position.y, ball.position.z,"solidBall");
            }
            catch (NoSuchSolidObjectType noSuchSolidObjectType)
            {
                noSuchSolidObjectType.printStackTrace();
            }
            try
            {
                solidBall2 = new SolidObject(ball2.position.x, ball2.position.y, ball2.position.z,"solidBall2");
            }
            catch (NoSuchSolidObjectType noSuchSolidObjectType)
            {
                noSuchSolidObjectType.printStackTrace();
            }

            this.physics = new Physics(solidObjects, ball, this);
            physics.addSolidObject(solidBall2);
            physics.wind=this.wind;
            physics.noise=false;
            //gameDisplay.updateBall(ball.position);

            this.physics2 = new Physics(solidObjects, ball2, this);
            physics2.addSolidObject(solidBall);
            physics2.wind=this.wind;
            physics2.noise=false;
            //gameDisplay.updateBall2(ball2.position);
        }
        else
        {
            this.physics = new Physics(solidObjects, ball, this);
            physics.wind=this.wind;
            physics.noise=true;
        }
    }

    private void createBalls()
    {
        if(!isHumanPlayer)
        {
            this.ballSim = new Ball(0, 0, 0, "ballSim");
        }
        else if(multiplayer)
        {
            this.ball = new Ball(-0.5f, 0.1f, 0,"ball1");
            this.ball2 = new Ball(0.5f, 0.1f, 0,"ball2");
        }
        else
        {
            this.ball = new Ball(0,0,0f,"ball1");
        }
    }

    public static Vector3 wind()
    {
        Vector3 windVec = new Vector3();

        windVec.x = (float) (Math.random() * 0.02f);
        windVec.y = (float) (Math.random() * 0.02f);
        windVec.z = 0;
        windVec.x -= 0.01f;
        windVec.y -= 0.01f;
//        float wStrenght = (float) Math.random()*0.01f;
//
//         windVec.scl(1/windVec.len());

//        windVec.scl(wStrenght);

        return windVec;
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

        gameController.move();

        if(!multiplayer)
        {
            updatePosition(physics,ball);
        }

        if(multiplayer)
        {
            // TODO: 19/06/2016 : what the fuck first ??
            if(first)
            {
                updatePosition(physics, ball);
                updatePosition(physics2, ball2);
                first = false;
            }

            updatePosition(physics, ball);
            updatePosition(physics2, ball2);

            if (multiplayer)
            {
                try
                {
                    solidBall = new SolidObject(ball.position.x, ball.position.y, ball.position.z,"solidBall");
                }
                catch (NoSuchSolidObjectType noSuchSolidObjectType)
                {
                    noSuchSolidObjectType.printStackTrace();
                }
                physics2.updateSolidObject(solidBall);
            }

            if(multiplayer)
            {
                try
                {
                    solidBall2 = new SolidObject(ball2.position.x, ball2.position.y, ball2.position.z,"solidBall2");
                }
                catch (NoSuchSolidObjectType noSuchSolidObjectType)
                {
                    noSuchSolidObjectType.printStackTrace();
                }
                physics.updateSolidObject(solidBall2);
            }
        }
    }

    public void pushBall(String ballName, Vector3 push)
    {
        //for ball to ball collision so not for ai balls

        if(ballName.equals("ball1"))
        {
            push.scl(1.6f);
            physics.push(push.x,push.y,push.z);
        }
        else if(ballName.equals("ball2"))
        {
            push.scl(1.6f);
            physics2.push(push.x,push.y,push.z);
        }
    }

    public void updatePosition(Physics physics, Ball ball)
    {
        if (physics.collides())
        {
            if (physics.bounceVector != null)
            {
                ball.direction.set(physics.bounceVector);
            }
        }
        else
        {
            ball.position.add(ball.direction);

            if(this.ball == ball)
            {
                gameDisplay.updateBall(ball.direction);
            }
            else if (this.ball == ball2)
            {
                gameDisplay.updateBall2(ball.direction);
            }
            // TODO: 19/06/2016 REmovelater
            else
            {
                gameDisplay.updateBall(ball.direction);
            }

            physics.updateVelocity(ball.direction);
        }
    }

    public static void togglePlayerTurn()
    {
        if(player1Turn)
        {
            player1Turn = false;
        }
        else
        {
            player1Turn = true;
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

    public boolean getTurn()
    {
        return player1Turn;
    }

    public void setTurn(boolean turn)
    {
        player1Turn = turn;
    }
}