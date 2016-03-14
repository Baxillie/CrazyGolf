package nl.dke13;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by nik on 3/14/16.
 */
public class StaticObject {

    ModelInstance object;
    Rectangle rectangle;

    public StaticObject(ModelInstance object, float modelWidth, float modelHeight, float modelX, float modelY) {
        this.object = object;
        rectangle = new Rectangle(modelX, modelY, modelWidth, modelHeight);
    }

    public ModelInstance getModel()
    {
        return object;
    }


}
