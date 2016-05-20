package nl.dke12.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;
import nl.dke12.controller.GameController;
import nl.dke12.controller.StateController;
import nl.dke12.desktop.SolidObject;
import nl.dke12.game.Ball;
import nl.dke12.game.GameWorld;
import nl.dke12.game.NewBallPhysics;

import java.util.ArrayList;

/**
 * displays the current state of the GameWorld
 */
public class GameDisplay implements Screen
{

    //GameWorld
    //User interface
    //GameController
    private GameWorld gameWorld;

    private CameraInputController cameraController;

    private Camera camera;
    private ModelBatch renderer;

    public ArrayList<SolidObject> solidObjects= new ArrayList<SolidObject>();;

    public Model model;
    public Model TWstatue;
    public Model model2;
    public Model modelwall;
    public Model selecter;
    public Model mill;
    private Model ballModel;


    private ModelInstance modelInstance;
    private ModelInstance modelInstance1;
    private ModelInstance modelInstance2;
    public static ModelInstance modelInstancewall;
    private ModelInstance select;
    private ModelInstance windmill;
    private ModelInstance golfBall;
    private ModelInstance golfBall2;

    private ArrayList<Model> models = new ArrayList<Model>();

    private StateController stateController;

    private Ball ball;

    private Environment environment;
    private Environment skybox;
    private AnimationController controller;
    private AnimationController spincontroller;

    private Vector3 shotVector = new Vector3(0,2f,0.8f);
    private GameController gameController;
    private ArrayList<ModelInstance> instances = new ArrayList<ModelInstance>();

    public GameDisplay(boolean multiplayer)
    {
        gameController = new GameController(this);
        create();
        this.gameWorld = new GameWorld(solidObjects,environment,multiplayer);
        gameController.load();
    }

    public void turn(float x,float y,float z,float rot)
    {
        shotVector.rotate(x,y,z,rot);
    }

    public void create()
    {
        // Create camera sized to screens width/height with Field of View of 75 degrees
        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());



        // Move the camera 5 units back along the z-axis and look at the origin
        camera.position.set(0f, -13f, 7f);
        camera.lookAt(0f, 0f, 0f);

        // Near and Far (plane) represent the minimum and maximum ranges of the camera in, um, units
        camera.near = 0.1f;
        camera.far = 300.0f;

        // Controller for the camera
        cameraController = new CameraInputController(camera);
        //Gdx.input.setInputProcessor(cameraController);

        this.renderer = new ModelBatch();

