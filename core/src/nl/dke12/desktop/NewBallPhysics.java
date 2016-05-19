package nl.dke12.desktop;

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
    public ArrayList<Vector3> instances = new ArrayList<>();
    public BoundingBox box;
    public ModelInstance object;
    public Vector3 nextPosition = new Vector3(0,0,0);
    public float radius;
    public int index;
    public boolean zcollide;
    public boolean fall;
    //height from object beneath it
    public float height=10;
    public ArrayList<GBall> gballs;
    public ArrayList<SolidObject> obstacles;
    public boolean gravity=true;
    private Vector3 bounceVector;
    private Triangle plane;
    public SolidObject closest;
    private boolean gravit = false;

    public NewBallPhysics(ModelInstance mod ,float x, float y, float z,float xspeed,float yspeed,float zspeed,ArrayList<SolidObject> models,ArrayList<GBall> gballs)
    {
        position = new Vector3();
        direction = new Vector3();
        position.set(x,y,z);
        direction.set(xspeed,yspeed,zspeed);
        this.obstacles=models;
        this.object = mod;
        this.radius = 0.5f;
        this.gballs = gballs;
    }

    public void push(float xpush, float ypush, float zpush)
    {
        float xvect=direction.x+xpush;
        float yvect=direction.y+ypush;
        float zvect=direction.z+zpush;
        Vector3 dir = new Vector3(xvect/2,yvect/2,zvect/2);
        if (dir.z>0)
            gravity = true;

        direction.set(dir);
    }

    public void stop()
    {
        Vector3 dir = new Vector3(0,0,0);
        direction.set(dir);
        System.out.print("stop");
    }

    public Vector3 getNormal(float point1, float point2, float point3)
    {
        Vector3 normal = new Vector3();
        return normal;
    }

    public void bounce(float j)
    {
        Vector3 velocityChange = new Vector3();
        velocityChange.set(direction);
        //stop();

        //float j = how far in the future to predict
        boolean ex=false;
        boolean wy=false;
        boolean zed=false;

        /*if (instances.get(index).x )
        {

        }*/
/*
        float boxMinX = instances.get(index).x-4.2f;
        float boxMinY = instances.get(index).y-4.2f;
        float boxMinZ = instances.get(index).z-4.2f;
        float boxMaxX = instances.get(index).x+4.2f;
        float boxMaxY = instances.get(index).y+4.2f;
        float boxMaxZ = instances.get(index).z+4.2f;


        if(position.x<boxMinX-radius||position.x>boxMaxX+radius)
        {
            velocityChange.x = 0 - velocityChange.x;
        }
        if(position.y<boxMinY-radius||position.y>boxMaxY+radius)
        {
            velocityChange.y = 0 - velocityChange.y;
        }
*/

        //for(int i=0;i<this.instances.size();i++)
        //{
        if (instances.size()>0);
        {
            float boxMinX = instances.get(index).x-4.0f;
            float boxMinY = instances.get(index).y-4.0f;
            float boxMinZ = instances.get(index).z-2.0f;

            float boxMaxX = instances.get(index).x+4.0f;
            float boxMaxY = instances.get(index).y+4.0f;
            float boxMaxZ = instances.get(index).z+6.0f;

            //System.out.print("get position {"+instances.get(index)+"}");

            float nextX = position.x+(direction.x*j);
            float nextY = position.y+(direction.y*j);
            float nextZ = position.z+(direction.z*j);
            if(nextX>boxMinX-radius&&nextX<boxMaxX+radius)
            {
                ex = true;
            }
            if(nextY>boxMinY-radius&&nextY<boxMaxY+radius)
            {
                wy = true;
            }
            if(nextZ>boxMinZ-radius&&nextZ<boxMaxZ+radius)
            {
                zed = true;
            }
            if(ex&&wy&&zed)
            {


                if((position.x<boxMinX-radius||position.x>boxMaxX+radius)&&
                        (nextY>boxMinY-radius&&nextY<boxMaxY+radius)&&
                        (nextZ>boxMinZ&&nextZ<boxMaxZ))//&&
                //(nextX>boxMinX-radius&&nextX<boxMaxX+radius))
                {
                    velocityChange.x = 0 - velocityChange.x*1.1f;
                    //velocityChange.x = 0;
                    System.out.print("trup");

                }
                else if((position.y<boxMinY-radius||position.y>boxMaxY+radius)&&
                        (nextX>boxMinX-radius&&nextX<boxMaxX+radius)&&
                        (nextZ>boxMinZ&&nextZ<boxMaxZ))//&&
                //(nextY>boxMinY-radius&&nextY<boxMaxY+radius))
                {
                    velocityChange.y = 0 - velocityChange.y*1.1f;
                    //velocityChange.y = 0;
                    System.out.print("HArp");
                }
                else if((position.z<boxMinZ-radius||position.z>boxMaxZ+radius)&&
                        (nextX>boxMinX-radius&&nextX<boxMaxX+radius)&&
                        (nextY>boxMinY-radius&&nextY<boxMaxY+radius))//&&
                //(nextZ>boxMinZ-radius&&nextZ<boxMaxZ+radius))
                {


                    if (direction.z<-0.08||direction.z>=0.08)
                    {
                        //System.out.print("KLIP");
                        //direction.z = 0 - direction.z*0.98f;
                        velocityChange.z= 0 - velocityChange.z*0.98f;
                        //velocityChange.z = 0;
                        //zcollide = true;
                        gravity=true;

                        //push(direction.x,direction.y,0);
                    }
                    else if (direction.z>=-0.08&&direction.z<=0.08)
                    {
                        gravity = false;
                        velocityChange.z= 0;
                        //push(direction.x,direction.y,0);
                    }
                }
                else
                {
                    zcollide = false;
                }
            }
            else
            {
                ex=false;
                wy =false;
                zed = false;
            }
        }
        //}
        /*if(position.z>boxMinZ-radius&&position.z<boxMaxZ+radius)
        {
            zed = true;
        }*/


        //ALMOST WORKS
        /*for(int i=0;i<this.instances.size();i++)
        {
            if (instances.size()>0);
            {
                float boxMinX = instances.get(i).x-4f;
                float boxMinY = instances.get(i).y-4f;
                float boxMinZ = instances.get(i).z-4f;
                float boxMaxX = instances.get(i).x+4f;
                float boxMaxY = instances.get(i).y+4f;
                float boxMaxZ = instances.get(i).z+4f;

                if ((position.x+direction.len()*1.8>boxMinX-radius&&position.x+direction.len()*1.8<boxMaxX+radius))
                    //||(position.x-direction.x()*1.8>boxMinX-radius&&position.x-direction.x()*1.8<boxMaxX+radius))
                {
                    velocityChange.x = 0 - velocityChange.x;

                }
                if ((position.y+direction.len()*1.8>boxMinY-radius&&position.y+direction.len()*1.8<boxMaxY+radius))
                    //|| (position.y-direction.y*1.8>boxMinY-radius&&position.y-direction.y*1.8<boxMaxY+radius))
                {
                    velocityChange.y = 0 - velocityChange.y;
                }*/
/*
                Vector3 Leftface = new Vector3(instances.get(i).x-4,instances.get(i).y,instances.get(i).z);
                Vector3 Rightface = new Vector3(instances.get(i).x+4,instances.get(i).y,instances.get(i).z);
                Vector3 Topface = new Vector3(instances.get(i).x,instances.get(i).y,instances.get(i).z+4);
                Vector3 Bottomface = new Vector3(instances.get(i).x,instances.get(i).y,instances.get(i).z-4);
                Vector3 Frontface = new Vector3(instances.get(i).x,instances.get(i).y+4,instances.get(i).z);
                Vector3 Backface = new Vector3(instances.get(i).x,instances.get(i).y-4,instances.get(i).z);

                if ((Leftface.sub(position).len()<Topface.sub(position).len()&&
                    Leftface.sub(position).len()<Bottomface.sub(position).len()&&
                    Leftface.sub(position).len()<Frontface.sub(position).len()&&
                    Leftface.sub(position).len()<Backface.sub(position).len())||
                    (Rightface.sub(position).len()<Topface.sub(position).len()&&
                    Rightface.sub(position).len()<Bottomface.sub(position).len()&&
                    Rightface.sub(position).len()<Frontface.sub(position).len()&&
                    Rightface.sub(position).len()<Backface.sub(position).len()))
                    {
                        velocityChange.x = 0 - velocityChange.x;
                    }
                if ((Frontface.sub(position).len()<Topface.sub(position).len()&&
                    Frontface.sub(position).len()<Bottomface.sub(position).len()&&
                    Frontface.sub(position).len()<Leftface.sub(position).len()&&
                    Frontface.sub(position).len()<Rightface.sub(position).len())||
                    (Backface.sub(position).len()<Topface.sub(position).len()&&
                    Backface.sub(position).len()<Bottomface.sub(position).len()&&
                    Backface.sub(position).len()<Leftface.sub(position).len()&&
                    Backface.sub(position).len()<Rightface.sub(position).len()))
                    {
                        velocityChange.y = 0 - velocityChange.y;
                    }*/
        // }

        push(velocityChange.x,velocityChange.y,velocityChange.z);
        push(velocityChange.x,velocityChange.y,velocityChange.z);
        //updatePosition();
        updateVelocity(velocityChange);
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
            if(closest!=null)
            {
                Vector3 waill = new Vector3(obstacles.get(h).getPosition());
                Vector3 cloisest = new Vector3(closest.getPosition());
                if(new Vector3(new Vector3(waill).sub(this.position)).len()<new Vector3(new Vector3(cloisest).sub(this.position)).len())
                //if(wall.position.
                {
                    plane = null;
                    closest = obstacles.get(h);
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
                                    /*System.out.println("new closest face"+
                                            closest.getPoints().get(i)+" "+closest.getPoints().get(k)+" "+closest.getPoints().get(l));*/
                                    //System.out.println("i="+i+" k="+k+" l="+l);
                                    //System.out.println("pos="+planePos);

                                }
                            }
                            else
                            {
                                plane= new Triangle(closest.getPoints().get(i),closest.getPoints().get(k),closest.getPoints().get(l));
                                //System.out.println("make plane"+closest.points.get(i)+" "+closest.points.get(k)+" "+closest.points.get(l));
                            }
                            /*System.out.println("new face"+
                                    closest.points.get(i)+" "+closest.points.get(k)+" "+closest.points.get(l));
                            System.out.println("dist="+newPlane.getDistance(position));
                            System.out.println("intersect="+newPlane.getIntersection(position));*/
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

            /*Vector3 planePos= new Vector3(plane.closestPoint(nextPosition));
            Vector3 planePosNow= new Vector3(plane.closestPoint(position));*/
            /*System.out.println("distance = "+new Vector3(closest.position).sub(position).len());
            System.out.println("closest = "+closest.position+" position = "+position);*/

            /*if((plane.getDistance(nextPosition)<2)
                && new Vector3(popo).sub(newp).len()>new Vector3(popo1).sub(nextPosition).len()
                && (plane.testIntersection(plane.getIntersection(nextPosition))))*/
            /*if (new Vector3(closest.position).sub(nextPosition).len()<3.9)&&
                (plane.getDistance(nextPosition)<1)&&
                (plane.testIntersection(plane.getIntersection(position))))*/
            if(new Vector3(new Vector3(nextposPlane).sub(nextPosition)).len()<1)
            {
                //centreline = inward facing normal of collision plane
                Vector3 unitNormal = new Vector3(plane.getNormal());
                //check if normal is outward facing
                Vector3 cent = new Vector3(unitNormal);
                Vector3 nextDist = new Vector3(nextposPlane).sub(nextPosition);
                if(nextDist.len()>(new Vector3(nextDist).add(cent)).len())
                {
                    unitNormal.scl(-1);
                }


                //*** THE NEXT LINE IS BROKEN (maybe)
                //normalLine = perpendicular to centreLine parallel to the direction
                //Vector3 planeDirection =  new Vector3(new Vector3(nextposPlane).sub(posPlane));
                //normalLine.scl(1/normalLine.len());
                //perpComponent = component of direction that is perpendicular to unitNormal
                float normalDirectionComp = (new Vector3(unitNormal).dot(direction)/unitNormal.len());
                //paraComponent = component of direction that is parallel to centreLine
                //float paraComponent = (new Vector3(direction).dot(normalLine))/centreLine.len();
                Vector3 normalDirection = new Vector3(new Vector3(unitNormal).scl(-normalDirectionComp));
                Vector3 planeDirection =  new Vector3(new Vector3(direction).add(normalDirection));
                //Vector3 paraLine = new Vector3(new Vector3(centreLine).scl(paraComponent));
                //Vector3 paraLine = new Vector3(new Vector3(direction).add(normalLine));
                //paraLine.scl(-1);


                Vector3 bounce = new Vector3(new Vector3(planeDirection).add(normalDirection));
                this.bounceVector = bounce.scl(0.9f);
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
                /*this.direction.x=this.direction.x*friction;*/
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
                //updatePosition();
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
                if (height > 1 || direction.z != 0) {
                    gravity = true;
                }
            }
        }
    }

    public void updatePosition()
    {
        float dist = 1f;
        float shoot = 0f;

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
                    /*gravit = false;
                    direction.x=bounceVector.x;
                    direction.y=bounceVector.y;*/
                }
            }
        }
        else
        {
            this.position.add(this.direction);
            object.transform.translate(this.direction);
            //System.out.println(this.position);
            updateVelocity(this.direction);
        }

        for(int i=0;i<this.instances.size();i++)
        {
            if (instances.size()>0);
            {
                boolean ex = false;
                boolean wy = false;
                boolean zed = false;

                float boxMinX = instances.get(index).x-4.0f;
                float boxMinY = instances.get(index).y-4.0f;
                float boxMinZ = instances.get(index).z-2.0f;
                float boxMaxX = instances.get(index).x+4.0f;
                float boxMaxY = instances.get(index).y+4.0f;
                float boxMaxZ = instances.get(index).z+6.0f;

                //System.out.print("get position {"+instances.get(index)+"}");

                float nextX = position.x;
                float nextY = position.y;
                float nextZ = position.z;
                if(nextX>boxMinX-radius&&nextX<boxMaxX+radius)
                {
                    ex = true;
                }
                if(nextY>boxMinY-radius&&nextY<boxMaxY+radius)
                {
                    wy = true;
                }
                if(nextZ>boxMinZ-radius&&nextZ<boxMaxZ+radius)
                {
                    zed = true;
                }

                //CHECK IF FALLING
                if((position.z>boxMaxZ+radius)&&
                        (position.x>boxMinX-radius&&position.x<boxMaxX+radius)&&
                        (position.y>boxMinY-radius&&position.y<boxMaxY+radius))
                {
                    this.fall=false;
                    this.height=position.z-boxMaxZ;
                    //System.out.println(height);
                }
                else
                {
                    this.fall=false;
                }

                //DEBUG
                    /*if(position.z>boxMinZ-radius&&position.z<boxMaxZ+radius)
                    {
                        Vector3 up = new Vector3(0,0,1);
                        this.position.add(up);
                        object.transform.translate(up);
                    }*/

                if(ex&&wy&&zed)
                {
                    if (position.z>boxMaxZ&&position.z-1<=boxMaxZ) {
                        if (direction.z < 0.08 && direction.z > -0.08) {
                            Vector3 velocityChange = new Vector3();
                            velocityChange.set(direction);
                            //direction.z=0f;
                            System.out.print("HERE");
                            zcollide = true;
                            gravity = false;
                        }
                    }
                }
            }
        }
    }
}
