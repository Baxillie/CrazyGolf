package nl.dke12.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.UBJsonReader;
import nl.dke12.controller.StateController;
import nl.dke12.game.SolidObject;
import nl.dke12.util.GameWorldSaver;

import java.util.ArrayList;

/**
 * The current editor, without the physics implemented
 * just allows you to create and save a world
 */



public class EditorScreen implements Screen
{
    PerspectiveCamera camera;
    CameraInputController cameraController; //makes the user be able to move the camera
    public ModelBatch modelBatch;

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

    private StateController stateController;


    private Environment environment;
    private Environment skybox;
    private AnimationController controller;
    private AnimationController spincontroller;
    private ArrayList<ModelInstance> instances = new ArrayList<ModelInstance>();
    private ArrayList<Model> models = new ArrayList<Model>();
    private ArrayList<SolidObject> physObjects = new ArrayList<SolidObject>();
    //public ArrayList<GBall> gballs = new ArrayList<GBall>();

    private boolean swing = false;

    private int placementX = 0;
    private int placementY = 0;
    private int placementZ = 4;

    private boolean editor = true;
    private boolean placetile = false;
    private boolean wall = false;
    private boolean floor = true;
    private boolean obstacle = false;

    private Vector3 shotVector = new Vector3(0,2f,0.8f);

    /*public GBall ball;
    public GBall ball2;
*/


    private int[][][] levelMatrix= new int[20][20][10];

    public EditorScreen(StateController stateController){
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

        models.add(model);
        models.add(model2);
        models.add(TWstatue);
        models.add(modelwall);
        models.add(selecter);
        models.add(mill);


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


/*
        if (Gdx.input.isKeyPressed(Input.Keys.T)) {
            this.ball.push(shotVector.x,shotVector.y,shotVector.z);
            this.ball.updatePosition();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Y)) {
            this.ball2.push(shotVector.x,shotVector.y,shotVector.z);
            this.ball2.updatePosition();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.G)) {
            this.ball.push(shotVector.x,shotVector.y,0);
            this.ball.updatePosition();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.PLUS)) {
            this.ball.push(0,0,0.2f);
            this.ball.updatePosition();
        }
*/

        /*this.ball.updatePosition();
        this.ball2.updatePosition();*/
/*        if (ball.collides())
        {
            ball.stop();
        }*/
        /*modelBatch.render(ball.object, environment);
        modelBatch.render(ball2.object, environment);*/



        // Pass in the box Instance and the environment
        modelBatch.begin(camera);
        modelBatch.render(modelInstance, skybox);
        modelBatch.render(modelInstance1, environment);
        //modelBatch.render(windmill, environment);



        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
            swing = true;
            controller.queue("Bend",1,1f,null,0f);
            //fileReader("core/assets/level1.txt");
        }

        Vector3 directVector = new Vector3(camera.direction);
        Vector3 sideVector = new Vector3(directVector);
        sideVector.rotate(90,0,0,90);
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.translate(-directVector.x/2,-directVector.y/2,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            camera.translate(directVector.x/2,directVector.y/2,0);
            //camera.direction.x;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.translate(sideVector.x/2,sideVector.y/2,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.translate(-sideVector.x/2,-sideVector.y/2,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.rotate(4,0,0,4);
            shotVector.rotate(4,0,0,4);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.rotate(-4,0,0,4);
            shotVector.rotate(-4,0,0,4);
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (floor == true && wall == false && obstacle == false) {
                placeTile(placementX, placementY, placementZ, model2);
                System.out.println("x="+placementX+"y="+placementY+"z="+placementZ);
                //level[(int) placementX/8+10][(int) placementY/8+10][(int) placementZ/8+5] = 3;
            } else if (floor == false && wall == true && obstacle == false) {
                placeTile(placementX, placementY, 8, modelwall);
                //level[(int) placementX][(int) placementY/8+10][(int) placementZ/8+5] = 2;
            } else if (floor == false && wall == false && obstacle == true) {
                placeTile(placementX, placementY, placementZ, mill);
                //level[(int) placementX/8+10][(int) placementY/8+10][(int) placementZ/8+5] = 1;
            }
        }



        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            //swing = true;
            GameWorldSaver.fileWriter("core/assets/level1.txt",level);

//			saveLevel("C:/Users/Public/Lol/level1.txt");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            //swing = true;

