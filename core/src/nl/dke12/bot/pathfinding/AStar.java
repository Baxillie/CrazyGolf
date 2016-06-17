package nl.dke12.bot.pathfinding;

import java.util.*;

/**
 * Created by Ajki on 07/06/2016.
 */
public class AStar
{
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


        final Comparator<MapNode> comparator = new Comparator<MapNode>()
        {
            @Override
            public int compare(MapNode o1, MapNode o2)
            {
                int a = getScore(fScores, o1);
                int b = getScore(fScores, o2);

                if(a < b)
                    return -1;
                else if (a > b)
                    return 1;
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
            MapNode currentNode = opened.poll();
            if(currentNode.equals(endNode))
            {
                return constructPath(path, endNode);
            }
            if(!currentNode.isWalkable())
            {
                continue;
            }


            closed.add(currentNode);

            currentNode.fullInformation();

            for(MapNode neighbour : currentNode.getNeighbours())
            {
                if(closed.contains(neighbour))
                {
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
                    opened.add(neighbour);
                }
                if (tentativeGScore >= getScore(gScores, neighbour)) //not better than the current one
                {
                    continue;
                }
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
            thePath.add(0, node);
            node = path.get(node);
        }

        return thePath;
    }
}

