package nl.dke12.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Ajki on 12/05/2016.
 */
public class GameObject {
    //position
    //model
    //bounding box

    private ModelInstance modelInstance;
    private Vector3 position;

    public GameObject()
    {
        modelInstance = null;
    }

    public GameObject(ModelInstance modelInstance)
    {
        this.modelInstance = modelInstance;
    }

    public ModelInstance getModelInstance()
    {
        return modelInstance;
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public void setPosition(Vector3 position)
    {
        this.position = position;
    }
}
