package nl.dke12.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;
import nl.dke12.game.InstanceModel;
import nl.dke12.game.SolidObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Loads games in a text file
 * creates a GameWorld based on the .txt file
 */
public class GameWorldLoader
{
    private ArrayList<InstanceModel> instances;
    private ArrayList<InstanceModel> mapOfWorld;
    private ArrayList<SolidObject> solidObjects;

    private ModelInstance skybox;
    private ModelInstance TWstatue;
    private ModelInstance floor;
    private ModelInstance select;
    private ModelInstance windmill;
    private ModelInstance wall;
    private ModelInstance hole;
    private ModelInstance golfBall;
    private ModelInstance golfBall2;
    private ModelInstance slope;
    private ModelInstance slopeL;
    private ModelInstance slopeU;
    private ModelInstance slopeR;

    private Model skyboxModel;
    private Model TWstatueModel;
    private Model floorModel;
    private Model wallModel;
    private Model selecterModel;
    private Model millModel;
    private Model ballModel;
    private Model holeModel;
    private Model slopeModel;
    private Model slopeModelL;
    private Model slopeModelU;
    private Model slopeModelR;

    private Vector3 holePosition;

    public GameWorldLoader(String name)
    {
        this.instances = new ArrayList<InstanceModel>();
        this.mapOfWorld = new ArrayList<InstanceModel>();
        this.solidObjects = new ArrayList<SolidObject>();
        fileReader(name);
    }

    public GameWorldLoader()
    {
        this.instances = new ArrayList<InstanceModel>();
        this.mapOfWorld = new ArrayList<InstanceModel>();
        this.solidObjects = new ArrayList<SolidObject>();
        loadModels();
    }

    public Vector3 getHolePosition()
    {
        return holePosition;
    }

    public void loadModels()
    {
        UBJsonReader jsonReader = new UBJsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);

