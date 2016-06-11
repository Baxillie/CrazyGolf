package nl.dke12.game;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;


/**
 * Created by Tom Conneely on 16/05/2016.
 */
public class Triangle {

    private Vector3 point1;
    private Vector3 point2;
    private Vector3 point3;
    private Vector3 normal;

    public Triangle(Vector3 pointOne,Vector3 pointTwo, Vector3 pointThree)
    {
        this.point1 = pointOne;
        this.point2 = pointTwo;
        this.point3 = pointThree;
        Vector3 vector1 = new Vector3(point1);
        vector1.add(point2);
        Vector3 vector2 = new Vector3(point3);
        vector2.add(point2);
        this.normal = new Vector3(vector1);
        this.normal.crs(vector2);
        this.normal.scl(1/normal.len());
    }

    public Vector3 getNormal()
    {
        return normal;
    }

    public Vector3 getIntersection(Vector3 point)
    {
        Vector3 vector1 = new Vector3(point1);
        vector1.add(point2);

        Vector3 norm1 = new Vector3(this.normal);
        Vector3 norm2 = new Vector3(this.normal);

        float t = norm1.dot(vector1) - norm2.dot(point);

        Vector3 norm3 = new Vector3(this.normal);

        Vector3 intersection = new Vector3(point);
        intersection.add(new Vector3(norm3).scl(t));

        return intersection;
    }

    // Compute barycentric coordinates (u, v, w) for
    // point p with respect to triangle (a, b, c)
    public Vector3 baryCentric(Vector3 p, Vector3 a, Vector3 b, Vector3 c, float u, float v, float w)
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

    public Vector3 getClosestPoint(Vector3 point)
    {
        Intersector intersector = new Intersector();
        Vector3 projection = new Vector3(point).sub(new Vector3(normal).scl((new Vector3(point).sub(point1)).dot(normal)));
        if(intersector.isPointInTriangle(projection,point1,point2,point3))
        {
            return projection;
        }
        else
        {
            /*Vector3 edge0 = new Vector3(new Vector3(point2).sub(point1));
            Vector3 edge1 = new Vector3(new Vector3(point3).sub(point1));
            Vector3 edge2 = new Vector3(new Vector3(point3).sub(point2));*/

            /*Vector2 side1=new Vector2(getClosestPointOnSegment(point2.x,point2.y,point1.x,point1.y,projection.x,projection.y));
            Vector2 side2=new Vector2(getClosestPointOnSegment(point3.x,point3.y,point1.x,point1.y,projection.x,projection.y));
            Vector2 side3=new Vector2(getClosestPointOnSegment(point3.x,point3.y,point2.x,point2.y,projection.x,projection.y));*/

            Vector3 edge0 = new Vector3(getPointonSegment(projection,point1,point3));
            Vector3 edge1 = new Vector3(getPointonSegment(projection,point2,point1));
            Vector3 edge2 = new Vector3(getPointonSegment(projection,point3,point2));

            if((new Vector3(edge0).sub(point)).len()<=(new Vector3(edge1).sub(point)).len()&&
            (new Vector3(edge0).sub(point)).len()<=(new Vector3(edge2).sub(point)).len())
            {
                return edge0;
            }
            else if((new Vector3(edge1).sub(point)).len()<(new Vector3(edge0).sub(point)).len()&&
                    (new Vector3(edge1).sub(point)).len()<=(new Vector3(edge2).sub(point)).len())
            {
                return edge1;
            }
            else if((new Vector3(edge2).sub(point)).len()<(new Vector3(edge1).sub(point)).len()&&
                    (new Vector3(edge2).sub(point)).len()<(new Vector3(edge0).sub(point)).len())
            {
                return edge2;
            }
            else
            {
                return edge2;
            }

        }
    }

