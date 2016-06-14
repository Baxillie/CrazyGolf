package nl.dke12.game;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Crates model instance of ball
 * holds velocity and position
 */
public class Ball
{
    protected Vector3 position;
    protected Vector3 direction;
    protected float radius;
    protected String type;

    public Ball(float x, float y, float z,String type)
    {
        position = new Vector3(x,y,z);
        direction = new Vector3(0,0,0);
        this.radius = 0.5f;
        this.type = type;
    }

    public Vector3 getPosition()
    {
        return position;
    }

}
