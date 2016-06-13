package nl.dke12.bot.pathfinding;

import nl.dke12.bot.maze.MazeMapNode;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Ajki on 13/06/2016.
 */
public class FloodFill
{
    public void calculateCosts(MapGraph mapGraph) throws PathNotFoundException
    {
        HashMap<MapNode, Integer> costmap = new HashMap<>();
        Stack<MapNode> checked = new Stack<>();
        LinkedBlockingQueue<MapNode> queue = new LinkedBlockingQueue<>();

        MapNode currentNode = mapGraph.getStartNode();

        queue.add(currentNode);
        costmap.put(currentNode, 0);
        checked.add(currentNode);

        while(!queue.isEmpty())
        {
            currentNode = queue.poll();
            for(MapNode n : currentNode.getNeighbours())
            {
                if (!checked.contains(n))
                {
                    queue.add(n);
                    costmap.put(n, costmap.get(currentNode) + 1);
                    checked.add(n);
                }
            }
        }

        //update the cost of all the nodes
        for(MapNode n : costmap.keySet())
        {
            n.cost = costmap.get(n);
        }
    }

    private void printMazeCosts(MapGraph mapGraph)
    {
        char[][] grid = new char[10][10];

        int[][] floodGrid = new int[grid.length][grid[0].length];

        for(MapNode n : mapGraph.getGraph() )
        //for(MapNode n : path2)
        {
            String identifier = n.getIdentifier();
            MazeMapNode node = (MazeMapNode) n;
            int x = node.getX();
            int y = node.getY();

            floodGrid[y][x] = n.getCost();
        }
        int p = 0;
        for(int i = 0; i < grid.length; i++)
        {
            for(int j = 0; j < grid[i].length; j++)
            {
                p = floodGrid[i][j];
                if(p < 10)
                    System.out.print("     " + p);
                else if (p < 100)
                    System.out.print("    "  + p);
                else
                    System.out.print("   "   + p);
            }
            System.out.println();
        }
        System.out.println();
    }


    public ArrayList<MapNode> calculatePath(MapGraph mapGraph) throws PathNotFoundException
    {
        MapNode startNode = mapGraph.getStartNode();

        calculateCosts(mapGraph);

        ArrayList<MapNode> thePath = new ArrayList<>();
        MapNode currentNode = mapGraph.getGoalNode();
        thePath.add(currentNode);

        final Comparator<MapNode> comparator = new Comparator<MapNode>()
        {
            @Override
            public int compare(MapNode o1, MapNode o2)
            {
                int a = o1.cost;
                int b = o2.cost;

                if(a < b)
                    return -1;
                else if (a > b)
                    return 1;
                else
                    return 0;
            }
        };

        PriorityQueue<MapNode> neighbours = new PriorityQueue<>(comparator);
        ArrayList<MapNode> potentialNeighbour = new ArrayList<>();
        Random rng = new Random(System.currentTimeMillis());

        while(!currentNode.equals(startNode))
        {
            neighbours.clear();

            for(MapNode n : currentNode.getNeighbours())
            {
                neighbours.add(n);
            }

            potentialNeighbour.clear();
            int temp = neighbours.peek().cost;

            while(neighbours.peek().cost == temp)
            {
                potentialNeighbour.add(neighbours.poll());
            }

            temp = rng.nextInt(potentialNeighbour.size());

            thePath.add(potentialNeighbour.get(temp));
            currentNode = potentialNeighbour.get(temp);
        }

        return thePath;
    }

}
