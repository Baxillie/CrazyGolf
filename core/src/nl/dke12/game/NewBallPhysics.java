package nl.dke12.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import java.util.ArrayList;

/**
 * Created by Tom Conneely on 13/05/2016.
 */

public class NewBallPhysics {

    public Vector3 position;
    public Vector3 direction;
    public ArrayList<Vector3> instances = new ArrayList<Vector3>();
    public BoundingBox box;
    public ModelInstance object;
    public Vector3 nextPosition = new Vector3(0,0,0);
    public float radius;
    public int index;
    public boolean zcollide;
    public boolean fall;
    //height from object beneath it
    public float height=10;
    public Ball ball;
    public ArrayList<SolidObject> obstacles;
    public boolean gravity=true;
    private Vector3 bounceVector;
    private Triangle plane;
    public SolidObject closest;
    private boolean gravit = false;

    public NewBallPhysics(ArrayList<SolidObject> obstacles, Ball ball)
    {
        this.obstacles=obstacles;
        position = new Vector3();
        direction = new Vector3();
        position.set(ball.position);
        direction.set(ball.direction);
        this.radius=ball.radius;
    }



    public void push(float xpush, float ypush, float zpush)
    {
        //System.out.println(position);
        float xvect=direction.x+xpush;
        float yvect=direction.y+ypush;
        float zvect=direction.z+zpush;
        Vector3 dir = new Vector3(xvect/2,yvect/2,zvect/2);
        if (dir.z>0)
        {
            gravity = true;

        }
        direction.set(dir);
        //System.out.print( direction.x+ direction.y+ direction.z+"\n");
        //System.out.print( xpush+ ypush+  zpush+"\n");
    }

    public void stop()
    {
        Vector3 dir = new Vector3(0,0,0);
        direction.set(dir);
        System.out.print("stop");
    }

    public boolean collides(float j)
    {
        //float j = how far in the future to predict
        nextPosition = new Vector3(new Vector3(position).add(direction));
        Vector3 collisionPoint = new Vector3();
        //calculate the closest plane = 3 closest points that are part of the same model/obscacle

        //for(SolidObject wall : obstacles)
        for(int h=0;h<obstacles.size();h++)
        {
            //obstacles = list of the solid (ie: with which the ball can interact) objects in the room,
            //each of which contains a list of it's (outer) points

            //System.out.println("h="+h);

            if(closest!=null)
            {
                Vector3 waill = new Vector3(obstacles.get(h).getPosition());
                Vector3 cloisest = new Vector3(closest.getPosition());
                if(new Vector3(new Vector3(waill).sub(this.position)).len()<new Vector3(new Vector3(cloisest).sub(this.position)).len())
                //if(wall.position.
                {

                    //System.out.println("new object"+closest.getPosition());
                    plane = null;
                    /*System.out.println("near"+obstacles.get(h).position);
                    System.out.println("far"+closest.position);
                    System.out.println("pos"+position);*/
                    closest = obstacles.get(h);
                    //System.out.println("new object"+closest.getPosition());
                }
                else
                {

                }
            }
            else
            {
                closest = obstacles.get(h);
                plane = null;
            }
            /*System.out.println("close"+closest.position);
            System.out.println("ball"+this.position);*/
        }


            /*
            Vector3 point1 = new Vector3(wall.points.get(i));
            plane = new Plane(close,closer,closest);
            if (plane!=null)
            {
                if((new Plane(close,closer,closest).distance(position)<plane.distance(position)))
                {
                    plane= new Plane(close,closer,closest);
                }
            }
            else
            {
                plane= new Plane(close,closer,closest);
            }*/
        if(closest!=null)
        {
            for(int i=0; i<closest.getPoints().size();i++)
            {
                for(int k=0;k<closest.getPoints().size();k++)
                {
                    for(int l=0;l<closest.getPoints().size();l++)
                    {
                        //System.out.println("i="+i+" k="+k+" l="+l);
                        if(i!=k&&i!=l&&k!=l)
                        {
                            Triangle newPlane = new Triangle(closest.getPoints().get(i),closest.getPoints().get(k),closest.getPoints().get(l));

                            if (plane!=null)
                            {
                                Vector3 planeInt= new Vector3(plane.getIntersection(position));
                                Vector3 newPlaneInt = new Vector3(newPlane.getIntersection(position));

                                Vector3 planePos= new Vector3(plane.closestPoint(position));
                                Vector3 newPlanePos = new Vector3(newPlane.closestPoint(position));

                                /*System.out.println("check face"+
                                        closest.getPoints().get(i)+" "+closest.getPoints().get(k)+" "+closest.getPoints().get(l));*/

                                /*if(newPlane.getDistance(position)<plane.getDistance(position)&&
                                plane.testIntersection(plane.getIntersection(position))&&
                                (new Vector3(newPlaneInt.sub(this.position)).len()<new Vector3(planeInt.sub(this.position)).len()))*/
                                if(new Vector3(planePos).sub(position).len()>new Vector3(newPlanePos).sub(position).len())
                                {
                                    plane = new Triangle(closest.getPoints().get(i),closest.getPoints().get(k),closest.getPoints().get(l));

                                }
                            }
                            else
                            {
                                plane= new Triangle(closest.getPoints().get(i),closest.getPoints().get(k),closest.getPoints().get(l));
                            }
                        }
                    }
                }
            }
        }

        //set collisionPoint to the point of intersection with the direction & the closest plane
        //collisionPoint = position.add(plane.getNormal().scl(plane.distance(nextPosition)));
        //(don't forget to account for gravity and friction)
        if (plane!=null)
        {
            //if (plane.getNormal().dot(nextPosition)>0&&nextPosition!=null)
            Vector3 posPlane = new Vector3(plane.closestPoint(position));
            //posPlane.scl(plane.getDistance(position));
            //System.out.println(posPlane);
            Vector3 nextposPlane = new Vector3(plane.closestPoint(nextPosition));
            //nextposPlane.scl(plane.getDistance(nextPosition));
            //decide whether or not a collision occurs

            /*System.out.println("dist"+plane.getDistance(position));
            System.out.println("pos"+closest.position);*/
            Vector3 newp = new Vector3(plane.getIntersection(nextPosition));
            Vector3 popo = new Vector3(position);
            Vector3 popo1 = new Vector3(position);

            Vector3 planePos= new Vector3(plane.closestPoint(nextPosition));
            Vector3 planePosNow= new Vector3(plane.closestPoint(position));
            /*System.out.println("distance = "+new Vector3(closest.position).sub(position).len());
            System.out.println("closest = "+closest.position+" position = "+position);*/

            /*if((plane.getDistance(nextPosition)<2)
                && new Vector3(popo).sub(newp).len()>new Vector3(popo1).sub(nextPosition).len()
                && (plane.testIntersection(plane.getIntersection(nextPosition))))*/
            /*if (new Vector3(closest.position).sub(nextPosition).len()<3.9)&&
                (plane.getDistance(nextPosition)<1)&&
                (plane.testIntersection(plane.getIntersection(position))))*/

            ///////////////////////////Wat////////////////////////////////////////////
            if(new Vector3(planePos).sub(nextPosition).len()<1)

            {

                //centreline = inward facing normal of collision plane
                Vector3 centreLine = new Vector3(plane.getNormal());
                centreLine.scl(1/centreLine.len());
                //check if normal is outward facing
                Vector3 cent = new Vector3(centreLine);
                /*if(new Vector3(planePos).sub(nextPosition).len()<(new Vector3(planePos).sub(new Vector3(nextPosition).add(cent)).len()))
                {
                    centreLine.scl(-1);
                }*/


                //normalLine = perpendicular to centreLine parallel to the direction
                Vector3 normalLine = new Vector3(new Vector3(planePosNow).sub(planePos));
                normalLine.scl(1/normalLine.len());
                //perpComponent = component of direction that is perpendicular to centreLine
                float perpComponent = (new Vector3(direction).dot(centreLine));
                //paraComponent = component of direction that is parallel to centreLine
                Vector3 perpLine = new Vector3(new Vector3(normalLine).scl(perpComponent));
                Vector3 paraLine = new Vector3(new Vector3(direction).add(perpLine));
                //perpLine.scl(-1);


                Vector3 bounce = new Vector3(new Vector3(perpLine).add(paraLine));
                this.bounceVector = bounce.scl(-0.5f);
                System.out.println("bounce"+bounceVector);
                System.out.println("plane"+plane.getPoints());
                System.out.println("direction"+direction);

                //stop();
                return true;
            }
            else
            {
                gravit = true;
            }
            return false;
        }
        return false;
    }

