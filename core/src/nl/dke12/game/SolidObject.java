package nl.dke12.game;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Created by Tom Conneely on 14/05/2016.
 */
public class SolidObject
{
    //all states the solidObject can present
    public final static String ball1="solidBall";
    public final static String ball2="solidBall2";
    public final static String floor="floor";
    public final static String wall="wall";
    public final static String windmill="windmill";
    public final static String hole="hole";
    public final static String slope="slope";
    public final static String slopeL="slopeL";
    public final static String slopeR="slopeR";
    public final static String slopeU="slopeU";
    private final String[] objectSet = {ball1, ball2, floor, wall, windmill, hole, slope, slopeL, slopeR, slopeU};

    private ArrayList<Vector3> points;
    private ArrayList<Triangle> planes;
    public ArrayList<Triangle> planes1;
    public ArrayList<Triangle> planes2;
    public ArrayList<Triangle> planes3;
    public ArrayList<Triangle> planes4;
    private Vector3 position;
    private String type;

    //store width, depth and height (x, y and z axis, respectifly)
    private float width;
    private float depth;
    private float height;

    public SolidObject(float x, float y, float z, float width, float depth, float height, String type) throws NoSuchSolidObjectType
    {
        if(!typeIsInList(type))
        {
            throw new NoSuchSolidObjectType("String type given in the SolidObject constructor is not Correct. Use the static strings");
        }

        position = new Vector3(x,y,z);
        this.type = type;

        this.width = width;
        this.depth = depth;
        this.height = height;

        points=new ArrayList<Vector3>();
        planes=new ArrayList<Triangle>();
        if(planes1==null)
        {
            planes1=new ArrayList<Triangle>();
        }
        if(planes2==null)
        {
            planes2=new ArrayList<Triangle>();
        }
        if(planes3==null)
        {
            planes3=new ArrayList<Triangle>();
        }
        if(planes4==null)
        {
            planes4=new ArrayList<Triangle>();
        }
        if(type!="hole")
        {
        addPoint(x-width,y-depth,z-height);
        addPoint(x-width,y-depth,z+height);
        addPoint(x-width,y+depth,z+height);
        addPoint(x-width,y+depth,z-height);
        addPoint(x+width,y+depth,z-height);
        addPoint(x+width,y+depth,z+height);
        addPoint(x+width,y-depth,z+height);
        addPoint(x+width,y-depth,z-height);
        addPlane(points.get(0),points.get(1),points.get(2),planes);
        addPlane(points.get(0),points.get(2),points.get(3),planes);
        addPlane(points.get(0),points.get(1),points.get(6),planes);
        addPlane(points.get(0),points.get(6),points.get(7),planes);
        addPlane(points.get(2),points.get(3),points.get(4),planes);
        addPlane(points.get(2),points.get(4),points.get(5),planes);
        addPlane(points.get(1),points.get(2),points.get(5),planes);
        addPlane(points.get(1),points.get(5),points.get(6),planes);
        addPlane(points.get(4),points.get(5),points.get(6),planes);
        addPlane(points.get(4),points.get(6),points.get(7),planes);
        }
        else
        {
            addPoint(x-width,y-depth,z-height);
            addPoint(x-width,y-depth,z+height);
            addPoint(x-width,y+depth,z+height);
            addPoint(x-width,y+depth,z-height);
            addPoint(x+width,y+depth,z-height);
            addPoint(x+width,y+depth,z+height);
            addPoint(x+width,y-depth,z+height);
            addPoint(x+width,y-depth,z-height);

            addPoint(x-width/2,y-depth/2,z+height);
            addPoint(x-width/2,y+depth/2,z+height);
            addPoint(x+width/2,y-depth/2,z+height);
            addPoint(x+width/2,y+depth/2,z+height);
            addPlane(points.get(0),points.get(1),points.get(2),planes);
            addPlane(points.get(0),points.get(2),points.get(3),planes);
            addPlane(points.get(0),points.get(1),points.get(6),planes);
            addPlane(points.get(0),points.get(6),points.get(7),planes);
            addPlane(points.get(2),points.get(3),points.get(4),planes);
            addPlane(points.get(2),points.get(4),points.get(5),planes);
            //addPlane(points.get(1),points.get(2),points.get(5),planes);
            //addPlane(points.get(1),points.get(5),points.get(6),planes);
            addPlane(points.get(4),points.get(5),points.get(6),planes);
            addPlane(points.get(4),points.get(6),points.get(7),planes);

            addPlane(points.get(1),points.get(8),points.get(2),planes);
            addPlane(points.get(1),points.get(8),points.get(10),planes);
            addPlane(points.get(1),points.get(10),points.get(6),planes);
            addPlane(points.get(10),points.get(6),points.get(11),planes);
            addPlane(points.get(5),points.get(6),points.get(11),planes);
            addPlane(points.get(5),points.get(11),points.get(9),planes);
            addPlane(points.get(5),points.get(9),points.get(2),planes);
            addPlane(points.get(2),points.get(9),points.get(8),planes);

        }
        /*addPlane(points.get(0),points.get(3),points.get(4),planes);
        addPlane(points.get(0),points.get(4),points.get(7),planes);*/

        if(x<160&&y<160)
        {
            addPlane(points.get(1),points.get(2),points.get(5),planes1);
            addPlane(points.get(1),points.get(5),points.get(6),planes1);

            addPlane(points.get(0),points.get(1),points.get(2),planes1);
            addPlane(points.get(0),points.get(2),points.get(3),planes1);
            addPlane(points.get(0),points.get(1),points.get(6),planes1);
            addPlane(points.get(0),points.get(6),points.get(7),planes1);
            addPlane(points.get(2),points.get(3),points.get(4),planes1);
            addPlane(points.get(2),points.get(4),points.get(5),planes1);

            addPlane(points.get(4),points.get(5),points.get(6),planes1);
            addPlane(points.get(4),points.get(6),points.get(7),planes1);
        }
        if(x<160&&y>=160)
        {
            addPlane(points.get(1),points.get(2),points.get(5),planes2);
            addPlane(points.get(1),points.get(5),points.get(6),planes2);

            addPlane(points.get(0),points.get(1),points.get(2),planes2);
            addPlane(points.get(0),points.get(2),points.get(3),planes2);
            addPlane(points.get(0),points.get(1),points.get(6),planes2);
            addPlane(points.get(0),points.get(6),points.get(7),planes2);
            addPlane(points.get(2),points.get(3),points.get(4),planes2);
            addPlane(points.get(2),points.get(4),points.get(5),planes2);

            addPlane(points.get(4),points.get(5),points.get(6),planes2);
            addPlane(points.get(4),points.get(6),points.get(7),planes2);
        }
        if(x>=160&&y<160)
        {
            addPlane(points.get(1),points.get(2),points.get(5),planes3);
            addPlane(points.get(1),points.get(5),points.get(6),planes3);

            addPlane(points.get(0),points.get(1),points.get(2),planes3);
            addPlane(points.get(0),points.get(2),points.get(3),planes3);
            addPlane(points.get(0),points.get(1),points.get(6),planes3);
            addPlane(points.get(0),points.get(6),points.get(7),planes3);
            addPlane(points.get(2),points.get(3),points.get(4),planes3);
            addPlane(points.get(2),points.get(4),points.get(5),planes3);

            addPlane(points.get(4),points.get(5),points.get(6),planes3);
            addPlane(points.get(4),points.get(6),points.get(7),planes3);
        }
        if(x>=160&&y>=160)
        {
            addPlane(points.get(1),points.get(2),points.get(5),planes4);
            addPlane(points.get(1),points.get(5),points.get(6),planes4);

            addPlane(points.get(0),points.get(1),points.get(2),planes4);
            addPlane(points.get(0),points.get(2),points.get(3),planes4);
            addPlane(points.get(0),points.get(1),points.get(6),planes4);
            addPlane(points.get(0),points.get(6),points.get(7),planes4);
            addPlane(points.get(2),points.get(3),points.get(4),planes4);
            addPlane(points.get(2),points.get(4),points.get(5),planes4);

            addPlane(points.get(4),points.get(5),points.get(6),planes4);
            addPlane(points.get(4),points.get(6),points.get(7),planes4);
        }
    }

