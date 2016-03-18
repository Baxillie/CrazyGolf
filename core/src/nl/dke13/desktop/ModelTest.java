package nl.dke13.desktop;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.UBJsonReader;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.collision.BoundingBox;

public class ModelTest implements Screen {

    PerspectiveCamera camera;
    CameraInputController cameraController; //makes the user be able to move the camera
    ModelBatch modelBatch;

    private Model model;
    private Model model1;
    private Model model2;
    private Model selecter;

    private ModelInstance modelInstance;
    private ModelInstance modelInstance1;
    private ModelInstance modelInstance2;
    private ModelInstance select;

    private Environment environment;
    private AnimationController controller;
    private ArrayList<ModelInstance> instances = new ArrayList<ModelInstance>();

    private int emptyX=0;
    private int emptyY=-2;
    private int emptyZ=0;

    private boolean editor = false;
    private boolean placetile = false;

    public ModelTest() {
        create();
    }

    private void create() {

        // Create camera sized to screens width/height with Field of View of 75 degrees
        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Move the camera 5 units back along the z-axis and look at the origin
        camera.position.set(0f,0f,7f);
        camera.lookAt(0f,0f,0f);

        // Near and Far (plane) represent the minimum and maximum ranges of the camera in, um, units
        camera.near = 0.1f;
        camera.far = 300.0f;

        // Controller for the camera
        cameraController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(cameraController);

        modelBatch = new ModelBatch();

        // Model loader needs a binary json reader to decode
        UBJsonReader jsonReader = new UBJsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);

        // Load models
        model = modelLoader.loadModel(Gdx.files.internal("core/assets/data/skybox.g3db"));
        model1 = modelLoader.loadModel(Gdx.files.internal("core/assets/data/dude.g3db"));
        model2 = modelLoader.loadModel(Gdx.files.internal("core/assets/data/tile1.g3db"));
        selecter = modelLoader.loadModel(Gdx.files.internal("core/assets/data/select.g3db"));


        // Now create an instance.  Instance holds the positioning data, etc of an instance of your model
        modelInstance = new ModelInstance(model);
        modelInstance1 = new ModelInstance(model1);
        modelInstance2 = new ModelInstance(model2);
        select = new ModelInstance(selecter);

        // fbx-conv is supposed to perform this rotation for you... it doesnt seem to
        modelInstance.transform.rotate(1, 0, 0, -90);
        modelInstance1.transform.rotate(1, 0, 0, -90);
        modelInstance2.transform.rotate(1, 0, 0, -90);
        select.transform.rotate(1, 0, 0, -90);

        // Move the model down a bit on the screen ( in a z-up world, down is -z ).
        modelInstance.transform.translate(0, 0, -2);
        modelInstance1.transform.translate(0, 2, -1);
        modelInstance2.transform.translate(0, 0, -2);
        select.transform.translate(0, 0, -2);

        // Scale the model down
        modelInstance.transform.scale(10f,10f,10f);
        modelInstance1.transform.scale(0.4f,0.4f,0.4f);
        modelInstance2.transform.scale(4f,4f,4f);
        select.transform.scale(4f,4f,4f);


        // Finally we want some light, or we wont see our color.  The environment gets passed in during
        // the rendering process.  Create one, then create an Ambient ( non-positioned, non-directional ) light.
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));

    }

    @Override
    public void dispose() {

        modelBatch.dispose();
        model.dispose();
        model1.dispose();
        model2.dispose();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();

        // Pass in the box Instance and the environment
        modelBatch.begin(camera);
        modelBatch.render(modelInstance, environment);
        modelBatch.render(modelInstance1, environment);

        for(int i=0;i<instances.size();i++)
        {
            modelBatch.render(instances.get(i), environment);
        }

        // Bounding box for edit mode placement
        BoundingBox box2 = new BoundingBox();
        modelInstance2.calculateBoundingBox(box2);

        if (Gdx.input.isKeyJustPressed(Keys.SPACE)){
            placeTile(emptyX,emptyY,emptyZ, model2);
        }

        if (Gdx.input.isKeyJustPressed(Keys.ENTER)){
            resetCamera();

            if(editor == true){
                editor = false;
                camera.position.set(0f,0f,7f);
                camera.lookAt(0f,0f,0f);
            }

            else if (editor == false){
                editor = true;
            }

        }

        // If edit mode activated
        if(editor==true){
            camera.position.set(0f,30f,0f);
            camera.lookAt(0f,0f,0f);
            Gdx.input.setInputProcessor(null);

            Gdx.gl.glViewport( Gdx.graphics.getWidth()/4,0,3*Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight() );
            Gdx.gl.glViewport( 0,0,3*Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight() );
            select.transform.translate(0,0,0);

            // Key bindings for moving pieces in edit mode
            if (Gdx.input.isKeyJustPressed(Keys.UP)){
                emptyZ-=8;
                select.transform.translate(0,2,0);
            }

            if (Gdx.input.isKeyJustPressed(Keys.DOWN)){
                emptyZ+=8;
                select.transform.translate(0,-2,0);
            }

            if (Gdx.input.isKeyJustPressed(Keys.LEFT)){
                emptyX-=8;
                select.transform.translate(-2,0,0);
            }

            if (Gdx.input.isKeyJustPressed(Keys.RIGHT)){
                emptyX+=8;
                select.transform.translate(2,0,0);
            }
            modelBatch.render(select, environment);

        }

        if(editor==false){
            Gdx.input.setInputProcessor(cameraController);
            Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        modelBatch.render(modelInstance2, environment);
        modelBatch.end();

    }

    public void placeTile(int x, int y, int z, Model model){

        ModelInstance modelInstance3 = new ModelInstance(model);
        modelInstance3.transform.translate(x,y,z);
        modelInstance3.transform.rotate(1, 0, 0, -90);
        modelInstance3.transform.scale(4f,4f,4f);

        emptyZ=z;
        instances.add(modelInstance3);

    }

    public void resetCamera(){
        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        camera.position.set(0f,0f,7f);
        camera.lookAt(0f,0f,0f);

        camera.near = 0.1f;
        camera.far = 300.0f;

        cameraController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(cameraController);

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
    public void show() {

    }

    @Override
    public void hide() {

    }
}