package nl.dke12.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.utils.Timer;
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

		int buttonX = 100;
		int buttonY = 425;
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

		final TextButton aiButton = new TextButton("SIMPLE AI", textButtonStyle);
		aiButton.setPosition(buttonX, buttonY);
		stage.addActor(aiButton);

		aiButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("simple ai button was clicked");
				stateController.displayAI("simpleAI");
			}
		});

		buttonY += increment;

		final TextButton aiButton1 = new TextButton("SIMULATION", textButtonStyle);
		aiButton1.setPosition(buttonX, buttonY);
		stage.addActor(aiButton1);

		aiButton1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("simulation was clicked");
				stateController.displayAI("simulation");
			}
		});

		buttonY += increment;

		final TextButton aiButton2 = new TextButton("SEMI-RANDOM", textButtonStyle);
		aiButton2.setPosition(buttonX, buttonY);
		stage.addActor(aiButton2);

		aiButton2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("semi random button was clicked");
				stateController.displayAI("semiRandom");
			}
		});

		buttonY += increment;

		final TextButton aiButton3 = new TextButton("A*", textButtonStyle);
		aiButton3.setPosition(buttonX, buttonY);
		stage.addActor(aiButton3);

		aiButton3.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("A* button was clicked");
				stateController.displayAI("pathfindingastar");
			}
		});

		buttonY += increment;

		final TextButton aiButton4 = new TextButton("FLOODFILL", textButtonStyle);
		aiButton4.setPosition(buttonX, buttonY);
		stage.addActor(aiButton4);

		aiButton4.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("floodfill button was clicked");
				stateController.displayAI("pathfindingfloodfill");
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


//		final Sound mp3Sound = Gdx.audio.newSound(Gdx.files.internal("core/assets/tiger_woods_song.mp3"));
//		mp3Sound.play();
//		final long id = mp3Sound.loop();
//		Timer.schedule(new Timer.Task(){
//
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
		stage.getViewport().update(width, height, true);
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