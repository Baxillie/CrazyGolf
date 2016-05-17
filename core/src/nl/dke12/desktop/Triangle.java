package nl.dke12.desktop;

import com.badlogic.gdx.math.Vector3;


/**
 * Created by Tom Conneely on 16/05/2016.
 */
public class Triangle {

    public Vector3 point1;
    public Vector3 point2;
    public Vector3 point3;
    private Vector3 normal;

    public Triangle(Vector3 pointOne,Vector3 pointTwo, Vector3 pointThree)
    {
        this.point1=pointOne;
        this.point2=pointTwo;
        this.point3=pointThree;
        Vector3 vector1 = new Vector3(point1);
        vector1.add(point2);
        Vector3 vector2 = new Vector3(point3);
        vector2.add(point2);
        this.normal = new Vector3(vector1);
        this.normal.crs(vector2);
        this.normal.scl(1/this.normal.len());
        //System.out.println("  norm="+normal);

    }

    public Vector3 getNormal()
    {
        return this.normal;
    }

    public Vector3 getIntersection(Vector3 point)
    {
        Vector3 vector1 = new Vector3(point1);
        vector1.add(point2);
        Vector3 norm1 = new Vector3(this.normal);
        Vector3 norm2 = new Vector3(this.normal);
        float t = norm1.dot(vector1)-norm2.dot(point);
        Vector3 norm3 = new Vector3(this.normal);
        Vector3 intersection = new Vector3(point);
        intersection.add(norm3.scl(t));

        return intersection;
    }

    public Vector3 getDistanceVector(Vector3 point)
    {
        Vector3 vector1 = new Vector3(point1);
        vector1.add(point2);
        Vector3 norm1 = new Vector3(this.normal);
        Vector3 norm2 = new Vector3(this.normal);
        float t = norm1.dot(vector1)-norm2.dot(point);
        Vector3 norm3 = new Vector3(this.normal);
        Vector3 distanceVector = new Vector3(norm3.scl(t));

        return distanceVector;
    }

    public float getDistance(Vector3 point)
    {
        Vector3 vector1 = new Vector3(point1);
        vector1.sub(point2);
        Vector3 norm1 = new Vector3(this.normal);
        Vector3 norm2 = new Vector3(this.normal);
        float t = norm1.dot(vector1)-norm2.dot(point);
        //System.out.println(" t="+t);
        Vector3 norm3 = new Vector3(this.normal);
        Vector3 intersection = new Vector3(point);
        intersection.add(norm3.scl(t));
        // if intersection is within the triangle
        if(testIntersection(intersection))
        {
            Vector3 pointTwo = new Vector3(intersection);
            float distance=pointTwo.sub(point).len();
            return distance;
        }
        // else clamp the point's barycentric coordinates relative to the triangle to within the interval [0,1]
        else
        {
            Vector3 baryPoint = new Vector3(Barycentric(intersection,new Vector3(new Vector3(point1).sub(point2))
                    ,new Vector3(new Vector3(point2).sub(point3)),new Vector3(new Vector3(point1).sub(point3)),0f,0f,0f));

            Vector3 closestPoint = new Vector3(clamp(baryPoint.x,0f,1f),clamp(baryPoint.y,0f,1f),clamp(baryPoint.z,0f,1f));
            Vector3 pointTwo = new Vector3(closestPoint);
            float distance=pointTwo.sub(point).len();
            //System.out.println("lol");
            return distance;
        }

    }

    // Compute barycentric coordinates (u, v, w) for
    // point p with respect to triangle (a, b, c)
    public Vector3 Barycentric(Vector3 p, Vector3 a, Vector3 b, Vector3 c, float u, float v, float w)
    {
        Vector3 v0 = new Vector3(new Vector3(b).sub(a));
        Vector3 v1 = new Vector3(new Vector3(c).sub(a));
        Vector3 v2 = new Vector3(new Vector3(p).sub(a));
        float d00 = v0.dot(v0);
        float d01 = v0.dot(v1);
        float d11 = v1.dot(v1);
        float d20 = v2.dot(v0);
        float d21 = v2.dot(v1);
        float denom = d00 * d11 - d01 * d01;
        v = (d11 * d20 - d01 * d21) / denom;
        w = (d00 * d21 - d01 * d20) / denom;
        u = 1.0f - v - w;
        Vector3 point = new Vector3(u,v,w);
        return point;
    }

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public boolean testIntersection(Vector3 point)
    {

        Vector3 p1 = new Vector3(this.point1);
        Vector3 p2 = new Vector3(this.point2);
        Vector3 p3 = new Vector3(this.point3);
        // replace this with a 3 unknown linear equation solver (using matices?)
        // (finding floats e,r,u  for the triangle formed by Vector3s v1,v2,v3 such that e⋅v1+r⋅v2+u⋅v3=p0
        //  where p0 is the point being tested for intersection)

            /*
            if(point.sub(p1).len()>p1.sub(p2).len()||
                point.sub(p1).len()>p1.sub(p3).len()||
                point.sub(p1).len()>p2.sub(p3).len()||
                point.sub(p2).len()>p1.sub(p2).len()||
                point.sub(p2).len()>p1.sub(p3).len()||
                point.sub(p2).len()>p2.sub(p3).len()||
                point.sub(p3).len()>p1.sub(p2).len()||
                point.sub(p3).len()>p1.sub(p3).len()||
                point.sub(p3).len()>p2.sub(p3).len())
            {
                return false;
            }
            else
            {
                return true;
            }
            */

        Vector3 baryPoint = new Vector3(Barycentric(point,new Vector3(new Vector3(point1).sub(point2))
                ,new Vector3(new Vector3(point2).sub(point3)),new Vector3(new Vector3(point1).sub(point3)),0f,0f,0f));
        if (baryPoint.x<1&&baryPoint.x>0&&baryPoint.y<1&&baryPoint.y>0&&baryPoint.z<1&&baryPoint.z>0)
        {
            return true;
        }
        else
        {
            return false;
        }

    }
}
