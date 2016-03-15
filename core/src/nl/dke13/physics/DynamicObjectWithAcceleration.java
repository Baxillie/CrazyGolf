package nl.dke13.physics;

import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by nik on 3/14/16.
 */
public class DynamicObjectWithAcceleration {

    //mass of dynamic object is always 1 unit

    private final int GRAVITY = 10;
    private final float  FRICTION_COEFF = 0.5f;
    private final float GROUND_FRICTION =  GRAVITY * FRICTION_COEFF * 0.001f;

    private Vector3 acceleration;
    private ModelInstance object;
    private Rectangle rectangle;
    private Vector3 velocity = new Vector3(0,0,0);

    //angle?!?!?

    public DynamicObjectWithAcceleration(ModelInstance object, float modelWidth, float modelHeight, float modelX, float modelY)
    {
        this.object = object;
        rectangle = new Rectangle(modelX, modelY, modelWidth, modelHeight);
        velocity = new Vector3(0,0,0);
    }

    public void setFirstAcceleration(Vector3 acceleration, float time)
    {
        //1 unit time is arbitrarily 5 fps
        //set actual acceleration to take friction into account
        this.acceleration = acceleration;
        setVelocity(time);
        setVelocity(acceleration, time);
    }

    public void setVelocity(Vector3 acceleration, float time)
    {
        Vector3 frictionAcceleration = new Vector3(-GROUND_FRICTION, -GROUND_FRICTION, -acceleration.z);
        this.acceleration = acceleration.add(frictionAcceleration);
        setVelocity(time);
    }

    private void setVelocity(float time)
    {
        //v = at
        time = time/100;
        velocity.x = acceleration.x * time + velocity.x;
        velocity.y = acceleration.y * time + velocity.y;
        velocity.z = acceleration.z * time + velocity.z;
    }

    //update object position according to velocity
    public void update()
    {
        System.out.println(velocity);
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
