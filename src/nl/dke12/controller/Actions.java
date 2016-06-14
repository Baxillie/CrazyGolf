package nl.dke12.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by Ajki on 23/05/2016.
 */
public abstract class Actions {

    final static int MOVE_CAMERA_LEFT = Input.Keys.A;

    final static int MOVE_CAMERA_RIGHT = Input.Keys.D;

    final static int MOVE_CAMERA_BACK = Input.Keys.S;

    final static int MOVE_CAMERA_FORWARD = Input.Keys.W;

    final static int ROTATE_CAMERA_CLOCKWISE = Input.Keys.E;

    final static int ROTATE_CAMERA_ANTICLOCKWISE = Input.Keys.Q;

    final static int MOVE_BALL = Input.Keys.SPACE; //Multiplayer does NOT work by these controls (Look in gamecontroller ~Line 190)

    final static int MOVE_BALL_2 = Input.Keys.SPACE; //Multiplayer does NOT work by these controls

    final static int DECREASE_FORCE = Input.Keys.MINUS;

    final static int INCREASE_FORCE = Input.Keys.EQUALS;

    final static int INCREASE_HEIGHT = Input.Keys.RIGHT_BRACKET;

    final static int DECREASE_HEIGHT = Input.Keys.LEFT_BRACKET;

    



}