    public void updateVelocity(Vector3 direction)
    {
        if(direction.len()>0.08)
        {
            float friction = 0.97f;
            this.direction.x=this.direction.x*friction;
            this.direction.y=this.direction.y*friction;
            if (gravit)
            {
                if (direction.z>0.08f)
                {
                    this.direction.z=this.direction.z/1.2f;
                }
                else
                {
                    if (direction.z>0)
                    {
                        direction.z=-direction.z;
                    }
                    else
                    {
                        if (direction.z>-1.5)
                            this.direction.z=this.direction.z*1.2f;
                    }

                }
            }
            else
            {
                direction.z=0;
                updatePosition();
                if (height>1||direction.z!=0)//||(new Vector3(closest.getPosition()).sub(position).len()>3.9))
                {
                    gravity = true;
                }
            }
        }
        else
        {

            if (direction.x<0.081 && direction.x>-0.081)
            {
                direction.x = 0;
            }
            if (direction.y<0.081 && direction.y>-0.081)
            {
                direction.y=0;
            }
            if (direction.z<=0.081 && direction.z>=-0.081)
            {
                if (height<=1)
                {
                    //direction.z=-0.01f;
                    gravity = false;
                    System.out.println("pork");
                }
                if (height>1||direction.z!=0)
                {
                    gravity = true;
                }
            }


        }
    }

    public void updatePosition()
    {
        float dist = 1f;
        float shoot = 0f;

        //if (direction.len()>0)
        //{

        if (collides(dist))
        {
            boolean bloop=false;
            if (bounceVector!=null)
            {
                if(bounceVector.z>0.08)
                {
                    gravit = true;
                    direction.set(bounceVector);
                }
                else
                {
                    //we should not be setting the gravity here, but oh well
                    gravit = false;
                    direction.x=bounceVector.x;
                    direction.y=bounceVector.y;

                    direction.z=0;
                }
            }
        }
        else
        {

            this.position.add(this.direction);
            updateVelocity(this.direction);
        }
    }

    public void update()
    {
        ball.position=this.position;
        ball.direction=this.direction;
        //gameWorld.advance(this.ball);
    }
}
