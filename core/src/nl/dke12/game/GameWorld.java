package nl.dke12.game;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import nl.dke12.desktop.SolidObject;

import java.util.ArrayList;

/**
 * Created by Ajki on 12/05/2016.
 */
public class GameWorld
{
    //GameController

    //render method that renders every object in the gameWorld  and also calls render from physics
    public ArrayList<SolidObject> solidObjects= new ArrayList<SolidObject>();;
    private Environment environment;
    private ArrayList<ModelInstance> instances = new ArrayList<ModelInstance>();
    protected boolean multiplayer;

    public GameWorld(ArrayList<SolidObject> gameObjects, Environment environment,boolean multiplayer)
    {
        this.solidObjects = gameObjects;
        this.environment = environment;
        this.multiplayer = multiplayer;
    }

    //@Override
    public void render(ModelBatch renderer)
    {
        /*for(SolidObject gameObject : solidObjects)
        {
            renderer.render(gameObject.getModelInstance(), environment);
        }*/
        for (int i = 0; i < instances.size(); i++) {
            renderer.render(instances.get(i), environment);
        }
    }
    //@Override
    public void dispose() {

        //modelBatch.dispose();
    }

//    public ArrayList<GameObject> getGameObjects()
//    {
//        return solidObjects;
//    }

    public void addGameObject(SolidObject gameObject)
    {
        solidObjects.add(gameObject);
    }

    public void advance(Ball ball)
    {
        ball.position.add(ball.direction);
        ball.getModelInstance().transform.translate(ball.direction);
        ball.getPhysics().update();
    }





}
