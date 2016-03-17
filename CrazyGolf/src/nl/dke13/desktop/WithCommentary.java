package nl.dke13.desktop;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
public class WithCommentary implements ApplicationListener {
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

	@Override
	public void create() {        
		// Create camera sized to screens width/height with Field of View of 75 degrees
		camera = new PerspectiveCamera(
				75,
				Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		// Move the camera 5 units back along the z-axis and look at the origin
		camera.position.set(0f,0f,7f);
		camera.lookAt(0f,0f,0f);

		// Near and Far (plane) represent the minimum and maximum ranges of the camera in, um, units
		camera.near = 0.1f; 
		camera.far = 300.0f;

		cameraController = new CameraInputController(camera); //controller for the camera
		Gdx.input.setInputProcessor(cameraController); //gives the controller acces to user input.
		// A ModelBatch is like a SpriteBatch, just for models.  Use it to batch up geometry for OpenGL
		modelBatch = new ModelBatch();

		// Model loader needs a binary json reader to decode
		UBJsonReader jsonReader = new UBJsonReader();
		// Create a model loader passing in our json reader
		G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
		// Now load the model by name
		// Note, the model (g3db file ) and textures need to be added to the assets folder of the Android proj
		model = modelLoader.loadModel(Gdx.files.getFileHandle("C:/Users/vince/workspace/core/assets/data/skybox.g3db", FileType.Absolute));
		model1 = modelLoader.loadModel(Gdx.files.getFileHandle("C:/Users/vince/workspace/core/assets/data/dude.g3db", FileType.Absolute));
		model2 = modelLoader.loadModel(Gdx.files.getFileHandle("C:/Users/vince/workspace/core/assets/data/tile1.g3db", FileType.Absolute));
		selecter = modelLoader.loadModel(Gdx.files.getFileHandle("C:/Users/vince/workspace/core/assets/data/select.g3db", FileType.Absolute));
		// Now create an instance.  Instance holds the positioning data, etc of an instance of your model
		modelInstance = new ModelInstance(model);
		modelInstance1 = new ModelInstance(model1);
		modelInstance2 = new ModelInstance(model2);
		select = new ModelInstance(selecter);

		//fbx-conv is supposed to perform this rotation for you... it doesnt seem to
		modelInstance.transform.rotate(1, 0, 0, -90);
		modelInstance1.transform.rotate(1, 0, 0, -90);
		modelInstance2.transform.rotate(1, 0, 0, -90);
		select.transform.rotate(1, 0, 0, -90);

		//move the model down a bit on the screen ( in a z-up world, down is -z ).
		modelInstance.transform.translate(0, 0, -2);
		modelInstance1.transform.translate(0, 2, -1);
		modelInstance2.transform.translate(0, 0, -2);
		select.transform.translate(0, 0, -2);
		//scale the model down
		modelInstance.transform.scale(10f,10f,10f);
		modelInstance1.transform.scale(0.4f,0.4f,0.4f);
		modelInstance2.transform.scale(4f,4f,4f);
		select.transform.scale(4f,4f,4f);


		// Finally we want some light, or we wont see our color.  The environment gets passed in during
		// the rendering process.  Create one, then create an Ambient ( non-positioned, non-directional ) light.
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
		//        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		//        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1.0f));

		// You use an AnimationController to um, control animations.  Each control is tied to the model instance
		//        controller = new AnimationController(modelInstance1);  
		//        // Pick the current animation by name
		//        controller.setAnimation("Bend",1, new AnimationListener(){
		//            @Override
		//            public void onEnd(AnimationDesc animation) {
		//                // this will be called when the current animation is done. 
		//                // queue up another animation called "balloon". 
		//                // Passing a negative to loop count loops forever.  1f for speed is normal speed.
		//               
		//            	controller.queue("Bend",-1,1f,null,0f);
		//            }
		//            @Override
		//            public void onLoop(AnimationDesc animation) {
		//                // TODO Auto-generated method stub
		//                
		//            }
		//            
		//        });

	}
	@Override
	public void dispose() {
		modelBatch.dispose();
		model.dispose();
		model1.dispose();
		model2.dispose();
	}
	@Override
	public void render() {
		// You've seen all this before, just be sure to clear the GL_DEPTH_BUFFER_BIT when working in 3D
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		// For some flavor, lets spin our camera around the Y axis by 1 degree each time render is called
		//camera.rotateAround(Vector3.Zero, new Vector3(0,1,0),1f);

		// When you change the camera details, you need to call update();
		// Also note, you need to call update() at least once.
		camera.update();

		// You need to call update on the animation controller so it will advance the animation.  Pass in frame delta
		//        controller.update(Gdx.graphics.getDeltaTime());
		// Like spriteBatch, just with models!  pass in the box Instance and the environment
		modelBatch.begin(camera);
		modelBatch.render(modelInstance, environment);
		modelBatch.render(modelInstance1, environment);

		for(int i=0;i<instances.size();i++)
		{
			modelBatch.render(instances.get(i), environment);
		}

		//Gdx.gl.glCullFace(GL20.GL_BACK);

		/*Left Half*/

		//Set up camera with viewport in mind
		//draw( 1.5f );
		/*Right Half*/

		//Set up camera again with other viewport in mind
		// draw( 5f );

		BoundingBox box2 = new BoundingBox();
		modelInstance2.calculateBoundingBox(box2);
		//System.out.println(""+(int)box2.getHeight()*4); 
		boolean firstclick = true;
		if (Gdx.input.isKeyJustPressed(Keys.SPACE))
		{	
			//modelBatch.render(placeTile(box2),environment);
			placeTile(emptyX,emptyY,emptyZ, model2);
		}


		if (!Gdx.input.isKeyJustPressed(Keys.SPACE))
		{
			firstclick = true;
		}
		if (Gdx.input.isKeyJustPressed(Keys.ENTER))
		{
			resetCamera();
			if(editor == true)
			{
				editor = false;
				/*camera = new PerspectiveCamera(
		                75,
		                Gdx.graphics.getWidth(),
		                Gdx.graphics.getHeight());*/
				camera.position.set(0f,0f,7f);
				camera.lookAt(0f,0f,0f);
			}
			else if (editor == false)
			{
				editor = true;
			}

		}
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE))
		{
			camera = new PerspectiveCamera(
					75,
					Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight());

			// Move the camera 5 units back along the z-axis and look at the origin
			camera.position.set(0f,0f,7f);
			camera.lookAt(0f,0f,0f);

			// Near and Far (plane) represent the minimum and maximum ranges of the camera in, um, units
			camera.near = 0.1f; 
			camera.far = 300.0f;

			cameraController = new CameraInputController(camera); //controller for the camera
			Gdx.input.setInputProcessor(cameraController); //gives the controller acces to user input.



		}

