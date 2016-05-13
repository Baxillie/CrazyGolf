package nl.dke13.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
@Deprecated

public class CrazyGolf implements Screen {
    //variables for a camera in the Application
    PerspectiveCamera camera; //camera which will be what the user sees in the application window
    CameraInputController cameraController; //makes the user be able to move the camera
    Viewport viewport;
    InputController input;

    //variables for the course
    ArrayList<Ball> balls;
    ArrayList<StaticObject> staticObjects;
    ArrayList<Model> models;
    ModelBatch modelBatch; // renders a model based on the modelInstance
    Physics physics;

    Display mainMenu;

    //Stage for ui
    Stage stage;
    UserInterface ui;

    boolean multiplayer;
    boolean hole1Done = false;
    boolean hole2Done = false;
    boolean hole3Done;

    /**
     * Called when the {@link Application} is first created.
     */
/*
    public CrazyGolf(boolean multiplayer, Display mainMenu)
    {
        //instantiate variables
        this.multiplayer = multiplayer;
        modelBatch = new ModelBatch(); //responsible for rendering instances
        balls = new ArrayList<Ball>();
        staticObjects = new ArrayList<StaticObject>(); //for holding all the model instances
        stage = new Stage();
        ui = new UserInterface();
        this.mainMenu = mainMenu;
        models = new ArrayList<>();

        //make the camera
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); //construct the camera
        //camera = new OrthographicCamera();
        camera.position.set(0f, 0f, 35f); // set the camera 10 unit to the right, up and back;
        camera.lookAt(0,0,0); //make the camera look to point 0,0,0 in the world
        camera.near = 1f; //makes it so the camera sees everything at least 1 unit away from it
        camera.far = 300f;//makes it so the camera sees everything up until 300 units away from it
        camera.update(); //updates all the changes
        viewport = new FitViewport(800, 480, camera);

        //make the user able to move the camera
        cameraController = new CameraInputController(camera); //controller for the camera

        //make a simple golf course
        createHole1(multiplayer);
        //initialise physics engine
        physics = new Physics(modelBatch, balls, staticObjects);

        //create a button :)
        //createButton();

        //input
        InputMultiplexer switcher = new InputMultiplexer();

        switcher.addProcessor(stage);

        if(!multiplayer)
        {
            input = new InputController(balls.get(0), ui, camera);
        }
        else{
            input = new InputController(balls.get(0), balls.get(1), ui, camera);
        }
        switcher.addProcessor(input);
       // switcher.addProcessor(cameraController);

        Gdx.input.setInputProcessor(switcher);
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
        textButtonStyle.font = skin.getFont("default");

        skin.add("default",  textButtonStyle);

        final TextButton textButton = new TextButton("Push Ball", textButtonStyle);
        textButton.setPosition(100, 200);
        stage.addActor(textButton);

        textButton.addListener(new ChangeListener(){

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("ball got pushed");
                balls.get(0).setVelocity(new Vector3(0f,1.1f,0));
            }
        });

    }

    /**
     * makes a basis golf course out of rectangles.
     */
    private void createHole1(boolean multiplayer) {
        balls.clear();
        staticObjects.clear();

        //floor
        float floorWidth = 30f;
        float floorHeight = 30f;
        float floorDepth = 1f;
        //2 sideWalls against the Y of the floor
        float sideWallWidth = 1f;
        float sideWallHeight = floorHeight;
        float sideWallDepth = 2 * floorDepth;
        //2 top Walls against the X of the floor
        float topWallWidth = floorWidth;
        float topWallHeight = 1f;
        float topWallDepth = 2 * floorDepth;

        ModelBuilder mb = new ModelBuilder();
        Material floorMaterial = new Material(ColorAttribute.createDiffuse(Color.FOREST));
        Material WallMaterial = new Material(ColorAttribute.createDiffuse(Color.BROWN));
        Model floor = mb.createBox(floorWidth, floorHeight, floorDepth, floorMaterial, VertexAttributes.Usage.Position);
        Model sideWall = mb.createBox(sideWallWidth, sideWallHeight, sideWallDepth, WallMaterial, VertexAttributes.Usage.Position);
        Model topWall = mb.createBox(topWallWidth, topWallHeight, topWallDepth, WallMaterial, VertexAttributes.Usage.Position);
        Model sphere = mb.createSphere(1, 1, 1, 10, 10, new Material(ColorAttribute.createDiffuse(Color.WHITE)), VertexAttributes.Usage.Position);

        Model hole = mb.createCylinder(1.2f, 0.5f, 1.2f, 10, new Material(ColorAttribute.createDiffuse(Color.BLACK)), VertexAttributes.Usage.Position);
        ModelInstance theHole = new ModelInstance(hole, 0, 14f, 0);
        theHole.transform.rotateRad(1, 0, 0, 3.14f / 2);
        //Hole needs to be the 1st object
        staticObjects.add(0, new StaticObject(theHole, 0, 14f, 0, 1.2f, 0.5f, 1.2f, "hole"));

        //add the floor
        staticObjects.add(new StaticObject(new ModelInstance(floor, 0, 0, 0), 100, 100, -5, floorWidth, floorHeight, floorDepth));
        //add sideWalls to the left and the right of the floor

        StaticObject sideWallLeft = new StaticObject(new ModelInstance(sideWall,
                0 - (floorWidth / 2) - (sideWallWidth / 2), 0, (sideWallDepth / 2) - (floorDepth / 2)),
                0 - (floorWidth / 2) - (sideWallWidth / 2), 0, (sideWallDepth / 2) - (floorDepth / 2),
                sideWallWidth, sideWallHeight, sideWallDepth);

        StaticObject sideWallRight = new StaticObject(new ModelInstance(sideWall,
                0 + (floorWidth / 2) + (sideWallWidth / 2), 0, (sideWallDepth / 2) - (floorDepth / 2)),
                0 + (floorWidth / 2) + (sideWallWidth / 2), 0, (sideWallDepth / 2) - (floorDepth / 2),
                sideWallWidth, sideWallHeight, sideWallDepth);

        staticObjects.add(sideWallLeft);
        staticObjects.add(sideWallRight);
        //add top Walls
        StaticObject topWallUp = new StaticObject(new ModelInstance(topWall,
                0, 0 - (floorHeight / 2) - (topWallHeight / 2), (topWallDepth / 2) - (floorDepth / 2)),
                0, 0 - (floorHeight / 2) - (topWallHeight / 2), (topWallDepth / 2) - (floorDepth / 2),
                topWallWidth, topWallHeight, topWallDepth);

        StaticObject topWallDown = new StaticObject(new ModelInstance(topWall,
                0, 0 + (floorHeight / 2) + (topWallHeight / 2), (topWallDepth / 2) - (floorDepth / 2)),
                0, 0 + (floorHeight / 2) + (topWallHeight / 2), (topWallDepth / 2) - (floorDepth / 2),
                topWallWidth, topWallHeight, topWallDepth);

        staticObjects.add(topWallDown);
        staticObjects.add(topWallUp);

        Model obstacle = mb.createBox(14f, 2f, 1, WallMaterial, VertexAttributes.Usage.Position);
        ModelInstance theObstacle = new ModelInstance(obstacle, 0, 0f, 1);
        staticObjects.add(new StaticObject(theObstacle, 0, 0, 1, 14, 2, 1));


        //add golf ball
        Ball ball;

        Model sphere1 = mb.createSphere(1, 1, 1, 10, 10, new Material(ColorAttribute.createDiffuse(Color.PINK)), VertexAttributes.Usage.Position);
        if (multiplayer) {
            balls.add(new Ball(new ModelInstance(sphere, -5, -14f, 1), -5, -14f, 1, 1, 1, 1));
            balls.add(new Ball(new ModelInstance(sphere1, 5, -14f, 1), 5, -14f, 1, 1, 1, 1));
        } else {
            balls.add(new Ball(new ModelInstance(sphere, 0, -14f, 1), 0, -14f, 1, 1, 1, 1));
        }
        models.add(floor);
        models.add(sideWall);
        models.add(topWall);
        models.add(sphere);
        models.add(hole);
    }

