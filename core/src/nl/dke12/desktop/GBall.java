package nl.dke12.desktop;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import java.util.ArrayList;

/**
 * Created by Tom Conneely on 24/04/2016.
 */
@Deprecated
public class GBall
{
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
    public ArrayList<GBall> gballs;
    public boolean gravity=true;

    public GBall(ModelInstance mod ,float x, float y, float z,float xspeed,float yspeed,float zspeed,ArrayList<Vector3> models,ArrayList<GBall> gballs)
    {
        position = new Vector3();
        direction = new Vector3();
        position.set(x,y,z);
        direction.set(xspeed,yspeed,zspeed);
        this.instances=models;
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
                    (nextZ>boxMinZ-radius&&nextZ<boxMaxZ+radius))//&&
                    //(nextX>boxMinX-radius&&nextX<boxMaxX+radius))
                    {
                        velocityChange.x = 0 - velocityChange.x*1.1f;
                        //velocityChange.x = 0;
                        System.out.print("trup");

                    }
                    else if((position.y<boxMinY-radius||position.y>boxMaxY+radius)&&
                    (nextX>boxMinX-radius&&nextX<boxMaxX+radius)&&
                    (nextZ>boxMinZ-radius&&nextZ<boxMaxZ+radius))//&&
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
                                velocityChange.z= 0 - velocityChange.z*0.80f;
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

/*
                    for(float i=0;i<direction.len();i+=0.1f)
                    {
                        float posX=position.x+direction.x*i;
                        float posY=position.y+direction.y*i;
                        float posZ=position.z+direction.z*i;

                        if ((posX>boxMinX-radius&&posX<boxMaxX+radius)
                            &&(posY>boxMinY-radius&&posY<boxMaxY+radius)
                            &&(posZ>boxMinZ-radius&&posZ<boxMaxZ+radius))
                        {
                            float length= i-0.1f;
                            float locatX = position.x+direction.x*length;
                            float locatY = position.y+direction.y*length;
                            float locatZ = position.z+direction.z*length;

                            if((locatX<boxMinX-radius||locatX>boxMaxX+radius)&&
                                    (locatY>boxMinY-radius&&locatY<boxMaxY+radius)&&
                                    (locatZ>boxMinZ-radius&&locatZ<boxMaxZ+radius))//&&
                            //(nextX>boxMinX-radius&&nextX<boxMaxX+radius))
                            {
                                velocityChange.x = 0 - velocityChange.x*1.1f;
                                //velocityChange.x = 0;
                                System.out.print("trup");

                            }
                            else if((locatY<boxMinY-radius||locatY>boxMaxY+radius)&&
                                    (locatX>boxMinX-radius&&locatX<boxMaxX+radius)&&
                                    (locatZ>boxMinZ-radius&&locatZ<boxMaxZ+radius))//&&
                            //(nextY>boxMinY-radius&&nextY<boxMaxY+radius))
                            {
                                velocityChange.y = 0 - velocityChange.y*1.1f;
                                //velocityChange.y = 0;
                                System.out.print("HArp");
                            }
                            else if((locatZ<boxMinZ-radius||locatZ>boxMaxZ+radius)&&
                                    (locatX>boxMinX-radius&&locatX<boxMaxX+radius)&&
                                    (locatY>boxMinY-radius&&locatY<boxMaxY+radius))//&&
                            //(nextZ>boxMinZ-radius&&nextZ<boxMaxZ+radius))
                            {
                                if (direction.z<-0.08||direction.z>=0.08)
                                {
                                    //System.out.print("KLIP");
                                    //direction.z = 0 - direction.z*0.98f;
                                    velocityChange.z= 0 - velocityChange.z*0.80f;
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
                        }
                    }
*/
                        //}
                        //else
                        //{
                        //    gravity = true;
                        //}
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


        //todo: fix Z next period
        //todo: corners are a bit fucked :3
        //velocityChange.z = 0 - velocityChange.z;
            //System.out.println("BOUNCE");

