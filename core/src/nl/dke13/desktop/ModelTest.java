package nl.dke13.desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

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
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.UBJsonReader;

import javafx.scene.transform.Transform;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationListener;

import javafx.scene.transform.Transform;
public class ModelTest implements Screen {

    PerspectiveCamera camera;
    CameraInputController cameraController; //makes the user be able to move the camera
    ModelBatch modelBatch;

    private Model model;
    private Model model1;
    private Model model2;
    private Model modelwall;
    private Model selecter;



    private ModelInstance modelInstance;
    private ModelInstance modelInstance1;
    private ModelInstance modelInstance2;
    private ModelInstance modelInstancewall;
    private ModelInstance select;

    private Environment environment;
    private Environment skybox;
    private AnimationController controller;
    private ArrayList<ModelInstance> instances = new ArrayList<ModelInstance>();

    private boolean swing=false;

    private int emptyX=0;
    private int emptyY=-2;
    private int emptyZ=0;

    private boolean editor = false;
    private boolean placetile = false;
    private boolean wall=false;
    private boolean floor=true;
    private boolean obstacle=false;

    public ModelTest() {
        create();
    }

    public void create() {

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
        model1 = modelLoader.loadModel(Gdx.files.internal("core/assets/data/guy.g3db"));
        model2 = modelLoader.loadModel(Gdx.files.internal("core/assets/data/floor.g3db"));
        modelwall = modelLoader.loadModel(Gdx.files.internal("core/assets/data/wall.g3db"));
        selecter = modelLoader.loadModel(Gdx.files.internal("core/assets/data/select.g3db"));

        // Now create an instance.  Instance holds the positioning data, etc of an instance of your model
        modelInstance = new ModelInstance(model);
        modelInstance1 = new ModelInstance(model1);
        modelInstance2 = new ModelInstance(model2);
        modelInstancewall = new ModelInstance(modelwall);
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
        modelInstancewall.transform.translate(0, 0, -2);
        select.transform.translate(0, 0, -2);

        // Scale the model down
        modelInstance.transform.scale(10f,10f,10f);
        modelInstance1.transform.scale(0.4f,0.4f,0.4f);
        modelInstance2.transform.scale(4f,4f,4f);
        modelInstancewall.transform.scale(4f,4f,4f);
        select.transform.scale(4f,4f,4f);


        // Finally we want some light, or we wont see our color.  The environment gets passed in during
        // the rendering process.  Create one, then create an Ambient ( non-positioned, non-directional ) light.
        environment = new Environment();
        skybox = new Environment();
        //environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));

        skybox.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));

        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        controller = new AnimationController(modelInstance1);
        controller.setAnimation("Bend",1, new AnimationListener(){

            @Override
            public void onEnd(AnimationDesc animation) {
                // this will be called when the current animation is done.
                // queue up another animation called "balloon".
                // Passing a negative to loop count loops forever.  1f for speed is normal speed.
                //controller.queue("balloon",-1,1f,null,0f);
                //controller.queue("Bend",-1,1f,null,0f);
                //swing = false;
            }

            @Override
            public void onLoop(AnimationDesc animation) {
                // TODO Auto-generated method stub

            }

        });
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
        modelBatch.render(modelInstance, skybox);
        modelBatch.render(modelInstance1, environment);

        if (Gdx.input.isKeyJustPressed(Keys.BACKSPACE)){
            swing = true;
//			loadLevel("C:/Users/Public/Lol/level1.txt");
        }

        if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_8)){
            swing = true;
//			saveLevel("C:/Users/Public/Lol/level1.txt");
        }

        if (swing == true)
        {
            controller.update(Gdx.graphics.getDeltaTime());
        }

        for(int i=0;i<instances.size();i++)
        {
            modelBatch.render(instances.get(i), environment);
        }

        // Bounding box for edit mode placement
        BoundingBox box2 = new BoundingBox();
        modelInstance2.calculateBoundingBox(box2);

        if (Gdx.input.isKeyJustPressed(Keys.SPACE)){
            if (floor == true && wall == false && obstacle == false)
            {
                placeTile(emptyX,emptyY,emptyZ, model2);
            }
            else if (floor == false && wall == true && obstacle == false)
            {
                placeTile(emptyX,emptyY,emptyZ, modelwall);
            }
            else if (floor == false && wall == false && obstacle == true)
            {
                placeTile(emptyX,emptyY,emptyZ, model1);
            }
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
            Gdx.gl.glViewport( 0,0,3*Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight());

            modelBatch.render(modelInstance2, environment);

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
            if (Gdx.input.isKeyJustPressed(Keys.NUM_1)||Gdx.input.isKeyJustPressed(Keys.NUMPAD_1)){
                floor = true;
                wall = false;
                obstacle = false;
            }
            if (Gdx.input.isKeyJustPressed(Keys.NUM_2)||Gdx.input.isKeyJustPressed(Keys.NUMPAD_2)){
                floor = false;
                wall = true;
                obstacle = false;
            }
            if (Gdx.input.isKeyJustPressed(Keys.NUM_3)||Gdx.input.isKeyJustPressed(Keys.NUMPAD_3)){
                floor = false;
                wall = false;
                obstacle = true;
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
        if (model==model2)
        {
            addToLevel(x,y,(char)1);
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

    public void resetCamera(){
        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        camera.position.set(0f,0f,7f);
        camera.lookAt(0f,0f,0f);

        camera.near = 0.1f;
        camera.far = 300.0f;

        cameraController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(cameraController);

    }



    private char[][] level = new char[50][50];

    public void addToLevel(int x,int y,char type)
    {
        if(x<level.length/2 && y<level.length/2 && x>-level.length/2 && y>-level.length/2)
        {
            level[x+25][y+25] = type;
        }
    }
    public void saveLevel(String name)
    {

        String s;
        try
        {
            File file = new File(name);
            FileWriter fWriter = new FileWriter (file);
            PrintWriter pWriter = new PrintWriter (fWriter);
            StringBuilder string = new StringBuilder();
            for(int j=0;j<level.length;j++)
            {
                for (int k=0;k<level[0].length;k++)
                {
                    if(level[j][k]==1)
                    {
                        //pWriter.print("1");
                        //System.out.println("j="+j+" k="+k);
                        string.setCharAt((k+50)%50,(char)1);
                    }
                    else
                    {
                        pWriter.print("0");
                    }

                }

            }
            pWriter.print(string);
            pWriter.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    public void loadLevel(String name)
    {
        File file = new File(name);
        String s;
        try
        {
            Scanner sc = new Scanner(file);

            while (sc.hasNext())
            {
		            /*int n = sc.nextInt();
		            System.out.println(n);*/
                s = sc.nextLine();
		            /*for (int i = 0; i < s.length(); i++)
					{
		            	//if (s.charAt(i) == '1')
		            	{
		            		for(int j=0;j<50;j++)
		            		{
		            			for (int k=0;k<50;k++)
		            			{

		            				level[j][k]=s.charAt(i);

		            			}
		            		}

		            		for(int j=0;j<50;j++)
		            		{
		            			for (int k=0;k<50;k++)
		            			{
		            				if(level[j][k]==1)
		            				{
		            					placeTile(10,0,10,model2);
		            				}

		            			}
		            		}

		            	}
					}*/
                char[] text = new char[50];
		            /*for(int i=0;i<s.length();i++)
            		{
		            	text[i] = s.charAt(i);
            		}*/
                for(int i=0;i<s.length();i++)
                {
            				/*if (s.length()>100)
            				{
	            				if (s.charAt(i) == '1')
	            				{
	            					level[(int) Math.floor(i/25)][i%25]=1;
	            					System.out.println("load");
	            				}
            				}*/
                    //s.getChars(0, s.length(), level[i], 0);
                    //level[i] = text[i].toCharArray();
                    if (s.charAt(i) == '1')
                    {
                        level[(int) Math.floor(i/50)][i%50]=1;
                    }


                }
                for(int j=0;j<level.length;j++)
                {
                    for (int k=0;k<level[0].length;k++)
                    {
                        if(level[j][k]==1)
                        {
                            placeTile((j),-2,(k),model2);
                            System.out.println("j="+j+" k="+k);
                        }

                    }
                }

            }
            sc.close();
        }
        catch (FileNotFoundException e)
        {
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
        // TODO Auto-generated method stub

    }



    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }
}