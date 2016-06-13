package nl.dke12.game;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import java.util.ArrayList;

/**
 * Created by Tom Conneely on 13/05/2016.
 */

public class Physics
{
    private Vector3 nextPosition = new Vector3(0,0,0);
    //height from object beneath it
    private float height = 10;
    private Ball ball;
    private ArrayList<SolidObject> obstacles;
    private ArrayList<Triangle> triangles;
    private ArrayList<Triangle> planes1;
    private ArrayList<Triangle> planes2;
    private ArrayList<Triangle> planes3;
    private ArrayList<Triangle> planes4;
    protected Vector3 bounceVector;
    private Triangle plane;
    private Vector3 collisionPoint;
    private Float prevDist;
    private Vector3 lastPos;
    private SolidObject closest;
    protected boolean gravit;
    private boolean printTriangles;

    public Physics(ArrayList<SolidObject> obstacles, Ball ball)
    {
        this.ball = ball;
        this.obstacles = obstacles;
        this.printTriangles=true;
        planes1=new ArrayList<Triangle>();
        planes2=new ArrayList<Triangle>();
        planes3=new ArrayList<Triangle>();
        planes4=new ArrayList<Triangle>();
        for(int h = 0; h < obstacles.size(); h++)
        {
            if(obstacles.get(h).getPlanes()!=null)
            {
                if(obstacles.get(h).getPosition().x<0&&obstacles.get(h).getPosition().y<0)
                {
                    ArrayList<Triangle> tris = new ArrayList<Triangle>(obstacles.get(h).getPlanes());

                    for(int r=0; r<tris.size();r++)
                    {
                        planes4.add(obstacles.get(h).getPlanes().get(r));
                    }
                }
                if(obstacles.get(h).getPosition().x<0&&obstacles.get(h).getPosition().y>=0)
                {
                    for(int r=0;r<obstacles.get(h).getPlanes().size();r++)
                    {
                        planes4.add(obstacles.get(h).getPlanes().get(r));
                    }
                }
                if(obstacles.get(h).getPosition().x>=0&&obstacles.get(h).getPosition().y<0)
                {
                    for(int r=0;r<obstacles.get(h).getPlanes().size();r++)
                    {
                        planes4.add(obstacles.get(h).getPlanes().get(r));
                    }
                }
                if(obstacles.get(h).getPosition().x>=0&&obstacles.get(h).getPosition().y>=0)
                {
                    for(int r=0;r<obstacles.get(h).getPlanes().size();r++)
                    {
                        planes4.add(obstacles.get(h).getPlanes().get(r));
                    }
                }
            }
        }
        Vector3 a1 = new Vector3(-4,-4,-0.6f);
        Vector3 a2 = new Vector3(-4,4,-0.6f);
        Vector3 a3 = new Vector3(4,4,-0.6f);
        Triangle triA = new Triangle(a1,a2,a3);

        Vector3 b1 = new Vector3(-4,-4,-0.6f);
        Vector3 b2 = new Vector3(4,4,-0.6f);
        Vector3 b3 = new Vector3(4,-4,-0.6f);
        Triangle triB = new Triangle(b1,b2,b3);

        Vector3 c1 = new Vector3(-12,4,-0.6f);
        Vector3 c2 = new Vector3(-4,12,-0.6f);
        Vector3 c3 = new Vector3(-4,4,-0.6f);
        Triangle triC = new Triangle(c1,c2,c3);

        Intersector inte = new Intersector();


        System.out.println("A = "+triA.closestPoint(ball.position));
        System.out.println("B = "+triB.closestPoint(ball.position));
        System.out.println("C = "+triC.closestPoint(ball.position));
    }

    public void push(float xpush, float ypush, float zpush)
    {
        float xvect = ball.direction.x+xpush;
        float yvect = ball.direction.y+ypush;
        float zvect = ball.direction.z+zpush;
        Vector3 dir = new Vector3(xvect/2,yvect/2,zvect/2);
        if (dir.z>0)
        {
            gravit = true;
        }
        ball.direction.set(dir);
    }

    public void stop()
    {
        Vector3 dir = new Vector3(0,0,0);
        ball.direction.set(dir);
        //System.out.print("stop");
    }

