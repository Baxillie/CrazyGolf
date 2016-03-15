package nl.dke13.physics;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Created by nik on 3/14/16.
 */
public class Physics
{
    private ArrayList<DynamicObject> dynamicObjects;
    private ArrayList<StaticObject> staticObjects;
    private ModelBatch renderer;

    public Physics(ModelBatch renderer, ArrayList<DynamicObject> dynamicObjects, ArrayList<StaticObject> staticObjects) {
        this.dynamicObjects = dynamicObjects;
        this.staticObjects = staticObjects;
        this.renderer = renderer;
    }

    public void render()
    {
        for(DynamicObject dynamicObject : dynamicObjects)
        {
            dynamicObject.update();
            renderer.render(dynamicObject.getModel());
        }
        for(StaticObject staticObject : staticObjects)
        {
            renderer.render(staticObject.getModel());
        }
    }

    public boolean isColliding(DynamicObject dynamicObject)
    {
        for(StaticObject staticObject: staticObjects)
        {
            if (staticObject.getRectangle().overlaps(dynamicObject.getRectangle()))
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
    public void hasCollided(DynamicObject dynamicObject)
    {
        if(isColliding(dynamicObject))
        {
            Vector3 velocityChange = dynamicObject.getVelocity(); //y & z acceleration doesnt change
            velocityChange.x= -velocityChange.x; //x is negative because it goes into opposite direction
            dynamicObject.setVelocity(velocityChange);
        }
    }
}
