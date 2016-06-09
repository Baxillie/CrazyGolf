package nl.dke12.bot.pathfinding;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by nik on 09/06/16.
 */
public abstract class MapNode
{
    private static int nodeCounter = 0;
    private String identifier;
    private ArrayList<MapNode> neighbours;

    public MapNode()
    {
        this(Integer.toString(nodeCounter));
        nodeCounter++;
    }

    public MapNode(String identifier)
    {
        this.identifier = identifier;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public ArrayList<MapNode> getNeighbours()
    {
        return neighbours;
    }

    public void giveNeighbour(MapNode potentialNeighbour) throws CannotNeighbourItselfException
    {
        if(potentialNeighbour.equals(this))
        {
            throw new CannotNeighbourItselfException();
        }
        neighbours.add(potentialNeighbour);
    }

    public class CannotNeighbourItselfException extends Exception
    {

    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        else if (!(o instanceof MapNode))
        {
            return false;
        }
        else
        {
            MapNode mapNode = (MapNode) o;
            return Objects.equals(identifier, mapNode.identifier);
        }
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(identifier);
    }

}
