package nl.dke12.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Ajki on 23/05/2016.
 */
public interface InputProcessor {

    boolean moveCamLeft();

    boolean moveCamBack();

    boolean moveCamRight();

    boolean rotateCamAntiClock();

    boolean moveCamForward();

    boolean rotateCamClock();

    boolean moveBall();

    boolean moveBall2();

    boolean decreaseForce();

    boolean increaseForce();

    boolean increaseHeight();

    boolean decreaseHeight();

    Vector3 getDirectionVector();
}
