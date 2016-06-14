package nl.dke12.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import nl.dke12.controller.StateController;
import nl.dke12.game.GameWorld;
import nl.dke12.game.InstanceModel;

import java.util.ArrayList;

/**
 * displays the current state of the GameWorld
 */
public class GameDisplay implements Screen
{
	private Camera camera;
	private CameraInputController cameraController;
	private ModelBatch renderer;

	private boolean multiplayer;
	private ArrayList<InstanceModel> instances;
	private ArrayList<InstanceModel> mapOfWorld;

	private ModelInstance skyboxModel;
	private ModelInstance TWModel;
	private ModelInstance ballModel;
	private ModelInstance ball2Model;

	private Model mapModel;
	private ModelInstance map;
	private Mesh mesh;

	private Environment environment;
	private Environment skyEnvironment;
	private AnimationController controller;
	private AnimationController spincontroller;

	private StateController stateController;

	private GameWorld gameWorld;

	//userinterface elements
	private Stage stage;
	private SpriteBatch spriteBatch;

	public GameDisplay(boolean multiplayer, GameWorld gameWorld, StateController stateController)
	//todo: add multiplayer to the constructor
	{
		create();
		this.instances = new ArrayList<InstanceModel>();
		this.multiplayer = multiplayer;
		this.gameWorld = gameWorld;

		this.stateController = stateController;

		setUpUserInteface();
	}

	public void create()
	{
		camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0f, -13f, 7f);
		camera.lookAt(0f, 0f, 0f);
		camera.near = 0.1f;
		camera.far = 300.0f;
		cameraController = new CameraInputController(camera);

		if(Gdx.input.isKeyPressed(Input.Keys.H))
		{
			/*Plz Ignore this, just me(Tom) trying stuff out*/
			HeightmapConverter heightmap = new HeightmapConverter(30,30,500,"Heightmap.png");


			Material material = new Material(new IntAttribute(IntAttribute.CullFace), ColorAttribute.createDiffuse(Color.GRAY));
			ModelBuilder modelBuilder = new ModelBuilder();

			modelBuilder.begin();

			mapModel = modelBuilder.end();

			map = new ModelInstance(mapModel);
			map.transform.scale(20f,20f,20f);
		}

		this.renderer = new ModelBatch();


		// Finally we want some light, or we wont see our color.  The environment gets passed in during
		// the rendering process.  Create one, then create an Ambient ( non-positioned, non-directional ) light.
		environment = new Environment();
		skyEnvironment = new Environment();

		skyEnvironment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

	}

	public void setUpUserInteface()
	{
		this.stage = new Stage();
		this.spriteBatch = new SpriteBatch();

		BitmapFont font = new BitmapFont();

		//amount of force
		Label.LabelStyle forceLabelStyle = new Label.LabelStyle(font,Color.RED);
		Label forceLabel = new Label("force: 1", forceLabelStyle);
		forceLabel.setX(0);
		forceLabel.setY(Gdx.graphics.getHeight() - forceLabel.getHeight());

		//amount of height
		Label.LabelStyle heightLabelStyle = new Label.LabelStyle(font, Color.BROWN);
		Label heightLabel = new Label("hit the ball high or low: 1", heightLabelStyle);
		heightLabel.setX(forceLabel.getX() + forceLabel.getWidth() + 20);
		heightLabel.setY(forceLabel.getY());

		stage.addActor(forceLabel);
		stage.addActor(heightLabel);

		gameWorld.getGameController().giveForceLabel(forceLabel);
		gameWorld.getGameController().giveHeightLabel(heightLabel);
	}

	public void find()
	{
		String type;
		for (InstanceModel instance : instances)
		{
			type = instance.type;
			if (type == "skybox")
				skyboxModel = instance.modelInstance;
			else if (type == "twstatue")
				TWModel = instance.modelInstance;
			else if (type == "ball")
				ballModel = instance.modelInstance;
			else if (type == "ball2")
				ball2Model = instance.modelInstance;
		}
	}

	public void updateBall(Vector3 direction)
	{
		if(multiplayer){

			ballModel.transform.translate(direction);
		}
	}

	public void updateBall2(Vector3 direction)
	{
		if(multiplayer){
			ball2Model.transform.translate(direction);

		}
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		gameWorld.render();

		if(Gdx.input.isKeyPressed(Input.Keys.BACKSPACE))
		{
			stateController.displayMenuScreen();
		}

		renderer.begin(camera);
		renderer.render(skyboxModel, skyEnvironment);
		renderer.render(TWModel, environment);
		renderer.render(ballModel, environment);

		if (multiplayer){
			renderer.render(ball2Model, environment);
		}

		//Tom messing around here too, don't touch plz (unless you know what you're doing)
		//draw using spritebatch? render using modelbatch?
		//renderer.setShader();
		//        renderer.render(map, environment);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//
		//        String vertexShader = "attribute vec4 a_position;    \n" +
		//                "attribute vec4 a_color;\n" +
		//                "attribute vec2 a_texCoord0;\n" +
		//                "uniform mat4 u_worldView;\n" +
		//                "varying vec4 v_color;" +
		//                "varying vec2 v_texCoords;" +
		//                "void main()                  \n" +
		//                "{                            \n" +
		//                "   v_color = vec4(1, 1, 1, 1); \n" +
		//                "   v_texCoords = a_texCoord0; \n" +
		//                "   gl_Position =  u_worldView * a_position;  \n"      +
		//                "}                            \n" ;
		//        String fragmentShader = "#ifdef GL_ES\n" +
		//                "precision mediump float;\n" +
		//                "#endif\n" +
		//                "varying vec4 v_color;\n" +
		//                "varying vec2 v_texCoords;\n" +
		//                "uniform sampler2D u_texture;\n" +
		//                "void main()                                  \n" +
		//                "{                                            \n" +
		//                "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" +
		//                "}";
		//
		//        ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
		//        mesh.render(shader,GL20.GL_TRIANGLES);





		for (int i = 0; i < mapOfWorld.size(); i++)
		{
			renderer.render(mapOfWorld.get(i).modelInstance, environment);
		}
		renderer.end();

		spriteBatch.begin();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		spriteBatch.end();

		camera.update();
	}

	public Camera getCamera()
	{
		return camera;
	}

	public void setInstances(ArrayList<InstanceModel> instances, ArrayList<InstanceModel> mapOfWorld)
	{
		this.instances = instances;
		this.mapOfWorld = mapOfWorld;
		find();
	}

	@Override
	public void show(){

	}

	@Override
	public void resize(int width, int height){
		stage.getViewport().update(width, height, true);
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
		renderer.dispose();
		stage.dispose();
	}

}