        skyboxModel = modelLoader.loadModel(Gdx.files.internal("core/assets/data/skybox.G3DB"));
        TWstatueModel = modelLoader.loadModel(Gdx.files.internal("core/assets/data/man.G3DB"));
        floorModel = modelLoader.loadModel(Gdx.files.internal("core/assets/data/floor.G3DB"));
        wallModel = modelLoader.loadModel(Gdx.files.internal("core/assets/data/wall.G3DB"));
        selecterModel = modelLoader.loadModel(Gdx.files.internal("core/assets/data/select.G3DB"));
        millModel = modelLoader.loadModel(Gdx.files.internal("core/assets/data/windmill.G3DB"));
        slopeModel = modelLoader.loadModel(Gdx.files.internal("core/assets/data/slope.G3DB"));
        slopeModelL = modelLoader.loadModel(Gdx.files.internal("core/assets/data/slope.G3DB"));
        slopeModelU = modelLoader.loadModel(Gdx.files.internal("core/assets/data/slope.G3DB"));
        slopeModelR = modelLoader.loadModel(Gdx.files.internal("core/assets/data/slope.G3DB"));
        ballModel = new ModelBuilder().createSphere(0.25f,0.25f,0.25f, 10, 10,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)), VertexAttributes.Usage.Position);
        holeModel = modelLoader.loadModel(Gdx.files.internal("core/assets/data/hole.G3DB"));

        skybox = new ModelInstance(skyboxModel);
        TWstatue = new ModelInstance(TWstatueModel);
        floor = new ModelInstance(floorModel);
        wall = new ModelInstance(wallModel);
        select = new ModelInstance(selecterModel);
        windmill = new ModelInstance(millModel);
        hole = new ModelInstance(holeModel);
        golfBall = new ModelInstance(ballModel);
        golfBall2 = new ModelInstance(ballModel);
        golfBall2.materials.get(0).set(ColorAttribute.createDiffuse(Color.RED));
        slope= new ModelInstance(slopeModel);
        slopeL= new ModelInstance(slopeModelL);
        slopeU= new ModelInstance(slopeModelU);
        slopeR= new ModelInstance(slopeModelR);

        transformInstances();
        fillInstances();
    }

    public void transformInstances()
    {
        TWstatue.transform.rotate(1, 0, 0, 90);
        windmill.transform.rotate(1, 0, 0, 180);


        // Move the model down a bit on the screen ( in a z-up world, down is -z ).
        skybox.transform.translate(0, 0, 0);
        TWstatue.transform.translate(0, 0, 0);
        floor.transform.translate(0, 0, 0);
        hole.transform.translate(0,0,0.5f);
        wall.transform.translate(0, 0, 0);
        windmill.transform.translate(0, 0, -2);
        select.transform.translate(0, 0, 0);
        slope.transform.translate(0, 0, 0.8f);
        slopeL.transform.translate(0, 0, 0.8f);
        slopeU.transform.translate(0, 0, 0.8f);
        slopeR.transform.translate(0, 0, 0.8f);


        // Scale the model down
        skybox.transform.scale(10f, 10f, 10f);
        TWstatue.transform.scale(0.8f, 0.8f, 0.8f);
        floor.transform.scale(4f, 4f, 4f);
        hole.transform.scale(4f,4f,4f);
        wall.transform.scale(4f, 4f, 4f);
        select.transform.scale(4f, 4f, 4f);
        slope.transform.scale(4f, 4f, 4f);
        slopeL.transform.scale(4f, 4f, 4f);
        slopeU.transform.scale(4f, 4f, 4f);
        slopeR.transform.scale(4f, 4f, 4f);

    }

    public void fillInstances()
    {
        instances.add(new InstanceModel(skybox, "skybox"));
        instances.add(new InstanceModel(TWstatue, "twstatue"));
        instances.add(new InstanceModel(floor, "floor"));
        instances.add(new InstanceModel(wall, "wall"));
        instances.add(new InstanceModel(select, "select"));
        instances.add(new InstanceModel(windmill, "windmill"));
        instances.add(new InstanceModel(hole, "hole"));
        instances.add(new InstanceModel(golfBall, "ball"));
        instances.add(new InstanceModel(golfBall2, "ball2"));
        instances.add(new InstanceModel(slope, "slope"));
        instances.add(new InstanceModel(slopeL, "slopeL"));
        instances.add(new InstanceModel(slopeU, "slopeU"));
        instances.add(new InstanceModel(slopeR, "slopeR"));
    }

    public void addObject(float x, float y, float z, Model model)
    {
        ModelInstance modelInstance3 = new ModelInstance(model);

        if (model == slopeModel)
        {
            modelInstance3.transform.translate(x, y, z-4);
            modelInstance3.transform.rotate(1, 0, 0, -90);
            modelInstance3.transform.scale(4f, 4f, 4f);
        }
        else if (model == slopeModelL)
        {
            modelInstance3.transform.translate(x, y, z-4);
            modelInstance3.transform.rotate(1, 0, 0, -90);
            modelInstance3.transform.rotate(0, 1, 0, 90);
            modelInstance3.transform.scale(4f, 4f, 4f);
        }
        else if (model == slopeModelU)
        {
            modelInstance3.transform.translate(x, y, z-4);
            modelInstance3.transform.rotate(1, 0, 0, -90);
            modelInstance3.transform.rotate(0, 1, 0, 180);
            modelInstance3.transform.scale(4f, 4f, 4f);
        }
        else if (model == slopeModelR)
        {
            modelInstance3.transform.translate(x, y, z-4);
            modelInstance3.transform.rotate(1, 0, 0, -90);
            modelInstance3.transform.rotate(0, 1, 0, -90);
            modelInstance3.transform.scale(4f, 4f, 4f);
        }
        else if (model == holeModel)
        {
            modelInstance3.transform.translate(x, y, z-6);
            modelInstance3.transform.rotate(1, 0, 0, 90);
            modelInstance3.transform.scale(4f, 4f, 4f);
            //System.out.println("hole position: " + new Vector3(x,y,z));
            holePosition = new Vector3(x,y,z);
            hole.transform.rotate(1,0,1,90);
        }
        else
        {
            modelInstance3.transform.translate(x, y, z-5);
            modelInstance3.transform.rotate(1, 0, 0, -90);
            modelInstance3.transform.scale(4f, 4f, 4f);
        }

        if (model == floorModel)
        {
            solidObjects.add(new SolidObject(x,y,z-8.1f,4f,4f,4f, "floor"));
            mapOfWorld.add(new InstanceModel(modelInstance3, "floor"));
        }
        if (model == wallModel)
        {
            solidObjects.add(new SolidObject(x,y,z-8.1f,4f,4f,8.5f, "wall"));

            modelInstance3.transform.scale(1f, 1f, 1f);
            mapOfWorld.add(new InstanceModel(modelInstance3, "wall"));
        }
        if (model == millModel)
        {
            modelInstance3.transform.rotate(1,0,0,180);
            solidObjects.add(new SolidObject(x,y,z-6.5f,4f,4f,4f, "windmill"));
            mapOfWorld.add(new InstanceModel(modelInstance3, "windmill"));
        }
        if (model == holeModel)
        {
            solidObjects.add(new SolidObject(x,y,z-8.1f, 4f,4f,4f, "hole"));
            mapOfWorld.add(new InstanceModel(modelInstance3, "hole"));
        }
        if (model == slopeModel)
        {
            SolidObject sloper = new SolidObject(x,y,z-9.2f, "slope");
            solidObjects.add(sloper);

            sloper.addPoint(x-4,y-4,z-2.5f);
            sloper.addPoint(x-4,y+4,z-4.1f);
            sloper.addPoint(x+4,y-4,z-2.5f);
            sloper.addPoint(x+4,y+4,z-4.1f);
            sloper.addPoint(x+4,y-4,z-4.1f);
            sloper.addPoint(x-4,y-4,z-4.1f);

            sloper.addPlane(sloper.getPoints().get(1), sloper.getPoints().get(3), sloper.getPoints().get(0),sloper.getPlanes());
            sloper.addPlane(sloper.getPoints().get(0), sloper.getPoints().get(2), sloper.getPoints().get(3),sloper.getPlanes());
            sloper.addPlane(sloper.getPoints().get(4), sloper.getPoints().get(5), sloper.getPoints().get(2),sloper.getPlanes());
            sloper.addPlane(sloper.getPoints().get(5), sloper.getPoints().get(5), sloper.getPoints().get(0),sloper.getPlanes());
            sloper.addPlane(sloper.getPoints().get(4), sloper.getPoints().get(2), sloper.getPoints().get(3),sloper.getPlanes());
            sloper.addPlane(sloper.getPoints().get(5), sloper.getPoints().get(0), sloper.getPoints().get(2),sloper.getPlanes());



            //modelInstance3.transform.translate(x, y, z);
            mapOfWorld.add(new InstanceModel(modelInstance3, "slope"));
        }
        if (model == slopeModelL)
        {
            SolidObject sloper = new SolidObject(x,y,z-9.3f, "slopeL");
            solidObjects.add(sloper);

            sloper.addPoint(x-4,y-4,z-2.5f);
            sloper.addPoint(x-4,y+4,z-2.5f);
            sloper.addPoint(x+4,y-4,z-4.1f);
            sloper.addPoint(x+4,y+4,z-4.1f);
            sloper.addPoint(x-4,y-4,z-4.1f);
            sloper.addPoint(x-4,y+4,z-4.1f);

            sloper.addPlane(sloper.getPoints().get(2), sloper.getPoints().get(3), sloper.getPoints().get(0),sloper.getPlanes());
            sloper.addPlane(sloper.getPoints().get(0), sloper.getPoints().get(1), sloper.getPoints().get(3),sloper.getPlanes());
            sloper.addPlane(sloper.getPoints().get(0), sloper.getPoints().get(4), sloper.getPoints().get(5),sloper.getPlanes());
            sloper.addPlane(sloper.getPoints().get(1), sloper.getPoints().get(0), sloper.getPoints().get(5),sloper.getPlanes());
            sloper.addPlane(sloper.getPoints().get(4), sloper.getPoints().get(0), sloper.getPoints().get(2),sloper.getPlanes());
            sloper.addPlane(sloper.getPoints().get(5), sloper.getPoints().get(1), sloper.getPoints().get(3),sloper.getPlanes());




            //modelInstance3.transform.translate(x, y, z);
            mapOfWorld.add(new InstanceModel(modelInstance3, "slopeL"));
        }
        if (model == slopeModelU)
        {
            SolidObject sloper = new SolidObject(x,y,z-9.3f, "slopeU");
            solidObjects.add(sloper);

            sloper.addPoint(x-4,y-4,z-4.1f);
            sloper.addPoint(x-4,y+4,z-2.5f);
            sloper.addPoint(x+4,y-4,z-4.1f);
            sloper.addPoint(x+4,y+4,z-2.5f);
            sloper.addPoint(x+4,y+4,z-4.1f);
            sloper.addPoint(x-4,y+4,z-4.1f);

            sloper.addPlane(sloper.getPoints().get(0), sloper.getPoints().get(2), sloper.getPoints().get(1),sloper.getPlanes());
            sloper.addPlane(sloper.getPoints().get(1), sloper.getPoints().get(2), sloper.getPoints().get(3),sloper.getPlanes());
            sloper.addPlane(sloper.getPoints().get(4), sloper.getPoints().get(5), sloper.getPoints().get(3),sloper.getPlanes());
            sloper.addPlane(sloper.getPoints().get(1), sloper.getPoints().get(3), sloper.getPoints().get(5),sloper.getPlanes());
            sloper.addPlane(sloper.getPoints().get(4), sloper.getPoints().get(3), sloper.getPoints().get(2),sloper.getPlanes());
            sloper.addPlane(sloper.getPoints().get(5), sloper.getPoints().get(1), sloper.getPoints().get(0),sloper.getPlanes());



            //modelInstance3.transform.translate(x, y, z);
            mapOfWorld.add(new InstanceModel(modelInstance3, "slopeU"));
        }
        if (model == slopeModelR)
        {
            SolidObject sloper = new SolidObject(x,y,z-9.3f, "slopeR");
            solidObjects.add(sloper);

            sloper.addPoint(x-4,y-4,z-4.1f);
            sloper.addPoint(x-4,y+4,z-4.1f);
            sloper.addPoint(x+4,y-4,z-2.5f);
            sloper.addPoint(x+4,y+4,z-2.5f);
            sloper.addPoint(x+4,y+4,z-4.1f);
            sloper.addPoint(x+4,y-4,z-4.1f);

            sloper.addPlane(sloper.getPoints().get(0), sloper.getPoints().get(1), sloper.getPoints().get(2),sloper.getPlanes());
            sloper.addPlane(sloper.getPoints().get(1), sloper.getPoints().get(2), sloper.getPoints().get(3),sloper.getPlanes());
            sloper.addPlane(sloper.getPoints().get(3), sloper.getPoints().get(4), sloper.getPoints().get(5),sloper.getPlanes());
            sloper.addPlane(sloper.getPoints().get(1), sloper.getPoints().get(3), sloper.getPoints().get(5),sloper.getPlanes());
            sloper.addPlane(sloper.getPoints().get(4), sloper.getPoints().get(3), sloper.getPoints().get(1),sloper.getPlanes());
            sloper.addPlane(sloper.getPoints().get(5), sloper.getPoints().get(2), sloper.getPoints().get(0),sloper.getPlanes());



            //modelInstance3.transform.translate(x, y, z);
            mapOfWorld.add(new InstanceModel(modelInstance3, "slopeR"));
        }
    }

    public void fileReader(String name) {
        loadModels();
        File file = ((Gdx.files.internal(name)).file());

        String s;
        try {
            Scanner sc = new Scanner(file);
            int xPos = 0;
            int yPos = 0;
            while (sc.hasNext()) {
                s = sc.next();

                for (int i = 0; i < s.length(); i++) {

                    if (s.charAt(i) == '1') {
                        addObject((xPos-10)*8, (yPos-10)*8, 4, floorModel);
                        yPos += 1;
                    }
                    if (s.charAt(i) == '2') {
                        addObject((xPos-10)*8, (yPos-10)*8, 8, wallModel);
                        yPos += 1;
                    }
                    if (s.charAt(i) == '3') {
                        addObject((xPos-10)*8, (yPos-10)*8, 4, millModel);
                        yPos += 1;
                    }
                    if (s.charAt(i) == '4') {
                        addObject((xPos-10)*8, (yPos-10)*8, 4, holeModel);
                        yPos += 1;
                    }
                    if (s.charAt(i) == '5') {
                        addObject((xPos-10)*8, (yPos-10)*8, 4, slopeModel);
                        yPos += 1;
                    }
                    if (s.charAt(i) == '7') {
                        addObject((xPos-10)*8, (yPos-10)*8, 4, slopeModelL);
                        yPos += 1;
                    }
                    if (s.charAt(i) == '8') {
                        addObject((xPos-10)*8, (yPos-10)*8, 4, slopeModelU);
                        yPos += 1;
                    }
                    if (s.charAt(i) == '9') {
                        addObject((xPos-10)*8, (yPos-10)*8, 4, slopeModelR);
                        yPos += 1;
                    }
                    if (s.charAt(i) == '0') {
                        yPos += 1;
                    }
                    if (s.charAt(i) == ';') {
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

    public ArrayList<InstanceModel> getModelInstances()
    {
        return instances;
    }

    public ArrayList<InstanceModel> getMapOfWorld()
    {
        return mapOfWorld;
    }

    public ArrayList<SolidObject> getSolidObjects()
    {
        return solidObjects;
    }

}


