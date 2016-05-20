package nl.dke12.game;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

import java.util.ArrayList;

/**
 * Created by Ajki on 12/05/2016.
 */
public class GameWorld
{
    //GameController

    //render method that renders every object in the gameWorld  and also calls render from physics
    private ArrayList<GameObject> gameObjects;
    private Environment environment;

    public GameWorld()
    {
        this(new ArrayList<GameObject>(), new Environment());
    }

    public GameWorld(ArrayList<GameObject> gameObjects, Environment environment)
    {
        this.gameObjects = gameObjects;
        this.environment = environment;
    }

    public GameWorld(Environment environment)
    {
        this(new ArrayList<GameObject>(), environment);
    }

    public void render(ModelBatch renderer)
    {
        for(GameObject gameObject : gameObjects)
        {
            renderer.render(gameObject.getModelInstance(), environment);
        }
    }

//    public ArrayList<GameObject> getGameObjects()
//    {
//        return gameObjects;
//    }

    public void addGameObject(GameObject gameObject)
    {
        gameObjects.add(gameObject);
    }

}
