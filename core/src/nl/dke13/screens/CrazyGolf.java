package nl.dke13.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import nl.dke13.physics.DynamicObject;
import nl.dke13.physics.Physics;
import nl.dke13.physics.StaticObject;

import java.util.ArrayList;


/**
 * Created by baxie on 12-3-16.
 */
public class CrazyGolf implements ApplicationListener
{
    //variables for a camera in the Application
    Camera camera; //camera which will be what the user sees in the application window
    CameraInputController cameraController; //makes the user be able to move the camera
    Viewport viewport;

    //variables for the course
    ArrayList<DynamicObject> dynamicObjects;
    ArrayList<StaticObject>  staticObjects;
    ModelBatch modelBatch; // renders a model based on the modelInstance
    Physics physics;


    /**
     * Called when the {@link Application} is first created.
     */
    @Override
    public void create()
    {
        modelBatch = new ModelBatch(); //responsible for rendering instances
        dynamicObjects = new ArrayList<DynamicObject>();
        staticObjects = new ArrayList<StaticObject>(); //for holding all the model instances

        //make the camera
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); //construct the camera
        camera.position.set(0f, 10f, 20f); // set the camera 10 unit to the right, up and back;
        camera.lookAt(0,00,0); //make the camera look to point 0,0,0 in the world
        camera.near = 1f; //makes it so the camera sees everything at least 1 unit away from it
        camera.far = 300f;//makes it so the camera sees everything up until 300 units away from it
        camera.update(); //updates all the changes
        viewport = new FitViewport(800, 480, camera);

        //make the user able to move the camera
        cameraController = new CameraInputController(camera); //controller for the camera
        Gdx.input.setInputProcessor(cameraController); //gives the controller acces to user input.

        //make a simple golf course
        createGolfCourseInstances();

        //initialise physics engine
        physics = new Physics(modelBatch, dynamicObjects, staticObjects);

    }

    /**
     * makes a basis golf course out of rectangles.
     */
    private void createGolfCourseInstances()
    {
        //floor
        float floorWidth = 10f;
        float floorHeight = 10f;
        float floorDepth =  1f;
        //2 sideWalls against the Y of the floor
        float sideWallWidth =  1f;
        float sideWallHeight = floorHeight;
        float sideWallDepth =  2 * floorDepth;
        //2 top Walls against the X of the floor
        float topWallWidth = floorWidth;
        float topWallHeight =  1f;
        float topWallDepth =  2 * floorDepth;

        ModelBuilder mb = new ModelBuilder();
        Material floorMaterial = new Material(ColorAttribute.createDiffuse(Color.FOREST));
        Material WallMaterial = new Material(ColorAttribute.createDiffuse(Color.BROWN));
        Model floor = mb.createBox(floorWidth, floorHeight, floorDepth, floorMaterial , VertexAttributes.Usage.Position);
        Model sideWall = mb.createBox(sideWallWidth, sideWallHeight, sideWallDepth, WallMaterial, VertexAttributes.Usage.Position);
        Model topWall = mb.createBox(topWallWidth, topWallHeight, topWallDepth, WallMaterial, VertexAttributes.Usage.Position);
        Model sphere = mb.createSphere(1,1,1, 10, 10, new Material(ColorAttribute.createDiffuse(Color.WHITE)), VertexAttributes.Usage.Position);



        //add the floor
        staticObjects.add(new StaticObject(new ModelInstance(floor, 0,0,0), 0, 0, 0, floorWidth, floorHeight, floorDepth));
        //add sideWalls to the left and the right of the floor

        StaticObject sideWallLeft = new StaticObject(new ModelInstance(sideWall,
                0 - (floorWidth / 2) - (sideWallWidth / 2), 0,  (sideWallDepth / 2) - (floorDepth / 2) ),
                0 - (floorWidth / 2) - (sideWallWidth / 2), 0, (sideWallHeight / 2) - (floorDepth / 2),
                sideWallWidth, sideWallHeight, sideWallDepth);

        StaticObject sideWallRight = new StaticObject(new ModelInstance(sideWall,
                0 + (floorWidth / 2) + (sideWallWidth / 2), 0, (sideWallDepth / 2) - (floorDepth / 2) ),
                0 + (floorWidth / 2) + (sideWallWidth / 2), 0, (sideWallDepth / 2) - (floorDepth / 2),
                sideWallWidth, sideWallHeight, sideWallDepth);

        staticObjects.add(sideWallLeft);
        staticObjects.add(sideWallRight);
        //add top Walls
        StaticObject topWallUp = new StaticObject(new ModelInstance(topWall,
                0, 0 - (topWallWidth / 2) - (topWallHeight / 2), (topWallDepth / 2) - (floorDepth / 2) ),
                0, 0 - (topWallWidth / 2) - (topWallHeight / 2), (topWallDepth / 2) - (floorDepth / 2),
                topWallWidth, topWallHeight, topWallDepth);

        StaticObject topWallDown = new StaticObject(new ModelInstance(topWall,
                0, 0 + (topWallWidth / 2) + (topWallHeight / 2), (topWallDepth / 2) - (floorDepth / 2) ),
                0, 0 + (topWallWidth / 2) + (topWallHeight / 2), (topWallDepth / 2) - (floorDepth / 2),
                topWallWidth, topWallHeight, topWallDepth);

        staticObjects.add(topWallDown);
        staticObjects.add(topWallUp);
        //add golf ball
        dynamicObjects.add(new DynamicObject(new ModelInstance(sphere, 0,0,1),-1.5f,0.5f, 1, 1,1,1, 20));
        dynamicObjects.get(0).setVelocity(new Vector3(1f,1f,0));
    }

    /**
     * Called when the {@link Application} is resized. This can happen at any point during a non-paused state but will never happen
     * before a call to {@link #create()}.
     *
     * @param width  the new width in pixels
     * @param height the new height in pixels
     */
    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height);
    }

    /**
     * Called when the {@link Application} should render itself.
     */
    @Override
    public void render()
    {
        //some necessary OPENGL stuff which I dont understand yet
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        //calls to the modelbatch to render the instance
        modelBatch.begin(camera);
        physics.render();
        modelBatch.end();

        //updates the location of the camera based on user input.
        cameraController.update();
    }

    /**
     * Called when the {@link Application} is paused, usually when it's not active or visible on screen. An Application is also
     * paused before it is destroyed.
     */
    @Override
    public void pause()
    {

    }

    /**
     * Called when the {@link Application} is resumed from a paused state, usually when it regains focus.
     */
    @Override
    public void resume()
    {

    }

    /**
     * Called when the {@link Application} is destroyed. Preceded by a call to {@link #pause()}.
     */
    @Override
    public void dispose()
    {/*
        modelBatch.dispose(); // delete the modelbatch
        //delele all models
        for(Model model : models)
        {
            model.dispose();
        }*/
    }
}