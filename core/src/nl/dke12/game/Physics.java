package nl.dke12.game;

import com.badlogic.gdx.Game;
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
    private Vector3 otherBall;
    protected Vector3 wind;
    private GameWorld gameWorld;
    private Triangle closestPlane;
    protected boolean noise;
    protected boolean gravit;
    private boolean printTriangles;

    public Physics(ArrayList<SolidObject> obstacles, Ball ball,GameWorld gameWorld)
    {
        this.ball = ball;
        this.obstacles = obstacles;
        this.printTriangles=true;
        planes1=new ArrayList<Triangle>();
        planes2=new ArrayList<Triangle>();
        planes3=new ArrayList<Triangle>();
        planes4=new ArrayList<Triangle>();
        this.gameWorld=gameWorld;
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
        float m = (float)Math.random()*0.03f;
        float n = (float)Math.random()*0.03f;
        float p = (float)Math.random()*0.03f;
        m-=0.015;
        n-=0.015;
        p-=0.015;

        Vector3 misclick = new Vector3(m,n,p);

        if(this.noise)
        {
            dir.add(misclick);
        }

            gravit = true;

        ball.direction.set(dir);
    }

    public void stop()
    {
        Vector3 dir = new Vector3(0,0,0);
        ball.direction.set(dir);
    }

    public boolean collides()
    {
        setGravit();

        //float j = how far in the future to predict
        nextPosition = new Vector3(new Vector3(ball.position).add(ball.direction));
        //calculate the closest plane = 3 closest points that are part of the same model/obscacle
        prevDist=500f;
        boolean planeFound=false;

        for(int h=0;h<obstacles.size();h++)
        {
            if((obstacles.get(h).getType()=="solidBall"&&ball.type=="ball2")||
               (obstacles.get(h).getType()=="solidBall2"&&ball.type=="ball1"))
            {
                otherBall=obstacles.get(h).getPosition();
                System.out.println(obstacles.get(h).getType()+" = "+otherBall);
            }
        }
        if (otherBall!=null)
        {
            if(new Vector3(nextPosition).sub(otherBall).len()<0.4)
            {

                Vector3 centreLine = new Vector3(otherBall).sub(ball.position);
                centreLine.scl(1/centreLine.len());

                //check if normal is outward facing
                    /*if(new Vector3(planePos).sub(nextPosition).len()<(new Vector3(planePos).sub(new Vector3(nextPosition).add(cent)).len()))
                    {
                        centreLine.scl(-1);
                    }*/

                //normalLine = perpendicular to centreLine parallel to the direction
                Vector3 normalLine = new Vector3();
                //perpComponent = component of direction that is perpendicular to centreLine
                float perpComponent = (new Vector3(ball.direction).dot(centreLine));

                //paraComponent = component of direction that is parallel to centreLine
                Vector3 perpLine = new Vector3(new Vector3(centreLine).scl(perpComponent));
                normalLine=new Vector3(ball.direction).sub(perpLine);
                perpLine.scl(-1);



                Vector3 oppositeBounce = new Vector3(new Vector3(perpLine).add(normalLine));
                oppositeBounce.z=-oppositeBounce.z;

                if(ball.type=="ball1")
                {
                    gameWorld.pushBall("ball2",oppositeBounce.scl(-1));
                }
                if(ball.type=="ball2")
                {
                    gameWorld.pushBall("ball1",oppositeBounce.scl(-1));
                }

                /*if(plane.getPoints().get(0).z==plane.getPoints().get(1).z&&
                        plane.getPoints().get(0).z==plane.getPoints().get(2).z)
                {
                    if (perpLine.len()>0.05)
                    {
                        perpLine.scl(0.5f);
                    }
                    else
                    {
                        //perpLine.z=0;
                        setGravit();
                    }
                }
                else
                {
                    if (perpLine.len()>0.05)
                    {
                        perpLine.scl(0.6f);
                    }

                }*/




                Vector3 bounce = new Vector3(new Vector3(perpLine).add(normalLine));
                //Vector3 bounce = new Vector3(new Vector3(perpLine).add(normalLine));
                /*if(plane.getPoints().get(0).z==plane.getPoints().get(1).z&&
                        plane.getPoints().get(0).z==plane.getPoints().get(2).z)
                {*/
                    this.bounceVector = bounce.scl(0.8f);
                    return true;
                /*}
                else
                {
                    this.bounceVector = bounce;//.scl(0.8f);
                }*/
            }
        }

        //triangles = obstacles.get(0).getPlanes();
        /*planes1 = obstacles.get(0).planes1;
        planes2 = obstacles.get(0).planes2;
        planes3 = obstacles.get(0).planes3;
        planes4 = obstacles.get(0).planes4;*/
        if(ball.position.x<0&&ball.position.y<0)
        {
            triangles= planes4;

        }
        if(ball.position.x<0&&ball.position.y>=0)
        {
            triangles= planes4;

        }
        if(ball.position.x>=0&&ball.position.y<0)
        {
            triangles= planes4;

        }
        if(ball.position.x>=0&&ball.position.y>=0)
        {
            triangles= planes4;

        }
            if(triangles!=null)
            {
                int counter=0;
                planeFound=false;
                Intersector inter = new Intersector();


                for(int m = 0; m < triangles.size(); m++)
                {
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

                                    }

                                    if(new Vector3(collPoint).sub(ball.position).len() == prevDist)
                                    {
                                        if (newPlane.closestPoint(ball.position).sub(ball.position).len()<plane.closestPoint(ball.position).sub(ball.position).len())
                                        {
                                            plane = newPlane;
                                            counter=0;
                                            this.collisionPoint = collPoint;

                                            prevDist=new Vector3(collisionPoint).sub(ball.position).len();
                                        }
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

                                    }

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

                            }*/
                        }
                    }
                    if (plane==null)
                    {
                        plane = triangles.get(m);
                    }
                }
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


            Vector3 colliPoint = new Vector3();
            Plane surface= new Plane(plane.getPoints().get(0),plane.getPoints().get(1),plane.getPoints().get(2));
            boolean collis=intersector.intersectSegmentPlane(ball.position,nextPosition,surface,colliPoint);
            boolean colliding = intersector.isPointInTriangle(colliPoint,plane.getPoints().get(0),plane.getPoints().get(1),plane.getPoints().get(2));


             if(collis&&colliding)
            //if(new Vector3(intersect).sub(ball.position).len()<new Vector3(nextPosition).sub(ball.position).len())
            {
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

                    //paraComponent = component of direction that is parallel to centreLine
                    Vector3 perpLine = new Vector3(new Vector3(centreLine).scl(-perpComponent));

                    if(plane.getPoints().get(0).z==plane.getPoints().get(1).z&&
                            plane.getPoints().get(0).z==plane.getPoints().get(2).z)
                    {
                        if (perpLine.len()>0.05)
                        {
                            perpLine.scl(0.5f);
                        }
                        else
                        {
                            //perpLine.z=0;
                            setGravit();
                        }
                    }
                    else
                    {
                        if (perpLine.len()>0.05)
                        {
                            perpLine.scl(0.6f);
                        }

                    }

                    Plane surf = new Plane(plane.getPoints().get(0),plane.getPoints().get(1),plane.getPoints().get(2));

                    //perpLine.scl(-1);
                    Vector3 paraLine = new Vector3(new Vector3(ball.direction).sub(perpLine));
                    Vector3 proj = new Vector3(ball.position);
                    proj.sub(new Vector3(surf.getNormal()).scl(new Vector3(ball.position).sub(plane.getPoints().get(0)).dot(surf.getNormal())));
                    Vector3 nextProj = new Vector3(nextPosition);
                    //Vector3 surfNorm = new Vector3(surf.getNormal()).scl(-1);
                    nextProj.sub(new Vector3(surf.getNormal()).scl(new Vector3(nextPosition).sub(plane.getPoints().get(0)).dot(surf.getNormal())));
                    nextProj.sub(proj);


                    Vector3 bounce = new Vector3(new Vector3(perpLine).add(nextProj));
                    //Vector3 bounce = new Vector3(new Vector3(perpLine).add(normalLine));
                    if(plane.getPoints().get(0).z==plane.getPoints().get(1).z&&
                            plane.getPoints().get(0).z==plane.getPoints().get(2).z)
                    {
                        this.bounceVector = bounce;//.scl(0.8f);
                    }
                    else
                    {
                        this.bounceVector = bounce;//.scl(0.8f);
                    }

                    if (lastPos!=null)
                    {
                        if(ball.position.dst(lastPos)<0.05)
                        {

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

            }
            return false;
        }
        return false;
    }

    public void updateVelocity(Vector3 direction)
    {

        if(triangles!=null)
        {

            for(int m = 0; m < triangles.size(); m++)
            {
                Triangle newTriangle= new Triangle(triangles.get(m).getPoints().get(0),triangles.get(m).getPoints().get(1),triangles.get(m).getPoints().get(2));
                if(closestPlane!=null)
                {
                    if(newTriangle.closestPoint(ball.position).sub(ball.position).len()<closestPlane.closestPoint(ball.position).sub(ball.position).len())
                    {
                        closestPlane = newTriangle;
                    }
                }
                else
                {
                    closestPlane = newTriangle;
                }
            }
        }

        if(closestPlane!=null)
        {
            Vector3 friction = new Vector3(direction);
            friction.scl(1/direction.len());
            if(new Vector3(closestPlane.closestPoint(ball.position)).sub(ball.position).len()<1)
            {
                float noise=(float)Math.random()*0.01f;
                if (this.noise)
                {
                    friction.scl(noise);
                }
                if(closestPlane.getPoints().get(0).z==closestPlane.getPoints().get(1).z&&
                        closestPlane.getPoints().get(0).z==closestPlane.getPoints().get(2).z)
                {
                    if(ball.direction.x!=0)
                    {
                        ball.direction.x = ball.direction.x-friction.x;

                    }
                    if(ball.direction.y!=0)
                    {
                        ball.direction.y = ball.direction.y-friction.y;

                    }
                }
            }
            else
            {
                friction.scl(0.00001f);
                if(ball.direction.x>0||ball.direction.x<0)
                {
                    ball.direction.x = ball.direction.x-friction.x;
                }
                if(ball.direction.y>0||ball.direction.y<0)
                {
                    ball.direction.y = ball.direction.y-friction.y;
                }
            }
        }
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

                /*if (direction.z <= 0.01 && direction.z >= -0.01)
                {
                    direction.z = 0;
                }*/
                direction.z=0;
                //todo: set gravit to true???
            }
        if (direction.x < 0.01 && direction.x > -0.01)
        {
            direction.x = 0;
        }
        if (direction.y < 0.01 && direction.y > -0.01)
        {
            direction.y = 0;
        }

        if (wind != null)
        {
            if(closestPlane!=null)
            {
                float scale = (float)Math.random()*0.007f;
                Vector3 newWind = new Vector3(wind).scl(scale);
                if (this.noise=true)
                {
                    ball.direction.add(newWind);
                    System.out.println("Wind"+ scale);
                }
            }
        }
    }
        //else
       // {

            //todo: or ball.stop();
       // }


    public void setGravit()
    {
        if(plane!=null)
        {
            if(new Vector3(plane.closestPoint(ball.position)).sub(ball.position).len()<0.85)
            {
                if(plane.getPoints().get(0).z==plane.getPoints().get(1).z&&
                plane.getPoints().get(0).z==plane.getPoints().get(2).z)
                {
                    if (ball.direction.z <= 0.05 && ball.direction.z >= -0.05)
                    {
                        gravit=false;
                    }
                    else
                    {
                        gravit=true;
                    }
                }
                else
                {
                    gravit=true;
                }
            }
            else
            {
                gravit=true;
            }

        }
    }

    public void addSolidObject(SolidObject solidObject){
        obstacles.add(solidObject);
    }

    public void updateSolidObject(SolidObject solidObject){
        for(int h = 0; h < obstacles.size(); h++){

            if(obstacles.get(h).getType().equals(solidObject.getType())||obstacles.get(h).getType().equals(solidObject.getType())){
                if(obstacles.get(h).getPosition()!=solidObject.getPosition())
                {
                    obstacles.remove(h);
                    obstacles.add(solidObject);
                }
            }
        }
    }

    public Ball getBall()
    {
        return this.ball;
    }


}