package nl.dke12.game;

import com.badlogic.gdx.graphics.g3d.Model;
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

    public GameObject()
    {

    }

    public GameObject(ModelInstance modelInstance)
    {
        this.modelInstance = modelInstance;
    }


    public ModelInstance getModelInstance()
    {
        return modelInstance;
    }
}
