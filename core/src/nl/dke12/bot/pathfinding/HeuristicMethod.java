package nl.dke12.bot.pathfinding;

/**
 * Created by nik on 6/10/16.
 */
public interface HeuristicMethod<E>
{
    int heuristicValue(E a, E b);
}
