package nl.dke13;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
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
    private Vector3 acceleration;

    private ModelInstance object;
    private Rectangle rectangle;
    private Vector3 velocity;

    //angle?!?!?

    public DynamicObject(ModelInstance object, float modelWidth, float modelHeight, float modelX, float modelY)
    {
        this.object = object;
        rectangle = new Rectangle(modelX, modelY, modelWidth, modelHeight);
    }

    public void setFirstAcceleration(Vector3 acceleration, float time)
    {
        //1 unit time is arbitrarily 5 fps
        //set actual acceleration to take friction into account
        Vector3 frictionAcceleration = new Vector3(-GROUND_FRICTION, -GROUND_FRICTION, -acceleration.z);
        this.acceleration = acceleration.add(frictionAcceleration);
        setVelocity(time);
    }

    public void setVelocity(Vector3 acceleration, float time)
    {
        this.acceleration = acceleration;
        setVelocity(time);
    }

    private void setVelocity(float time)
    {
        //v = at
        velocity.x = acceleration.x * time;
        velocity.y = acceleration.y * time;
        velocity.z = time;
    }

    //update object position according to velocity
    public void update()
    {
        object.transform.translate(velocity);
        rectangle.setPosition(velocity.x, velocity.y);
    }
    public ModelInstance getModel()
    {
        return object;
    }

    public Rectangle getRectangle()
    {
        return rectangle;
    }

    public Vector3 getAcceleration()
    {
        return acceleration;
    }



}
