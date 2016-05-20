package nl.dke12.desktop;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Created by Tom Conneely on 14/05/2016.
 */
public class SolidObject
{
    private ArrayList<Vector3> points = new ArrayList<Vector3>();
    private Vector3 position = new Vector3();
    public SolidObject(float x, float y, float z, float width, float depth, float height)
    {
        position = new Vector3(x,y,z);
        points=new ArrayList<Vector3>();
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

    public void getClosestPlane(Vector3 point)
    {

    }

    public ArrayList<Vector3> getPoints()
    {
        return points;
    }

    public Vector3 getPosition()
    {
        return position;
    }
}
