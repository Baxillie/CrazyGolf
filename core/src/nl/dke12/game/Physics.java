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

    public Physics(ArrayList<SolidObject> obstacles, Ball ball,GameWorld gameWorld)
    {
        this.ball = ball;
        this.obstacles = obstacles;
        this.gameWorld = gameWorld;

        planes1 = new ArrayList<Triangle>();
        planes2 = new ArrayList<Triangle>();
        planes3 = new ArrayList<Triangle>();
        planes4 = new ArrayList<Triangle>();

        for(int h = 0; h < obstacles.size(); h++)
        {
            if(obstacles.get(h).getPlanes()!=null)
            {
                if(obstacles.get(h).getPosition().x < 0 && obstacles.get(h).getPosition().y < 0)
                {
                    ArrayList<Triangle> tris = new ArrayList<Triangle>(obstacles.get(h).getPlanes());

                    for(int r=0; r<tris.size(); r++)
                    {
                        planes4.add(obstacles.get(h).getPlanes().get(r));
                    }
                }
                if(obstacles.get(h).getPosition().x < 0 && obstacles.get(h).getPosition().y >= 0)
                {
                    for(int r=0;r<obstacles.get(h).getPlanes().size();r++)
                    {
                        planes4.add(obstacles.get(h).getPlanes().get(r));
                    }
                }
                if(obstacles.get(h).getPosition().x >= 0 && obstacles.get(h).getPosition().y < 0)
                {
                    for(int r=0;r<obstacles.get(h).getPlanes().size();r++)
                    {
                        planes4.add(obstacles.get(h).getPlanes().get(r));
                    }
                }
                if(obstacles.get(h).getPosition().x >=0 &&obstacles.get(h).getPosition().y >= 0)
                {
                    for(int r=0;r<obstacles.get(h).getPlanes().size();r++)
                    {
                        planes4.add(obstacles.get(h).getPlanes().get(r));
                    }
                }
            }
        }
    }

    public void push(float xpush, float ypush, float zpush)
    {
        float xvect = ball.direction.x + xpush;
        float yvect = ball.direction.y + ypush;
        float zvect = ball.direction.z + zpush;

        Vector3 dir = new Vector3(xvect/2, yvect/2, zvect/2);

        float m = (float) Math.random() * 0.03f;
        float n = (float) Math.random() * 0.03f;
        float p = (float) Math.random() * 0.03f;

        m -= 0.015;
        n -= 0.015;
        p -= 0.015;

        Vector3 misclick = new Vector3(m,n,p);

        if(this.noise)
        {
            //dir.add(misclick);
        }
        gravit = true;

        ball.direction.set(dir);
    }

    public void push(Vector3 pushVector)
    {
        push(pushVector.x, pushVector.y, pushVector.z);
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
        prevDist = 500f;
        boolean planeFound = false;

        for(int h = 0; h<obstacles.size(); h++)
        {
            if((obstacles.get(h).getType().equals("solidBall") && ball.type.equals("ball2"))||
               (obstacles.get(h).getType().equals("solidBall2") && ball.type.equals("ball1")))
            {
                otherBall = obstacles.get(h).getPosition();
                //System.out.println(obstacles.get(h).getType() + " = " + otherBall);
            }
        }
        if (otherBall != null)
        {
            if(new Vector3(nextPosition).sub(otherBall).len()<0.4)
            {
                Vector3 centreLine = new Vector3(otherBall).sub(ball.position);
                centreLine.scl(1/centreLine.len());

                float perpComponent = (new Vector3(ball.direction).dot(centreLine));
                Vector3 perpLine = new Vector3(new Vector3(centreLine).scl(perpComponent));
                Vector3 normalLine = new Vector3(ball.direction).sub(perpLine);
                perpLine.scl(-1);

                Vector3 oppositeBounce = new Vector3(new Vector3(perpLine).add(normalLine));
                oppositeBounce.z = -oppositeBounce.z;

                if(ball.type.equals("ball1"))
                {
                    gameWorld.pushBall("ball2",oppositeBounce.scl(-1));
                }
                if(ball.type.equals("ball2"))
                {
                    gameWorld.pushBall("ball1",oppositeBounce.scl(-1));
                }

                Vector3 bounce = new Vector3(new Vector3(perpLine).add(normalLine));
                this.bounceVector = bounce.scl(0.8f);
                return true;
            }
        }

        if(ball.position.x < 0 && ball.position.y < 0)
        {
            triangles = planes4;
        }

        if(ball.position.x < 0 && ball.position.y >= 0)
        {
            triangles = planes4;
        }

        if(ball.position.x >= 0 && ball.position.y < 0)
        {
            triangles = planes4;
        }

        if(ball.position.x >= 0 && ball.position.y >= 0)
        {
            triangles = planes4;
        }
            if(triangles != null)
            {
                int counter = 0;
                planeFound = false;
                Intersector inter = new Intersector();

                for(int m = 0; m < triangles.size(); m++)
                {
                    if(plane != null)
                    {
                        Triangle newPlane = triangles.get(m);
                        Plane surf = new Plane(newPlane.getPoints().get(0), newPlane.getPoints().get(1), newPlane.getPoints().get(2));
                        Vector3 nextPos = new Vector3(ball.position).add(ball.direction);

                        if(surf.testPoint(ball.position) != surf.testPoint(nextPos))
                        {
                            Vector3 collPoint = new Vector3();

                            boolean coll = inter.intersectSegmentPlane(ball.position, nextPosition, surf,collPoint);
                            boolean colliding = inter.isPointInTriangle(collPoint, newPlane.getPoints().get(0), newPlane.getPoints().get(1), newPlane.getPoints().get(2));

                            if(coll && colliding)
                            {
                                if(prevDist != null)
                                {
                                    if(new Vector3(collPoint).sub(ball.position).len() < prevDist)
                                    {
                                        plane = newPlane;
                                        counter = 0;
                                        planeFound = false;
                                        this.collisionPoint = collPoint;
                                        prevDist = new Vector3(collisionPoint).sub(ball.position).len();
                                    }

                                    if(new Vector3(collPoint).sub(ball.position).len() == prevDist)
                                    {
                                        if (newPlane.closestPoint(ball.position).sub(ball.position).len() < plane.closestPoint(ball.position).sub(ball.position).len())
                                        {
                                            plane = newPlane;
                                            counter = 0;
                                            this.collisionPoint = collPoint;

                                            prevDist = new Vector3(collisionPoint).sub(ball.position).len();
                                        }
                                    }
                                    else
                                    {
                                        if(counter < triangles.size())
                                        {
                                            counter += 1;
                                        }
                                        else
                                        {
                                            planeFound = true;
                                        }
                                    }
                                }
                                else
                                {
                                    plane = newPlane;
                                    prevDist = new Vector3(plane.closestPoint(ball.position)).sub(ball.position).len();
                                }
                            }
                        }
                    }
                    if (plane == null)
                    {
                        plane = triangles.get(m);
                    }
                }
            }

        //set collisionPoint to the point of intersection with the direction & the closest plane
        //collisionPoint = position.add(plane.getNormal().scl(plane.distance(nextPosition)));
        //(don't forget to account for gravity and friction)
        if (plane != null)
        {
            //decide whether or not a collision occurs
            nextPosition = new Vector3(new Vector3(ball.position).add(ball.direction));

            Vector3 nextPosPlane = new Vector3(plane.closestPoint(nextPosition));
            Vector3 currPosPlane = new Vector3(plane.closestPoint(ball.position));

            Intersector intersector = new Intersector();

            Ray ray = new Ray(ball.position,ball.direction);
            Vector3 intersect = new Vector3();
            boolean hit = intersector.intersectRayTriangle(ray,plane.getPoints().get(0),plane.getPoints().get(1),plane.getPoints().get(2),intersect);

            Vector3 colliPoint = new Vector3();
            Plane surface = new Plane(plane.getPoints().get(0),plane.getPoints().get(1),plane.getPoints().get(2));
            boolean collis = intersector.intersectSegmentPlane(ball.position,nextPosition,surface,colliPoint);
            boolean colliding = intersector.isPointInTriangle(colliPoint,plane.getPoints().get(0),plane.getPoints().get(1),plane.getPoints().get(2));

            if(collis && colliding)
            {
                Vector3 centreLine = new Vector3(currPosPlane).sub(ball.position);
                centreLine.scl(1/centreLine.len());
                Vector3 normalLine = new Vector3(new Vector3(nextPosPlane).sub(currPosPlane));
                normalLine.scl(1/normalLine.len());
                float perpComponent = (new Vector3(ball.direction).dot(centreLine));
                Vector3 perpLine = new Vector3(new Vector3(centreLine).scl(-perpComponent));

                if(plane.getPoints().get(0).z == plane.getPoints().get(1).z &&
                        plane.getPoints().get(0).z == plane.getPoints().get(2).z)
                {
                    if (perpLine.len() > 0.05)
                    {
                        perpLine.scl(0.5f);
                    }
                    else
                    {
                        setGravit();
                    }
                }
                else
                {
                    if (perpLine.len() > 0.05)
                    {
                        perpLine.scl(0.6f);
                    }
                }

                Plane surf = new Plane(plane.getPoints().get(0),plane.getPoints().get(1),plane.getPoints().get(2));
                Vector3 proj = new Vector3(ball.position);
                proj.sub(new Vector3(surf.getNormal()).scl(new Vector3(ball.position).sub(plane.getPoints().get(0)).dot(surf.getNormal())));
                Vector3 nextProj = new Vector3(nextPosition);
                nextProj.sub(new Vector3(surf.getNormal()).scl(new Vector3(nextPosition).sub(plane.getPoints().get(0)).dot(surf.getNormal())));
                nextProj.sub(proj);

                Vector3 bounce = new Vector3(new Vector3(perpLine).add(nextProj));
                this.bounceVector = bounce;

                if (lastPos != null)
                {
                    if(ball.position.dst(lastPos) == 0)
                    {
                        //ball has stopped
                        gameWorld.isMoving = false;
                        System.out.println("Ball has stopped");
                    }
                    else
                    {
                        gameWorld.isMoving = true;
                    }
                    lastPos = new Vector3(ball.position);
                }
                else
                {
                    lastPos = new Vector3(ball.position);
                }
                return true;
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
                Triangle newTriangle = new Triangle(triangles.get(m).getPoints().get(0), triangles.get(m).getPoints().get(1), triangles.get(m).getPoints().get(2));
                if(closestPlane != null)
                {
                    if(newTriangle.closestPoint(ball.position).sub(ball.position).len() < closestPlane.closestPoint(ball.position).sub(ball.position).len())
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

        if(closestPlane != null)
        {
            Vector3 friction = new Vector3(direction);
            friction.scl(1/direction.len());
            if(new Vector3(closestPlane.closestPoint(ball.position)).sub(ball.position).len() < 1)
            {
                float noise = (float) Math.random() * 0.01f;
                if (this.noise)
                {
                    friction.scl(noise);
                }
                if(closestPlane.getPoints().get(0).z == closestPlane.getPoints().get(1).z &&
                        closestPlane.getPoints().get(0).z == closestPlane.getPoints().get(2).z)
                {
                    if(ball.direction.x != 0)
                    {
                        ball.direction.x = ball.direction.x-friction.x;
                    }
                    if(ball.direction.y != 0)
                    {
                        ball.direction.y = ball.direction.y-friction.y;
                    }
                }
            }
            else
            {
                friction.scl(0.00001f);
                if(ball.direction.x > 0 || ball.direction.x < 0)
                {
                    ball.direction.x = ball.direction.x - friction.x;
                }
                if(ball.direction.y > 0 || ball.direction.y < 0)
                {
                    ball.direction.y = ball.direction.y - friction.y;
                }
            }
        }

        if (gravit)
        {
            ball.direction.add(new Vector3(0,0,-0.035f));
        }
        else
        {
            direction.z = 0;
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
                float scale = (float)Math.random()*0.003f;
                Vector3 newWind = new Vector3(wind).scl(scale);
                if (this.noise=true)
                {
                    ball.direction.add(newWind);
                    //System.out.println("Wind"+ scale);
                }
            }
        }
    }

    public void setGravit()
    {
        if(plane != null)
        {
            if(new Vector3(plane.closestPoint(ball.position)).sub(ball.position).len() < 0.85)
            {
                if(plane.getPoints().get(0).z == plane.getPoints().get(1).z&&
                plane.getPoints().get(0).z == plane.getPoints().get(2).z)
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

    public void setBall(Ball ball)
    {
        this.ball = ball;
    }
}