package nl.dke12.desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.UBJsonReader;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationListener;

import nl.dke12.controller.StateController;

@Deprecated
public class ModelTest implements Screen {

    PerspectiveCamera camera;
    CameraInputController cameraController; //makes the user be able to move the camera
    public ModelBatch modelBatch;

    private Model model;
    private Model TWstatue;
    private Model model2;
    private Model modelwall;
    private Model selecter;
    private Model mill;
    private Model ballModel;


    private ModelInstance modelInstance;
    private ModelInstance modelInstance1;
    private ModelInstance modelInstance2;
    public static ModelInstance modelInstancewall;
    private ModelInstance select;
    private ModelInstance windmill;
    private ModelInstance golfBall;
    private ModelInstance golfBall2;

    private StateController stateController;

    private Environment environment;
    private Environment skybox;
    private AnimationController controller;
    private AnimationController spincontroller;
    private ArrayList<ModelInstance> instances = new ArrayList<>();
    private ArrayList<SolidObject> physObjects = new ArrayList<>();
    public ArrayList<GBall> gballs = new ArrayList<>();

    private boolean swing = false;

    private int placementX = 0;
    private int placementY = 0;
    private int placementZ = -1;

    private boolean editor = false;
    private boolean placetile = false;
    private boolean wall = false;
    private boolean floor = true;
    private boolean obstacle = false;

    private Vector3 shotVector = new Vector3(0,1.5f,0.8f);

    public NewBallPhysics ball;
    public GBall ball2;
    private int[][][] levelMatrix= new int[20][20][10];

    public ModelTest(StateController stateController){
        this.stateController = stateController;
        create();
    }

    public void create() {

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

        modelBatch = new ModelBatch();

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
        controller.setAnimation("Bend", 1, new AnimationListener() {

            @Override
            public void onEnd(AnimationDesc animation) {
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
            public void onLoop(AnimationDesc animation) {
                // TODO Auto-generated method stub

            }

        });
        spincontroller = new AnimationController(windmill);
        spincontroller.setAnimation("Spin", 1, new AnimationListener() {

            @Override
            public void onEnd(AnimationDesc animation) {
                spincontroller.queue("Spin", -1, 1f, null, 0f);
            }

            @Override
            public void onLoop(AnimationDesc animation) {
                // TODO Auto-generated method stub

            }

        });
        this.ball = new NewBallPhysics(golfBall,0,0,0,0,0,0,this.physObjects,this.gballs);
    }

    @Override
    public void dispose() {

        modelBatch.dispose();
        model.dispose();
        TWstatue.dispose();
        model2.dispose();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();

        if (Gdx.input.isKeyPressed(Keys.T)) {
            this.ball.push(shotVector.x,shotVector.y,shotVector.z);
            this.ball.updatePosition();
        }
        if (Gdx.input.isKeyPressed(Keys.Y)) {
            this.ball.push(shotVector.x,shotVector.y,shotVector.z);
            this.ball.updatePosition();
        }
        if (Gdx.input.isKeyPressed(Keys.G)) {
            this.ball.push(shotVector.x,shotVector.y,0);
            this.ball.updatePosition();
        }
        if (Gdx.input.isKeyPressed(Keys.PLUS)) {
            if (ball.position.z<5)
            {
                this.ball.push(0,0,10);
                this.ball.updatePosition();
            }
        }


        this.ball.updatePosition();
        //this.ball2.updatePosition();
/*        if (ball.collides())
        {
            ball.stop();
        }*/
        modelBatch.render(ball.object, environment);
        //modelBatch.render(ball2.object, environment);

        // Pass in the box Instance and the environment
        modelBatch.begin(camera);
        modelBatch.render(modelInstance, skybox);
        modelBatch.render(modelInstance1, environment);
        //modelBatch.render(windmill, environment);

        if (Gdx.input.isKeyJustPressed(Keys.BACKSPACE)) {
            //swing = true;
            fileReader("core/assets/level1.txt");
        }

        if (Gdx.input.isKeyJustPressed(Keys.SHIFT_LEFT)) {
            swing = true;
            controller.queue("Bend",1,1f,null,0f);
            //fileReader("core/assets/level1.txt");
        }

        Vector3 directVector = new Vector3(camera.direction);
        Vector3 sideVector = new Vector3(directVector);
        sideVector.rotate(90,0,0,90);
        if (Gdx.input.isKeyPressed(Keys.S)) {
            camera.translate(-directVector.x/2,-directVector.y/2,0);
        }
        if (Gdx.input.isKeyPressed(Keys.Z)) {
            camera.translate(directVector.x/2,directVector.y/2,0);
            //camera.direction.x;
        }
        if (Gdx.input.isKeyPressed(Keys.Q)) {
            camera.translate(sideVector.x/2,sideVector.y/2,0);
        }
        if (Gdx.input.isKeyPressed(Keys.D)) {
            camera.translate(-sideVector.x/2,-sideVector.y/2,0);
        }
        if (Gdx.input.isKeyPressed(Keys.A)) {
            camera.rotate(4,0,0,4);
            shotVector.rotate(4,0,0,4);
        }
        if (Gdx.input.isKeyPressed(Keys.E)) {
            camera.rotate(-4,0,0,4);
            shotVector.rotate(-4,0,0,4);
        }


        if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_8)) {
            //swing = true;
            fileWriter("core/assets/level1.txt");
//			saveLevel("C:/Users/Public/Lol/level1.txt");
        }

