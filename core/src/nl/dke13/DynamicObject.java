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

    //mass of dynamic object is always 1 unit

    private final int GRAVITY = 10;
    private final double FRICTION_COEFF = 0.5;
    private final int GROUND_FRICTION = (int) (GRAVITY * FRICTION_COEFF);
    private float acceleration;

    private ModelInstance object;
    private Rectangle rectangle;
    private float velocity;


    //angle?!?!?

    public DynamicObject(ModelInstance object, float modelWidth, float modelHeight, float modelX, float modelY) {
        this.object = object;
        rectangle = new Rectangle(modelX, modelY, modelWidth, modelHeight);
    }

    public void giveVelocity(float acceleration, float time)
    {
        //1 unit time is arbitrarily 5 fps
        velocity = acceleration * time;
        this.acceleration = acceleration;

    }

    //update object position according to velocity
    public void update()
    {
        //todo find a vector that shows acceleration or velocity with angle
        //object.transform.translate(x, y, z);
        //todo translate rectangle together with object
    }

    public ModelInstance getModel()
    {
        return object;
    }

    public Rectangle getRectangle()
    {
        return rectangle;
    }



}
