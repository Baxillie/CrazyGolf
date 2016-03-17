package nl.dke13.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import nl.dke13.physics.Ball;
import nl.dke13.physics.Physics;
import nl.dke13.physics.StaticObject;

import java.util.ArrayList;


/**
 * Created by baxie on 12-3-16.
 */
public class CrazyGolf implements Screen
{
    //variables for a camera in the Application
    Camera camera; //camera which will be what the user sees in the application window
    CameraInputController cameraController; //makes the user be able to move the camera
    Viewport viewport;
    InputController input;

    //variables for the course
    ArrayList<Ball> dynamicObjects;
    ArrayList<StaticObject>  staticObjects;
    ModelBatch modelBatch; // renders a model based on the modelInstance
    Physics physics;
    Ball ball;

    //Stage for ui
    Stage stage;
    SpriteBatch batch;
    Sprite slider;
    /**
     * Called when the {@link Application} is first created.
     */
    public CrazyGolf()
    {
        //instantiate variables
        modelBatch = new ModelBatch(); //responsible for rendering instances
        dynamicObjects = new ArrayList<Ball>();
        staticObjects = new ArrayList<StaticObject>(); //for holding all the model instances
        stage = new Stage();
        batch = new SpriteBatch();

        //make the camera
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); //construct the camera
       // camera = new OrthographicCamera();
        camera.position.set(0f, 0f, 35f); // set the camera 10 unit to the right, up and back;
        camera.lookAt(0,0,0); //make the camera look to point 0,0,0 in the world
        camera.near = 1f; //makes it so the camera sees everything at least 1 unit away from it
        camera.far = 300f;//makes it so the camera sees everything up until 300 units away from it
        camera.update(); //updates all the changes
        viewport = new FitViewport(800, 480, camera);

        //make the user able to move the camera

        cameraController = new CameraInputController(camera); //controller for the camera


        //make a simple golf course
        createGolfCourseInstances();

        //initialise physics engine
        physics = new Physics(modelBatch, dynamicObjects, staticObjects);

        //create a button :)
        createButton();
        createSlider();

        //input
        InputMultiplexer switcher = new InputMultiplexer();
        //set the controls for the camera
//        cameraController.rotateLeftKey = 21; // left arrow key :)
//        cameraController.rotateRightKey = 22;   //right
//        cameraController.backwardKey = 20;      //down - zoom
//        cameraController.forwardKey = 19;       //up  - zoom
//
//        cameraController.forwardButton = 51; //w
//cameraController.rotateAngle = 5.5f;
        //cameraController.activateKey = 31; //c for camera :)

        switcher.addProcessor(stage);

        input = new InputController(ball);
        switcher.addProcessor(input);
        switcher.addProcessor(cameraController);


        Gdx.input.setInputProcessor(switcher);
        //input = new InputController(cameraController);
 //       switcher.addProcessor(cameraController);
   //     Gdx.input.setInputProcessor(switcher);

    }

    //
    private void createButton()
    {
        Skin skin = new Skin();
        Pixmap pixmap = new Pixmap(100,100,Pixmap.Format.RGBA8888);
        pixmap.setColor(0,1,0,0.75f);
        Texture pixmaptex = new Texture( pixmap );
        pixmap.fill();

        skin.add("white", new Texture(pixmap));

        BitmapFont bfont = new BitmapFont();
        skin.add("default", bfont);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        //textButtonStyle.up = skin.newDrawable("white", "core/assets/tiger_woods.png");
        //textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        //textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        //textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);

        textButtonStyle.font = skin.getFont("default");

        skin.add("default",  textButtonStyle);

        final TextButton textButton = new TextButton("Push Ball", textButtonStyle);
        textButton.setPosition(100, 200);
        stage.addActor(textButton);

        textButton.addListener(new ChangeListener(){

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("ball got pushed");
                ball.setVelocity(new Vector3(0f,1.1f,0));
            }
        });

    }

    /**
     * makes a basis golf course out of rectangles.
     */
    private void createGolfCourseInstances()
    {
        //floor
        float floorWidth = 30f;
        float floorHeight = 30f;
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

        Model hole = mb.createCylinder(1.2f, 0.5f, 1.2f , 10, new Material(ColorAttribute.createDiffuse(Color.BLACK)), VertexAttributes.Usage.Position);
        ModelInstance theHole = new ModelInstance(hole, 0, 14f, 0);
        theHole.transform.rotateRad(1,0,0,3.14f/2);
        //Hole needs to be the 1st object
        staticObjects.add(0, new StaticObject(theHole, 0, 14f, 0, 1.2f, 0.5f, 1.2f));

        //add the floor
        staticObjects.add(new StaticObject(new ModelInstance(floor, 0,0,0), 100, 100, -5, floorWidth, floorHeight, floorDepth));
        //add sideWalls to the left and the right of the floor

        StaticObject sideWallLeft = new StaticObject(new ModelInstance(sideWall,
                0 - (floorWidth / 2) - (sideWallWidth / 2), 0, (sideWallDepth / 2) - (floorDepth / 2) ),
                0 - (floorWidth / 2) - (sideWallWidth / 2), 0, (sideWallDepth / 2) - (floorDepth / 2),
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

        Model obstacle = mb.createBox(14f, 2f, 1, WallMaterial, VertexAttributes.Usage.Position);
        ModelInstance theObstacle = new ModelInstance(obstacle, 0, 0f, 1);
        staticObjects.add(new StaticObject(theObstacle, 0,0,1,14,2,1));


        //add golf ball
        ball = new Ball(new ModelInstance(sphere, 0,-14f,1),0,-14f,1, 1,1,1);
        dynamicObjects.add(ball);
        //todo: It goes inside wall with 2.5 speed
        // dynamicObjects.get(0).setVelocity(new Vector3(0f,1.05f,0));
    }

    @Override
    public void show() {

    }

    /**
     * Called when the {@link Application} is resized.
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
    public void render(float delta)
    {
        //some necessary OPENGL stuff which I dont understand yet
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        //calls to the modelbatch to render the instance
        modelBatch.begin(camera);
        physics.render();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1/30f));
        stage.draw();
        modelBatch.end();

        //2d ui
        renderUI();

        //updates the location of the camera based on user input.
        cameraController.update();

        //check for user input
        input.update();
    }

    private void renderUI()
    {
        batch.begin();
        batch.draw(slider, 10, 10);
        batch.end();
    }

    public void createSlider()
    {
//        Texture slider = new Texture(Gdx.files.internal("core/assets/slider.png"));
//        Skin skin = new Skin();
//        TextureAtlas atlas = new TextureAtlas();
//        atlas.
//        Window window = new Window("default");
//        Image image = new Image(slider);
//        window.add(image);
//        stage.addActor(window);
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

    @Override
    public void hide() {

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