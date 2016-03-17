package nl.dke13.physics;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;


public class Ball {

    //mass of dynamic object is always 1 unit
    private final float DECCELERATION = 0.95f; //arbitrary number depending on input for velocity
    private ModelInstance object;
    private ObjectBox box;
    private Vector3 velocity;
    private Vector3 position;
    ModelBuilder mb;

    public Ball(ModelInstance object, float modelX, float modelY, float modelZ, float modelWidth, float modelHeight, float modelDepth)
    {
        this.object = object;
        this.box = new ObjectBox(modelX, modelY, modelZ, modelWidth, modelHeight, modelDepth);
        velocity = new Vector3(0,0,0);
        position = new Vector3(modelX,modelY,modelZ);
        mb = new ModelBuilder();
    }

    public void updateVelocity(Vector3 velocity)
    {
        if(!velocity.isZero(0.001f)) {
            this.velocity.x = velocity.x * DECCELERATION;
            this.velocity.y = velocity.y * DECCELERATION;
            this.velocity.z = velocity.z * DECCELERATION;
        } else
            this.velocity.set(0,0,0);
    }

    //update object position according to velocity
    public void update()
    {
        updateVelocity(velocity);

        object.transform.translate(velocity);
        position.add(velocity);
        box.setXYZ(velocity.x, velocity.y, velocity.z);
    }

    public void setVelocity(Vector3 velocity)
    {
        this.velocity = velocity;
    }

    public ModelInstance getModel()
        {
            return object;
        }

    public ObjectBox getBox()
        {
            return box;
        }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getVelocity()
        {
            return velocity;
        }


}


