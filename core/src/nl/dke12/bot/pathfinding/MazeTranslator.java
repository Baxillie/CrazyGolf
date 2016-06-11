package nl.dke12.bot.pathfinding;

/**
 * Created by Ajki on 07/06/2016.
 */
public class MazeTranslator extends GridTranslator<Maze>
{
    private Maze maze;
    public MazeTranslator()
    {
        super();
    }

    @Override
    public Grid makeGrid(Maze maze)
    {
        Node[][] grid =  new Node[maze.getHeight()][maze.getWidth()];
        int endNodeX = maze.endCoords[0];
        int endNodeY = maze.endCoords[1];
        int startNodeX = maze.beginCoords[0];
        int startNodeY = maze.beginCoords[1];

        for(int i = 0; i < grid.length; i++)
        {
            for(int j = 0; j < grid[i].length; j++)
            {
                Node gridNode = null;
                char mazeChar = '-';
                try
                {
                    mazeChar = maze.getCell(i, j);
                } catch (ArrayIndexOutOfBoundsException e)
                {
                    System.out.println("Creation of grid by the MazeTranslator went wrong. Error in dimensions");
                }

                if (mazeChar == Maze.openChar)
                {
                    gridNode = new Node();
                    gridNode.walkable = true;
                }
                else
                {
                    gridNode = new Node();

                    if(i == startNodeX && j == startNodeY)
                    {
                        gridNode.isStartNode = true;
                        //startNodeX = i;
                        //startNodeY = j;
                        gridNode.walkable = true;
                    }
                    else if(i == endNodeX && j == endNodeY)
                    {
                        gridNode.isEndNode = true;
                        //endNodeX = i;
                        //endNodeY = j;
                        gridNode.walkable = true;
                    }
                    else
                    {
                        gridNode.isEndNode = false;
                        gridNode.isStartNode = false;
                        gridNode.walkable = false;
                    }
//                    if(mazeChar != Maze.wallchar)
//                    {
//                        gridNode.walkable = true;
//                    }
                }

                gridNode.x = j;
                gridNode.y = i;
                grid[i][j] = gridNode;
            }
        }

        Grid theGrid  = new Grid(grid);
        theGrid.endNodeX = endNodeX;
        theGrid.endNodeY = endNodeY;
        theGrid.startNodeX = startNodeX;
        theGrid.startNodeY = startNodeY;

        return theGrid;
    }


}
