package nl.dke13.screens;

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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Instructions implements Screen
{
    private Texture texture;
    private Sprite sprite;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private MenuScreen menuScreen;
    private MainMenu mainMenu;

    public Instructions(MainMenu mainMenu ,MenuScreen menuScreen){
        this.menuScreen = menuScreen;
        this.mainMenu = mainMenu;
        create();
    }

    private void create(){
        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("core/assets/data/CSS.png"));
        sprite = new Sprite(texture);

        //ButtonBack
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();

        Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();

        skin.add("white", new Texture(pixmap));

        BitmapFont bfont = new BitmapFont();
        skin.add("default",bfont);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();

        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        final TextButton backButton=new TextButton("BACK",textButtonStyle);
        backButton.setPosition(50, 125);
        stage.addActor(backButton);

        backButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
               // backButton.setText("Back to main menu");
                mainMenu.setScreen(new MenuScreen(mainMenu));
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        sprite.draw(batch);
        batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
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
        batch.dispose();
        texture.dispose();
    }
}
