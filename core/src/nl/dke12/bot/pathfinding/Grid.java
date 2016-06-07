package nl.dke12.bot.pathfinding;

/**
 * Created by Ajki on 07/06/2016.
 */
public class Grid
{
    private Node[][] grid;
    protected int endNodeX;
    protected int endNodeY;
    protected int startNodeX;
    protected int startNodeY;


    public Grid(Node[][] grid)
    {
        this.grid = grid;
    }

    public Node getNode(int x, int y) throws ArrayIndexOutOfBoundsException
    {
        try
        {
            return grid[x][y];
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            throw e;
        }
    }
    public  Node getEndNode()
    {
        return grid[]
    }
    public int getWidth()
    {
        return grid.length;
    }

    public int getHeight()
    {
        return grid[0].length;
    }
}