    private void createHole2(boolean multiplayer) {
        balls.clear();
        staticObjects.clear();
        //floor
        float floorWidth = 50f;
        float floorHeight = 25f;
        float floorDepth = 1f;
        //2 sideWalls against the Y of the floor
        float sideWallWidth = 1f;
        float sideWallHeight = floorHeight;
        float sideWallDepth = 2 * floorDepth;
        //2 top Walls against the X of the floor
        float topWallWidth = floorWidth;
        float topWallHeight = 1f;
        float topWallDepth = 2 * floorDepth;

        ModelBuilder mb = new ModelBuilder();
        Material floorMaterial = new Material(ColorAttribute.createDiffuse(Color.FOREST));
        Material WallMaterial = new Material(ColorAttribute.createDiffuse(Color.BROWN));
        Model floor = mb.createBox(floorWidth, floorHeight, floorDepth, floorMaterial, VertexAttributes.Usage.Position);
        Model sideWall = mb.createBox(sideWallWidth, sideWallHeight, sideWallDepth, WallMaterial, VertexAttributes.Usage.Position);
        Model topWall = mb.createBox(topWallWidth, topWallHeight, topWallDepth, WallMaterial, VertexAttributes.Usage.Position);
        Model sphere = mb.createSphere(1, 1, 1, 10, 10, new Material(ColorAttribute.createDiffuse(Color.WHITE)), VertexAttributes.Usage.Position);

        Model hole = mb.createCylinder(1.2f, 0.5f, 1.2f, 10, new Material(ColorAttribute.createDiffuse(Color.BLACK)), VertexAttributes.Usage.Position);
        ModelInstance theHole = new ModelInstance(hole, -22, 10f, 0);
        theHole.transform.rotateRad(1, 0, 0, 3.14f / 2);
        //Hole needs to be the 1st object
        staticObjects.add(0, new StaticObject(theHole, -22, 10f, 0, 1.2f, 0.5f, 1.2f, "hole"));

        //add the floor
        staticObjects.add(new StaticObject(new ModelInstance(floor, 0, 0, 0), 100, 100, -5, floorWidth, floorHeight, floorDepth));
        //add sideWalls to the left and the right of the floor

        StaticObject sideWallLeft = new StaticObject(new ModelInstance(sideWall,
                0 - (floorWidth / 2) - (sideWallWidth / 2), 0, (sideWallDepth / 2) - (floorDepth / 2)),
                0 - (floorWidth / 2) - (sideWallWidth / 2), 0, (sideWallDepth / 2) - (floorDepth / 2),
                sideWallWidth, sideWallHeight, sideWallDepth);

        StaticObject sideWallRight = new StaticObject(new ModelInstance(sideWall,
                0 + (floorWidth / 2) + (sideWallWidth / 2), 0, (sideWallDepth / 2) - (floorDepth / 2)),
                0 + (floorWidth / 2) + (sideWallWidth / 2), 0, (sideWallDepth / 2) - (floorDepth / 2),
                sideWallWidth, sideWallHeight, sideWallDepth);

        staticObjects.add(sideWallLeft);
        staticObjects.add(sideWallRight);
        //add top Walls
        StaticObject topWallUp = new StaticObject(new ModelInstance(topWall,
                0, 0 - (floorHeight / 2) - (topWallHeight / 2), (topWallDepth / 2) - (floorDepth / 2)),
                0, 0 - (floorHeight / 2) - (topWallHeight / 2), (topWallDepth / 2) - (floorDepth / 2),
                topWallWidth, topWallHeight, topWallDepth);

        StaticObject topWallDown = new StaticObject(new ModelInstance(topWall,
                0, 0 + (floorHeight / 2) + (topWallHeight / 2), (topWallDepth / 2) - (floorDepth / 2)),
                0, 0 + (floorHeight / 2) + (topWallHeight / 2), (topWallDepth / 2) - (floorDepth / 2),
                topWallWidth, topWallHeight, topWallDepth);

        staticObjects.add(topWallDown);
        staticObjects.add(topWallUp);

        Model obstacle = mb.createBox(30f, 3f, 1, WallMaterial, VertexAttributes.Usage.Position);
        ModelInstance theObstacle = new ModelInstance(obstacle, -10, 0f, 1);
        staticObjects.add(new StaticObject(theObstacle, -10, 0, 1, 30, 3, 1));


        //add golf ball
        Ball ball;
        Model sphere1 = mb.createSphere(1, 1, 1, 10, 10, new Material(ColorAttribute.createDiffuse(Color.PINK)), VertexAttributes.Usage.Position);

        if (multiplayer) {
            balls.add(new Ball(new ModelInstance(sphere, -22, -10f, 1), -22, -10f, 1, 1, 1, 1));
            balls.add(new Ball(new ModelInstance(sphere1, -22, -7f, 1), -22, -7f, 1, 1, 1, 1));
        } else {
            balls.add(new Ball(new ModelInstance(sphere, -22, -10f, 1), -22, -10f, 1, 1, 1, 1));
        }
        models.add(floor);
        models.add(sideWall);
        models.add(topWall);
        models.add(sphere);
        models.add(hole);
    }