        // Model loader needs a binary json reader to decode
        UBJsonReader jsonReader = new UBJsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);

        // Load models



        model = modelLoader.loadModel(Gdx.files.internal("core/assets/data/skybox.G3DB"));
        TWstatue = modelLoader.loadModel(Gdx.files.internal("core/assets/data/man.G3DB"));
        model2 = modelLoader.loadModel(Gdx.files.internal("core/assets/data/floor.G3DB"));
        modelwall = modelLoader.loadModel(Gdx.files.internal("core/assets/data/wall.G3DB"));
        selecter = modelLoader.loadModel(Gdx.files.internal("core/assets/data/select.G3DB"));
        mill = modelLoader.loadModel(Gdx.files.internal("core/assets/data/windmill.G3DB"));
        //ballModel = modelLoader.loadModel(Gdx.files.internal("core/assets/data/ball.G3DB"));
        ballModel = new ModelBuilder().createSphere(0.25f,0.25f,0.25f, 10, 10, new Material(ColorAttribute.createDiffuse(Color.WHITE)), VertexAttributes.Usage.Position);

        // Now create an instance.  Instance holds the positioning data, etc of an instance of your model
        modelInstance = new ModelInstance(model);
        modelInstance1 = new ModelInstance(TWstatue);
        modelInstance2 = new ModelInstance(model2);
        modelInstancewall = new ModelInstance(modelwall);
        select = new ModelInstance(selecter);
        windmill = new ModelInstance(mill);
        golfBall = new ModelInstance(ballModel);
        golfBall2 = new ModelInstance(ballModel);
        golfBall2.materials.get(0).set(ColorAttribute.createDiffuse(Color.RED));


        // fbx-conv is supposed to perform this rotation for you... it doesnt seem to
        /*modelInstance.transform.rotate(1, 0, 0, 90);*/
        modelInstance1.transform.rotate(1, 0, 0, 90);
        /*modelInstance2.transform.rotate(1, 0, 0, 90);
        select.transform.rotate(1, 0, 0, 90);*/
        //windmill.transform.rotate(1, 0, 0, 90);

        // Move the model down a bit on the screen ( in a z-up world, down is -z ).
        modelInstance.transform.translate(0, 0, 0);
        modelInstance1.transform.translate(0, 0, 0);
        modelInstance2.transform.translate(0, 0, 0);
        modelInstancewall.transform.translate(0, 0, 0);
        windmill.transform.translate(0, 0, 5);
        select.transform.translate(0, 0, 0);

        // Scale the model down
        modelInstance.transform.scale(10f, 10f, 10f);
        modelInstance1.transform.scale(0.4f, 0.4f, 0.4f);
        modelInstance2.transform.scale(4f, 4f, 4f);
        modelInstancewall.transform.scale(4f, 4f, 4f);
        select.transform.scale(4f, 4f, 4f);
        //golfBall.transform.scale(1.5f, 1.5f, 1.5f);




        // Finally we want some light, or we wont see our color.  The environment gets passed in during
        // the rendering process.  Create one, then create an Ambient ( non-positioned, non-directional ) light.
        environment = new Environment();
        skybox = new Environment();
        //environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));

        skybox.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));

        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        controller = new AnimationController(modelInstance1);
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
                swing = false;*/
                //swing = false;


            }

            @Override
            public void onLoop(AnimationController.AnimationDesc animation) {
                // TODO Auto-generated method stub

            }

        });
        spincontroller = new AnimationController(windmill);
        spincontroller.setAnimation("Spin", 1, new AnimationController.AnimationListener() {

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
                    spincontroller.queue("Bend", -1, 1f, null, 0f);
                }
                swing = false;*/
                //swing = false;
                spincontroller.queue("Spin", -1, 1f, null, 0f);
            }

            @Override
            public void onLoop(AnimationController.AnimationDesc animation) {
                // TODO Auto-generated method stub

            }

        });
        /*this.ball = new GBall(golfBall,0,0,0,0,0,0,this.physObjects,this.gballs);
        this.ball2 = new GBall(golfBall2,0,0,0,0,0,0,this.physObjects,this.gballs);*/
        //gballs.add(this.ball);
        //gballs.add(ball2);

        this.ball = new Ball(0,0,0,0,0,0,this.solidObjects);
    }


    @Override
    public void show() {

    }

    public void addObject(float x, float y, float z, Model model)
    {

        ModelInstance modelInstance3 = new ModelInstance(model);
        modelInstance3.transform.translate(x, y, z-5);
        modelInstance3.transform.rotate(1, 0, 0, -90);
        modelInstance3.transform.scale(4f, 4f, 4f);
        instances.add(modelInstance3);

        if (model == model2)
        {
            this.gameWorld.solidObjects.add(new SolidObject(x,y,z-8,4f,4f,4f));
        }
        if (model == modelwall)
        {
            this.gameWorld.solidObjects.add(new SolidObject(x,y,z-4.5f,4f,4f,4f));
        }
        if (model == TWstatue)
        {

        }
    }



    @Override
    public void render(float delta)
    {
    //main loop of game
        //call render of GameWorld which does the physics stuff
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        renderer.begin(camera);
        renderer.render(modelInstance, skybox);
        renderer.render(modelInstance1, environment);


        //renderer.begin(camera);
        for (int i = 0; i < instances.size(); i++)
        {
            renderer.render(instances.get(i), environment);
        }
        renderer.end();

        gameController.moveCamera(camera);

        camera.update();
    }

    @Override
    public void resize(int width, int height)
    {

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

    }

}