    public Vector3 closestPoint(Vector3 point )
    {
        Vector3 edge0 = new Vector3(new Vector3(point2).sub(point1));
        Vector3 edge1 = new Vector3(new Vector3(point3).sub(point1));
        Vector3 v0 = new Vector3(new Vector3(point1).sub(point));

        float a = edge0.dot( edge0 );
        float b = edge0.dot( edge1 );
        float c = edge1.dot( edge1 );
        float d = edge0.dot( v0 );
        float e = edge1.dot( v0 );

        float det = a*c - b*b;
        float s = b*e - c*d;
        float t = b*d - a*e;

        if ( s + t < det )
        {
            if ( s < 0.f )
            {
                if ( t < 0.f )
                {
                    if ( d < 0.f )
                    {
                        s = clamp( -d/a, 0.f, 1.f );
                        t = 0.f;
                    }
                    else
                    {
                        s = 0.f;
                        t = clamp( -e/c, 0.f, 1.f );
                    }
                }
                else
                {
                    s = 0.f;
                    t = clamp( -e/c, 0.f, 1.f );
                }
            }
            else if ( t < 0.f )
            {
                s = clamp( -d/a, 0.f, 1.f );
                t = 0.f;
            }
            else
            {
                float invDet = 1.f / det;
                s *= invDet;
                t *= invDet;
            }
        }
        else
        {
            if ( s < 0.f )
            {
                float tmp0 = b+d;
                float tmp1 = c+e;
                if ( tmp1 > tmp0 )
                {
                    float numer = tmp1 - tmp0;
                    float denom = a-2*b+c;
                    s = clamp( numer/denom, 0.f, 1.f );
                    t = 1-s;
                }
                else
                {
                    t = clamp( -e/c, 0.f, 1.f );
                    s = 0.f;
                }
            }
            else if ( t < 0.f )
            {
                if ( a+d > b+e )
                {
                    float numer = c+e-b-d;
                    float denom = a-2*b+c;
                    s = clamp( numer/denom, 0.f, 1.f );
                    t = 1-s;
                }
                else
                {
                    s = clamp( -e/c, 0.f, 1.f );
                    t = 0.f;
                }
            }
            else
            {
                float numer = c+e-b-d;
                float denom = a-2*b+c;
                s = clamp( numer/denom, 0.f, 1.f );
                t = 1.f - s;
            }
        }

        return new Vector3(new Vector3(point1).add(new Vector3(edge0).scl(s).add(new Vector3(edge1).scl(t))));
    }

    public ArrayList<Vector3> getPoints()
    {
        ArrayList<Vector3> points= new ArrayList<Vector3>();
        points.add(point1);
        points.add(point2);
        points.add(point3);

        return points;
    }

    /**
     * Returns closest point on segment to point
     *
     * @param sx1
     *            segment x coord 1
     * @param sy1
     *            segment y coord 1
     * @param sx2
     *            segment x coord 2
     * @param sy2
     *            segment y coord 2
     * @param px
     *            point x coord
     * @param py
     *            point y coord
     * @return closets point on segment to point
     */
    public static Vector2 getClosestPointOnSegment(float sx1, float sy1, float sx2, float sy2, float px, float py)
    {
        float xDelta = sx2 - sx1;
        float yDelta = sy2 - sy1;

        if ((xDelta == 0) && (yDelta == 0))
        {
            throw new IllegalArgumentException("Segment start equals segment end");
        }

        float u = ((px - sx1) * xDelta + (py - sy1) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

        final Vector2 closestPoint;
        if (u < 0)
        {
            closestPoint = new Vector2(sx1, sy1);
        }
        else if (u > 1)
        {
            closestPoint = new Vector2(sx2, sy2);
        }
        else
        {
            closestPoint = new Vector2((int) Math.round(sx1 + u * xDelta), (int) Math.round(sy1 + u * yDelta));
        }

        return closestPoint;
    }


    public Vector3 getPointonSegment ( Vector3 point,  Vector3 P0, Vector3 P1 )
    {
        Vector3 v = new Vector3(P1).sub(P0);
        Vector3 w = new Vector3(point).sub(P0);

        float c1=new Vector3(v).dot(w);
        float c2=new Vector3(v).dot(v);
        if(c1 <= 0 )  // before P0
        {
            return P0;
        }
        if (c2 <= c1) // after P1
        {
            return P1;
        }

        float b = c1 / c2;
        Vector3 Pb = new Vector3(P0.add(v).scl(b));
        return Pb;
    }
}