        //}
        //position.set(position.x-velocityChange.x/2,position.y-velocityChange.y/2,position.z-velocityChange.z/2);
        /*if (zcollide = true)
        {
            push(velocityChange.x,velocityChange.y,velocityChange.z);
            push(velocityChange.x,velocityChange.y,velocityChange.z);
        }*/
        push(velocityChange.x,velocityChange.y,velocityChange.z);
        push(velocityChange.x,velocityChange.y,velocityChange.z);
       //updatePosition();
        updateVelocity(velocityChange);
    }

    public boolean collides(float j)
    {
        //float j = how far in the future to predict
        boolean ex=false;
        boolean wy=false;
        boolean zed=false;
        for(int i=0;i<this.instances.size();i++)
        {
            if (instances.size()>0);
            {
                float boxMinX = instances.get(i).x-4f;
                float boxMinY = instances.get(i).y-4f;
                float boxMinZ = instances.get(i).z-2f;
                float boxMaxX = instances.get(i).x+4f;
                float boxMaxY = instances.get(i).y+4f;
                float boxMaxZ = instances.get(i).z+6f;



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
                /*if((position.z>boxMaxZ+radius)&&
                        (position.x>boxMinX-radius&&position.x<boxMaxX+radius)&&
                        (position.y>boxMinY-radius&&position.y<boxMaxY+radius))
                {*/

                //}
                if(ex&&wy&&zed)
                {
                    this.index=i;
                    //System.out.print("get position {"+instances.get(i)+"}");
                    /*push(-direction.x,-direction.y,direction.z);
                    push(direction.x,direction.y,direction.z);*/
                    //System.out.print("MaxZ"+boxMaxZ);


                    return true;
                }
                else
                {
                    ex=false;
                    wy =false;
                    zed = false;
                }

            }
        }
        for(GBall bowl : gballs)
        {
            if ((bowl.position.x+bowl.direction.x == this.position.x+this.direction.x)&&
            (bowl.position.y+bowl.direction.y == this.position.y+this.direction.y)&&
            (bowl.position.z+bowl.direction.z == this.position.z+this.direction.z))
            {
                //return true;
            }
        }
        return false;
    }

    public void updateVelocity(Vector3 direction)
    {
        //if (zcollide)
        //{
            if(direction.len()>0.08)
            {
                /*if (false)
                {
                    //this.direction.sub(direction.x/50,direction.y/50,0);
                    this.direction.scl(0.99f);
                }
                else
                {
                    //this.direction.sub(direction.x/60,direction.y/60,0);
                    this.direction.scl(0.99f);
                }*/
                //this.direction.z = direction.z /direction.len();
                //ystem.out.println("position={"+position+"}");
                float friction = 0.95f;
                this.direction.x=this.direction.x*friction;
                this.direction.y=this.direction.y*friction;
                /*this.direction.x=this.direction.x*friction;*/
                if (gravity)
                {
                    if (direction.z>0.08f)
                    {
                        this.direction.z=this.direction.z/2;
                    }
                    else
                    {
                        if (direction.z>0)
                        {
                            direction.z=-direction.z;
                        }
                        else
                        {
                            this.direction.z=this.direction.z*2;
                        }
                    }
                }
                else
                {
                    direction.z=0;
                    //updatePosition();
                    if (height>1||direction.z!=0)
                    {
                        gravity = true;
                    }
                }
            }
            else
            {
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

                        Vector3 corner1 = new Vector3(instances.get(i).sub(4,4,4));
                        Vector3 corner2 = new Vector3(instances.get(i).sub(-4,4,4));
                        Vector3 corner3 = new Vector3(instances.get(i).sub(-4,-4,4));
                        Vector3 corner4 = new Vector3(instances.get(i).sub(-4,-4,-4));
                        Vector3 corner5 = new Vector3(instances.get(i).sub(4,4,-4));
                        Vector3 corner6 = new Vector3(instances.get(i).sub(4,-4,-4));
                        Vector3 corner7 = new Vector3(instances.get(i).sub(4,-4,4));
                        Vector3 corner8 = new Vector3(instances.get(i).sub(-4,4,-4));

                        if (corner1.sub(position).len()<1)
                        {
                            push(position.sub(corner1).x*-2,position.sub(corner1).y*-2,position.sub(corner1).z*-2);
                        }
                        else if(corner2.sub(position).len()<1)
                        {
                            push(position.sub(corner2).x*-2,position.sub(corner2).y*-2,position.sub(corner2).z*-2);
                        }
                        else if(corner3.sub(position).len()<1)
                        {
                            push(position.sub(corner3).x*-2,position.sub(corner3).y*-2,position.sub(corner3).z*-2);
                        }
                        else if(corner4.sub(position).len()<1)
                        {
                            push(position.sub(corner4).x*-2,position.sub(corner4).y*-2,position.sub(corner4).z*-2);
                        }
                        else if(corner5.sub(position).len()<1)
                        {
                            push(position.sub(corner5).x*-2,position.sub(corner5).y*-2,position.sub(corner5).z*-2);
                        }
                        else if(corner6.sub(position).len()<1)
                        {
                            push(position.sub(corner6).x*-2,position.sub(corner6).y*-2,position.sub(corner6).z*-2);
                        }
                        else if(corner7.sub(position).len()<1)
                        {
                            push(position.sub(corner2).x*-2,position.sub(corner2).y*-2,position.sub(corner2).z*-2);
                        }
                        else if(corner8.sub(position).len()<1)
                        {
                            push(position.sub(corner2).x*-2,position.sub(corner2).y*-2,position.sub(corner2).z*-2);
                        }
                        else
                        {
                            this.direction.set(0, 0, 0);
                        }
                    }
                }*/
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

                }


            }
        //}
        //else
        //{

        //GRAVITY
        /*if (gravity)
        {
                if (this.direction.z>0)
                {

                    if (this.direction.z>0.11)
                    {
                        this.direction.z*=0.5;
                    }
                    else
                    {
                        this.direction.z=-this.direction.z;

                    }
                }
                else if (this.direction.z>-1.1)
                {

                        this.direction.z*=2;
                }
            //}
        }
        else
        {
            System.out.print("position:"+position);
        }*/
    }

    public void updatePosition()
    {
        float dist = 1.2f;
        float shoot = 0f;

        //if (direction.len()>0)
        //{

            if (collides(dist))
            {
                boolean bloop=false;
                //System.out.println(position);
                /*
                if (collides(0.5f))
                {
                    bounce();
                }
                else
                {
                    this.direction.set(this.direction.scl(0.5f));
                    this.position.add(this.direction);
                    object.transform.translate(this.direction);
                    //System.out.println(this.position);
                    updateVelocity(this.direction);
                }*/


                /*for(dist=0f;dist<1.2;dist+=0.1f)
                {
                    if (collides(dist))
                    {
                        bloop = true;
                        shoot= dist;
                    }

                    //break;
                }
                if (bloop == true)
                {
                    bloop = false;
                    bounce(shoot);
                    System.out.println("position"+position);
                    System.out.println("distance"+shoot);

                }*/
                bounce(dist);



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
                        if (position.z>boxMaxZ&&position.z-1<=boxMaxZ)
                        {
                            if (direction.z<0.08&&direction.z>-0.08)
                            {
                                Vector3 velocityChange = new Vector3();
                                velocityChange.set(direction);
                                //direction.z=0f;
                                System.out.print("HERE");
                                zcollide = true;
                                gravity=false;
                                /*if (position.z!=boxMaxZ+1)
                                {
                                    velocityChange.z=boxMaxZ+1-position.z;
                                }*/
                                /*velocityChange.z = 0 - velocityChange.z;
                                zcollide = true;
                                updateVelocity(velocityChange)*/
                            }

                            /*else
                            {
                                if (this.direction.z>0)
                                {
                                    if (this.direction.z>0.01)
                                    {
                                        this.direction.z*=0.5;
                                    }
                                    else
                                    {
                                        this.direction.z=-this.direction.z;
                                    }
                                }
                                else if (this.direction.z>-1.1)
                                {
                                    this.direction.z*=2;
                                }
                            }*/
                        }
                        /*else if (position.z>boxMaxZ&&position.z+direction.z>boxMaxZ)
                        {
                            if (this.direction.z>0)
                            {
                                if (this.direction.z>0.008)
                                {
                                    this.direction.z*=0.5;
                                }
                                else
                                {
                                    this.direction.z=-this.direction.z;
                                }
                            }
                            else if (this.direction.z>-1.1)
                            {
                                this.direction.z*=2;
                            }
                            this.position.add(this.direction);
                            object.transform.translate(this.direction);
                        }*/
                    }
                }
            }
        //}
    }
}
