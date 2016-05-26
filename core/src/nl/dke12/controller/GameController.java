package nl.dke12.controller;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import nl.dke12.game.Physics;

/**
 * Controls the flow of the game
 * Handles input and manages movement and camera and score (User interface)
 */
public class GameController
{
    private Physics physics;
    private Physics physics2;
    private Vector3 shotVector;

    private InputProcessor inputProcessor;
    private AIInputProcessor aiInputProcessor;

    private float forceMultiplier;
    private float heightMultiplier;

    private Label forceLabel;
    private Label heightLabel;

    public GameController(Physics physics, boolean isHumanPlayer)
    {
        this.physics = physics;
        this.shotVector = new Vector3(0,1,0.8f);

        forceMultiplier = 1f;
        heightMultiplier = 1f;

        if(isHumanPlayer)
            inputProcessor = new HumanInputProcessor();
        else
            inputProcessor = new AIInputProcessor();
    }

    public void giveHeightLabel(Label label)
    {
        this.heightLabel = label;
    }

    public void giveForceLabel(Label label)
    {
        this.forceLabel = label;
    }

    public GameController(Physics physics, Physics physics2, boolean isHumanPlayer)
    {
        this(physics, isHumanPlayer);
        this.physics2 = physics2;
    }

    public void setForceMultiplier(float multiplier)
    {
        if(multiplier < 0.1f)
        {
            forceMultiplier = 0;
        }
        else if(multiplier > 1)
        {
            forceMultiplier = 1;
        }
        else
        {
            forceMultiplier = multiplier;
        }
        String newLabelText = String.format("force: %.1f", forceMultiplier);
        if(forceLabel != null)
        {
            forceLabel.setText(newLabelText);
        }
    }

    public void setHeightMultiplier(float multiplier)
    {
        if(multiplier < 0.1f)
        {
            heightMultiplier = 0;
        }
        else if(multiplier > 1)
        {
            heightMultiplier= 1;
        }
        else
        {
            heightMultiplier= multiplier;
        }
        String newLabelText = String.format("hit the bal heigh or low: %.1f", heightMultiplier);
        if(heightLabel != null)
        {
            heightLabel.setText(newLabelText);
        }
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
        if(inputProcessor.decreaseForce())
        {
            System.out.println("should decrease force multiplier");
            setForceMultiplier(forceMultiplier - 0.1f);
        }
        if(inputProcessor.decreaseHeight())
        {
            System.out.println("should decrease height multiplier");
            setHeightMultiplier(heightMultiplier - 0.1f);
        }
        if(inputProcessor.increaseForce())
        {
            System.out.println("should incfrease force multiplier");
            setForceMultiplier(forceMultiplier + 0.1f);
        }
        if(inputProcessor.increaseHeight())
        {
            System.out.println("should increase height multiplier");
            setHeightMultiplier(heightMultiplier + 0.1f);
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
        Vector3 multiPliedVector = new Vector3(shotVector);
        multiPliedVector.x *= forceMultiplier;
        multiPliedVector.y *= forceMultiplier;
        multiPliedVector.z *= heightMultiplier;

        if (inputProcessor.moveBall()) {
            System.out.println("move ball is true");
            if(inputProcessor instanceof HumanInputProcessor)
            {
                physics.push(multiPliedVector.x,multiPliedVector.y,multiPliedVector.z);
            }else
            {
                Vector3 direc = new Vector3(inputProcessor.getDirectionVector());
                System.out.println("pushing ball with vector from processor: " + direc);

                physics.push(direc.x * forceMultiplier, direc.y * forceMultiplier, direc.z * heightMultiplier);
            }
            //physics.updatePosition();
        }
        if (inputProcessor.moveBall2()) {
            physics2.push(multiPliedVector.x,multiPliedVector.y,multiPliedVector.z);
            //physics2.updatePosition();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.G)) {
            physics.push(multiPliedVector.x,multiPliedVector.y,0);
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
