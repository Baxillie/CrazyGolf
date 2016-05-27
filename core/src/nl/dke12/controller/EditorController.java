package nl.dke12.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import nl.dke12.util.GameWorldSaver;
import nl.dke12.util.ModelSelectButton;

/**
 * Created by Ajki on 12/05/2016.
 */
public class EditorController
{
    public static final String FLOOR = "floor";
    public static final String WALL = "wall";
    public static final String HOLE = "hole";
    public static final String SLOPE = "slope";


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
            changeWhatToPlace("floor");
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2))
        {
            changeWhatToPlace("wall");
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_3))
        {
            changeWhatToPlace("windmill");
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_4))
        {
            changeWhatToPlace("hole");
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_5))
        {
            changeWhatToPlace("slope");
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_7))
        {
            changeWhatToPlace("slopeL");
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_8))
        {
            changeWhatToPlace("slopeU");
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_9))
        {
            changeWhatToPlace("slopeR");
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
        if (Gdx.input.isKeyPressed(Input.Keys.S) &&
                ((Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))||(Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT))))
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
        if (Gdx.input.isKeyPressed(Input.Keys.S) &&
                !(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)||(Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT))))
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

    public void changeWhatToPlace(String model)
    {
        whatToPlace = model;
    }


}
