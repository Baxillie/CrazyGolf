package nl.dke12.bot.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
//import java.util.Objects;

/**
 * Created by nik on 09/06/16.
 */
public abstract class MapNode
{
    private static int nodeCounter = 0;
    private String identifier;
    private ArrayList<MapNode> neighbours;
    protected int cost;
    private HashMap<MapNode, Integer> costs;
    private boolean walkable;

    public MapNode()
    {
        this(Integer.toString(nodeCounter));
        nodeCounter++;
        this.walkable = true;
    }

    public MapNode(String identifier)
    {
        this.identifier = identifier;
        this.neighbours = new ArrayList<MapNode>();
        this.costs = new HashMap<MapNode, Integer>();
        this.walkable = true;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public void setWalkable(boolean walkable)
    {
        this.walkable = walkable;
    }

    public boolean isWalkable()
    {
        return walkable;
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
            return ((MapNode) o).getIdentifier().equals(this.identifier);
            //return false;
        }
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

    public int getCost()
    {
        return cost;
    }

    public void setCost(int cost)
    {
        this.cost = cost;
    }
}
