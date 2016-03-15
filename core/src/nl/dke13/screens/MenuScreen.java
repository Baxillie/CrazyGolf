package nl.dke13.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.awt.*;

/**
 * Created by nik on 3/15/16.
 */
public class MenuScreen implements Screen {

    private Stage stage;
    private Skin skin;
    private SpriteBatch sprite;

    private Game game;

    public MenuScreen(Game game)
    {
        this.game = game;
    }

    @Override
    public void show() {
        //create the stage
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        //create the skin for button 1
        skin = new Skin();

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