    private void createHole3(boolean multiplayer) {
        balls.clear();
        staticObjects.clear();
        //floor
        float floor1Width = 12f;
        float floor1Height = 20f;
        float floor1Depth = 1f;

        float floor2Width = 10;
        float floor2Height = 10;
        float floor2Depth = 1;

        ModelBuilder mb = new ModelBuilder();
        Material floorMaterial = new Material(ColorAttribute.createDiffuse(Color.FOREST));
        Material WallMaterial = new Material(ColorAttribute.createDiffuse(Color.BROWN));

        Model floor1 = mb.createBox(floor1Width, floor1Height, floor1Depth, floorMaterial, VertexAttributes.Usage.Position);
        Model floor2 = mb.createBox(floor2Width, floor2Height, floor2Depth, floorMaterial, VertexAttributes.Usage.Position);

        StaticObject floors1 = new StaticObject(new ModelInstance(floor1, -10, -5, 0), -100, -100, -100, floor1Width, floor1Height, floor1Depth);
        StaticObject floors2 = new StaticObject(new ModelInstance(floor1, 10, 5, 0), -100, -100, -100, floor1Width, floor1Height, floor1Depth);
        StaticObject floors3 = new StaticObject(new ModelInstance(floor2, 0, 0, 0), -100, -100, -100, floor2Width, floor2Height, floor2Depth);
        staticObjects.add(floors1);
        staticObjects.add(floors2);
        staticObjects.add(floors3);

        //2 sideWalls against the Y of the floor
        float sideWallWidth = 1f;
        float sideWallHeight = 10;
        float sideWallDepth = 2f;

        float topWallWidth = 12;
        float topWallHeight = 1f;
        float topWallDepth = 1;

        Model sideWall = mb.createBox(sideWallWidth, sideWallHeight, sideWallDepth, WallMaterial, VertexAttributes.Usage.Position);
        Model topWall = mb.createBox(topWallWidth, topWallHeight, topWallDepth, WallMaterial, VertexAttributes.Usage.Position);

        StaticObject sideWall1 = new StaticObject(new ModelInstance(sideWall, -16f, 0, 0), -16f, 0, 1, sideWallWidth, sideWallHeight, sideWallDepth);
        StaticObject sideWall2 = new StaticObject(new ModelInstance(sideWall, -16f, -10, 0), -16f, -10, 1, sideWallWidth, sideWallHeight, sideWallDepth);
        StaticObject sideWall3 = new StaticObject(new ModelInstance(sideWall, 16f, 0, 0), 16f, 0, 1, sideWallWidth, sideWallHeight, sideWallDepth);
        StaticObject sideWall4 = new StaticObject(new ModelInstance(sideWall, 16f, 10, 0), 16f, 10, 1, sideWallWidth, sideWallHeight, sideWallDepth);
        StaticObject sideWall5 = new StaticObject(new ModelInstance(sideWall, -3.5f, -10, 0), -3.5f, -10, 1, sideWallWidth, sideWallHeight, sideWallDepth);
        StaticObject sideWall6 = new StaticObject(new ModelInstance(sideWall, 3.5f, 10, 0), 3.5f, 10, 1, sideWallWidth, sideWallHeight, sideWallDepth);

        staticObjects.add(sideWall1);
        staticObjects.add(sideWall2);
        staticObjects.add(sideWall3);
        staticObjects.add(sideWall4);
        staticObjects.add(sideWall5);
        staticObjects.add(sideWall6);

        //top walls
        StaticObject topWall1 = new StaticObject(new ModelInstance(topWall, -2, 5.5f, 0), -2, 5.5f, 0, topWallWidth, topWallHeight, topWallDepth);
        StaticObject topWall2 = new StaticObject(new ModelInstance(topWall, 2, -5.5f, 0), 2, -5.5f, 0, topWallWidth, topWallHeight, topWallDepth);
        StaticObject topWall3 = new StaticObject(new ModelInstance(topWall, -11, 5.5f, 0), -11, 5.5f, 0, topWallWidth, topWallHeight, topWallDepth);
        StaticObject topWall4 = new StaticObject(new ModelInstance(topWall, -10, -15.5f, 0), -10, -15.5f, 0, topWallWidth, topWallHeight, topWallDepth);
        StaticObject topWall5 = new StaticObject(new ModelInstance(topWall, 11, -5.5f, 0), 11, -5.5f, 0, topWallWidth, topWallHeight, topWallDepth);
        StaticObject topWall6 = new StaticObject(new ModelInstance(topWall, 10, 15.5f, 0), 10, 15.5f, 0, topWallWidth, topWallHeight, topWallDepth);

        staticObjects.add(topWall1);
        staticObjects.add(topWall2);
        staticObjects.add(topWall3);
        staticObjects.add(topWall4);
        staticObjects.add(topWall5);
        staticObjects.add(topWall6);

        //Hole needs to be the 1st object
        Model hole = mb.createCylinder(1.2f, 0.5f, 1.2f, 10, new Material(ColorAttribute.createDiffuse(Color.BLACK)), VertexAttributes.Usage.Position);
        ModelInstance theHole = new ModelInstance(hole, 10, 12, 0);
        theHole.transform.rotateRad(1, 0, 0, 3.14f / 2);
        staticObjects.add(0, new StaticObject(theHole, 10, 12, 0, 1.2f, 0.5f, 1.2f, "hole"));

        //add golf ball
        Ball ball;
        Model sphere = mb.createSphere(1, 1, 1, 10, 10, new Material(ColorAttribute.createDiffuse(Color.WHITE)), VertexAttributes.Usage.Position);
        Model sphere1 = mb.createSphere(1, 1, 1, 10, 10, new Material(ColorAttribute.createDiffuse(Color.PINK)), VertexAttributes.Usage.Position);

        if (multiplayer) {
            balls.add(new Ball(new ModelInstance(sphere, -9, -13f, 1), -9, -13f, 1, 1, 1, 1));
            balls.add(new Ball(new ModelInstance(sphere1, -11, -13f, 1), -11, -13f, 1, 1, 1, 1));
        } else {
            balls.add(new Ball(new ModelInstance(sphere, -10, -12, 1), -10, -12f, 1, 1, 1, 1));
        }

    }

