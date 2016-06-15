package nl.dke12.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Created by Ajki on 20/05/2016.
 */
public class InstanceModel
{
    public ModelInstance modelInstance;
    public String type;

    public InstanceModel(ModelInstance modelInstance, String type)
    {
        this.modelInstance = modelInstance;
        this.type = type;
    }
}
