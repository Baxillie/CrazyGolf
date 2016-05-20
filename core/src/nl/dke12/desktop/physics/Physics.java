package nl.dke12.desktop.physics;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import nl.dke12.util.Log;

import java.util.ArrayList;
@Deprecated
public class Physics
{
    private ArrayList<Ball> balls;
    private ArrayList<StaticObject> staticObjects;
    private Vector3 minVelocity = new Vector3(1f,1f,0f);
    private ModelBatch renderer;
    private StaticObject collidedObject;
    private int collidedBall;

    public Physics(ModelBatch renderer, ArrayList<Ball> balls, ArrayList<StaticObject> staticObjects)
    {
        this.balls = balls;
        this.staticObjects = staticObjects;
        this.renderer = renderer;
    }

    public void render()
    {
        for(Ball ball : balls)
        {
            Log.log(ball.debugString());
            ball.update();
            calculateGravity(ball);
            hasCollided(ball);
            if(collidedBall>0)
            {
                balls.get(collidedBall).update();
            }
            renderer.render(ball.getModel());
        }
        for(StaticObject staticObject : staticObjects)
        {
            renderer.render(staticObject.getModel());
        }
    }

    private void calculateGravity(Ball ball)
    {
        //if it bumps
        if(ball.getBox().isBumpZ())
        {
            System.out.println("zbump");
            Vector3 vel = ball.getVelocity();
            if(Math.abs(ball.getVelocity().z) < 0.001)
            {
                vel.z = 0;
                ball.setVelocity(vel);
                System.out.println("calling other if");
            }
            else
            {
              //  vel.z *= -5;
                ball.setVelocity(vel);
                System.out.println("is it going here? :3 ");
            }
        }/*
        else if (ball.getPrevVelocity().z > 0  && Math.abs(ball.getVelocity().z) < 0.001)
        {
            ball.incrementVelocity(new Vector3(0,0,-1f));
        }*/
        else
        {
            ball.incrementVelocity(new Vector3(0,0,-1f));
        }
    }

    public void hasCollided(Ball ball)
    {/*
        if(isCollidingBall(ball))
        {
            Vector3 velocityChange1 = ball.getVelocity();
            Ball ball2 = balls.get(collidedBall);
            Vector3 velocityChange2 = ball2.getVelocity();

//            velocityChange1.x = (ball.getVelocity().x * (ball.getMass() - ball2.getMass()) + (2f * ball2.getMass() * ball2.getVelocity().x)) / (ball.getMass() + ball2.getMass());
//            velocityChange1.y = (ball.getVelocity().y * (ball.getMass() - ball2.getMass()) + (2f * ball2.getMass() * ball2.getVelocity().y)) / (ball.getMass() + ball2.getMass());
//
//            velocityChange2.x = (ball2.getVelocity().x * (ball2.getMass() - ball.getMass()) + (2f * ball.getMass() * ball.getVelocity().x)) / (ball.getMass() + ball2.getMass());
//            velocityChange2.y = (ball2.getVelocity().y * (ball2.getMass() - ball.getMass()) + (2f * ball.getMass() * ball.getVelocity().y)) / (ball.getMass() + ball2.getMass());


            ball2.setVelocity(new Vector3 (velocityChange1.x/2, velocityChange1.y/2, velocityChange1.z));
            ball.setVelocity(velocityChange2);

        }
*/
        if(isColliding(ball))
        {
            //Hole is the 1st entry in staticObject array-list
            if(collidedObject.getLabel().equals("hole"))
            {
                if(Math.abs(ball.getVelocity().x) <= minVelocity.x && Math.abs(ball.getVelocity().y) <= minVelocity.y)
                {
                    ball.setVelocity(new Vector3(0f, ball.getVelocity().y, -0.5f));
                    return;
                }
                return;
            }

            Vector3 velocityChange = ball.getVelocity();

            if(ball.getBox().isBumpX())
            {
                velocityChange.x = 0 - velocityChange.x;
            }
            if(ball.getBox().isBumpY())
            {
                velocityChange.y = 0 - velocityChange.y;
            }
            if(ball.getBox().isBumpZ())
            {
                velocityChange.z = 0 - velocityChange.z;
            }

            //todo: fix Z next period
            //todo: corners are a bit fucked :3
            //velocityChange.z = 0 - velocityChange.z;
            ball.updateVelocity(velocityChange);
        }
    }

    public boolean isColliding(Ball ball)
    {
        ObjectBox box;
        box = ball.getBox();
        for(StaticObject staticObject: staticObjects)
        {
            if (box.overlaps(staticObject.getBox()))
            {
                collidedObject = staticObject;
                return true;
            }
        }
        return false;
    }

    public boolean isCollidingBall(Ball ball)
    {
        for(int i = 0; i < balls.size()-1; i++ )
        {
            if(balls.get(i) != ball)
            {
                double distance = Math.sqrt(
                        ((balls.get(i).getPosition().x - ball.getPosition().x) * (balls.get(i).getPosition().x - ball.getPosition().x))
                +((balls.get(i).getPosition().y - ball.getPosition().y) * (balls.get(i).getPosition().y - ball.getPosition().y))
                );

                if (distance < balls.get(i).getRadius() + ball.getRadius()) {
                    //balls have collided
                    collidedBall = i;
                    return true;
                }
            }
        }
        collidedBall = -1;
        return false;
    }
}
