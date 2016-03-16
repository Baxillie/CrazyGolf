package nl.dke13.physics;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class Physics
{
    private ArrayList<DynamicObject> dynamicObjects;
    private ArrayList<StaticObject> staticObjects;
    private ModelBatch renderer;

    public Physics(ModelBatch renderer, ArrayList<DynamicObject> dynamicObjects, ArrayList<StaticObject> staticObjects)
    {
        this.dynamicObjects = dynamicObjects;
        this.staticObjects = staticObjects;
        this.renderer = renderer;
    }

    public void render()
    {
        for(DynamicObject dynamicObject : dynamicObjects)
        {
            dynamicObject.update();
            hasCollided(dynamicObject);
            renderer.render(dynamicObject.getModel());
        }
        for(StaticObject staticObject : staticObjects)
        {
            renderer.render(staticObject.getModel());
        }
    }

    /*
    * not final because only for side wall collision
    */
    public void hasCollided(DynamicObject dynamicObject)
    {
        //todo: doesnt work if ball is inside other object
        if(isColliding(dynamicObject))
        {
            Vector3 velocityChange = dynamicObject.getVelocity(); //y & z acceleration doesnt change

            velocityChange.x = 0 - velocityChange.x;
            velocityChange.y = 0 - velocityChange.y;
            velocityChange.z = 0 - velocityChange.z;

            dynamicObject.updateVelocity(velocityChange);
        }
    }

    public boolean isColliding(DynamicObject dynamicObject)
    {
        ObjectBox box;
        box = dynamicObject.getBox();
        for(StaticObject staticObject: staticObjects)
        {
            if (box.overlaps(staticObject.getBox()))
            {
                return true;
            }
        }
        return false;
    }
}
