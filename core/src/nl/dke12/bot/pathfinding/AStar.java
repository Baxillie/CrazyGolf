package nl.dke12.bot.pathfinding;

import java.util.*;

/**
 * Created by Ajki on 07/06/2016.
 */
public class AStar
{/*
    // For each node, which node it can most efficiently be reached from.
    // If a node can be reached from many nodes, cameFrom will eventually contain the
    // most efficient previous step.
    cameFrom := the empty map

    // For each node, the cost of getting from the start node to that node.
    gScore := map with default value of Infinity
    // The cost of going from start to start is zero.
    gScore[start] := 0
    // For each node, the total cost of getting from the start node to the goal
    // by passing by that node. That value is partly known, partly heuristic.
    fScore := map with default value of Infinity
    // For the first node, that value is completely heuristic.
    fScore[start] := heuristic_cost_estimate(start, goal)

    while openSet is not empty
    current := the node in openSet having the lowest fScore[] value
    if current = goal
    return reconstruct_path(cameFrom, current)

    openSet.Remove(current)
        closedSet.Add(current)
        for each neighbor of current
    if neighbor in closedSet
    continue		// Ignore the neighbor which is already evaluated.
    // The distance from start to a neighbor
    tentative_gScore := gScore[current] + dist_between(current, neighbor)
    if neighbor not in openSet	// Discover a new node
    openSet.Add(neighbor)
        else if tentative_gScore >= gScore[neighbor]
        continue		// This is not a better path.

    // This path is the best until now. Record it!
    cameFrom[neighbor] := current
    gScore[neighbor] := tentative_gScore
    fScore[neighbor] := gScore[neighbor] + heuristic_cost_estimate(neighbor, goal)
*/

    public int getScore (HashMap<MapNode, Integer> scores, MapNode node)
    {
        if (!scores.containsKey(node))
            scores.put(node, Integer.MAX_VALUE);

        return scores.get(node);
    }

    public ArrayList<MapNode> calculatePath(MapGraph mapgraph) throws PathNotFoundException
    {
        MapNode beginNode = mapgraph.getStartNode();
        MapNode endNode = mapgraph.getGoalNode();
        HashMap<MapNode, Integer> fScores = new HashMap<>();
        HashMap<MapNode, MapNode> path = new HashMap<>();
        HashMap<MapNode, Integer> gScores = new HashMap<>();

        //System.out.println(beginNode);
        //mapgraph.printFullInformation();

        final Comparator<MapNode> comparator = new Comparator<MapNode>()
        {
            @Override
            public int compare(MapNode o1, MapNode o2)
            {
                int a = getScore(fScores, o1);
                int b = getScore(fScores, o2);

                if(a < b)
                    return 1;
                else if (a > b)
                    return -1;
                else
                    return 0;
            }
        };

        PriorityQueue<MapNode> opened = new PriorityQueue<>(comparator);
        opened.add(beginNode);
        gScores.put(beginNode, 0);
        fScores.put(beginNode, mapgraph.heuristicDistance(beginNode, endNode));

        PriorityQueue<MapNode> closed = new PriorityQueue<>(comparator);

        while(!opened.isEmpty())
        {
            //System.out.println("opened is not empty");
            MapNode currentNode = opened.poll();
            if(currentNode.equals(endNode))
            {
                //System.out.println("finished searching");
                return constructPath(path, endNode);
            }

            closed.add(currentNode);

            currentNode.fullInformation();

            for(MapNode neighbour : currentNode.getNeighbours())
            {
                if(closed.contains(neighbour))
                {
                    //System.out.println("node already in closed list");
                    continue;
                }

                int tentativeGScore = Integer.MAX_VALUE;

                try
                {
                    tentativeGScore = getScore(gScores, currentNode) + neighbour.getTravelCostToNeighbour(currentNode);
                }
                catch (MapNode.NeighbourException e)
                {
                    System.out.println("we fucked the neighbours up");
                    e.printStackTrace();
                }

                if(!opened.contains(neighbour)) //discover new node
                {
                    //System.out.println("node added to open list");
                    opened.add(neighbour);
                }
                else if (tentativeGScore >= getScore(gScores, neighbour)) //not better than the current one
                {
                    //System.out.println("node not better than current one");
                    continue;
                }
                // found new best node
                //System.out.println("best node found");
                path.put(neighbour, currentNode);
                gScores.put(neighbour, tentativeGScore);
                fScores.put(neighbour, getScore(gScores,neighbour)+ mapgraph.heuristicDistance(neighbour, endNode));
            }
        }
        System.out.println("opened is empty");
        throw new PathNotFoundException();
    }

    private ArrayList<MapNode> constructPath(HashMap<MapNode, MapNode> path, MapNode endNode)
    {
        ArrayList<MapNode> thePath = new ArrayList<>();
        MapNode node = endNode;

        while(node != null)
        {
            thePath.add(node);
            node = path.get(node);
        }

        return thePath;
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

    public Stack<MapNode> getNeighboursOfNode(MapNode node)
    {
        Stack<MapNode> stack = new Stack();
        for(MapNode neighbour : node.getNeighbours())
        {
            stack.add(neighbour);
        }
        return stack;
    }
}

