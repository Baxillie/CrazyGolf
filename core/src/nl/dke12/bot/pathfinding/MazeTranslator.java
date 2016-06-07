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
        int endNodeX = 0;
        int endNodeY = 0;
        int startNodeX = 0;
        int startNodeY = 0;

        for(int i = 0; i < grid.length; i++)
        {
            for(int j = 0; j < grid[i].length; j++)
            {
                Node gridNode = null;
                char mazeChar = ' ';
                try
                {
                    mazeChar = maze.getCell(i, j);
                } catch (ArrayIndexOutOfBoundsException e)
                {
                    System.out.println("Creation of grid by the MazeTranslator went wrong. Error in dimensions");
                }

                if (mazeChar == Maze.openChar)
                {
                    gridNode = new Node(true);
                } else
                {
                    gridNode = new Node(false);

                    if(mazeChar == Maze.startChar)
                    {
                        gridNode.isStartNode = true;
                        startNodeX = i;
                        startNodeY = j;
                    }
                    else if(mazeChar == Maze.endChar)
                    {
                        gridNode.isEndNode = true;
                        endNodeX = i;
                        endNodeY = j;
                    }
                    else
                    {
                        gridNode.isEndNode = false;
                        gridNode.isStartNode = false;
                    }
                }
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
