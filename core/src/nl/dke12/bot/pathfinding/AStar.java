package nl.dke12.bot.pathfinding;

import java.util.*;

/**
 * Created by Ajki on 07/06/2016.
 */
public class AStar
{
//    private MapGraph mapGraph;
//    private Node startNode;
//    private Node endNode;
//    private PriorityQueue<Node> opened;
//    private PriorityQueue<Node> closed;
//    private final Comparator<Node> COMPARATOR = new Comparator()
//    {
//        @Override
//        public int compare(Object o1, Object o2)
//        {
//            int a = ((Node) o1).getFitness();
//            int b = ((Node) o2).getFitness();
//
//            if(a == b)
//            {
//                return 0;
//            }
//            else if(a > b)
//            {
//                return 1;
//            }
//            else
//            {
//                return -1;
//            }
//        }
//    };
//
//    public AStar(MapGraph mapGraph)
//    {
//        this.mapGraph = mapGraph;
//        this.endNode = mapGraph.getEndNode();
//        this.startNode = mapGraph.getStartNode();
//    }
//
//    public void setFitnesses()
//    {
//        Node currentNode;
//        for(int i = 0; i < mapGraph.getWidth(); i++)
//        {
//            for(int j = 0; j < mapGraph.getHeight(); j++)
//            {
//                currentNode = mapGraph.getNode(i,j);
//                currentNode.distanceCost = (Math.abs(mapGraph.endNodeX - i) + Math.abs(mapGraph.endNodeY - j));
//                currentNode.stepCost = Integer.MAX_VALUE / 2; //infinite
//            }
//        }
//    }
//
//    public ArrayList<Node> calculatePath() throws PathNotFoundException
//    {
//        System.out.printf("Beginning is at x: %d y: %d%n", startNode.x, startNode.y);
//        System.out.printf("End is at x: %d y: %d%n", endNode.x, endNode.y);
//
//        setFitnesses();
//        opened = new PriorityQueue<>(COMPARATOR);
//        closed = new PriorityQueue<>(COMPARATOR);
//        opened.add(startNode);
//        boolean targetFound = false;
//
////        HashMap<Node, Node> nodeCameFrom = new HashMap<>();
////        ageDatabase.put("jenny", 2);
////        ageDatabase.put("vinc", 3);
////        for(String i: ageDatabase.keySet())
////        {
////            ageDatabase.get(i);
////        }
//
//        startNode.accumulativeStepCost = 0;
//        int accumalitiveStepCost = 0;
//        while(!opened.isEmpty() && !targetFound)
//        {
//            Node currentNode = opened.poll();
//          //  System.out.printf("considering node at x:%d y:%d\n", currentNode.x, currentNode.y);
//            if(currentNode.equals(endNode))
//            {
//                targetFound = true;
//                continue;
//            }
//            closed.add(currentNode);
//
//            Stack<Node> neighbours = getNeighboursOfNode(currentNode);
//        //    System.out.println("Starting to look at neighbours:");
//            while(!neighbours.isEmpty()) {
//                Node neighbouringNode = neighbours.pop();
//                //System.out.printf("considering neighbour at x:%d y:%d\n", neighbouringNode.x, neighbouringNode.y);
//                if (closed.contains(neighbouringNode)) {
//                    continue; //already considered this neighbour
//                }
//                int tentiveStepCost = currentNode.accumulativeStepCost + accumalitiveStepCost;
//                if (!opened.contains(neighbouringNode)) {
//                    opened.add(neighbouringNode);
//                }
//                else if (tentiveStepCost >= neighbouringNode.stepCost) //not better than the current one
//                {
//                    continue;
//                }
//                else // found new best node
//                {
//
//               //     System.out.println("setting parent");
//                    currentNode.parent = neighbouringNode;
//                    neighbouringNode.stepCost = tentiveStepCost;
//                    System.out.printf("node at x:%d y:%d\n", currentNode.x, currentNode.y);
//                    System.out.printf("parent at x:%d y:%d\n", neighbouringNode.x, neighbouringNode.y);
//
//                }
//            }
//        }
//
//        if(targetFound)
//        {
//            return constructPath(endNode);
//        }
//        else
//        {
//            throw new PathNotFoundException();
//        }
//    }
//
//    private ArrayList<Node> constructPath(Node endNode)
//    {
//        ArrayList<Node> path = new ArrayList<>();
//        Node currentNode = endNode;
//        path.add(currentNode);
//        System.out.printf("node's parent is :%s", currentNode.parent.toString());
//        while(currentNode.parent != null)
//        {
//            Node nodeInPath = currentNode.parent;
//            path.add(nodeInPath);
//            currentNode = nodeInPath;
//        }
//        return path;
//    }
//
//    public class PathNotFoundException extends Exception
//    {
//
//    }
//    /*
//    While there are still more possible next steps in the open list and we haven’t found the target:
//    Select the most likely next step (based on both the heuristic and path costs)
//    Remove it from the open list and add it to the closed
//    Consider each neighbor of the step. For each neighbor:
//    Calculate the path cost of reaching the neighbor
//    If the cost is less than the cost known for this location then remove it from the open or closed lists (since we’ve now found a better route)
//    If the location isn’t in either the open or closed list then record the costs for the location and add it to the open list (this means it’ll be considered in the next search). Record how we got to this location
//    The loop ends when we either find a route to the destination or we run out of steps. If a route is found we back track up the records of how we reached each location to determine the path
//    */
//
//    public Stack<Node> getNeighboursOfNode(Node node)
//    {
//        Stack<Node> neighbours = new Stack<>();
//        for(int i = -1 ; i <= 1; i++)
//        {
//            for(int j = -1; j <= 1; j++)
//            {
//                try
//                {
//                    if(i == 0 && j == 0)
//                    {
//                        continue;
//                    }
//                    Node potentialNeighbour = mapGraph.getNode(node.x + i, node.y +j);
//                    if(potentialNeighbour.walkable)
//                    {
//                        neighbours.add(potentialNeighbour);
//                    }
//                    else
//                    {
//                       // System.out.printf("cant add node at x:%d y:%d because not walkable\n",
//                         //       potentialNeighbour.x, potentialNeighbour.y);;
//                    }
//                }
//                catch (ArrayIndexOutOfBoundsException e)
//                {}
//            }
//        }
//        return neighbours;
//    }
}

