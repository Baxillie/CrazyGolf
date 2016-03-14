package nl.dke13;

import com.badlogic.gdx.graphics.g3d.ModelBatch;

import java.util.ArrayList;

/**
 * Created by nik on 3/14/16.
 */
public class physics
{
    private ArrayList<DynamicObject> dynamicObjects;
    private ArrayList<StaticObject> staticObjects;
    private ModelBatch renderer;

    public physics(ModelBatch renderer, ArrayList<DynamicObject> dynamicObjects, ArrayList<StaticObject> staticObjects) {
        this.dynamicObjects = dynamicObjects;
        this.staticObjects = staticObjects;
        this.renderer = renderer;
    }

    public void render()
    {
        for(DynamicObject dynamicObject: dynamicObjects)
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
                return true;

            else
                return false;
        }
        return false;
    }


    public void hasCollided()
    {
        //todo implement bouncing
    }
}
