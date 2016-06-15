package nl.dke12.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;

/**
 * layer between AI and GameController
 * used by AI to give input
 */
public class AIInputProcessor implements InputProcessor
{

	private boolean move;
	private Vector3 direction;

	public AIInputProcessor()
	{
		direction = new Vector3(0,0,0);
	}
	public boolean moveCamLeft()
	{
		return Gdx.input.isKeyPressed(Input.Keys.A);
	}

	public boolean moveCamBack()
	{
		return Gdx.input.isKeyPressed(Input.Keys.S);
	}

	public boolean moveCamRight()
	{
		return Gdx.input.isKeyPressed(Input.Keys.D);
	}

	public boolean rotateCamAntiClock()
	{
		return Gdx.input.isKeyPressed(Input.Keys.Q);
	}

	public boolean moveCamForward()
	{
		return Gdx.input.isKeyPressed(Input.Keys.W);
	}

	public boolean rotateCamClock()
	{
		return Gdx.input.isKeyPressed(Input.Keys.E);
	}

	@Override
	public boolean decreaseForce() {
		return false;
	}

	@Override
	public boolean increaseForce() {
		return false;
	}

	@Override
	public boolean increaseHeight() {
		return false;
	}

	@Override
	public boolean decreaseHeight() {
		return false;
	}

	public void setMove(boolean move)
	{
		this.move = move;
	}
	@Override
	public boolean moveBall()
	{
		if (this.move)
		{
			this.move = false;
			return true;
		}
		return this.move;
	}

	public boolean getMove()
	{
		return move;
	}


	@Override
	public boolean moveBall2() {
		return false;
	}

	@Override
	public Vector3 getDirectionVector()
	{
		if (direction != null)
		{
			Vector3 temp = new Vector3(direction);
			direction.set(0,0,0);
			return temp;
		}
		return direction;
	}

	public void setDirectionVector(Vector3 direction)
	{
		this.direction = direction;
	}
}
