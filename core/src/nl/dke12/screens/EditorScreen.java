package nl.dke12.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import nl.dke12.controller.EditorController;
import nl.dke12.controller.StateController;
import nl.dke12.game.InstanceModel;
import nl.dke12.util.GameWorldLoader;
import nl.dke12.util.ModelSelectButton;

import java.util.ArrayList;

/**
 * The current editor, without the physics implemented
 * just allows you to create and save a world
 */
public class EditorScreen implements Screen
{
    private PerspectiveCamera camera;
    private ModelBatch renderer;

    private GameWorldLoader gameWorldLoader;

    private ArrayList<InstanceModel> instances;
    private ModelInstance floorModel;
    private ModelInstance wallModel;
    private ModelInstance millModel;
    private ModelInstance skyboxModel;
    private ModelInstance TWmodel;
    private ModelInstance select;
    private ModelInstance holeModel;
    private ModelInstance slopeModel;
    private ModelInstance slopeModelL;
    private ModelInstance slopeModelU;
    private ModelInstance slopeModelR;


    private ArrayList<InstanceModel> mapOfWorld;
    private StateController stateController;

    private Environment environment;
    private Environment skyEnvironment;

    private int[][][] level = new int[30][30][30];

    private int placementX = 0;
    private int placementY = 0;
    private int placementZ = 4;

    private EditorController controller;

    private ModelSelectButton grassButton;
    private ModelSelectButton wallButton;
    private ModelSelectButton holeButton;
    private ModelSelectButton slopeButton;

    private SpriteBatch spriteBatch;

    public EditorScreen(StateController stateController)
    {
        instances = new ArrayList<InstanceModel>();
        this.gameWorldLoader = new GameWorldLoader();
        this.instances = gameWorldLoader.getModelInstances();
        this.mapOfWorld = new ArrayList<InstanceModel>();
        find();

        this.stateController = stateController;
        this.controller = new EditorController(stateController);
        create();
    }

    public void create()
    {
        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, -13f, 7f);
        camera.lookAt(0f, 0f, 0f);
        camera.near = 0.1f;
        camera.far = 300.0f;

        renderer = new ModelBatch();

        // Finally we want some light, or we wont see our color.  The environment gets passed in during
        // the rendering process.  Create one, then create an Ambient ( non-positioned, non-directional ) light.
        environment = new Environment();
        skyEnvironment = new Environment();

