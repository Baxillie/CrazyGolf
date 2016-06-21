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
        ball2Model = null; //todo: change this thing into NOT this :P
        this.instances = new ArrayList<InstanceModel>();
        this.multiplayer = multiplayer;
        this.gameWorld = gameWorld;

        this.stateController = stateController;

        setUpUserInteface();
    }

    public void create()
    {
        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, -13f, 11f);
        camera.lookAt(0f, 0f, 0f);
        camera.near = 0.1f;
        camera.far = 300.0f;
        cameraController = new CameraInputController(camera);

        if(Gdx.input.isKeyPressed(Input.Keys.H))
        {
        /*Plz Ignore this, just me(Tom) trying stuff out*/
        // HeightmapConverter heightmap = new HeightmapConverter(30,30,500,"Heightmap.png");


        Material material = new Material(new IntAttribute(IntAttribute.CullFace), ColorAttribute.createDiffuse(Color.GRAY));
        ModelBuilder modelBuilder = new ModelBuilder();
//        Mesh mesh = new Mesh(true, heightmap.vertices.length, heightmap.indices.length,
//                new VertexAttribute(VertexAttributes.Usage.Position, 3, "heightmap"),
//                new VertexAttribute(VertexAttributes.Usage.Position, 2, "text"));

        /*Mesh mesh = new Mesh(true, 20, 20, new VertexAttribute(VertexAttributes.Usage.Position, 3, "test"));
        mesh.setVertices(new float[] { -0.5f, -0.5f, 0,
                0.5f, -0.5f, 0,
                0, 3f, 0,
                0, 0, 1});

        mesh.setIndices(new short[] {0, 3, 0, 2, 0, 1, 1, 3, 1, 2, 2, 3});*/
//        mesh.setVertices(heightmap.vertices);
//        mesh.setIndices(heightmap.indices);
        //System.out.println("vert"+heightmap.vertices);

        modelBuilder.begin();
//        modelBuilder.part("test", mesh, GL20.GL_LINES, material);
        mapModel = modelBuilder.end();

        map = new ModelInstance(mapModel);
        map.transform.scale(20f,20f,20f);
        }
        /*for(int i=0;i<heightmap.vertices.length;i++)
        {
            System.out.println("vert"+heightmap.vertices[i]);
        }*/

        /*if (this.mesh == null) {
            this.mesh = new Mesh(true, 3, 4,
                    new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));

            this.mesh.setVertices(new float[] { -0.5f, -0.5f, 0,
                    0.5f, -0.5f, 0,
                    0, 0.5f, 0 });

            this.mesh.setIndices(new short[] { 0, 2, 1,0});
            modelBuilder.begin();
            modelBuilder.part("test", mesh, GL20.GL_LINE_STRIP, material);
            mapModel = modelBuilder.end();
            map = new ModelInstance(mapModel);
        }*/
        //////

        this.renderer = new ModelBatch();


        // Finally we want some light, or we wont see our color.  The environment gets passed in during
        // the rendering process.  Create one, then create an Ambient ( non-positioned, non-directional ) light.
        environment = new Environment();
        skyEnvironment = new Environment();

        skyEnvironment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
/*
        controller = new AnimationController(TWModel);
        controller.setAnimation("Bend", 1, new AnimationController.AnimationListener() {
            @Override
            public void onEnd(AnimationController.AnimationDesc animation) {
                // this will be called when the current animation is done.
                // queue up another animation called "balloon".
                // Passing a negative to loop count loops forever.  1f for speed is normal speed.
                //controller.queue("balloon",-1,1f,null,0f);
                /*if (swing == false)
                {
                    controller.queue("Bend",1,1f,null,0f);
                }
                else
                {

                }*/
                /*if (swing== true)
                {
                    controller.queue("Bend", -1, 1f, null, 0f);
                }
                swing = false;
                //swing = false;
            }
            @Override
            public void onLoop(AnimationController.AnimationDesc animation) {}
        });*/
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
        ballModel.transform.translate(direction);
    }

    public void updateBall2(Vector3 direction)
    {
        ball2Model.transform.translate(direction);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        gameWorld.render();

        //System.out.println("ball is in hole: " + gameWorld.ballIsInHole());

        if(Gdx.input.isKeyPressed(Input.Keys.BACKSPACE))
        {
            stateController.displayMenuScreen();
        }

        renderer.begin(camera);
        renderer.render(skyboxModel, skyEnvironment);
        renderer.render(TWModel, environment);
        renderer.render(ballModel, environment);

        if (multiplayer)
            renderer.render(ball2Model, environment);

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
