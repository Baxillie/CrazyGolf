package nl.dke12.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import nl.dke12.controller.StateController;


public class MenuScreen implements Screen {
	private SpriteBatch batch;
	private SpriteBatch batch1;
	private Texture texture;
	private Sprite sprite;
	private Stage stage;
	private Skin skin;
	private StateController stateController;

	public MenuScreen(StateController stateController){
		this.stateController = stateController;
		create();
	}

	private void create() {
		batch = new SpriteBatch();
		texture = new Texture(Gdx.files.internal("core/assets/t_woods.png"));
		sprite = new Sprite(texture);

		//Button1
		batch1 = new SpriteBatch();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		skin = new Skin();

		Pixmap pixmap = new Pixmap(100, 100, Format.RGBA8888);
		pixmap.setColor(Color.GREEN);
		pixmap.fill();

		skin.add("white", new Texture(pixmap));

		BitmapFont bfont = new BitmapFont();
		skin.add("default",bfont);

		TextButtonStyle textButtonStyle = new TextButtonStyle();

		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);

		int buttonX = 150;
		int buttonY = 350;
		int increment = -50;

		final TextButton playgGameButton=new TextButton("SINGLE PLAYER",textButtonStyle);
		playgGameButton.setPosition(buttonX, buttonY);
		stage.addActor(playgGameButton);

		playgGameButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				//System.out.println("Clicked! Is checked: " + button.isChecked());
				//if(backButton.isPressed() == true)
				//playgGameButton.setText("Starting new game");
				System.out.println("single player was clicked");
				stateController.displayGameDisplay(false);
				//stateController.displayAI();
			}
		});

		buttonY += increment;

		final TextButton playMultiPlayer=new TextButton("MULTIPLAYER",textButtonStyle);
		playMultiPlayer.setPosition(buttonX, buttonY);
		stage.addActor(playMultiPlayer);

		playMultiPlayer.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				//System.out.println("Clicked! Is checked: " + button.isChecked());
				//playMultiPlayer.setText("Starting new game");
				System.out.println("multiplayer was clicked");
				stateController.displayGameDisplay(true);

			}
		});

		buttonY += increment;

		final TextButton aiButton = new TextButton("AI GAME", textButtonStyle);
		aiButton.setPosition(buttonX, buttonY);
		stage.addActor(aiButton);

		aiButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				stateController.displayAI();
			}
		});

		buttonY += increment;

		final TextButton editorButton=new TextButton("EDITOR",textButtonStyle);
		editorButton.setPosition(buttonX, buttonY);
		stage.addActor(editorButton);

		editorButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				//System.out.println("Clicked! Is checked: " + button.isChecked());
				//editorButton.setText("Starting new game");
				System.out.println("editor screen was clicked");
				stateController.displayEditorScreen();
			}
		});

		buttonY += increment;

		final TextButton controlButton = new TextButton("CONTROLS",textButtonStyle);
		controlButton.setPosition(buttonX, buttonY);
		stage.addActor(controlButton);
		controlButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				//System.out.println("Clicked! Is checked: " + button.isChecked());
				//controlButton.setText("CONTROLS");
				System.out.println("controls was clicked");
				stateController.displayInstructions();
			}
		});

		//New Screen
//		//todo: uncomment this
//		final Sound mp3Sound = Gdx.audio.newSound(Gdx.files.internal("core/assets/tiger_woods_song.mp3"));
//		mp3Sound.play();
//		final long id = mp3Sound.loop();
//		Timer.schedule(new Timer.Task(){
//			@Override
//			public void run(){
//				mp3Sound.stop(id);
//			}
//		}, 3.0f);



	}

	@Override
	public void dispose() {
		/*batch.dispose();
		texture.dispose();
		batch1.dispose();*/
	}

	@Override
	public void render(float delta) {
		//Background
		//	Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		sprite.draw(batch);
		batch.end();
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	@Override
	public void resize(int width, int height){

	}

	@Override
	public void pause()	{

	}

	@Override
	public void resume() {

	}

	@Override
	public void show() {
			}

	@Override
	public void hide() {

	}
}