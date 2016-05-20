package nl.dke12.game;

import com.badlogic.gdx.math.Vector3;
import nl.dke12.desktop.GameObject;

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

    public Ball(float xspeed, float yspeed, float zspeed)
    {
        position = new Vector3(0,0,0);
        direction = new Vector3(xspeed,yspeed,zspeed);
        this.radius = 0.5f;
    }

    public Vector3 getPosition()
    {
        return position;
    }

}
