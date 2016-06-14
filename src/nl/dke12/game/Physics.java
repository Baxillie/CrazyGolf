package nl.dke12.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
	protected Vector3 bounceVector;
	public Vector3 solidBall;
	private Triangle plane;
	private SolidObject closest;
	protected boolean gravit;

	Vector3 empty = new Vector3(0,0,0);

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

	public boolean collides(){
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
			if(obstacles.get(h).getType().equals("solidBall")||obstacles.get(h).getType().equals("solidBall2")){
				solidBall = obstacles.get(h).getPosition();
				return ballPhysics(h);

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
						if(i!=k && i!=l && k!=l)
						{
							Triangle newPlane = new Triangle(closest.getPoints().get(i),closest.getPoints().get(k),closest.getPoints().get(l));
							if (plane!=null)
							{
								Vector3 planePos= new Vector3(plane.closestPoint(ball.position));
								Vector3 newPlanePos = new Vector3(newPlane.closestPoint(ball.position));

								if(new Vector3(planePos).sub(ball.position).len()>new Vector3(newPlanePos).sub(ball.position).len())
								{
									if(closest.getType().equals("slopeL")||closest.getType().equals("slope"))
									{
										System.out.println("pos"+closest.getPosition());
									}
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
			nextPosition = new Vector3(new Vector3(ball.position).add(ball.direction));

			Vector3 nextPosPlane = new Vector3(plane.closestPoint(nextPosition));
			Vector3 currPosPlane = new Vector3(plane.closestPoint(ball.position));


			//todo: ADD MORE STUFF
			// it does not detect a collision with vertically sloped planes

			if(closest.getType().equals("slope"))
			{
				System.out.println("dist=  "+new Vector3(nextPosPlane).sub(nextPosition).len());
				System.out.println("pointPlane= "+plane.closestPoint(ball.position));
				System.out.println("plane"+plane.getPoints().get(0)+plane.getPoints().get(1)+plane.getPoints().get(2));
				System.out.println("ball=  "+ball.position);
			}

			if(new Vector3(nextPosPlane).sub(nextPosition).len()<1)
			{
				//centreline = inward facing normal of collision plane
				//Vector3 centreLine = new Vector3(plane.getNormal());

				Vector3 centreLine = new Vector3(nextPosPlane).sub(nextPosition);
				centreLine.scl(1/centreLine.len());

				//normalLine = perpendicular to centreLine parallel to the direction
				Vector3 normalLine = new Vector3(new Vector3(nextPosPlane).sub(currPosPlane));
				normalLine.scl(1/normalLine.len());

				//perpComponent = component of direction that is perpendicular to centreLine
				float perpComponent = (new Vector3(ball.direction).dot(centreLine));

				//paraComponent = component of direction that is parallel to centreLine
				Vector3 perpLine = new Vector3(new Vector3(centreLine).scl(-perpComponent));

				Vector3 paraLine = new Vector3(new Vector3(ball.direction).sub(perpLine));

				Vector3 bounce = new Vector3(new Vector3(perpLine).add(normalLine));
				//Vector3 bounce = new Vector3(new Vector3(perpLine).add(normalLine));
				this.bounceVector = bounce.scl(0.4f);

				if(closest.getType().equals("slope"))
				{
					System.out.println("ZLOP");
				}

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

	public boolean ballPhysics(int index){
		
		System.out.println("Ball Position: " + ball.position);
		Vector3 solidBallPosition = obstacles.get(index).getPosition();
		Vector3 diffVector = solidBallPosition.sub(ball.position);
//		System.out.println("Ball Position: " + ball.position + "\n"
//						+ "solidBall Position: " + solidBallPosition + "\n" +
//							"diffVector: " + diffVector);

		if(diffVector.x < 0.1 && diffVector.y < 0.1){
			System.out.println("balls touched");
			return true;

		}
		else{
			return false;
		}
	}

	public void updateVelocity(Vector3 direction)
	{
		if(direction.len() > 0.08)
		{
			float friction = 0.85f;
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
				//todo: set gravit to true???
			}
		}
		else
		{
			//not sure if this will work but don't wanna try it now
//			if (direction.x < 0.081 && direction.x > -0.081 && direction.y < 0.081 && direction.y > -0.081 && direction.y != 0 && direction.x != 0){
//				GameWorld.togglePlayerTurn();
//			}
			if (direction.x < 0.081 && direction.x > -0.081)
			{
				direction.x = 0;
			}
			if (direction.y < 0.081 && direction.y > -0.081 && direction.y != 0)
			{
				direction.y = 0;
				GameWorld.togglePlayerTurn();
			}
			if (direction.z <= 0.081 && direction.z >= -0.081)
			{
				direction.z = 0;
			}
//			System.out.println(ball.position);

			//todo: or ball.stop();
		}
	}

	public Ball getBall()
	{
		return this.ball;
	}
	
	public void addSolidObject(SolidObject solidObject){
		obstacles.add(solidObject);
	}

	public void updateSolidObject(SolidObject solidObject){
		for(int h = 0; h < obstacles.size(); h++){

			if(obstacles.get(h).getType().equals("solidBall")||obstacles.get(h).getType().equals("solidBall2")){
				obstacles.remove(h);
				obstacles.add(solidObject);
			}
		}
	}
}