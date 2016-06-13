package nl.dke12.controller;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import nl.dke12.game.Physics;
import nl.dke12.util.Logger;

/**
 * Controls the flow of the game
 * Handles input and manages movement and camera and score (User interface)
 */
public class GameController
{
    private static final float SCALING_SPEED_CONSTANT = 2f;

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
        this.shotVector = new Vector3(0,2,0.8f);

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
        String newLabelText = String.format("hit the ball high or low: %.1f", heightMultiplier);
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
        if (inputProcessor.moveBall()) {
            Logger.getInstance().log("game controller decided to move the ball");
            if(inputProcessor instanceof HumanInputProcessor)
            {
                pushBall(shotVector);
            }
            //it's the ai
            else
            {
                Vector3 aiShotVector = new Vector3(inputProcessor.getDirectionVector());
                Logger.getInstance().log("AI has been allowed to shoot the ball with vector: " + aiShotVector.toString() +
                " and force multiplier: " + forceMultiplier + " and height multiplier:  " + heightMultiplier);
                pushBall(aiShotVector);
            }
            //physics.updatePosition();
        }
        if (inputProcessor.moveBall2()) {
            pushBall2(shotVector);
            //physics2.updatePosition();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.G)) {
            pushBall(new Vector3(shotVector.x, shotVector.y, 0));
            //physics.updatePosition();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.PLUS)) {
            if (physics.getBall().getPosition().z<5)
            {
                pushBall(new Vector3(0,0,10));
                //physics.updatePosition();
            }
        }
    }

    private void pushBall(Vector3 directionVector)
    {
        Vector3 finalDirectionVector = new Vector3(directionVector);
        finalDirectionVector = finalDirectionVector.nor();
        finalDirectionVector.x *= (forceMultiplier * SCALING_SPEED_CONSTANT);
        finalDirectionVector.y *= (forceMultiplier * SCALING_SPEED_CONSTANT);
        finalDirectionVector.z *= (heightMultiplier * SCALING_SPEED_CONSTANT);

        Logger.getInstance().log("Going to push the ball with vector: " + finalDirectionVector.toString());
        physics.push(finalDirectionVector.x, finalDirectionVector.y, finalDirectionVector.z);

    }

    private void pushBall2(Vector3 directionVector)
    {
        Vector3 finalDirectionVector = new Vector3(directionVector);
        finalDirectionVector = finalDirectionVector.nor();
        finalDirectionVector.x *= (forceMultiplier * SCALING_SPEED_CONSTANT);
        finalDirectionVector.y *= (forceMultiplier * SCALING_SPEED_CONSTANT);
        finalDirectionVector.z *= (heightMultiplier * SCALING_SPEED_CONSTANT);

        Logger.getInstance().log("Going to push the second ball with vector: " + finalDirectionVector.toString());
        physics2.push(finalDirectionVector.x, finalDirectionVector.y, finalDirectionVector.z);
    }
}
