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
    private PrintWriter pw;
    private int count = 0;

    public Physics(ModelBatch renderer, ArrayList<DynamicObject> dynamicObjects, ArrayList<StaticObject> staticObjects)
    {
        this.dynamicObjects = dynamicObjects;
        this.staticObjects = staticObjects;
        this.renderer = renderer;

        try {
            Date now = new Date();
            pw = new PrintWriter("log_" + now.getHours() + "." + now.getMinutes() + ".txt");
        } catch (FileNotFoundException e) {
            System.out.print("File not found");
        }
    }

    public void render()
    {
        pw.println("Frame " + count++);
        int i = 0;
        for(DynamicObject dynamicObject : dynamicObjects)
        {
            dynamicObject.update();
            pw.println("The position of DO: " + dynamicObject.getPosition().toString());
            pw.print(dynamicObject.getBox().toString());
            hasCollided(dynamicObject);
            renderer.render(dynamicObject.getModel());
        }
        for(StaticObject staticObject : staticObjects)
        {
            pw.println("The position of SO: " + i + " "+ staticObject.getPosition().toString());
            pw.print(staticObject.getBox().toString());
            renderer.render(staticObject.getModel());
            i++;
        }
    }

    /*
    * not final because only for side wall collision
    */
    public void hasCollided(DynamicObject dynamicObject)
    {
        pw.println("Dynamic object parameters:");
        pw.print(dynamicObject.getBox().toString());
        //todo: doesnt work if ball is inside other object
        if(isColliding(dynamicObject))
        {
            Vector3 velocityChange = dynamicObject.getVelocity(); //y & z acceleration doesnt change
            velocityChange.y= 0 - velocityChange.y; //x is negative because it goes into opposite direction
            dynamicObject.updateVelocity(velocityChange);
        }
    }

    public boolean isColliding(DynamicObject dynamicObject)
    {
        ObjectBox box;
        box = dynamicObject.getBox();
        int i = -1;
        for(StaticObject staticObject: staticObjects)
        {
            i++;
            pw.println("comparing do with so " + i + " with parameters:");
            pw.print(staticObject.getBox().toString());
            boolean ifReturnValue;
            if (ifReturnValue = box.overlaps(staticObject.getBox(),pw))
            {
                pw.println("got in if statement: it returned " + ifReturnValue);
                pw.println("It collided with so " + i);
                return true;
            }
        }
        return false;
    }


}
