package nl.dke13.physics;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class StaticObject {

    private ModelInstance object;
    private ObjectBox box;
    private Vector3 position;
    private String label;

    public StaticObject(ModelInstance object, float modelX, float modelY, float modelZ, float modelWidth, float modelHeight, float modelDepth, String label)
    {
        this.object = object;
        this.box = new ObjectBox(modelX, modelY, modelZ, modelWidth, modelHeight, modelDepth);
        position = new Vector3(modelX, modelY, modelZ);
        this.label = label;
    }

    public StaticObject(ModelInstance object, float modelX, float modelY, float modelZ, float modelWidth, float modelHeight, float modelDepth)
    {
        this.object = object;
        this.box = new ObjectBox(modelX, modelY, modelZ, modelWidth, modelHeight, modelDepth);
        position = new Vector3(modelX, modelY, modelZ);
        label = "";
    }

    public ModelInstance getModel()
    {
        return object;
    }

    public ObjectBox getBox()
    {
        return box;
    }

    public String getLabel()
    {
        return label;
    }

    public Vector3 getPosition() {
        return position;
    }

}
