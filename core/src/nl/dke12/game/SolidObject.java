package nl.dke12.game;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Created by Tom Conneely on 14/05/2016.
 */
public class SolidObject
{
    private ArrayList<Vector3> points;
    private Vector3 position;
    private String type;


    public SolidObject(float x, float y, float z, float width, float depth, float height, String type)
    {
        position = new Vector3(x,y,z);
        this.type = type;
        points=new ArrayList<>();
        addPoint(x-width,y-depth,z-height);
        addPoint(x-width,y-depth,z+height);
        addPoint(x-width,y+depth,z+height);
        addPoint(x-width,y+depth,z-height);
        addPoint(x+width,y+depth,z-height);
        addPoint(x+width,y+depth,z+height);
        addPoint(x+width,y-depth,z+height);
        addPoint(x+width,y-depth,z-height);

    }

    public SolidObject(float x, float y, float z)
    {
        position.set(x,y,z);
    }

    public void addPoint(float x, float y, float z)
    {
        points.add(new Vector3(x,y,z));
    }

    public ArrayList<Vector3> getPoints()
    {
        return points;
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public String getType()
    {
        return type;
    }

}
