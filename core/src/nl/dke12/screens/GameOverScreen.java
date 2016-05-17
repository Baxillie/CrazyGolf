package nl.dke12.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import nl.dke12.controller.StateController;

public class GameOverScreen implements Screen{

    private int player1Turns;
    private int player2Turns;
    private Stage stage;
    private boolean multiplayer;
    private StateController stateController;
    private SpriteBatch batch;
    private Sprite sprite;

    public GameOverScreen(int player1Turns, int player2Turns, StateController stateController) {
        this.player1Turns = player1Turns;
        this.player2Turns = player2Turns;
        stage = new Stage();
        multiplayer = true;
        this.stateController = stateController;
        createLabels();
        createButton();
        createBackground();
    }

    public GameOverScreen(int player1Turns, StateController stateController) {
        this.player1Turns = player1Turns;
        stage = new Stage();
        multiplayer = false;
        this.stateController = stateController;
        createLabels();
        createButton();
        createBackground();
    }

    private void createLabels()
    {
        Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGB888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        //create small skin
        Skin smallSkin = new Skin();
        smallSkin.add("BLACK", new Texture(pixmap));
        BitmapFont smallFont = new BitmapFont();

        Label.LabelStyle smallLabelStyle = new Label.LabelStyle(smallFont, Color.PINK);
        smallSkin.add("default", smallLabelStyle);

        Label player1 = new Label(String.format("Player 1: %d turns", player1Turns),smallSkin);
        player1.setPosition(10, 70);

        Label player2 = new Label(String.format("Player 2: %d turns", player2Turns),smallSkin);
        player2.setPosition(10, 30);

        stage.addActor(player1);
        if (multiplayer)
            stage.addActor(player2);

        Gdx.input.setInputProcessor(stage);
    }

    public void createButton()
    {
        Skin skin = new Skin();

        Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        skin.add("black", new Texture(pixmap));

        BitmapFont bfont = new BitmapFont();
        bfont.setColor(Color.BLACK);
        skin.add("default",bfont);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();

        textButtonStyle.font = skin.getFont("default");
        textButtonStyle.fontColor = Color.BLACK;
        skin.add("default", textButtonStyle);

        final TextButton menuButton=new TextButton("Back To Menu",textButtonStyle);
        menuButton.setPosition(Gdx.graphics.getWidth() - 125, 25);
        stage.addActor(menuButton);

        menuButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                //System.out.println("Clicked! Is checked: " + button.isChecked());
                //if(backButton.isPressed() == true)
                //playgGameButton.setText("Starting new game");
                stateController.displayMenuScreen();
            }
        });
    }

    public void createBackground ()
    {
        batch = new SpriteBatch();
        Texture texture = new Texture(Gdx.files.internal("core/assets/gameover.png"));
        sprite = new Sprite(texture);
    }
    @Override
    public void render(float delta)
    {
        //some necessary OPENGL stuff which I dont understand yet
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        batch.begin();
        sprite.draw(batch);
        batch.end();
        stage.draw();
    }

    @Override
    public void show() {

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
