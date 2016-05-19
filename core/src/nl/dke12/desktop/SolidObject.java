package nl.dke12.desktop;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Created by Ajki on 19/05/2016.
 */
public class SolidObject
{
    private Vector3 position;
    private ArrayList<Vector3> points;

    public SolidObject(float x, float y, float z, float scalex, float scaley, float scalez)
    {
        position = new Vector3(x,y,z).scl(scalex);
        points = new ArrayList<>();
    }

    public SolidObject(float x, float y, float z)
    {
        position = new Vector3(x,y,z);
        points = new ArrayList<>();
    }

    public void addPoint(float x, float y, float z)
    {
        points.add(new Vector3(x,y,z));
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public ArrayList<Vector3> getPoints()
    {
        return points;
    }
}
