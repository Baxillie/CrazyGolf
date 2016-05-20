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
    import com.badlogic.gdx.utils.Array;
    import com.badlogic.gdx.utils.UBJsonReader;
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
        private ArrayList<ModelInstance> instances;
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
            this.solidObjects = new ArrayList<>();
            fileReader(name);
        }

        public void loadModels()
        {
            UBJsonReader jsonReader = new UBJsonReader();
            G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);

            skyboxModel = modelLoader.loadModel(Gdx.files.internal("core/assets/data/skyboxModel.G3DB"));
            TWstatueModel = modelLoader.loadModel(Gdx.files.internal("core/assets/data/man.G3DB"));
            floorModel = modelLoader.loadModel(Gdx.files.internal("core/assets/data/floorModel.G3DB"));
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

            instances.add(skybox);
            instances.add(TWstatue);
            instances.add(floor);
            instances.add(wall);
            instances.add(select);
            instances.add(windmill);
            instances.add(golfBall);
            instances.add(golfBall2);
        }

        public void addObject(float x, float y, float z, Model model)
        {
            ModelInstance modelInstance3 = new ModelInstance(model);
            modelInstance3.transform.translate(x, y, z-5);
            modelInstance3.transform.rotate(1, 0, 0, -90);
            modelInstance3.transform.scale(4f, 4f, 4f);
            instances.add(modelInstance3);

            if (model == floorModel)
            {
                solidObjects.add(new SolidObject(x,y,z-10,4f,4f,4f, "floor"));
            }
            if (model == wallModel)
            {
                solidObjects.add(new SolidObject(x,y,z-6.5f,4f,4f,4f, "wall"));
            }
            if (model == millModel)
            {
                solidObjects.add(new SolidObject(x,y,z-6.5f,4f,4f,4f, "windmill"));
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

        public ArrayList<ModelInstance> getModelInstances()
        {
            return instances;
        }

        public ArrayList<SolidObject> getSolidObjects()
        {
            return solidObjects;
        }

    }


