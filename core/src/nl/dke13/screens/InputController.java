package nl.dke13.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

/**
 * Created by Ajki on 17/03/2016.
 */
public class InputController implements InputProcessor {

    CameraInputController camera;

    public InputController(CameraInputController camera)
    {
        this.camera = camera;

        //create multipleyer
//        InputMultiplexer multiplexer = new InputMultiplexer();
//        multiplexer.addProcessor(camera);
//        camera.
//        //multiplexer.addProcessor(new InputController());
//        Gdx.input.setInputProcessor(multiplexer);
    }
    @Override
    public boolean keyDown(int keycode)
    {
        switch (keycode)
        {
            case 21:
            {
                System.out.println("pressed left arrow key");

                break;
            }
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {


        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