            /*GameWorldLoader loader = new GameWorldLoader(getEditorScreen());
            loader.fileReader("core/assets/level1.txt");*/

        }





        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
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
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    placementY += 8;
                    select.transform.translate(0, 2, 0);
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                    placementY -= 8;
                    select.transform.translate(0, -2, 0);
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                    placementX -= 8;
                    select.transform.translate(-2, 0, 0);
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                    placementX += 8;
                    select.transform.translate(2, 0, 0);
                }
            }
            if (camera.direction.x>camera.direction.y)
            {
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    placementX += 8;
                    select.transform.translate(2, 0, 0);
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                    placementX -= 8;
                    select.transform.translate(-2, 0, 0);
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                    placementY -= 8;
                    select.transform.translate(0, -2, 0);
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                    placementY += 8;
                    select.transform.translate(0, 2, 0);
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_1)) {
                floor = true;
                wall = false;
                obstacle = false;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2)) {
                floor = false;
                wall = true;
                obstacle = false;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_3)) {
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

/*   public Vector3 crossProduct(Vector3 v)
    {
        double x= this.x;
        double y= this.y;
        double z= this.z;

        //return this.set(y * vector.z - z * vector.y, z * vector.x - x * vector.z, x * vector.y - y * vector.x);

        Vector3 vector = new Vector3();
        vector.set((float)(y * v.z - z * v.y),(float) (z * v.x - x * v.z),(float)( x * v.y - y * v.x));

        return vector;
    }*/

    public EditorScreen getEditorScreen()
    {
        return this;
    }


    public void placeTile(int x, int y, int z, Model model) {

        ModelInstance modelInstance3 = new ModelInstance(model);
        modelInstance3.transform.translate(x, y, z-5);
        modelInstance3.transform.rotate(1, 0, 0, -90);
        modelInstance3.transform.scale(4f, 4f, 4f);

        //placementZ = z;


        instances.add(modelInstance3);

        if (model == model2) {
            addToLevel(x, y, z, (char) 1);
            this.physObjects.add(new SolidObject(x,y,z-8,4f,4f,4f));
        }
        if (model == modelwall) {
            addToLevel(x, y, z, (char) 2);
            BoundingBox box = new BoundingBox();
            //modelInstancewall.calculateBoundingBox(box);
            //this.physObjects.add(new Vector3(x,y,z-5));
            this.physObjects.add(new SolidObject(x,y,z-4.5f,4f,4f,4f));
            //modelInstancewall.transform.rotate(1, 0, 0, -90);
//            CrazyGolf.staticObjects.add(0, new StaticObject(modelInstancewall, x+4, y-4, z, 8 , 8 , 8 ));
        }
        if (model == TWstatue) {
            addToLevel(x, y, z, (char) 3);
        }
		/*if (model==model2)
		{
			saveLevel(x,y,0);
		}
		if (model==model2)
		{
			saveLevel(x,y,0);
		}*/

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

    public void addToLevel(int x, int y, int z, char type) {
        /*if (x < level.length / 2 && y < level.length / 2 && x > -level[0].length / 2 && y > -level[0].length / 2 && z < level[0][0].length / 2 && z > -level[0][0].length / 2) {
            level[x + 10][y + 10][z + 5] = type;
            System.out.print(level[x + 10][y + 10][z + 5]);
        }*/
        level[x/8+10][y/8+10][1] = type;
        //System.out.println(level[(x+10)/8][(y+10)/8][(z+10)/8]);
        //System.out.println(" x/8+10 = "+x/8+10+" y/8+10 = "+y/8+10+" z/8+10 = "+z/8+10);
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
        // TODO Auto-generated method stub

    }


    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }
}

/*
	public void actionPerformed(ActionEvent e)
	{
		//String pointslist = points.getText();
		//frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
//		String points = "(4,3),(4,6),(4,5),(3,6),(3,2),(6,2),(6,7),(5,7),(5,1),(2,7),(7,1)";
//		readPoints(pointslist);
		//search();

		try {
			File file = new File("level1.txt");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			//bw.write(pointslist);
			bw.close();
			//readPoints(file);

		} catch (IOException e1) {
			e1.printStackTrace();
		}


	}

};
*/