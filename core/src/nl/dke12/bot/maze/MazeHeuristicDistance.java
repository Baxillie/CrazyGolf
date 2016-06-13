package nl.dke12.bot.maze;

import nl.dke12.bot.pathfinding.HeuristicMethod;
import nl.dke12.bot.pathfinding.MapNode;

/**
 * Created by nik on 6/10/16.
 */
public class MazeHeuristicDistance implements HeuristicMethod<MazeMapNode>
{
    @Override
    public int heuristicValue(MazeMapNode a, MazeMapNode b)
    {
        return (Math.abs(a.getX()-b.getX())) + Math.abs((a.getY()-b.getY()));
    }
}
