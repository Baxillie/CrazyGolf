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
        Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();

        //create big skin for game over
        Skin bigSkin = new Skin();
        bigSkin.add("white", new Texture(pixmap));
        BitmapFont bigFont = new BitmapFont();
        bigFont.getData().scale(4);

        //create small skin
        Skin smallSkin = new Skin();
        smallSkin.add("white", new Texture(pixmap));
        BitmapFont smallFont = new BitmapFont();


        Label.LabelStyle bigLabelStyle = new Label.LabelStyle(bigFont, Color.FIREBRICK);
        bigSkin.add("default", bigLabelStyle);

        Label.LabelStyle smallLabelStyle = new Label.LabelStyle(smallFont, Color.LIME);
        smallSkin.add("default", smallLabelStyle);


        Label labelGameOver = new Label("Game Over", bigSkin);
        labelGameOver.setPosition(((Gdx.graphics.getWidth() / 10) * 5) - (labelGameOver.getWidth() / 2), (Gdx.graphics.getHeight() / 10) * 7);

        Label player1 = new Label(String.format("Player one took %d turns to put the ball in the hole", player1Turns),smallSkin);
        player1.setPosition(labelGameOver.getX() + 30, labelGameOver.getY() - 70);

        Label player2 = new Label(String.format("Player two took %d turns to put the ball in the hole", player2Turns),smallSkin);
        player2.setPosition(labelGameOver.getX() + 30, labelGameOver.getY() - 100);

        stage.addActor(labelGameOver);
        stage.addActor(player1);
        stage.addActor(player2);
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
