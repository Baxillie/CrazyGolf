package nl.dke12.game;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by Ajki on 19/06/2016.
 */
public class BallSimData
{
    private Vector3 direction;
    private Vector3 endPosition;
    private float heightModifier;
    private float forceModifier;

    public BallSimData(Vector3 direction, float heightModifier, float forceModifier, Vector3 endPosition)
    {
        this.direction = direction;
        this.heightModifier = heightModifier;
        this.forceModifier = forceModifier;
        this.endPosition = endPosition;
    }

    public Vector3 getDirection()
    {
        return direction;
    }

    public void setDirection(Vector3 direction)
    {
        this.direction = direction;
    }

    public Vector3 getEndPosition()
    {
        return endPosition;
    }

    public void setEndPosition(Vector3 endPosition)
    {
        this.endPosition = endPosition;
    }

    public float getHeightModifier()
    {
        return heightModifier;
    }

    public void setHeightModifier(float heightModifier)
    {
        this.heightModifier = heightModifier;
    }

    public float getForceModifier()
    {
        return forceModifier;
    }

    public void setForceModifier(float forceModifier)
    {
        this.forceModifier = forceModifier;
    }

}
