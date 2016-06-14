package nl.dke12.game;

import com.badlogic.gdx.math.Vector3;
import nl.dke12.controller.GameController;
import nl.dke12.controller.InputProcessor;
import nl.dke12.screens.GameDisplay;
import nl.dke12.util.GameWorldLoader;

import java.util.ArrayList;

/**
 * Created by Ajki on 12/05/2016.
 */
public class GameWorld
{
	private ArrayList<SolidObject> solidObjects;
	private ArrayList<InstanceModel> instances;
	private ArrayList<InstanceModel> mapOfWorld;
	private boolean multiplayer;
	private boolean first = true;
	private Ball ball;
	private Ball ball2;
	private GameWorldLoader worldLoader;
	private GameController gameController;
	private GameDisplay gameDisplay;
	private Physics physics;
	private Physics physics2;
	private static boolean player1Turn;
	private Vector3 holePosition;
	private SolidObject solidBall;
	private SolidObject solidBall2;
	

	public GameWorld(boolean multiplayer, boolean isHumanPlayer)
	{
		this.multiplayer = multiplayer;
		if(multiplayer){
			GameController.trueMultiplayer();
		}
		player1Turn = true;

		this.worldLoader = new GameWorldLoader("core/assets/level1.txt");
		this.instances = worldLoader.getModelInstances();
		this.mapOfWorld = worldLoader.getMapOfWorld();
		this.solidObjects = worldLoader.getSolidObjects();
		holePosition = worldLoader.getHolePosition();

		//this.gameDisplay = new GameDisplay(multiplayer, this);
		//gameDisplay.setInstances(instances);

		createBalls();
		createPhysics();
		createController(isHumanPlayer);
	}

	public GameController getGameController()
	{return gameController;}

	public void setDisplay(GameDisplay display)
	{
		this.gameDisplay = display;
		gameDisplay.setInstances(instances, mapOfWorld);
		gameDisplay.updateBall(ball.position);
		gameDisplay.updateBall2(ball2.position);
	}

	private void createController(boolean isHumanPlayer)
	{
		if(multiplayer)
		{
			this.gameController = new GameController(physics, physics2, false);
		}
		else
		{
			if(isHumanPlayer)
				this.gameController = new GameController(physics, true);
			else
				this.gameController = new GameController(physics, false);
		}

	}

	private void createPhysics()
	{
		if(multiplayer)
		{

			solidBall = new SolidObject(ball.position.x, ball.position.y, ball.position.z,"solidBall");
			solidBall2 = new SolidObject(ball2.position.x, ball2.position.y, ball2.position.z,"solidBall2");

			this.physics = new Physics(solidObjects, ball);
			physics.addSolidObject(solidBall2);
			//gameDisplay.updateBall(ball.position);
			
			this.physics2 = new Physics(solidObjects, ball2);	
			physics2.addSolidObject(solidBall);
			//gameDisplay.updateBall2(ball2.position);
		}
		else
		{
			this.physics = new Physics(solidObjects, ball);
		}
	}


	private void createBalls()
	{
		if(multiplayer)
		{
			this.ball = new Ball(-0.2f, 0.1f, 0);
			this.ball2 = new Ball(0.2f, 0.1f, 0);
		}
		else
		{
			this.ball = new Ball(0,0,0);
		}
	}

	public boolean ballIsInHole()
	{
		Vector3 ballPosition = ball.position;
		Vector3 holePosition = getHolePosition();
		if(ballPosition.z < -10)
		{
			return true;
		}
		return false;
	}

	public void render()
	{
		gameController.moveCamera(gameDisplay.getCamera());
		gameController.move();

		if(!multiplayer){
			updatePosition(physics,ball);
		}

		if(multiplayer){
			if(first){
				updatePosition(physics,ball);
				updatePosition(physics2,ball2);
				first = false;
				
			}
			
			if(player1Turn){

				updatePosition(physics,ball);
			}
			else{
				updatePosition(physics2,ball2);

			}

			if (!player1Turn && multiplayer){
				solidBall = new SolidObject(ball.position.x, ball.position.y, ball.position.z,"solidBall");
				physics2.updateSolidObject(solidBall);

			}


			if(player1Turn && multiplayer){


				solidBall2 = new SolidObject(ball2.position.x, ball2.position.y, ball2.position.z,"solidBall2");
				physics.updateSolidObject(solidBall2);

			}

		}

	}

	public void updatePosition(Physics physics, Ball ball)
	{

		//System.out.println(ball.direction.z);
		if (physics.collides())
		{

			if (physics.bounceVector != null)
			{
				if(physics.bounceVector.z > 0.08)
				{
					physics.gravit = false;
					ball.direction.set(physics.bounceVector);
					System.out.println("ball stopped");

				}

				else
				{
					//we should not be setting the gravity here, but oh well
					//physics.gravit = false;
					ball.direction.x=physics.bounceVector.x;
					ball.direction.y=physics.bounceVector.y;

				}

			}
		}
		else
		{
			ball.position.add(ball.direction);
			if (ball==this.ball){

				gameDisplay.updateBall(ball.direction);
			}
			else{
				gameDisplay.updateBall2(ball.direction);
			}
			physics.updateVelocity(ball.direction);



		}

	}

	public static void togglePlayerTurn(){
		if(player1Turn){
			player1Turn = false;
		}
		else{
			player1Turn = true;
		}
	}

	public Vector3 getBallPosition()
	{
		return ball.position;
	}

	public Vector3 getBallDirection()
	{
		return ball.direction;
	}

	public Vector3 getHolePosition()
	{
		return holePosition;
	}

	public boolean getTurn(){
		return player1Turn;
	}

	public void setTurn(boolean turn){
		player1Turn = turn;

	}

}