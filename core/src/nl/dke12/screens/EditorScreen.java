package nl.dke12.screens;

import com.badlogic.gdx.Screen;
import nl.dke12.controller.StateController;

/**
 * The current editor, without the physics implemented
 * just allows you to create and save a world
 */
public class EditorScreen implements Screen
{
    private StateController stateController;

    public EditorScreen(StateController stateController)
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
