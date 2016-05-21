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
        private ModelInstance golfBall;
        private ModelInstance golfBall2;

        private Model skyboxModel;
        private Model TWstatueModel;
        private Model floorModel;
        private Model wallModel;
        private Model selecterModel;
        private Model millModel;
        private Model ballModel;

        public GameWorldLoader(String name)
        {
            this.instances = new ArrayList<>();
            this.mapOfWorld = new ArrayList<>();
            this.solidObjects = new ArrayList<>();
            fileReader(name);
        }

        public GameWorldLoader()
        {
            this.instances = new ArrayList<>();
            this.mapOfWorld = new ArrayList<>();
            this.solidObjects = new ArrayList<>();
            loadModels();
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
            ballModel = new ModelBuilder().createSphere(0.25f,0.25f,0.25f, 10, 10,
                        new Material(ColorAttribute.createDiffuse(Color.WHITE)), VertexAttributes.Usage.Position);

            skybox = new ModelInstance(skyboxModel);
            TWstatue = new ModelInstance(TWstatueModel);
            floor = new ModelInstance(floorModel);
            wall = new ModelInstance(wallModel);
            select = new ModelInstance(selecterModel);
            windmill = new ModelInstance(millModel);
            golfBall = new ModelInstance(ballModel);
            golfBall2 = new ModelInstance(ballModel);
            golfBall2.materials.get(0).set(ColorAttribute.createDiffuse(Color.RED));

            transformInstances();
            fillInstances();
        }

        public void transformInstances()
        {
            TWstatue.transform.rotate(1, 0, 0, 90);

            // Move the model down a bit on the screen ( in a z-up world, down is -z ).
            skybox.transform.translate(0, 0, 0);
            TWstatue.transform.translate(0, 0, 0);
            floor.transform.translate(0, 0, 0);
            wall.transform.translate(0, 0, 0);
            windmill.transform.translate(0, 0, 5);
            select.transform.translate(0, 0, 0);

            // Scale the model down
            skybox.transform.scale(10f, 10f, 10f);
            TWstatue.transform.scale(0.4f, 0.4f, 0.4f);
            floor.transform.scale(4f, 4f, 4f);
            wall.transform.scale(4f, 4f, 4f);
            select.transform.scale(4.3f, 4.3f, 4.3f);

        }

        public void fillInstances()
        {
            instances.add(new InstanceModel(skybox, "skybox"));
            instances.add(new InstanceModel(TWstatue, "twstatue"));
            instances.add(new InstanceModel(floor, "floor"));
            instances.add(new InstanceModel(wall, "wall"));
            instances.add(new InstanceModel(select, "select"));
            instances.add(new InstanceModel(windmill, "windmill"));
            instances.add(new InstanceModel(golfBall, "ball"));
            instances.add(new InstanceModel(golfBall2, "ball2"));
        }

        public void addObject(float x, float y, float z, Model model)
        {
            ModelInstance modelInstance3 = new ModelInstance(model);
            modelInstance3.transform.translate(x, y, z-5);
            modelInstance3.transform.rotate(1, 0, 0, -90);
            modelInstance3.transform.scale(4f, 4f, 4f);

            if (model == floorModel)
            {
                solidObjects.add(new SolidObject(x,y,z-10,4f,4f,4f, "floor"));
                mapOfWorld.add(new InstanceModel(modelInstance3, "floor"));
            }
            if (model == wallModel)
            {
                solidObjects.add(new SolidObject(x,y,z-6.5f,4f,4f,4f, "wall"));
                mapOfWorld.add(new InstanceModel(modelInstance3, "wall"));
            }
            if (model == millModel)
            {
                solidObjects.add(new SolidObject(x,y,z-6.5f,4f,4f,4f, "windmill"));
                mapOfWorld.add(new InstanceModel(modelInstance3, "windmill"));
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


