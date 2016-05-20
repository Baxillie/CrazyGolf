package nl.dke12.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import nl.dke12.screens.EditorScreen;
import nl.dke12.screens.GameDisplay;
import nl.dke12.util.GameWorldLoader;

/**
 * Controlls the flow of the game
 * Handles input and manages movement and camera and score (User interface)
 */
public class GameController
{
    //Ball
    //InputProcessor
    //Makes GameDisplay
    //Makes GameWorld
    //
    private GameDisplay gameDisplay;
    public GameController(GameDisplay gameDisplay)
    {
        this.gameDisplay = gameDisplay;
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
        if (Gdx.input.isKeyPressed(Input.Keys.Z))
        {
            camera.translate(directVector.x/2,directVector.y/2,0);
        //camera.direction.x;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q))
        {
            camera.translate(sideVector.x/2,sideVector.y/2,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D))
        {
            camera.translate(-sideVector.x/2,-sideVector.y/2,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A))
        {
            camera.rotate(4,0,0,4);
            gameDisplay.turn(4,0,0,4);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E))
        {
            camera.rotate(-4,0,0,4);
            gameDisplay.turn(-4,0,0,4);
        }
    }

    public void load()
    {
        GameWorldLoader loader = new GameWorldLoader(gameDisplay);
        loader.fileReader("core/assets/level1.txt");
    }

    public void move(Vector3 shotVector)
    {
        if (Gdx.input.isKeyPressed(Input.Keys.T)) {
            gameDisplay.getBall().getPhysics().push(shotVector.x,shotVector.y,shotVector.z);
            gameDisplay.getBall().getPhysics().updatePosition();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Y)) {
            gameDisplay.getBall().getPhysics().push(shotVector.x,shotVector.y,shotVector.z);
            gameDisplay.getBall().getPhysics().updatePosition();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.G)) {
            gameDisplay.getBall().getPhysics().push(shotVector.x,shotVector.y,0);
            gameDisplay.getBall().getPhysics().updatePosition();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.PLUS)) {
            if (gameDisplay.getBall().position.z<5)
            {
                gameDisplay.getBall().getPhysics().push(0,0,10);
                gameDisplay.getBall().getPhysics().updatePosition();
            }
        }
    }
}
