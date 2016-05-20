package nl.dke12.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import nl.dke12.desktop.GameObject;

import java.util.ArrayList;

/**
 * Crates model instance of ball
 * holds velocity and position
 */
public class Ball extends GameObject
{
    public Vector3 position;
    public Vector3 direction;
    public Vector3 nextPosition = new Vector3(0,0,0);
    public float radius;
    private ModelInstance ballModel;
    protected NewBallPhysics physics;
    //velocity
    public Ball(ModelInstance model, float x, float y, float z, float xspeed, float yspeed, float zspeed, ArrayList<SolidObject> solidObjects)
    {
        position = new Vector3();
        direction = new Vector3();
        position.set(x,y,z);
        direction.set(xspeed,yspeed,zspeed);
        this.radius = 0.5f;
        this.physics= new NewBallPhysics(solidObjects,this);
        this.ballModel=model;
    }
    public NewBallPhysics getPhysics()
    {
        return this.physics;
    }

    public ModelInstance getModelInstance()
    {
        return ballModel;
    }

    //getters and setters for everything
}
