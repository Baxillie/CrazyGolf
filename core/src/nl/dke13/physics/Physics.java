package nl.dke13.physics;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

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

    public boolean isColliding(DynamicObject dynamicObject)
    {
        ObjectBox a, b;
        for(StaticObject staticObject: staticObjects)
        {
            a = staticObject.getBox();
            b = dynamicObject.getBox();
            //check the X axis
            if(Math.abs(a.getX() - b.getX()) < a.getWidth() + b.getWidth())
            {
                //check the Y axis
                if(Math.abs(a.getY() - b.getY()) < a.getHeight() + b.getHeight())
                {
                    //check the Z axis
                    if(Math.abs(a.getZ() - b.getZ()) < a.getDepth() + b.getDepth())
                    {
                        return true;
                    }
                }
            }
            return false;
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
            System.out.println("IT'S FUCKING BOUNCING!! WEEEEEEEEE :D :D :D :D :D");
            Vector3 velocityChange = dynamicObject.getVelocity(); //y & z acceleration doesnt change
            velocityChange.x= -velocityChange.x; //x is negative because it goes into opposite direction
            dynamicObject.setVelocity(velocityChange);
        }
    }
}
