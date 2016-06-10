package nl.dke12.bot.pathfinding;

import java.util.*;

/**
 * Created by Ajki on 07/06/2016.
 */
public class AStar
{
    public ArrayList<MapNode> calculatePath(MapNode beginNode, MapNode endNOde) throws PathNotFoundException
    {
        //System.out.printf("Beginning is at x: %d y: %d%n", beginNode.x, startNode.y);
        //System.out.printf("End is at x: %d y: %d%n", endNode.x, endNode.y);




        while(!opened.isEmpty() && !targetFound)
        {
            Node currentNode = opened.poll();
          //  System.out.printf("considering node at x:%d y:%d\n", currentNode.x, currentNode.y);
            if(currentNode.equals(endNode))
            {
                targetFound = true;
                continue;
            }
            closed.add(currentNode);

            Stack<Node> neighbours = getNeighboursOfNode(currentNode);
        //    System.out.println("Starting to look at neighbours:");
            while(!neighbours.isEmpty()) {
                Node neighbouringNode = neighbours.pop();
                //System.out.printf("considering neighbour at x:%d y:%d\n", neighbouringNode.x, neighbouringNode.y);
                if (closed.contains(neighbouringNode)) {
                    continue; //already considered this neighbour
                }
                int tentiveStepCost = currentNode.accumulativeStepCost + accumalitiveStepCost;
                if (!opened.contains(neighbouringNode)) {
                    opened.add(neighbouringNode);
                }
                else if (tentiveStepCost >= neighbouringNode.stepCost) //not better than the current one
                {
                    continue;
                }
                else // found new best node
                {

               //     System.out.println("setting parent");
                    currentNode.parent = neighbouringNode;
                    neighbouringNode.stepCost = tentiveStepCost;
                    System.out.printf("node at x:%d y:%d\n", currentNode.x, currentNode.y);
                    System.out.printf("parent at x:%d y:%d\n", neighbouringNode.x, neighbouringNode.y);

                }
            }
        }

        if(targetFound)
        {
            return constructPath(endNode);
        }
        else
        {
            throw new PathNotFoundException();
        }
    }

    private ArrayList<Node> constructPath(MapNode endNode)
    {
        ArrayList<Node> path = new ArrayList<>();
        MapNode currentNode = endNode;
        path.add(currentNode);
        System.out.printf("node's parent is :%s", currentNode.parent.toString());
        while(currentNode.parent != null)
        {
            Node nodeInPath = currentNode.parent;
            path.add(nodeInPath);
            currentNode = nodeInPath;
        }
        return path;
    }

    public class PathNotFoundException extends Exception
    {

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

    public Stack<Node> getNeighboursOfNode(MapNode node)
    {
        Stack<MapNode> stack = new Stack();
        for(MapNode neighbour : node.getNeighbours())
        {
            stack.add(neighbour);
        }
    }
}

