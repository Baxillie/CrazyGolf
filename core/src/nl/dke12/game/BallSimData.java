package nl.dke12.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Ajki on 19/06/2016.
 */
public class BallSimData
{
    private Vector3 direction;
    private Vector3 endPosition;
    private Vector3 holePosition;
    private float heightModifier;
    private float forceModifier;

    public BallSimData(Vector3 direction, float heightModifier, float forceModifier, Vector3 endPosition, Vector3 holePosition)
    {
        this.direction = direction;
        this.heightModifier = heightModifier;
        this.forceModifier = forceModifier;
        this.endPosition = endPosition;
        this.holePosition = holePosition;
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

    public float absDistFromHole()
    {
        Vector2 absDist = new Vector2(endPosition.x, endPosition.y).add(new Vector2(holePosition.x, holePosition.y));
        return Math.abs(absDist.len());
    }

    @Override
    public String toString()
    {
        return "dir: " + direction.toString() + "       height:" + heightModifier + "       force:" + forceModifier + "     endPos: " + endPosition.toString() + "\n" ;
    }

}