    public SolidObject(float x, float y, float z, float width, float depth, float height,boolean rotation, String type)  throws NoSuchSolidObjectType
    {
        if(!typeIsInList(type))
        {
            throw new NoSuchSolidObjectType("String type given in the SolidObject constructor is not Correct. Use the static strings");
        }
        position = new Vector3(x,y,z);
        this.type = type;
        float newWidth =(float) Math.sqrt(width*width+depth*depth);
        points=new ArrayList<Vector3>();
        planes=new ArrayList<Triangle>();
        addPoint(x-newWidth,y,z-height);
        addPoint(x-newWidth,y,z+height);
        addPoint(x,y+newWidth,z+height);
        addPoint(x,y+newWidth,z-height);
        addPoint(x+newWidth,y+depth,z-height);
        addPoint(x+newWidth,y+depth,z+height);
        addPoint(x,y-newWidth,z+height);
        addPoint(x,y-newWidth,z-height);
    }


//slopes
    public SolidObject(float x, float y, float z,String type) throws NoSuchSolidObjectType
    {
        if(!typeIsInList(type))
        {
            throw new NoSuchSolidObjectType("String type given in the SolidObject constructor is not Correct. Use the static strings");
        }
        this.type=type;
        position = new Vector3(x,y,z);
        points = new ArrayList<Vector3>();
        planes=new ArrayList<Triangle>();
    }

    private boolean typeIsInList(String type)
    {
        for(String t : objectSet)
        {
            if(!type.equals(t))
            {
                return false;
            }
        }
        return true;
    }

    public void addPlane(Vector3 point1,Vector3 point2, Vector3 point3, ArrayList<Triangle> area)
    {
        area.add(new Triangle(point1,point2,point3));
    }

    public void addPoint(float x, float y, float z)
    {
        points.add(new Vector3(x,y,z));
    }

    public ArrayList<Vector3> getPoints()
    {
        return points;
    }

    public ArrayList<Triangle> getPlanes()
    {
        return planes;
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public String getType()
    {
        return type;
    }

    public float getWidth()
    {
        return width;
    }

    public float getDepth() {
        return depth;
    }

    public float getHeight() {
        return height;
    }
}
