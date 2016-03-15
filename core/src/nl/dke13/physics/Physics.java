package nl.dke13.physics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by nik on 3/14/16.
 */
public class Physics
{
    private ArrayList<DynamicObject> dynamicObjects;
    private ArrayList<StaticObject> staticObjects;
    private ModelBatch renderer;
    ShapeRenderer shapeRenderer;

    public Physics(ModelBatch renderer, ArrayList<DynamicObject> dynamicObjects, ArrayList<StaticObject> staticObjects) {
        this.dynamicObjects = dynamicObjects;
        this.staticObjects = staticObjects;
        this.renderer = renderer;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void render(Camera camera)
    {

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        for(DynamicObject dynamicObject : dynamicObjects)
        {
            System.out.println("DynamicObject transform: " + Arrays.toString(dynamicObject.getModel().transform.getValues()));
            dynamicObject.update();
            hasCollided(dynamicObject);
            renderer.render(dynamicObject.getModel());
            Rectangle rect = dynamicObject.getRectangle();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.CORAL);
            shapeRenderer.rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
            shapeRenderer.end();
        }
        for(StaticObject staticObject : staticObjects)
        {
            renderer.render(staticObject.getModel());
            Rectangle rect = staticObject.getRectangle();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.CORAL);
            shapeRenderer.rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
            shapeRenderer.end();
        }
    }

    public boolean isColliding(DynamicObject dynamicObject)
    {
        for(StaticObject staticObject: staticObjects)
        {
            if (staticObject.getRectangle().overlaps(dynamicObject.getRectangle()))
            {
                System.out.println("IT FUCKING COLLIDED!!!");
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
            System.out.println("IT SHOULD FUCKING BOUNCE!!!!! AARRRRGGGGGG...");
            Vector3 velocityChange = dynamicObject.getVelocity(); //y & z acceleration doesnt change
            velocityChange.x= -velocityChange.x; //x is negative because it goes into opposite direction
            dynamicObject.setVelocity(velocityChange);
        }
    }
}
