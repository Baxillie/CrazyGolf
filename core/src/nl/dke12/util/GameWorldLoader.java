    package nl.dke12.util;

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.graphics.Color;
    import com.badlogic.gdx.graphics.g3d.Model;
    import com.badlogic.gdx.graphics.g3d.ModelInstance;
    import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
    import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
    import com.badlogic.gdx.utils.UBJsonReader;
    import nl.dke12.game.SolidObject;
    import nl.dke12.screens.GameDisplay;

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
        private GameDisplay screen;
        private ArrayList<ModelInstance> instances;
        private ModelInstance modelInstance;
        private ModelInstance modelInstance1;
        private ModelInstance modelInstance2;
        public static ModelInstance modelInstancewall;
        private ModelInstance select;
        private ModelInstance windmill;

        public Model model;
        public Model TWstatue;
        public Model model2;
        public Model modelwall;
        public Model selecter;
        public Model mill;
        private Model ballModel;


        public GameWorldLoader(GameDisplay screen)
        {
            this.screen = screen;
            this.instances = new ArrayList<>();
        }

        public void loadModels()
        {
            UBJsonReader jsonReader = new UBJsonReader();
            G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);

            model = modelLoader.loadModel(Gdx.files.internal("core/assets/data/skybox.G3DB"));
            TWstatue = modelLoader.loadModel(Gdx.files.internal("core/assets/data/man.G3DB"));
            model2 = modelLoader.loadModel(Gdx.files.internal("core/assets/data/floor.G3DB"));
            modelwall = modelLoader.loadModel(Gdx.files.internal("core/assets/data/wall.G3DB"));
            selecter = modelLoader.loadModel(Gdx.files.internal("core/assets/data/select.G3DB"));
            mill = modelLoader.loadModel(Gdx.files.internal("core/assets/data/windmill.G3DB"));

            modelInstance = new ModelInstance(model);
            modelInstance1 = new ModelInstance(TWstatue);
            modelInstance2 = new ModelInstance(model2);
            modelInstancewall = new ModelInstance(modelwall);
            select = new ModelInstance(selecter);
            windmill = new ModelInstance(mill);

        }

        public void addObject(float x, float y, float z, Model model)
        {
            ModelInstance modelInstance3 = new ModelInstance(model);
            modelInstance3.transform.translate(x, y, z-5);
            modelInstance3.transform.rotate(1, 0, 0, -90);
            modelInstance3.transform.scale(4f, 4f, 4f);
            instances.add(modelInstance3);

            if (model == model2)
            {
                this.gameWorld.solidObjects.add(new SolidObject(x,y,z-10,4f,4f,4f));
            }
            if (model == modelwall)
            {
                this.gameWorld.solidObjects.add(new SolidObject(x,y,z-6.5f,4f,4f,4f));
            }
            if (model == TWstatue)
            {

            }
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

                        if (s.charAt(i) == '1') {
                            screen.addObject((xPos-10)*8, (yPos-10)*8, 4, screen.model2);
                            yPos += 1;
                        }
                        if (s.charAt(i) == '2') {
                            screen.addObject((xPos-10)*8, (yPos-10)*8, 8, screen.modelwall);
                            yPos += 1;
                        }
                        if (s.charAt(i) == '3') {
                            screen.addObject((xPos-10)*8, (yPos-10)*8, 4, screen.mill);
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
    }


