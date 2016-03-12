package nl.dke13;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;


/**
 * Created by baxie on 12-3-16.
 */
public class CrazyGolf implements ApplicationListener
{
    //variables for a camera in the Application
    PerspectiveCamera camera; //camera which will be what the user sees in the application window
    CameraInputController cameraController; //makes the user be able to move the camera

    //variables for the sphere
    Model sphere; //holds all the information about what to render(how the sphere looks)
    ModelInstance instance; //holds the location, rotation and scale of the model
    ModelBatch modelBatch; // renders a model based on the modelInstance


    /**
     * Called when the {@link Application} is first created.
     */
    @Override
    public void create()
    {
        modelBatch = new ModelBatch(); //responsible for rendering instances

        //make the camera
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); //construct the camera
        camera.position.set(10f, 15f, 10f); // set the camera 10 unit to the right, up and back;
        camera.lookAt(0,0,0); //make the camera look to point 0,0,0 in the world
        camera.near = 1f; //makes it so the camera sees everything at least 1 unit away from it
        camera.far = 300f;//makes it so the camera sees everything up until 300 units away from it
        camera.update(); //updates all the changes

        //make the user able to move the camera
        cameraController = new CameraInputController(camera); //controller for the camera
        Gdx.input.setInputProcessor(cameraController); //gives the controller acces to user input.

        //make the sphere
        ModelBuilder mb = new ModelBuilder();
        sphere = mb.createSphere(
                5f, 5f, 5f, //dimensions
                100, 100,  //when these numbers are bigger the sphere becomes more round(?)
                new Material(ColorAttribute.createDiffuse(Color.WHITE)), //give it a white color
                VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position); //add Position so the model can have a location in the world(is required).
                                                                                  //add Normal so things like lightning work on the object(not required).
        instance = new ModelInstance(sphere); //required to hold the world location of the model for the renderer.
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
        modelBatch.render(instance);
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
        sphere.dispose(); //deletes the sphere
    }
}
