package nl.dke12.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import nl.dke12.controller.EditorController;
import nl.dke12.controller.StateController;
import nl.dke12.game.InstanceModel;
import nl.dke12.util.GameWorldLoader;

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

    private ArrayList<InstanceModel> mapOfWorld;
    private StateController stateController;

    private Environment environment;
    private Environment skyEnvironment;

    private int[][][] level = new int[30][30][30];

    private int placementX = 0;
    private int placementY = 0;
    private int placementZ = 4;

    private EditorController controller;

    public EditorScreen(StateController stateController)
    {
        instances = new ArrayList<InstanceModel>();
        this.gameWorldLoader = new GameWorldLoader();
        this.instances = gameWorldLoader.getModelInstances();
        this.mapOfWorld = new ArrayList<InstanceModel>();
        find();

        this.stateController = stateController;
        this.controller = new EditorController();
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
        }
    }

    public void placeTile(int x, int y, int z, ModelInstance model)
    {
        ModelInstance modelInstance3 = model.copy();
        modelInstance3.transform.translate(x/4, y/4, z/4 -1.25f);
        modelInstance3.transform.rotate(1, 0, 0, -90);

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
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        controller.moveCamera(camera);
        camera.update();

        // Pass in the box Instance and the environment
        renderer.begin(camera);
        renderer.render(skyboxModel, skyEnvironment);
        renderer.render(TWmodel, environment);

        for (int i = 0; i < mapOfWorld.size(); i++)
        {
            renderer.render(mapOfWorld.get(i).modelInstance, environment);
        }

        controller.whatToPlace();
        String whatToPlace = controller.getWhatToPlace();
        if (controller.spaceBar())
        {
            System.out.println(whatToPlace + " " +  placementX + " " + placementY + " " + placementZ);
            if (whatToPlace.equals("floor"))
            {
                placeTile(placementX, placementY, placementZ, floorModel);
            }
            if (whatToPlace.equals("wall"))
            {
                placeTile(placementX, placementY, 8, wallModel);
            }
            if (whatToPlace.equals("windmill"))
            {
                placeTile(placementX, placementY, placementZ, millModel);
            }
            if (whatToPlace.equals("hole"))
            {
                placeTile(placementX, placementY, placementZ, holeModel);
            }
            if (whatToPlace.equals("slope"))
            {
                placeTile(placementX, placementY, placementZ, slopeModel);
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
