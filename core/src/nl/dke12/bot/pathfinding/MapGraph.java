package nl.dke12.bot.pathfinding;

import java.util.ArrayList;

/**
 * Created by Ajki on 07/06/2016.
 */
public class MapGraph {
    private Node startNode;
    private Node goalNode;
    private ArrayList<Node> graph;

    public MapGraph(Node startNode, Node goalNode) {
        this.startNode = startNode;
        this.goalNode = goalNode;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getGoalNode() {
        return goalNode;
    }
}
