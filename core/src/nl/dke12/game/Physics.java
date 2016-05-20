package nl.dke12.game;

import com.badlogic.gdx.math.Vector3;
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
    private Vector3 bounceVector;
    private Triangle plane;
    private SolidObject closest;
    private boolean gravit = false;

    public Physics(ArrayList<SolidObject> obstacles, Ball ball)
    {
        this.ball = ball;
        this.obstacles = obstacles;
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
        System.out.print("stop");
    }

    public boolean collides()
    {
        //float j = how far in the future to predict
        nextPosition = new Vector3(new Vector3(ball.position).add(ball.direction));
        //calculate the closest plane = 3 closest points that are part of the same model/obscacle

        for(int h = 0; h < obstacles.size(); h++)
        {
            //obstacles = list of the solid (ie: with which the ball can interact) objects in the room,
            //each of which contains a list of it's (outer) points
            if(closest!=null)
            {
                Vector3 wallPos = new Vector3(obstacles.get(h).getPosition());
                Vector3 closestPos = new Vector3(closest.getPosition());
                if(new Vector3(new Vector3(wallPos).sub(ball.position)).len()<new Vector3(new Vector3(closestPos).sub(ball.position)).len())
                {
                    plane = null;
                    closest = obstacles.get(h);
                }
            }
            else
            {
                closest = obstacles.get(h);
                plane = null;
            }
        }

        if(closest!=null)
        {
            for(int i=0; i<closest.getPoints().size();i++)
            {
                for(int k=0;k<closest.getPoints().size();k++)
                {
                    for(int l=0;l<closest.getPoints().size();l++)
                    {
                        if(i!=k&&i!=l&&k!=l)
                        {
                            Triangle newPlane = new Triangle(closest.getPoints().get(i),closest.getPoints().get(k),closest.getPoints().get(l));
                            if (plane!=null)
                            {
                                Vector3 planePos= new Vector3(plane.closestPoint(ball.position));
                                Vector3 newPlanePos = new Vector3(newPlane.closestPoint(ball.position));

                                if(new Vector3(planePos).sub(ball.position).len()>new Vector3(newPlanePos).sub(ball.position).len())
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
            //decide whether or not a collision occurs

            Vector3 nextPosPlane = new Vector3(plane.closestPoint(nextPosition));
            Vector3 currPosPlane = new Vector3(plane.closestPoint(ball.position));

            if(new Vector3(nextPosPlane).sub(nextPosition).len()<1)
            {
                //centreline = inward facing normal of collision plane
                Vector3 centreLine = new Vector3(plane.getNormal());
                centreLine.scl(1/centreLine.len());

                //check if normal is outward facing
                /*if(new Vector3(planePos).sub(nextPosition).len()<(new Vector3(planePos).sub(new Vector3(nextPosition).add(cent)).len()))
                {
                    centreLine.scl(-1);
                }*/

                //normalLine = perpendicular to centreLine parallel to the direction
                Vector3 normalLine = new Vector3(new Vector3(currPosPlane).sub(nextPosPlane));
                normalLine.scl(1/normalLine.len());
                //perpComponent = component of direction that is perpendicular to centreLine
                float perpComponent = (new Vector3(ball.direction).dot(centreLine));
                //paraComponent = component of direction that is parallel to centreLine
                Vector3 perpLine = new Vector3(new Vector3(normalLine).scl(perpComponent));
                Vector3 paraLine = new Vector3(new Vector3(ball.direction).add(perpLine));
                //perpLine.scl(-1);

                Vector3 bounce = new Vector3(new Vector3(perpLine).add(paraLine));
                this.bounceVector = bounce.scl(-0.5f);

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
        if(direction.len() > 0.08)
        {
            float friction = 0.97f;
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
                            ball.direction.z = ball.direction.z/0.8f;
                        //todo: possibly put an else here if it's broken
                    }
                }
            }
            else
            {
                direction.z=0;
                updatePosition();
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

    public void updatePosition()
    {
        if (collides())
        {
            if (bounceVector!=null)
            {
                if(bounceVector.z>0.08)
                {
                    gravit = true;
                    ball.direction.set(bounceVector);
                }
                else
                {
                    //we should not be setting the gravity here, but oh well
                    gravit = false;
                    ball.direction.x=bounceVector.x;
                    ball.direction.y=bounceVector.y;

                    ball.direction.z=0;
                }
            }
        }
        else
        {
            ball.position.add(ball.direction);
            updateVelocity(ball.direction);
        }
    }

    public Ball getBall()
    {
        return this.ball;
    }
}
