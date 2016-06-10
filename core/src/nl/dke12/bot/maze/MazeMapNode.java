package nl.dke12.bot.maze;

import nl.dke12.bot.pathfinding.MapNode;

/**
 * Created by nik on 09/06/16.
 */
public class MazeMapNode extends MapNode
{
    private int x;
    private int y;

    public MazeMapNode(int x, int y)
    {
        super(String.format("%2d;%2d",x,y));
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}