    public boolean collides()
    {
        //float j = how far in the future to predict
        nextPosition = new Vector3(new Vector3(ball.position).add(ball.direction));
        //calculate the closest plane = 3 closest points that are part of the same model/obscacle
        prevDist=500f;
        boolean planeFound=false;

        //triangles = obstacles.get(0).getPlanes();
        /*planes1 = obstacles.get(0).planes1;
        planes2 = obstacles.get(0).planes2;
        planes3 = obstacles.get(0).planes3;
        planes4 = obstacles.get(0).planes4;*/
        if(ball.position.x<0&&ball.position.y<0)
        {
            triangles= planes4;
            System.out.println("zone1");
        }
        if(ball.position.x<0&&ball.position.y>=0)
        {
            triangles= planes4;
            System.out.println("zone2");
        }
        if(ball.position.x>=0&&ball.position.y<0)
        {
            triangles= planes4;
            System.out.println("zone3");
        }
        if(ball.position.x>=0&&ball.position.y>=0)
        {
            triangles= planes4;
            System.out.println("zone4");
        }
            if(triangles!=null)
            {
                int counter=0;
                planeFound=false;
                Intersector inter = new Intersector();


                for(int m = 0; m < triangles.size(); m++)
                {
                    if(printTriangles)
                    {
                        //System.out.println("triangle = "+triangles.get(m).getPoints().get(0)+"   "+triangles.get(m).getPoints().get(1)+"   " + triangles.get(m).getPoints().get(2));
                        /*if(m==triangles.size()-1&&h==obstacles.size()-1)
                        {
                            this.printTriangles=false;
                        }*/
                    }
                    if(plane!=null)
                    {
                        //Intersector inp = new Intersector();
                        Triangle newPlane = triangles.get(m);
                        Vector3 planePos= new Vector3(plane.closestPoint(ball.position));
                        Vector3 newPlanePos = new Vector3(newPlane.closestPoint(ball.position));

                        Plane surf = new Plane(newPlane.getPoints().get(0),newPlane.getPoints().get(1),newPlane.getPoints().get(2));
                        Vector3 colliPoint = new Vector3();
                        Vector3 nextPos = new Vector3(ball.position).add(ball.direction);
                        boolean collis = inter.intersectSegmentPlane(ball.position,nextPos,surf,colliPoint);
                        boolean collision = inter.isPointInTriangle(colliPoint,newPlane.getPoints().get(0),newPlane.getPoints().get(1),newPlane.getPoints().get(2));


                        if(surf.testPoint(ball.position) != surf.testPoint(nextPos))
                        {
                            /*float h=surf.distance(ball.position);
                            Vector3 proj = new Vector3(ball.position);
                            proj.sub(new Vector3(surf.getNormal().scl(new Vector3(ball.position).sub(newPlane.getPoints().get(0)).dot(surf.getNormal()))));
                            float theta = new Vector3(proj).sub(ball.position).dot(ball.direction);
                            theta=theta/(new Vector3(proj).sub(ball.position).len()*ball.direction.len());
                            theta=(float)Math.acos((double)theta);
                            float mu = 180-90-theta;
                            float ratio=h/(float)Math.sin(mu);
                            float distance= ratio*(float)Math.sin(90);
                            Vector3 collDist = new Vector3(ball.direction).scl(1/ball.direction.len());
                            collDist.scl(distance);
                            Vector3 collPoint = new Vector3(ball.position).add(collDist);
                            this.collisionPoint = collPoint;*/

                            Vector3 collPoint = new Vector3();

                            //Intersector sect = new Intersector();
                            boolean coll= inter.intersectSegmentPlane(ball.position,nextPosition,surf,collPoint);
                            boolean colliding = inter.isPointInTriangle(collPoint,newPlane.getPoints().get(0),newPlane.getPoints().get(1),newPlane.getPoints().get(2));
                            //this.collisionPoint = collPoint;

                            if(coll&&colliding)
                            {
                                if(prevDist!=null)
                                {
                                    if(new Vector3(collPoint).sub(ball.position).len() < prevDist)
                                    {

                                        plane = newPlane;
                                        counter=0;
                                        planeFound=false;
                                        //prevColliPoint=colliPoint;
                                        this.collisionPoint = collPoint;
                                        prevDist=new Vector3(collisionPoint).sub(ball.position).len();
                                        /*System.out.println("checkplane "+newPlane.getPoints().get(0)+" "+newPlane.getPoints().get(1)+" "+newPlane.getPoints().get(2));
                                        System.out.println("check dist "+new Vector3(newPlanePos).sub(ball.position).len());
                                        System.out.println("plane pos "+planePos);
                                        System.out.println("ew nplane pos "+newPlanePos);
                                        System.out.println("new plane");*/
                                        System.out.println("new"+plane.getPoints().get(0)+" "+plane.getPoints().get(1)+" "+plane.getPoints().get(2));
                                        System.out.println("col"+collisionPoint);
                                    }

                                    else
                                    {
                                        if(counter<triangles.size())
                                        {
                                            counter+=1;
                                        }
                                        else
                                        {
                                            planeFound=true;
                                        }
                                        System.out.println("Old Dist"+prevDist);
                                        System.out.println("Further Dist"+new Vector3(collPoint).sub(ball.position).len());
                                    }
                                /*}
                                else
                                {
                                    plane = newPlane;
                                    counter=0;
                                    planeFound=false;
                                    prevColliPoint=colliPoint;
                                }*/

                                }
                                else
                                {
                                    plane = newPlane;
                                    prevDist=new Vector3(plane.closestPoint(ball.position)).sub(ball.position).len();
                                }
                            }
                        }
                        else
                        {
                            /*if(new Vector3(planePos).sub(ball.position).len()>new Vector3(newPlanePos).sub(ball.position).len())
                            {
                                plane = newPlane;
                                counter=0;
                            }
                            if (collis==true)
                            {
                                System.out.println("pos = "+ball.position);
                                System.out.println("dir = "+ball.direction);
                                System.out.println("plane = "+newPlane.getPoints().get(0)+newPlane.getPoints().get(1)+newPlane.getPoints().get(2));
                            }*/
                        }
                    }
                    if (plane==null)
                    {
                        plane = triangles.get(m);
                    }
                }
            }
            if(printTriangles)
            {
                //System.out.println("\n");
            }




        //set collisionPoint to the point of intersection with the direction & the closest plane
        //collisionPoint = position.add(plane.getNormal().scl(plane.distance(nextPosition)));
        //(don't forget to account for gravity and friction)
        if (plane!=null)
        {
            //decide whether or not a collision occurs
            nextPosition = new Vector3(new Vector3(ball.position).add(ball.direction));

            Vector3 nextPosPlane = new Vector3(plane.closestPoint(nextPosition));
            Vector3 currPosPlane = new Vector3(plane.closestPoint(ball.position));


            //todo: ADD MORE STUFF
            // it does not detect a collision with vertically sloped planes

            /*
            Vector3 nom = new Vector3(plane.getNormal());
            Vector3 point = new Vector3(nextPosition);
            point.sub(plane.closestPoint(nextPosition));
            float inFront = nom.dot(point);
            */
            Intersector intersector=new Intersector();

            Ray ray = new Ray(ball.position,ball.direction);
            Vector3 intersect = new Vector3();
            boolean hit=intersector.intersectRayTriangle(ray,plane.getPoints().get(0),plane.getPoints().get(1),plane.getPoints().get(2),intersect);
            /*System.out.println("intersect "+intersect);
            System.out.println("nextPosit "+nextPosition);*/
            //if(new Vector3(nextPosPlane).sub(nextPosition).len()<=1)


            Vector3 colliPoint = new Vector3();
            Plane surface= new Plane(plane.getPoints().get(0),plane.getPoints().get(1),plane.getPoints().get(2));
            boolean collis=intersector.intersectSegmentPlane(ball.position,nextPosition,surface,colliPoint);
            boolean colliding = intersector.isPointInTriangle(colliPoint,plane.getPoints().get(0),plane.getPoints().get(1),plane.getPoints().get(2));

            if(!(collis&&colliding))
            {
                System.out.println("no hit "+plane.getPoints().get(0)+" "+plane.getPoints().get(1)+" "+plane.getPoints().get(2));
                System.out.println("direct "+ball.direction);
                System.out.println("positi "+ball.position);
                System.out.println("collis"+collis);
                System.out.println("colliding"+colliding);
                System.out.println("collipoint"+colliPoint);
            }
            if(collis&&colliding)
            {
                /*System.out.println("hit "+plane.getPoints().get(0)+" "+plane.getPoints().get(1)+" "+plane.getPoints().get(2));
                System.out.println("direct "+ball.direction);
                System.out.println("positi "+ball.position);*/
            }

            if(ball.position.z<-0.1)
            {

                System.out.println("LOOK HERE");
            }

            //if(new Vector3(intersect).sub(ball.position).len()<new Vector3(nextPosition).sub(ball.position).len()&&hit)
            //
             if(collis&&colliding)
            //if(new Vector3(intersect).sub(ball.position).len()<new Vector3(nextPosition).sub(ball.position).len())
            {
                System.out.println("intersect "+new Vector3(intersect).sub(ball.position).len());
                System.out.println("nextPosition "+new Vector3(nextPosition).sub(ball.position).len());
                //if(new Vector3(intersect).sub(ball.position).len()!=0)
                {
                    //centreline = inward facing normal of collision plane
                    //Vector3 centreLine = new Vector3(plane.getNormal());
                    Vector3 centreLine = new Vector3(currPosPlane).sub(ball.position);
                    centreLine.scl(1/centreLine.len());

                    //check if normal is outward facing
                    /*if(new Vector3(planePos).sub(nextPosition).len()<(new Vector3(planePos).sub(new Vector3(nextPosition).add(cent)).len()))
                    {
                        centreLine.scl(-1);
                    }*/

                    //normalLine = perpendicular to centreLine parallel to the direction
                    Vector3 normalLine = new Vector3(new Vector3(nextPosPlane).sub(currPosPlane));
                    normalLine.scl(1/normalLine.len());
                    //perpComponent = component of direction that is perpendicular to centreLine
                    float perpComponent = (new Vector3(ball.direction).dot(centreLine));
                    //System.out.println("comp"+perpComponent);
                    //paraComponent = component of direction that is parallel to centreLine
                    Vector3 perpLine = new Vector3(new Vector3(centreLine).scl(-perpComponent));

                    Plane surf = new Plane(plane.getPoints().get(0),plane.getPoints().get(1),plane.getPoints().get(2));

                    //perpLine.scl(-1);
                    Vector3 paraLine = new Vector3(new Vector3(ball.direction).sub(perpLine));
                    Vector3 proj = new Vector3(ball.position);
                    proj.sub(new Vector3(surf.getNormal().scl(new Vector3(ball.position).sub(plane.getPoints().get(0)).dot(surf.getNormal()))));
                    Vector3 nextProj = new Vector3(nextPosition);
                    nextProj.sub(new Vector3(surf.getNormal().scl(new Vector3(nextPosition).sub(plane.getPoints().get(0)).dot(surf.getNormal()))));
                    nextProj.sub(proj);

                    //System.out.println("perp"+perpLine);
                    Vector3 bounce = new Vector3(new Vector3(perpLine).add(nextProj));
                    //Vector3 bounce = new Vector3(new Vector3(perpLine).add(normalLine));
                    this.bounceVector = bounce.scl(0.8f);
                    //this.bounceVector.set(0,0,0);
                    /*System.out.println("dir "+ball.direction);
                    System.out.println("pos "+ball.position);
                    System.out.println("closePoint "+plane.closestPoint(ball.position));
                    System.out.println("bounce "+bounceVector);
                    System.out.println(plane.getPoints().get(0)+" "+plane.getPoints().get(1)+" "+plane.getPoints().get(2));*/

                    //this.bounceVector = bounce;
                    //FUUUUUCCKK todo: go over physics again, because it's not bouncing off the wrong plane (in other words, calculations are still fucked somehow)
                    //System.out.println("plane"+plane.getPoints().get(0)+plane.getPoints().get(1)+plane.getPoints().get(2));
                    //if(closest.getType().equals("slope"))
                    {
                        System.out.println("bounce"+this.bounceVector);
                        System.out.println("planeNormal"+perpLine);
                        System.out.println("planeDirection"+nextProj);
                        System.out.println("plane"+plane.getPoints().get(0)+plane.getPoints().get(1)+plane.getPoints().get(2));
                    }
                    //System.out.println("updir"+bounceVector);

                    if (lastPos!=null)
                    {
                        if(ball.position.dst(lastPos)<0.05)
                        {
                            //System.out.println("Ball has STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP");
                        }
                        lastPos= new Vector3(ball.position);
                    }
                    else
                    {
                        lastPos= new Vector3(ball.position);
                    }

                    return true;
                }
            }
            else
            {
                //gravit = true;
                //System.out.println("plane"+plane.getPoints().get(0)+plane.getPoints().get(1)+plane.getPoints().get(2));
            }
            return false;
        }
        return false;
    }

