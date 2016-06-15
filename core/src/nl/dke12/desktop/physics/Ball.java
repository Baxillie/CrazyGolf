package nl.dke12.desktop.physics;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

@Deprecated
public class Ball {

    //getMass of dynamic object is always 1 unit
    private final float DECCELERATION = 0.95f; //arbitrary number depending on input for velocity
    private ModelInstance object;
    private ObjectBox box;
    private Vector3 velocity;
    private Vector3 position;
    private double radius;
    private float mass;
    private Vector3 prevVelocity;
    private boolean ballDone;

    public Ball(ModelInstance object, float modelX, float modelY, float modelZ, float modelWidth, float modelHeight, float modelDepth)
    {
        this.object = object;
        this.box = new ObjectBox(modelX, modelY, modelZ, modelWidth, modelHeight, modelDepth);
        this.radius = modelWidth/2;
        mass = 1f;
        velocity = new Vector3(0,0,0);
        prevVelocity = new Vector3(0,0,2);
        position = new Vector3(modelX,modelY,modelZ);
        ballDone = false;
        object.transform.translate(0,13,0);
        object.transform.scale(0.15f,0.15f,0.15f);
}

    public void updateVelocity(Vector3 velocity)
    {
        prevVelocity = velocity;
        //stopBall();
        if(!velocity.isZero(0.001f))
        {
            this.velocity.x = velocity.x * DECCELERATION;
            this.velocity.y = velocity.y * DECCELERATION;
            this.velocity.z = velocity.z * DECCELERATION;
        }
        else
        {
            this.velocity.set(0, 0, 0);
        }
    }

    public void incrementVelocity(Vector3 vector3)
    {
        velocity.x += vector3.x;
        velocity.y += vector3.y;
        velocity.z += vector3.z;
    }
    //update object position according to velocity
    public void update()
    {
        updateVelocity(velocity);
        object.transform.translate(velocity);
        position.add(velocity);
        box.incrementXYZ(velocity.x, velocity.y, velocity.z);
    }

    public void stopBall()
    {
        //todo: fix hack
        if(position.z < -0.5f)
        {
            setVelocity(new Vector3(0,0,0));
            ballDone = true;
        }
        else
            ballDone = false;

    }

    public void setVelocity(Vector3 velocity)
    {
        this.velocity = velocity;
    }

    public ModelInstance getModel()
        {
            return object;
        }

    public ObjectBox getBox()
        {
            return box;
        }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getVelocity()
        {
            return velocity;
        }

    public Vector3 getPrevVelocity() { return prevVelocity; }

    public boolean getBallDone() {return ballDone;}

    public double getRadius(){return radius;}

    public float getMass() {return mass;}

    public String debugString()
    {
        String s = "ball:\n";
        s += "position: " + position.toString();
        s += "\nvelocity: " + velocity.toString();
        s += "\nprev vel: " + prevVelocity.toString();
        s += "\nbounding box:\n";
        s += box.debugString();
        return s;
    }
}


