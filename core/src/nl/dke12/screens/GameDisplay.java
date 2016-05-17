package nl.dke12.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import nl.dke12.game.GameObject;
import nl.dke12.game.GameWorld;

/**
 * displays the current state of the GameWorld
 */
public class GameDisplay implements Screen
{

    //GameWorld
    //User interface
    //GameController
    private GameWorld gameWorld;

    private Camera camera;
    private ModelBatch renderer;

    public GameDisplay(GameWorld gameWorld)
    {
        this.gameWorld = gameWorld;
        this.camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.translate(0,-10, 10);
        camera.lookAt(0,0,0);


        this.renderer = new ModelBatch();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
    //main loop of game
        //call render of GameWorld which does the physics stuff
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        renderer.begin(camera);
        gameWorld.render(renderer);
        renderer.end();

        camera.update();
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