    @Override
    public void render(float delta) {
        //some necessary OPENGL stuff which I dont understand yet
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        //calls to the modelbatch to render the instance
        modelBatch.begin(camera);
        physics.render();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        modelBatch.end();

        //2d ui
        ui.render(delta);

        //updates the location of the camera based on user input.
        cameraController.update();
        camera.update();

        //check for user input
        input.update();


        if (input.isGameOver() && input.isMultiplayer()) {
            if (hole1Done && hole2Done) {
               // display.setScreen(new GameOverScreen(input.getPlayer1Turn(), input.getPlayer2Turns(), display));
                this.dispose();
            } else if (hole1Done) {
                hole2Done = true;
                createHole3(multiplayer);
                input.setBall1(balls.get(0));
                input.setBall2(balls.get(1));
            } else {
                hole1Done = true;
                createHole2(multiplayer);
                input.setBall1(balls.get(0));
                input.setBall2(balls.get(1));
            }
        } else if (input.isGameOver()) {
            if (hole1Done && hole2Done) {
              //  display.setScreen(new GameOverScreen(input.getPlayer1Turn(), display));
                this.dispose();
            } else if (hole1Done) {
                hole2Done = true;
                createHole3(multiplayer);
                input.setBall1(balls.get(0));
            } else {
                hole1Done = true;
                createHole2(multiplayer);
                input.setBall1(balls.get(0));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        for (Model model : models) {
            model.dispose();
        }
        modelBatch.dispose();
    }

    @Override
    public void show() {

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
}