        spincontroller.update(Gdx.graphics.getDeltaTime());

        if(swing == true) {
            controller.update(Gdx.graphics.getDeltaTime());
        }
        if(swing == false) {
            //controller.update(0);
            //controller.update(Gdx.graphics.getDeltaTime());
        }

       // HeightmapConverter heightmap = new HeightmapConverter(200,200,50,"Heightmap.png");

        for (int i = 0; i < instances.size(); i++) {
            modelBatch.render(instances.get(i), environment);
        }

        // Bounding box for edit mode placement
        BoundingBox box2 = new BoundingBox();
        modelInstance2.calculateBoundingBox(box2);

        if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            if (floor == true && wall == false && obstacle == false) {
                placeTile(placementX, placementY, placementZ, model2);
                System.out.println("x="+placementX+"y="+placementY+"z="+placementZ);
                //level[(int) placementX/8+10][(int) placementY/8+10][(int) placementZ/8+5] = 3;
            } else if (floor == false && wall == true && obstacle == false) {
                placeTile(placementX, placementY, placementZ, modelwall);
                //level[(int) placementX][(int) placementY/8+10][(int) placementZ/8+5] = 2;
            } else if (floor == false && wall == false && obstacle == true) {
                placeTile(placementX, placementY, placementZ, mill);
                //level[(int) placementX/8+10][(int) placementY/8+10][(int) placementZ/8+5] = 1;
            }
        }


        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            resetCamera();

            if (editor == true) {
                editor = false;
                /*camera.position.set(0f, 0f, 7f);
                camera.lookAt(0f, 0f, 0f);*/
            } else if (editor == false) {
                editor = true;
            }

        }

        // If edit mode activated
        if (editor == true) {
            /*camera.position.set(0f, 0f, 35f);
            camera.lookAt(0f, 0f, 0f);*/
            //Gdx.input.setInputProcessor(null);

            Gdx.gl.glViewport(Gdx.graphics.getWidth() / 4, 0, 3 * Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight());
            Gdx.gl.glViewport(0, 0, 3 * Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight());

            //modelBatch.render(modelInstance2, environment);

            select.transform.translate(0, 0, 0);

            // Key bindings for moving pieces in edit mode
            if (camera.direction.y>camera.direction.x)
            {
                if (Gdx.input.isKeyJustPressed(Keys.UP)) {
                    placementY += 8;
                    select.transform.translate(0, 2, 0);
                }

                if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
                    placementY -= 8;
                    select.transform.translate(0, -2, 0);
                }

                if (Gdx.input.isKeyJustPressed(Keys.LEFT)) {
                    placementX -= 8;
                    select.transform.translate(-2, 0, 0);
                }

                if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
                    placementX += 8;
                    select.transform.translate(2, 0, 0);
                }
            }
            if (camera.direction.x>camera.direction.y)
            {
                if (Gdx.input.isKeyJustPressed(Keys.UP)) {
                    placementX += 8;
                    select.transform.translate(2, 0, 0);
                }

                if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
                    placementX -= 8;
                    select.transform.translate(-2, 0, 0);
                }

                if (Gdx.input.isKeyJustPressed(Keys.LEFT)) {
                    placementY -= 8;
                    select.transform.translate(0, -2, 0);
                }

                if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
                    placementY += 8;
                    select.transform.translate(0, 2, 0);
                }
            }
            if (Gdx.input.isKeyJustPressed(Keys.NUM_1) || Gdx.input.isKeyJustPressed(Keys.NUMPAD_1)) {
                floor = true;
                wall = false;
                obstacle = false;
            }
            if (Gdx.input.isKeyJustPressed(Keys.NUM_2) || Gdx.input.isKeyJustPressed(Keys.NUMPAD_2)) {
                floor = false;
                wall = true;
                obstacle = false;
            }
            if (Gdx.input.isKeyJustPressed(Keys.NUM_3) || Gdx.input.isKeyJustPressed(Keys.NUMPAD_3)) {
                floor = false;
                wall = false;
                obstacle = true;
            }
            modelBatch.render(select, environment);

        }

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder meshBuilder;
        Node node1 = modelBuilder.node();
        node1.translation.set(0,5,0);
        meshBuilder = modelBuilder.part("part1", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material());
        //meshBuilder.setColor(2f,2f,2f,0);
        meshBuilder.cone(6, 8, 6, 10);
        //meshBuilder.setColor(20f,20f,20f,0);
        Node node = modelBuilder.node();
        node.translation.set(0,1,0);
        meshBuilder = modelBuilder.part("part2", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material());
        meshBuilder.cylinder(1,5,1,10);
        Model customMod = modelBuilder.end();
        ModelInstance customModel = new ModelInstance(customMod);
        customModel.transform.translate(12, 0, 0);

       // modelBatch.render(customModel, environment);

        if (editor == false) {
            //Gdx.input.setInputProcessor(cameraController);
            //Gdx.input.setInputProcessor(null);
            Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        //modelBatch.render(modelInstance2, environment);
        modelBatch.end();
    }

    public void placeTile(int x, int y, int z, Model model) {

        ModelInstance modelInstance3 = new ModelInstance(model);
        modelInstance3.transform.translate(x, y, z-5);
        modelInstance3.transform.rotate(1, 0, 0, -90);
        modelInstance3.transform.scale(4f, 4f, 4f);

        placementZ = z;

        instances.add(modelInstance3);
        if (model == model2) {
            addToLevel(x, y, z, (char) 1);
            this.physObjects.add(new SolidObject(x,y,z-8,4f,4f,4f));
            System.out.println("plce"+x+" "+y+" "+z);
            System.out.println("poss"+ball.position);
            if(ball.closest!=null)
            {
                //System.out.println("close"+ball.cloqsest.position);
            }

        }
        if (model == modelwall) {
            addToLevel(x, y, z-5, (char) 2);
            BoundingBox box = new BoundingBox();
            //modelInstancewall.calculateBoundingBox(box);
            this.physObjects.add(new SolidObject(x,y,z-5f,4f,4f,4f));
        }
        if (model == mill) {
            addToLevel(x, y, z, (char) 3);
            SolidObject object = new SolidObject(x,y,z-5);
            this.physObjects.add(object);
            object.addPoint(x-4,y,z+4);
            object.addPoint(x+4,y,z+4);
            object.addPoint(x,y-4,z+4);
            object.addPoint(x,y+4,z+4);
        }
    }

    public void resetCamera() {
        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) ;

        camera.position.set(0f, -13f, 7f);
        camera.lookAt(0f, 0f, 0f);

        camera.near = 0.1f;
        camera.far = 500.0f;

        cameraController = new CameraInputController(camera);
        //Gdx.input.setInputProcessor(cameraController);

    }

    private int[][][] level = new int[30][30][30];

    public void addToLevel(int x, int y, int z, char type)
    {
        level[x/8+10][y/8+10][1] = type;
    }

    public void fileReader(String name) {
        File file = ((Gdx.files.internal(name)).file());

        String s;
        try {
            Scanner sc = new Scanner(file);
            int xPos = 0;
            int yPos = 0;
            int zPos = 0;
            int semiColonCount = 0;
            while (sc.hasNext()) {
                s = sc.next();
                for (int i = 0; i < s.length(); i++) {
                    //System.out.println(s.charAt(i));
                    if (s.charAt(i) == '1') {
                        placeTile((xPos - 10) * 8, (yPos - 10) * 8, -3, model2);
                        yPos += 1;
                    }
                    if (s.charAt(i) == '2') {
                        placeTile((xPos - 10) * 8, (yPos - 10) * 8, -3, modelwall);
                        yPos += 1;
                    }
                    if (s.charAt(i) == '3') {
                        placeTile((xPos - 10) * 8, (yPos - 10) * 8, 0, mill);
                        yPos += 1;
                    }
                    if (s.charAt(i) == '0') {
                        yPos += 1;
                    }
                    if (s.charAt(i) == ';') {
                        semiColonCount++;
                        xPos += 1;
                        yPos = 0;
                    }
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void fileWriter(String name)
    {
        try {
            File file = ((Gdx.files.internal(name)).file());
            PrintWriter out = new PrintWriter(file);

            for(int i=0;i<level.length;i++)
            {
                for(int j=0;j<level[i][1].length;j++)
                {
                    //System.out.print(level[i][1][j]);
                    out.print(level[i][j][1]);
                }
                out.print(";");
            }

            out.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
