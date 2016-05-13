package nl.dke13.screens;

import com.badlogic.gdx.Screen;
import nl.dke13.controller.StateController;

/**
 * allows you to select a hole and sends it to the state controller
 */
public class HoleSelectionScreen implements Screen
{
    //arraylist of all the files available
    //button to apply your selection/confirm
    //displays a list of the names of the levels
    //can select more than one hole

    StateController stateController;

    public HoleSelectionScreen(StateController stateController)
    {
        this.stateController = stateController;
    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
