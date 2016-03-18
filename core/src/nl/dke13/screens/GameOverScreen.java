package nl.dke13.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.sun.javaws.Main;

/**
 * Created by Ajki on 18/03/2016.
 */
public class GameOverScreen implements Screen{

    private int player1Turns;
    private int player2Turns;
    Stage stage;
    boolean multiplayer;
    MainMenu mainMenu;

    public GameOverScreen(int player1Turns, int player2Turns, MainMenu mainMenu) {
        this.player1Turns = player1Turns;
        this.player2Turns = player2Turns;
        stage = new Stage();
        multiplayer = true;
        this.mainMenu = mainMenu;
        createLabels();
        createButton();
    }

    public GameOverScreen(int player1Turns, MainMenu mainMenu) {
        this.player1Turns = player1Turns;
        stage = new Stage();
        multiplayer = false;
        this.mainMenu = mainMenu;
        createLabels();
        createButton();
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
        if (multiplayer)
            stage.addActor(player2);

        Gdx.input.setInputProcessor(stage);
    }

    public void createButton()
    {
        Skin skin = new Skin();

        Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();

        skin.add("white", new Texture(pixmap));

        BitmapFont bfont = new BitmapFont();
        //bfont.scale(1);
        skin.add("default",bfont);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();

        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        final TextButton menuButton=new TextButton("Back To Menu",textButtonStyle);
        menuButton.setPosition(Gdx.graphics.getWidth()/2 - menuButton.getWidth()/2, 150);
        stage.addActor(menuButton);

        menuButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                //System.out.println("Clicked! Is checked: " + button.isChecked());
                //if(backButton.isPressed() == true)
                //playgGameButton.setText("Starting new game");
                mainMenu.setScreen(new MenuScreen(mainMenu));
            }
        });


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta)
    {
        //some necessary OPENGL stuff which I dont understand yet
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

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
