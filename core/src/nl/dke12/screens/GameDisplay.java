package nl.dke12.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
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

    private Environment environment;
    private Environment skyEnvironment;
    private AnimationController controller;
    private AnimationController spincontroller;

    private GameWorld gameWorld;

    public GameDisplay(boolean multiplayer, GameWorld gameWorld)
            //todo: add multiplayer to the constructor
    {
        create();
        ball2Model = null; //todo: change this thing into NOT this :P
        this.instances = new ArrayList<>();
        this.multiplayer = multiplayer;
        this.gameWorld = gameWorld;
    }

    public void create()
    {
        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, -13f, 7f);
        camera.lookAt(0f, 0f, 0f);
        camera.near = 0.1f;
        camera.far = 300.0f;
        cameraController = new CameraInputController(camera);

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

        renderer.begin(camera);
        renderer.render(skyboxModel, skyEnvironment);
        renderer.render(TWModel, environment);
        renderer.render(ballModel, environment);

        if (multiplayer)
            renderer.render(ball2Model, environment);

        for (int i = 0; i < mapOfWorld.size(); i++)
        {
            renderer.render(mapOfWorld.get(i).modelInstance, environment);
        }
        renderer.end();

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
