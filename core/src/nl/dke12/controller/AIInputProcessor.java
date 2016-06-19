package nl.dke12.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import nl.dke12.bot.pathfinding.BotShotInterpolation;
import nl.dke12.bot.pathfinding.MapNode;

import java.util.ArrayList;

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
        return Gdx.input.isKeyPressed(Actions.MOVE_CAMERA_LEFT);
    }

    public boolean moveCamBack()
    {
        return Gdx.input.isKeyPressed(Actions.MOVE_CAMERA_BACK);
    }

    public boolean moveCamRight()
    {
        return Gdx.input.isKeyPressed(Actions.MOVE_CAMERA_RIGHT);
    }

    public boolean rotateCamAntiClock()
    {
        return Gdx.input.isKeyPressed(Actions.ROTATE_CAMERA_ANTICLOCKWISE);
    }

    public boolean moveCamForward()
    {
        return Gdx.input.isKeyPressed(Actions.MOVE_CAMERA_FORWARD);
    }

    public boolean rotateCamClock()
    {
        return Gdx.input.isKeyPressed(Actions.ROTATE_CAMERA_CLOCKWISE);
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

    public Vector3 getDirectionVectorFromInterpolation(ArrayList<MapNode> path)
    {
        BotShotInterpolation interpolation = new BotShotInterpolation(path);
        return interpolation.getDirectionVector();
    }


    public void setDirectionVector(Vector3 direction)
    {
        this.direction = direction;
    }
}
