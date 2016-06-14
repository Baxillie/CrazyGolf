package nl.dke12.bot.pathfinding;

import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Created by Ajki on 07/06/2016.
 */
public class AStar
{
    private Grid grid;
    private Node startNode;
    private Node endNode;
    private PriorityQueue<Node> opened;
    private PriorityQueue<Node> closed;
    private final Comparator<Node> COMPARATOR = new Comparator()
    {
        @Override
        public int compare(Object o1, Object o2)
        {
            int a = ((Node) o1).getFitness();
            int b = ((Node) o2).getFitness();

            if(a == b)
            {
                return 0;
            }
            else if(a > b)
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
    };

    public AStar(Grid grid)
    {
        this.grid = grid;
        this.endNode = grid.getEndNode();
        this.startNode = grid.getStartNode();
    }

    public void setFitnesses()
    {
        Node currentNode;
        for(int i =0; i < grid.getWidth(); i++)
        {
            for(int j = 0; j < grid.getHeight(); j++)
            {
                currentNode = grid.getNode(i,j);
                currentNode.distanceCost = (Math.abs(grid.endNodeX - i) + Math.abs(grid.endNodeY - j)) * 10;
            }
        }
    }

    public Node calculatePath()
    {
        setFitnesses();
        opened = new PriorityQueue<>(COMPARATOR);
        closed = new PriorityQueue<>(COMPARATOR);
        opened.add(startNode);
        boolean target = false;

        while(!opened.isEmpty() && !target)
        {
            Node nextNode = opened.poll();
            if(nextNode.equals(endNode))
            {
                target = true;
                continue;
            }
            closed.add(nextNode);

            Stack<Node> neighbours = getNeighboursOfNode(nextNode);
            while(!neighbours.isEmpty())
            {
                Node neighbouringNode = neighbours.pop();
            }
        }
        return endNode;
    }
    /*
    While there are still more possible next steps in the open list and we haven’t found the target:
    Select the most likely next step (based on both the heuristic and path costs)
    Remove it from the open list and add it to the closed
    Consider each neighbor of the step. For each neighbor:
    Calculate the path cost of reaching the neighbor
    If the cost is less than the cost known for this location then remove it from the open or closed lists (since we’ve now found a better route)
    If the location isn’t in either the open or closed list then record the costs for the location and add it to the open list (this means it’ll be considered in the next search). Record how we got to this location
    The loop ends when we either find a route to the destination or we run out of steps. If a route is found we back track up the records of how we reached each location to determine the path
    */

    public Stack<Node> getNeighboursOfNode(Node node)
    {
        Stack<Node> neighbours = new Stack<>();
        for(int i = -1 ; i <= 1; i++)
        {
            for(int j = -1; j <= 1; j++)
            {
                try
                {
                    if(i == 0 && j == 0)
                    {
                        continue;
                    }
                    neighbours.add(grid.getNode(i,j));
                }
                catch (ArrayIndexOutOfBoundsException e)
                {}
            }
        }
        return neighbours;
    }
}

