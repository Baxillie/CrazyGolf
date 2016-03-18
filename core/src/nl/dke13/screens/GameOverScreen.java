package nl.dke13.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by Ajki on 18/03/2016.
 */
public class GameOverScreen implements Screen{

    private int player1Turns;
    private int player2Turns;
    Stage stage;

    public GameOverScreen(int player1Turns, int player2Turns) {
        this.player1Turns = player1Turns;
        this.player2Turns = player2Turns;
        stage = new Stage();
        createLabels();

    }

    private void createLabels()
    {
        Skin skin = new Skin();

        Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();

        skin.add("white", new Texture(pixmap));

        BitmapFont bfont = new BitmapFont();
        bfont.getData().scale(4);

        skin.add("default",bfont);
        Label.LabelStyle labelStyle = new Label.LabelStyle(bfont, Color.FIREBRICK);

        skin.add("default", labelStyle);

        Label label = new Label("Game Over", skin);
        label.setPosition(((Gdx.graphics.getWidth() / 10) * 5) - (label.getWidth() / 2), (Gdx.graphics.getHeight() / 10) * 8);

        stage.addActor(label);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.draw();
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
