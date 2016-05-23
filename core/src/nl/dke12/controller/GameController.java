package nl.dke12.controller;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import nl.dke12.game.Physics;

/**
 * Controlls the flow of the game
 * Handles input and manages movement and camera and score (User interface)
 */
public class GameController
{
    private Physics physics;
    private Physics physics2;
    private Vector3 shotVector;

    private InputProcessor inputProcessor;
    private AIInputProcessor aiInputProcessor;

    public GameController(Physics physics, boolean isHumanPlayer)
    {
        this.physics = physics;
        this.shotVector = new Vector3(0,1,0.8f);

        if(isHumanPlayer)
            inputProcessor = new HumanInputProcessor();
        else
            inputProcessor = new AIInputProcessor();
    }

    public GameController(Physics physics, Physics physics2, boolean isHumanPlayer)
    {
        this(physics, isHumanPlayer);
        this.physics2 = physics2;
    }

    public void setShotVector(Vector3 vector)
    {
        this.shotVector = vector;
    }

    public void moveCamera(Camera camera)
    {
        Vector3 directVector = new Vector3(camera.direction);
        Vector3 sideVector = new Vector3(directVector);
        sideVector.rotate(90,0,0,90);

        if (inputProcessor.moveCamBack())
        {
            camera.translate(-directVector.x/2,-directVector.y/2,0);
        }
        if (inputProcessor.moveCamForward())
        {
            camera.translate(directVector.x/2,directVector.y/2,0);
        }
        if (inputProcessor.moveCamLeft())
        {
            camera.translate(sideVector.x/2,sideVector.y/2,0);
        }
        if (inputProcessor.moveCamRight())
        {
            camera.translate(-sideVector.x/2,-sideVector.y/2,0);
        }
        if (inputProcessor.rotateCamAntiClock())
        {
            camera.rotate(4,0,0,4);
            shotVector.rotate(4,0,0,4);
        }
        if (inputProcessor.rotateCamClock())
        {
            camera.rotate(-4,0,0,4);
            shotVector.rotate(-4,0,0,4);
        }
    }

    public InputProcessor getInputProcessor()
    {
        return inputProcessor;
    }

    public void move()
    {
        if (inputProcessor.moveBall()) {
            System.out.println("move ball is true");
            if(inputProcessor instanceof HumanInputProcessor)
            {
                physics.push(shotVector.x,shotVector.y,shotVector.z);
            }else
            {
                Vector3 direc = new Vector3(inputProcessor.getDirectionVector());
                System.out.println("pushing ball with vector from processor: " + direc);

                physics.push(direc.x, direc.y, direc.z);
            }
            //physics.updatePosition();
        }
        if (inputProcessor.moveBall2()) {
            physics2.push(shotVector.x,shotVector.y,shotVector.z);
            //physics2.updatePosition();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.G)) {
            physics.push(shotVector.x,shotVector.y,0);
            //physics.updatePosition();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.PLUS)) {
            if (physics.getBall().getPosition().z<5)
            {
                physics.push(0,0,10);
                //physics.updatePosition();
            }
        }
    }
}
