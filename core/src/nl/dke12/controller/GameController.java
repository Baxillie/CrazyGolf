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

    public GameController(Physics physics)
    {
        this.physics = physics;
        this.shotVector = new Vector3(0,1,0.8f);
        inputProcessor = new InputProcessor();
        aiInputProcessor = new AIInputProcessor();
    }

    public GameController(Physics physics, Physics physics2)
    {
        this.physics = physics;
        this.physics2 = physics2;
        this.shotVector  = new Vector3(0,1,0.8f);
        inputProcessor = new InputProcessor();
        aiInputProcessor = new AIInputProcessor();
    }

    public void moveCamera(Camera camera)
    {
        Vector3 directVector = new Vector3(camera.direction);
        Vector3 sideVector = new Vector3(directVector);
        sideVector.rotate(90,0,0,90);
        if (Gdx.input.isKeyPressed(Input.Keys.S))
        {
            camera.translate(-directVector.x/2,-directVector.y/2,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W))
        {
            camera.translate(directVector.x/2,directVector.y/2,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A))
        {
            camera.translate(sideVector.x/2,sideVector.y/2,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D))
        {
            camera.translate(-sideVector.x/2,-sideVector.y/2,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q))
        {
            camera.rotate(4,0,0,4);
            shotVector.rotate(4,0,0,4);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E))
        {
            camera.rotate(-4,0,0,4);
            shotVector.rotate(-4,0,0,4);
        }
    }

    public void move()
    {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            physics.push(shotVector.x,shotVector.y,shotVector.z);
            //physics.updatePosition();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.H)) {
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
