package nl.dke13.physics;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Created by nik on 3/14/16.
 */
public class Physics
{
    private ArrayList<DynamicObjectWithAcceleration> dynamicObjectWithAccelerations;
    private ArrayList<StaticObject> staticObjects;
    private ModelBatch renderer;

    public Physics(ModelBatch renderer, ArrayList<DynamicObjectWithAcceleration> dynamicObjectWithAccelerations, ArrayList<StaticObject> staticObjects) {
        this.dynamicObjectWithAccelerations = dynamicObjectWithAccelerations;
        this.staticObjects = staticObjects;
        this.renderer = renderer;
    }

    public void render()
    {
        for(DynamicObjectWithAcceleration dynamicObjectWithAcceleration : dynamicObjectWithAccelerations)
        {
            dynamicObjectWithAcceleration.update();
            renderer.render(dynamicObjectWithAcceleration.getModel());
        }
        for(StaticObject staticObject : staticObjects)
        {
            renderer.render(staticObject.getModel());
        }
    }

    public boolean isColliding(DynamicObjectWithAcceleration dynamicObjectWithAcceleration)
    {
        for(StaticObject staticObject: staticObjects)
        {
            if (staticObject.getRectangle().overlaps(dynamicObjectWithAcceleration.getRectangle()))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        return false;
    }

    /*
    * not final because only for side wall collision
    */
    public void hasCollided(DynamicObjectWithAcceleration dynamicObjectWithAcceleration, float time)
    {
        if(isColliding(dynamicObjectWithAcceleration))
        {
            Vector3 acc = dynamicObjectWithAcceleration.getAcceleration(); //y & z acceleration doesnt change
            acc.x= -acc.x; //x is negative because it goes into opposite direction
            dynamicObjectWithAcceleration.setVelocity(acc, time);
        }
    }
}