        skyEnvironment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));



        float textureWidth = 130;
        float textureHeight = 100;
        float startingX = Gdx.graphics.getWidth() - textureWidth;
        float startingY = Gdx.graphics.getHeight() - textureHeight;

        Texture grassTexture = new Texture(Gdx.files.internal("core/assets/Grass.png"));
        grassButton = new ModelSelectButton(grassTexture, startingX , startingY, textureWidth, textureHeight,
                controller, EditorController.FLOOR);

        Texture wallTexture = new Texture(Gdx.files.internal("core/assets/Wall.png"));
        wallButton = new ModelSelectButton(wallTexture,startingX,startingY - 100,textureWidth,textureHeight,
                controller, EditorController.WALL);

        Texture holeTexture = new Texture(Gdx.files.internal("core/assets/Hole.png"));
        holeButton = new ModelSelectButton(holeTexture,startingX,startingY - 200,textureWidth,textureHeight,
                controller, EditorController.HOLE);

        Texture slopeTexture = new Texture(Gdx.files.internal("core/assets/Slope.png"));
        slopeButton = new ModelSelectButton(slopeTexture,startingX,startingY - 300,textureWidth,textureHeight,
                controller, EditorController.SLOPE);

        spriteBatch = new SpriteBatch();



    }

    public void find()
    {
        String type;
        for (InstanceModel instance : instances)
        {
            type = instance.type;
            if (type == "floor")
                floorModel = instance.modelInstance;
            else if (type == "wall")
                wallModel = instance.modelInstance;
            else if (type == "windmill")
                millModel = instance.modelInstance;
            else if (type == "skybox")
                skyboxModel = instance.modelInstance;
            else if (type == "twstatue")
                TWmodel = instance.modelInstance;
            else if (type == "select")
                select = instance.modelInstance;
            else if (type == "hole")
                holeModel = instance.modelInstance;
            else if (type == "slope")
                slopeModel = instance.modelInstance;
            else if (type == "slopeL")
                slopeModelL = instance.modelInstance;
            else if (type == "slopeU")
                slopeModelU = instance.modelInstance;
            else if (type == "slopeR")
                slopeModelR = instance.modelInstance;
        }
    }

    public void placeTile(int x, int y, int z, ModelInstance model)
    {
        ModelInstance modelInstance3 = model.copy();
        modelInstance3.transform.translate(x/4, y/4, z/4 -1.25f);
        modelInstance3.transform.rotate(1, 0, 0, -90);

        if (model == slopeModelL)
        {
            modelInstance3.transform.rotate(0, 1, 0, 90);
        }
        else if (model == slopeModelU)
        {
            modelInstance3.transform.rotate(0, 1, 0, 180);
        }
        else if (model == slopeModelR)
        {
            modelInstance3.transform.rotate(0, 1, 0, -90);
        }
        else if (model == holeModel)
        {
            modelInstance3.transform.translate(0,0.375f,0);
            modelInstance3.transform.rotate(0, 0, 1, 180);
            modelInstance3.transform.translate(0,-3/4,0);
        }

        if (model == floorModel) {
            addToLevel(x, y, z, (char) 1);
            mapOfWorld.add(new InstanceModel(modelInstance3, "floor"));
        }
        if (model == wallModel) {
            addToLevel(x, y, z, (char) 2);
            mapOfWorld.add(new InstanceModel(modelInstance3, "wall"));
        }
        if (model == millModel) {
            addToLevel(x, y, z, (char) 3);
            mapOfWorld.add(new InstanceModel(modelInstance3, "windmill"));
        }
        if (model == holeModel) {
            addToLevel(x, y, z, (char) 4);
            System.out.println("add hole");
            mapOfWorld.add(new InstanceModel(modelInstance3, "hole"));
        }
        if (model == slopeModel) {
            addToLevel(x, y, z, (char) 5);
            mapOfWorld.add(new InstanceModel(modelInstance3, "slope"));
        }
        if (model == slopeModelL) {
            addToLevel(x, y, z, (char) 7);
            mapOfWorld.add(new InstanceModel(modelInstance3, "slopeL"));
        }
        if (model == slopeModelU) {
            addToLevel(x, y, z, (char) 8);
            mapOfWorld.add(new InstanceModel(modelInstance3, "slopeU"));
        }
        if (model == slopeModelR) {
            addToLevel(x, y, z, (char) 9);
            mapOfWorld.add(new InstanceModel(modelInstance3, "slopeR"));
        }
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        controller.moveCamera(camera);
        camera.update();

        controller.whatToPlace();

        // Pass in the box Instance and the environment
        renderer.begin(camera);
        renderer.render(skyboxModel, skyEnvironment);
        renderer.render(TWmodel, environment);

        for (int i = 0; i < mapOfWorld.size(); i++)
        {
            renderer.render(mapOfWorld.get(i).modelInstance, environment);
        }

        //controller.whatToPlace();
        String whatToPlace = controller.getWhatToPlace();
        if (controller.spaceBar())
        {
            System.out.println(whatToPlace + " " +  placementX + " " + placementY + " " + placementZ);
            if (whatToPlace.equals(EditorController.FLOOR))
            {
                placeTile(placementX, placementY, placementZ, floorModel);
            }
            if (whatToPlace.equals(EditorController.WALL))
            {
                placeTile(placementX, placementY, 8, wallModel);
            }
            /*if (whatToPlace.equals("windmill"))
            {
                placeTile(placementX, placementY, placementZ, millModel);
            }*/
            if (whatToPlace.equals(EditorController.HOLE))
            {
                placeTile(placementX, placementY, placementZ, holeModel);
            }
            if (whatToPlace.equals(EditorController.SLOPE))
            {
                placeTile(placementX, placementY, placementZ, slopeModel);
            }
            if (whatToPlace.equals("slopeL"))
            {
                placeTile(placementX, placementY, placementZ, slopeModelL);
            }
            if (whatToPlace.equals("slopeU"))
            {
                placeTile(placementX, placementY, placementZ, slopeModelU);
            }
            if (whatToPlace.equals("slopeR"))
            {
                placeTile(placementX, placementY, placementZ, slopeModelR);
            }
        }

        controller.save(level);
        select.transform.translate(0, 0, 0);

        if (controller.up())
        {
            placementY += 8;
            select.transform.translate(0, 2, 0);
        }
        if (controller.down())
        {
            placementY -= 8;
            select.transform.translate(0, -2, 0);
        }
        if (controller.left())
        {
            placementX -= 8;
            select.transform.translate(-2, 0, 0);
        }
        if (controller.right())
        {
            placementX += 8;
            select.transform.translate(2, 0, 0);
        }

        renderer.render(select, environment);
        renderer.end();

        spriteBatch.begin();
        float x = Gdx.input.getX();
        float y = Math.abs((Gdx.graphics.getHeight() - Gdx.input.getY()));
        grassButton.update(spriteBatch,x,y);
        wallButton.update(spriteBatch,x,y);
        holeButton.update(spriteBatch,x,y);
        slopeButton.update(spriteBatch,x,y);
        spriteBatch.end();
    }

    public void addToLevel(int x, int y, int z, char type)
    {
        level[x/8+10][y/8+10][1] = type;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
        renderer.dispose();
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
