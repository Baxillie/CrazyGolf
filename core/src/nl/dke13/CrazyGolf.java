package nl.dke13;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.lang.reflect.Array;
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

    //variables for the sphere
    ArrayList<Model> models; //holds all the information about what to render(all the models)
    ArrayList<ModelInstance> instances; //holds the location, rotation and scale of the models
    ModelBatch modelBatch; // renders a model based on the modelInstance


    /**
     * Called when the {@link Application} is first created.
     */
    @Override
    public void create()
    {
        modelBatch = new ModelBatch(); //responsible for rendering instances
        models = new ArrayList<Model>();
        instances = new ArrayList<ModelInstance>(); //for holding all the model instances
System.out.print("test");
        //make the camera
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); //construct the camera
        camera.position.set(0f, 0f, 20f); // set the camera 10 unit to the right, up and back;
        camera.lookAt(0,0,0); //make the camera look to point 0,0,0 in the world
        camera.near = 1f; //makes it so the camera sees everything at least 1 unit away from it
        camera.far = 300f;//makes it so the camera sees everything up until 300 units away from it
        camera.update(); //updates all the changes
        viewport = new FitViewport(800, 480, camera);

        //make the user able to move the camera
        cameraController = new CameraInputController(camera); //controller for the camera
        Gdx.input.setInputProcessor(cameraController); //gives the controller acces to user input.

        //make a simple golf course
        createGolfCourseInstances();

        //old code for a simple sphere
//        sphere = mb.createSphere(
//                5f, 5f, 5f, //dimensions
//                100, 100,  //when these numbers are bigger the sphere becomes more round(?)
//                new Material(ColorAttribute.createDiffuse(Color.WHITE)), //give it a white color
//                VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position); //add Position so the model can have a location in the world(is required).
//        //add Normal so things like lightning work on the object(not required).
//        instances.add(new ModelInstance(sphere)); //required to hold the world location of the model for the renderer.
    }

    /**
     * makes a basis golf course out of rectangles.
     */
    private void createGolfCourseInstances()
    {
        //floor
        float floorX = 10f;
        float floorY = 10f;
        float floorZ =  1f;
        //2 sidewalls against the Y of the floor
        float sideWallX =  1f;
        float sideWallY = floorY;
        float sideWallZ =  2 * floorZ;
        //2 top walls against the X of the floor
        float topWallX = floorX;
        float topWallY =  1f;
        float topWallZ =  2 * floorZ;

        ModelBuilder mb = new ModelBuilder();
        Material floorMaterial = new Material(ColorAttribute.createDiffuse(Color.FOREST));
        Material wallMaterial = new Material(ColorAttribute.createDiffuse(Color.BROWN));
        Model floor = mb.createBox(floorX, floorY, floorZ, floorMaterial , VertexAttributes.Usage.Position);
        Model sideWall = mb.createBox(sideWallX, sideWallY, sideWallZ, wallMaterial, VertexAttributes.Usage.Position);
        Model topWall = mb.createBox(topWallX, topWallY, topWallZ, wallMaterial, VertexAttributes.Usage.Position);

        //add the floor
        instances.add(new ModelInstance(floor, 0,0,0));
        //add sidewalls to the left and the right of the floor
        instances.add(new ModelInstance(sideWall, 0 - (floorX / 2) - (sideWallX / 2), 0, (sideWallZ / 2) - (floorZ / 2) ));
        instances.add(new ModelInstance(sideWall, 0 + (floorX / 2) + (sideWallX / 2), 0, (sideWallZ / 2) - (floorZ / 2) ));
        //add top walls
        instances.add(new ModelInstance(topWall, 0, 0 - (topWallX / 2) - (topWallY / 2), (topWallZ / 2) - (floorZ / 2) ));
        instances.add(new ModelInstance(topWall, 0, 0 + (topWallX / 2) + (topWallY / 2), (topWallZ / 2) - (floorZ / 2) ));
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
        modelBatch.render(instances);
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
    {
        modelBatch.dispose(); // delete the modelbatch
        //delele all models
        for(Model model : models)
        {
            model.dispose();
        }
    }
}
