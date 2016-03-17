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
    private Vector3 minVelocity = new Vector3(0.7f,0.7f,0f);
    private int i;

    public Physics(ModelBatch renderer, ArrayList<DynamicObject> dynamicObjects, ArrayList<StaticObject> staticObjects)
    {
        this.dynamicObjects = dynamicObjects;
        this.staticObjects = staticObjects;
        this.renderer = renderer;
        i = 0;
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

        if(isColliding(dynamicObject))
        {
            System.out.println("we're colliding i=" + i);
            //Hole is the 1st entry in staticObject array-list
            if(i == 1)
            {
                if(Math.abs(dynamicObject.getVelocity().x) <= minVelocity.x && Math.abs(dynamicObject.getVelocity().y) <= minVelocity.y)
                {
                    dynamicObject.setVelocity(new Vector3(0f, 0f, -0.1f));
                    return;
                }
                return;
            }

            Vector3 velocityChange = dynamicObject.getVelocity();

            if(dynamicObject.getBox().isBumpX())
            {
                velocityChange.x = 0 - velocityChange.x;
            }
            if(dynamicObject.getBox().isBumpY())
            {
                velocityChange.y = 0 - velocityChange.y;
            }

            //todo: fix Z next period
            //todo: corners are a bit fucked :3
            //velocityChange.z = 0 - velocityChange.z;
            dynamicObject.updateVelocity(velocityChange);
        }
    }

    public boolean isColliding(DynamicObject dynamicObject)
    {
        ObjectBox box;
        box = dynamicObject.getBox();
        i = 0;
        for(StaticObject staticObject: staticObjects)
        {
            i++;
            if (box.overlaps(staticObject.getBox()))
            {
                return true;
            }
        }
        return false;
    }
}
