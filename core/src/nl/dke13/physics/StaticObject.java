package nl.dke13.physics;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Rectangle;

public class StaticObject {

    ModelInstance object;
    ObjectBox box;

    public StaticObject(ModelInstance object, float modelX, float modelY, float modelZ, float modelWidth, float modelHeight, float modelDepth)
    {
        this.object = object;
        this.box = new ObjectBox(modelX, modelY, modelZ, modelWidth, modelHeight, modelDepth);
    }

    public ModelInstance getModel()
    {
        return object;
    }

    public ObjectBox getBox()
    {
        return box;
    }

}
