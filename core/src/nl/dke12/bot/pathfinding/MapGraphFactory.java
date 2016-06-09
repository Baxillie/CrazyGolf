package nl.dke12.bot.pathfinding;

/**
 * Created by Ajki on 07/06/2016.
 */
public abstract class MapGraphFactory<E>
{
    public MapGraphFactory() {}

    public abstract MapGraph makeMapGraph(E e);


}
