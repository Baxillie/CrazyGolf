package nl.dke12.bot.maze;

import nl.dke12.bot.pathfinding.MapGraph;
import nl.dke12.bot.pathfinding.MapGraphFactory;
import nl.dke12.bot.pathfinding.MapNode;

import java.util.ArrayList;

/**
 * Created by Ajki on 07/06/2016.
 */
public class MazeTranslator implements MapGraphFactory<Maze>
{
    private final boolean debug = false;

    @Override
    public MapGraph makeMapGraph(Maze maze)
    {
        char[][] grid =  maze.getMaze();
        int endNodeX = maze.endCoords[1];
        int endNodeY = maze.endCoords[0];
        int startNodeX = maze.beginCoords[1];
        int startNodeY = maze.beginCoords[0];

        if(debug)
        {
            System.out.println("making a map graph of the following maze:");
            maze.printMaze();
            System.out.printf("Start node is at x: %d\ty: %d\nEnd node is at x: %d\ty: %d\n",
                    startNodeX, startNodeY, endNodeX, endNodeY);
        }

        MapNode[][] mapNodeGrid = new MapNode[grid.length][grid[0].length];

        for(int i = 0; i < grid.length; i++)
        {
            for(int j = 0; j < grid[i].length; j++)
            {
                if(grid[i][j] == Maze.openChar)
                {
                    getMapNode(j,i, mapNodeGrid);
                    giveNeighbours(j,i, mapNodeGrid, grid);
                }
            }
        }

        MapGraph theMapGraph = new MapGraph(
                getMapNode(startNodeX, startNodeY, mapNodeGrid),
                getMapNode(endNodeX, endNodeY, mapNodeGrid),
                getArrayListOfAllNodes(mapNodeGrid),
                new MazeHeuristicDistance()
                );
        return theMapGraph;
    }

    private MapNode getMapNode(int x, int y, MapNode[][] grid) throws ArrayIndexOutOfBoundsException
    {
        if(grid[y][x] == null)
        {
            grid[y][x] = new MazeMapNode(x,y);
        }
        return grid[y][x];
    }

    private void giveNeighbours(int x, int y, MapNode[][] grid, char[][] charGrid)
    {
        MapNode node = grid[y][x];
        for(int i = -1; i < 2; i++)
        {
            for(int j = -1; j < 2; j++)
            {
                if( i == 0 && j == 0)
                {
                    continue;
                }
                else
                {
                    try {
                        if ( charGrid[y + i][x + j] == Maze.openChar)
                        {
                            MapNode neighbouringNode = getMapNode(x + j, y + i, grid);
                            if(x == x+j || y == y+i) //not diagonal
                            {
                                node.giveNeighbour(neighbouringNode, 10);
                            }
                            else
                            {
                                node.giveNeighbour(neighbouringNode, 14);
                            }
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException e){} //handles getting nodes outside of grid, e.g a node at a wall
                    catch (MapNode.NeighbourException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private ArrayList<MapNode> getArrayListOfAllNodes(MapNode[][] grid)
    {
        ArrayList<MapNode> arrayList = new ArrayList<>();
        for(int i = 0; i < grid.length; i++)
        {
            for(int j = 0; j < grid[i].length; j++)
            {
                if(grid[i][j] != null)
                {
                    arrayList.add(grid[i][j]);
                }
            }
        }
        return arrayList;
    }

}
