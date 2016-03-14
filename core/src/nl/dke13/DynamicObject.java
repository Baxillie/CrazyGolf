package nl.dke13;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by nik on 3/14/16.
 */
public class DynamicObject  {

    ModelInstance object;
    Rectangle rectangle;
    float velocity;
    //angle?!?!?

    public DynamicObject(ModelInstance object, float modelWidth, float modelHeight, float modelX, float modelY) {
        this.object = object;
        rectangle = new Rectangle(modelX, modelY, modelWidth, modelHeight);
    }

    public void giveVelocity(float acceleration)
    {
        velocity = 0;
        acceleration = acceleration;

    }

    //update velocity
    public void update()
    {
        object.transform.translate(0,0,0); //todo correct numbers
        //change rectangle
    }

    public ModelInstance getModel()
    {
        return object;
    }

    public boolean isColliding()
    {
        return true; //todo give the rectangle to check:)

    }

    public void hasCollided()
    {

    }


}
