package nl.dke12.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;
import nl.dke12.game.GameObject;
import nl.dke12.game.GameWorld;

/**
 * Loads games in a text file
 * creates a GameWorld based on the .txt file
 */
public class GameWorldLoader
{
    public static GameWorld createGameWorld(String filepath) {
        ModelLoader modelLoader = new G3dModelLoader(new UBJsonReader());
        ModelBuilder modelBuilder = new ModelBuilder();

        if (filepath.equals("test"))
        {
            Environment environment = new Environment();
            environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
            environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

            GameWorld gameWorld = new GameWorld(environment);

            //simple grass tile
            Model grass = modelLoader.loadModel(Gdx.files.internal("core/assets/data/floor.G3DB"));
            //Model grass = modelBuilder.createBox(5,5,5, new Material(ColorAttribute.createDiffuse(Color.FOREST)), VertexAttributes.Usage.Position);
            ModelInstance grassInstance = new ModelInstance(grass);

            grassInstance.transform.translate(0,0,0);
            grassInstance.transform.rotate(new Vector3(1,0,0), 90f);
            grassInstance.transform.scale(4f, 4f, 4f);


            gameWorld.addGameObject(new GameObject(grassInstance));

            return gameWorld;
        }
        return null;
    }
}
