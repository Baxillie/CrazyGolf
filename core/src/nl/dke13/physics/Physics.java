package nl.dke13.physics;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class Physics
{
    private ArrayList<Ball> dynamicObjects;
    private ArrayList<StaticObject> staticObjects;
    private Vector3 minVelocity = new Vector3(1f,1f,0f);
    private ModelBatch renderer;
    private int i;

    public Physics(ModelBatch renderer, ArrayList<Ball> dynamicObjects, ArrayList<StaticObject> staticObjects)
    {
        this.dynamicObjects = dynamicObjects;
        this.staticObjects = staticObjects;
        this.renderer = renderer;
        i = 0;
    }

    public void render()
    {
        for(Ball dynamicObject : dynamicObjects)
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
    public void hasCollided(Ball dynamicObject)
    {

        if(isColliding(dynamicObject))
        {
            System.out.println("we're colliding i=" + i);
            System.out.println(dynamicObject.getVelocity().toString());
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

    public boolean isColliding(Ball dynamicObject)
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
