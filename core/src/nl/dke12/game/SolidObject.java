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

    public SolidObject(float x, float y, float z, float width, float depth, float height,boolean rotation, String type)
    {
        position = new Vector3(x,y,z);
        this.type = type;
        float newWidth =(float) Math.sqrt(width*width+depth*depth);
        points=new ArrayList<Vector3>();
        addPoint(x-newWidth,y,z-height);
        addPoint(x-newWidth,y,z+height);
        addPoint(x,y+newWidth,z+height);
        addPoint(x,y+newWidth,z-height);
        addPoint(x+newWidth,y+depth,z-height);
        addPoint(x+newWidth,y+depth,z+height);
        addPoint(x,y-newWidth,z+height);
        addPoint(x,y-newWidth,z-height);
    }



    public SolidObject(float x, float y, float z,String type)
    {
        this.type=type;
        position = new Vector3(x,y,z);
        points=new ArrayList<Vector3>();
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