    public void updateVelocity(Vector3 direction)
    {
        if(direction.len() > 0.08)
        {
            float friction = 0.98f;
            ball.direction.x = ball.direction.x*friction;
            ball.direction.y = ball.direction.y*friction;
            if (gravit)
            {
                if (direction.z > 0.08f)
                {
                    //todo: check whether the /1.2f is correct and this breaks everything
                    ball.direction.z = ball.direction.z * 0.8f;
                }
                else
                {
                    if (direction.z > 0)
                    {
                        direction.z = -direction.z;
                    }
                    else
                    {
                        if (direction.z > -0.9)
                        {
                            ball.direction.z = ball.direction.z/0.8f;
                        }
                            //ball.direction.z = ball.direction.z/0.8f;
                        //todo: possibly put an else here if it's broken
                    }
                }
            }
            else
            {
                direction.z=0;
                //todo: set gravit to true???
            }
        }
        else
        {
            if (direction.x < 0.081 && direction.x > -0.081)
            {
                direction.x = 0;
            }
            if (direction.y < 0.081 && direction.y > -0.081)
            {
                direction.y = 0;
            }
            if (direction.z <= 0.081 && direction.z >= -0.081)
            {
                direction.z = 0;
            }
            //todo: or ball.stop();
        }
    }

    public Ball getBall()
    {
        return this.ball;
    }
}