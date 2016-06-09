package nl.dke12.bot.pathfinding;

/**
 * Created by Ajki on 07/06/2016.
 */
public class Node
{
    protected boolean walkable;
    protected boolean explored;
    protected Node parent;
    private int fitness;
    protected int stepCost;
    protected int distanceCost;
    protected int accumulativeStepCost = 0;
    protected boolean isEndNode;
    protected boolean isStartNode;
    protected int x;
    protected int y;

    public Node(boolean walkable)
    {
        this.walkable = walkable;
        this.explored = false;
    }

    public int getFitness()
    {
        this.fitness = this.stepCost + this.distanceCost;
        return this.fitness;
    }

    public boolean isEndNode()
    {
        return isEndNode;
    }

    public boolean isStartNode()
    {
        return isStartNode;
    }
}
