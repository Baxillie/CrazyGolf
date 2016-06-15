package nl.dke12.bot;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by Ajki on 08/06/2016.
 */
public interface BotInterface<E>
{
    E requestCourse(); //E is whichever class you use to represent the golf course

    Vector3 makeSimulationShot(int force, int heightForce); // returns vector where ball will end up

    void makeShot(int force, int heightForce);

    boolean ballIsDoneRolling();

    boolean gameOver();

}