		if(editor==true)
		{
			camera.position.set(0f,30f,0f);
			camera.lookAt(0f,0f,0f);
			Gdx.input.setInputProcessor(null);

			Gdx.gl.glViewport( Gdx.graphics.getWidth()/4,0,3*Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight() );
			Gdx.gl.glViewport( 0,0,3*Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight() );
			select.transform.translate(0,0,0);
			//modelBatch.render(select, environment);

			if (Gdx.input.isKeyJustPressed(Keys.UP))
			{
				emptyZ-=8;
				select.transform.translate(0,2,0);
			}

			if (Gdx.input.isKeyJustPressed(Keys.DOWN))
			{
				emptyZ+=8;
				select.transform.translate(0,-2,0);
			}

			if (Gdx.input.isKeyJustPressed(Keys.LEFT))
			{
				emptyX-=8;
				select.transform.translate(-2,0,0);
			}

			if (Gdx.input.isKeyJustPressed(Keys.RIGHT))
			{
				emptyX+=8;
				select.transform.translate(2,0,0);
			}
			modelBatch.render(select, environment);
			//

		}
		if(editor==false)
		{



			Gdx.input.setInputProcessor(cameraController);
			Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		}

		modelBatch.render(modelInstance2, environment);
		modelBatch.end();


	}

	public void placeTile(int x, int y, int z, Model model)
	{
		/*If the mountain will not come to Mahomet, 
		 * Mahomet will go to the mountain*/

		ModelInstance modelInstance3 = new ModelInstance(model);
		modelInstance3.transform.translate(x,y,z);
		modelInstance3.transform.rotate(1, 0, 0, -90);
		modelInstance3.transform.scale(4f,4f,4f);

		emptyZ=z;

		instances.add(modelInstance3);

	}

	//    public void resetCamera(PerspectiveCamera camera){am
	//    	float[] matrix1 = new float[16];
	//		
	//		matrix1[0]=1;matrix1[1]=0;matrix1[2]=0;matrix1[3]=0;
	//
	//		matrix1[4]=0;matrix1[5]=1;matrix1[6]=0;matrix1[7]=0;
	//
	//		matrix1[8]=0;matrix1[9]=0;matrix1[10]=1;matrix1[11]=0;
	//
	//		matrix1[12]=0;matrix1[13]=0;matrix1[14]=-7;matrix1[15]=1;
	//		
	//		Matrix4 temp = new Matrix4(matrix1);
	//		
	//    }

	public void resetCamera(){
		camera = new PerspectiveCamera(
				75,
				Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		// Move the camera 5 units back along the z-axis and look at the origin
		camera.position.set(0f,0f,7f);
		camera.lookAt(0f,0f,0f);

		// Near and Far (plane) represent the minimum and maximum ranges of the camera in, um, units
		camera.near = 0.1f; 
		camera.far = 300.0f;

		cameraController = new CameraInputController(camera); //controller for the camera
		Gdx.input.setInputProcessor(cameraController); //gives the controller acces to user input.

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
}