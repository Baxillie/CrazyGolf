package nl.dke12.bot.pathfinding;

/**
 * Created by Ajki on 07/06/2016.
 */
public class AStar
{
    private Grid grid;

    public AStar(Grid grid)
    {
        this.grid = grid;
    }

    public Node calculatePath()
    {
        Node endNode = grid.getEndNode();
        Node startNode = grid.getNode(grid.startNodeX, grid.startNodeY);



        return endNode;
    }
}
