package nl.dke13.physics;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;


public class DynamicObject {

    //mass of dynamic object is always 1 unit
    private final float DECCELERATION = 0.1f; //arbitrary number depending on input for velocity
    private ModelInstance object;
    private ObjectBox box;
    private Vector3 velocity = new Vector3(0,0,0);
    private float energy;

    public DynamicObject(ModelInstance object, float modelX, float modelY, float modelZ, float modelWidth, float modelHeight, float modelDepth, float energy)
    {
        this.object = object;
        this.box = new ObjectBox(modelX, modelY, modelZ, modelWidth, modelHeight, modelDepth);
        velocity = new Vector3(0,0,0);
        this.energy = energy;
    }

    public void setVelocity(Vector3 velocity) // x*a ; y*b ; z*c
    {
        if(energy > 0) {
            this.velocity.x = velocity.x - DECCELERATION;
            this.velocity.y = velocity.y - DECCELERATION;
            //this.velocity.z = velocity.z - DECCELERATION;
        } else
            this.velocity.set(0,0,0);
    }

    //update object position according to velocity
    public void update()
    {
        setVelocity(velocity);
        energy--;
        System.out.println(velocity);
        object.transform.translate(velocity);

        box.setXYZ(velocity.x, velocity.y, velocity.z);
    }

    public ModelInstance getModel()
        {
            return object;
        }

    public ObjectBox getBox()
        {
            return box;
        }

    public Vector3 getVelocity()
        {
            return velocity;
        }

}


