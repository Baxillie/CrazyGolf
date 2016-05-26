package nl.dke12.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import nl.dke12.util.GameWorldSaver;

/**
 * Created by Ajki on 12/05/2016.
 */
public class EditorController
{
    private String whatToPlace;
    private StateController stateController;

    public EditorController(StateController stateController)
    {
        whatToPlace = "floor";
        this.stateController = stateController;
    }

    public void whatToPlace()
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_1))
        {
            whatToPlace = "floor";
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2))
        {
            whatToPlace = "wall";
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_3))
        {
            whatToPlace = "windmill";
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_4))
        {
            whatToPlace = "hole";
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_5))
        {
            whatToPlace = "slope";
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_7))
        {
            whatToPlace = "slopeL";
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_8))
        {
            whatToPlace = "slopeU";
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_9))
        {
            whatToPlace = "slopeR";
        }
    }

    public String getWhatToPlace()
    {
        return whatToPlace;
    }

    public boolean spaceBar()
    {
        return Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
    }

    public boolean up()
    {
        return Gdx.input.isKeyJustPressed(Input.Keys.UP);
    }

    public boolean down()
    {
        return Gdx.input.isKeyJustPressed(Input.Keys.DOWN);
    }

    public boolean left()
    {
        return Gdx.input.isKeyJustPressed(Input.Keys.LEFT);
    }

    public boolean right()
    {
        return Gdx.input.isKeyJustPressed(Input.Keys.RIGHT);
    }

    public void save(int[][][] level)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            GameWorldSaver saver = new GameWorldSaver();
            saver.fileWriter("core/assets/level1.txt",level);
        }
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
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E))
        {
            camera.rotate(-4,0,0,4);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.BACKSPACE))
        {
            stateController.displayMenuScreen();
        }
    }
}
