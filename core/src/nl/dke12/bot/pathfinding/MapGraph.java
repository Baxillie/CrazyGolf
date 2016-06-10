package nl.dke12.bot.pathfinding;

import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * Created by Ajki on 07/06/2016.
 */
public class MapGraph {
    private MapNode startNode;
    private MapNode goalNode;
    private ArrayList<MapNode> graph;

    public MapGraph(MapNode startNode, MapNode goalNode) {
        this.startNode = startNode;
        this.goalNode = goalNode;
        this.graph = new ArrayList<>();
        graph.add(startNode);
        graph.add(goalNode);
    }

    public MapGraph(MapNode startNode, MapNode goalNode, ArrayList<MapNode> allNodes)
    {
        this.startNode = startNode;
        this.goalNode  = goalNode;
        this.graph = allNodes;
        System.out.println("length of graph:" + graph.size());
    }

    public MapNode getStartNode() {
        return startNode;
    }

    public MapNode getGoalNode() {
        return goalNode;
    }

    @Override
    public String toString()
    {
        String toReturn = new String();
        for(MapNode n: graph)
        {
            toReturn += String.format("%s%n", n.toString());
        }
        return toReturn;
    }

    public void printFullInformation()
    {
        for(MapNode n : graph)
        {
            System.out.print(n.fullInformation());
        }
    }
}
