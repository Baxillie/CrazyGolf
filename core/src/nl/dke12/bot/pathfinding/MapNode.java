package nl.dke12.bot.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by nik on 09/06/16.
 */
public abstract class MapNode
{
    private static int nodeCounter = 0;
    private String identifier;
    private ArrayList<MapNode> neighbours;
    private HashMap<MapNode, Integer> costs;

    public MapNode()
    {
        this(Integer.toString(nodeCounter));
        nodeCounter++;
    }

    public MapNode(String identifier)
    {
        this.identifier = identifier;
        this.neighbours = new ArrayList<>();
        this.costs = new HashMap<>();
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public ArrayList<MapNode> getNeighbours()
    {
        return neighbours;
    }

    public void giveNeighbour(MapNode potentialNeighbour, int travelCost) throws NeighbourException
    {
        if(potentialNeighbour.equals(this))
        {
            throw new NeighbourException("Argument {MapNode} was itself. Cannot neighbour itself");
        }
        neighbours.add(potentialNeighbour);
        costs.put(potentialNeighbour, travelCost);
    }

    public int getTravelCostToNeighbour(MapNode neighbour) throws NeighbourException
    {
        Integer cost = costs.get(neighbour);
        if(cost == null)
        {
            throw new NeighbourException("Argument {MapNode} is not a neighbour, cannot retrieve travel cost");
        }
        else
        {
            return cost.intValue();
        }
    }

    public class NeighbourException extends Exception
    {
        public NeighbourException(String errorMessage)
        {
            super(errorMessage);
        }
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

    @Override
    public String toString()
    {
        return String.format("{Mapnode;id[\"%s\"]}",identifier);
    }

    private String neighboursToString()
    {
        String toReturn = new String();
        for(MapNode neighbour : neighbours)
        {
            toReturn += String.format("\t%s%n", neighbour.toString());
        }
        return toReturn;
    }

    public String fullInformation()
    {
        return toString() + "\n" + neighboursToString();
    }

}
