package nl.dke12.bot.maze;

import nl.dke12.bot.pathfinding.AStar;
import nl.dke12.bot.pathfinding.MapNode;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by nik on 6/6/16.
 */
public class Maze{

    private static boolean debug = false;

    //general information and utility
    private int width;
    private int height;
    protected int[] beginCoords;
    protected int[] endCoords;
    private Random rng;

    //maze details
    private char[][] maze; // b is beginning, e is exit, x is wall
    public static final char startChar = 'b';
    public static final char endChar   = 'e';
    public static final char wallchar  = 'x';
    public static final char openChar  = '-';
    private final char pathChar = '*';

    //copy of maze to give when someone asks to prevent changes in actual maze
    private char[][] mazeCopy;

    public Maze(int width, int height)
    {
        this.width = width;
        this.height = height;
        //this.rng = new Random(System.currentTimeMillis());
        this.rng = new Random(100);
        initialiseBaseMaze();
        createPaths();
        makeWalls();
    }

    private void initialiseBaseMaze()
    {
        maze = new char[height][width];
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                maze[i][j] = openChar;
            }
        }
        //create starting location
        this.beginCoords = new int[] { 0, rng.nextInt(width)};
        maze[beginCoords[0]][beginCoords[1]] = startChar;
        //create end location
        this.endCoords = new int[] { height - 1, rng.nextInt(width)};
        maze[endCoords[0]][endCoords[1]] = endChar;
    }

    private void createPaths()
    {
        //informative variables
        final int minstep = 1;
        final int maxstep = 3;
        final int endX = endCoords[1];
        final int endY = endCoords[0];

        //create randomish correct path from b to e
        int currentX = beginCoords[1];
        int currentY = beginCoords[0];
        boolean goDown = rng.nextBoolean();
        int stepsize = 0;

        //arraylist storing every intersection coordinate of the correct path
        ArrayList<int[]> intersections = new ArrayList<int[]>();

        while(currentX != endX || currentY != endY)
        {
            stepsize = rng.nextInt(maxstep - minstep) + minstep;
            if(debug) {
                System.out.printf("Current x: %d\t end x: %d\ncurrent y: %d\t end y: %d\ngoing down: %b\n" +
                                "stepsize: %d\ncurrent maze:\n",
                        currentX, endX, currentY, endY, goDown, stepsize);
                printMaze();
            }
            if(currentY == endY) //make straight line to the end
            {
                if(debug){
                    System.out.println("because current Y is equal to end Y, making straight line to end");
                }
                if(currentX < endX)
                {
                    stepsize = endX - currentX;
                    currentX += makePathRight(currentX, currentY, stepsize);;
                }
                else
                {
                    stepsize = currentX - endX;
                    currentX -= makePathLeft(currentX, currentY, stepsize);;
                }
            }
            else if(goDown) // go down
            {
//                if(currentY + stepsize >= height) // check for going down out of maze
//                {
//                    stepsize = endY - currentY;
//                }
                if(debug){
                    System.out.println("deciding to make a path down with stepsize: " + stepsize);
                }
                currentY += makePathDown(currentX, currentY, stepsize);;
            }
            else // decide to go left or right
            {
                if(currentX <= 0) //should definitely go to right
                {
//                    if(currentX + stepsize >= width)
//                    {
//                        stepsize = width - currentX;
//                    }
                    if(debug)
                    {
                        System.out.println("deciding to make a path to the right with stepsize: " + stepsize);
                    }
                    currentX += makePathRight(currentX, currentY, stepsize);;
                }
                else if(currentX >= width - 1) //should definitely go left
                {
//                    if(currentX - stepsize < 0)
//                    {
//                        stepsize = currentX;
//                    }
                    if(debug)
                    {
                        System.out.println("deciding to make a path to the left with stepsize: " + stepsize);
                    }
                    currentX -= makePathLeft(currentX, currentY, stepsize);;
                }
                else // randomly decide
                {
                    if(rng.nextBoolean()) // go left
                    {
//                        if(currentX - stepsize < 0)
//                        {
//                            stepsize = currentX;
//                        }
                        if(debug)
                        {
                            System.out.println("deciding to make a path to the left with stepsize: " + stepsize);
                        }
                        currentX -= makePathLeft(currentX, currentY, stepsize);;
                    }
                    else //go right
                    {
//                        if(currentX + stepsize >= width)
//                        {
//                            stepsize = width - currentX;
//                        }
                        if(debug)
                        {
                            System.out.println("deciding to make a path to the right with stepsize: " + stepsize);
                        }
                        currentX += makePathRight(currentX, currentY, stepsize);;
                    }
                }
            }
            goDown = !goDown; //switch from going down to going left or right every loop
            intersections.add(new int[] {currentX, currentY});
            //print information
            if(debug)
            {
                System.out.println("maze after placing path:");
                printMaze();
                System.out.println("##################################################################");
            }
        }
        //make random paths away from the correct path
        createRandomPaths(intersections, rng);
    }

    private void createRandomPaths(ArrayList<int[]> intersections, Random rng)
    {
        final int minRandomPaths = 10;
        final int maxRandomPaths = 20;
        final int minRandomPathLength = width / 4;
        final int maxRandomPathLength = width / 2;
        final int randomPaths = rng.nextInt(maxRandomPaths - minRandomPaths) + minRandomPaths;

        ArrayList<int[]> newIntersections = new ArrayList<int[]>();

        for(int i = 0; i < randomPaths; i++) {
            //select a intersection randomly. higher change to make a new path and the end of a random path
            int[] intersection = null;
            if (!newIntersections.isEmpty() && rng.nextBoolean())
            {
                intersection = newIntersections.get(rng.nextInt(newIntersections.size()));
            }
            else
            {
                 intersection = intersections.get(rng.nextInt(intersections.size()));
            }
            //needed to store new intersections
            int x = intersection[0];
            int y = intersection[1];
            int endX = 0;
            int endY = 0;
            //decide to make a path up, down, left or right randomly or the direction with the most walls
            int direction = 0;
            if(rng.nextBoolean())
            {
                direction = whereAreMostWalls(x,y);
            }
            else
            {
                direction = rng.nextInt(4);
            }

            int pathLength = rng.nextInt(maxRandomPathLength - minRandomPathLength) + minRandomPathLength;
            if(direction == 0) // up
            {
                endX = x;
                endY = y - makePathUp(x, y, pathLength);
            }
            else if(direction == 1) // down
            {
                endX = x;
                endY = y + makePathDown(x, y, pathLength);
            }
            else if(direction == 2) // left
            {
                endX = x - makePathLeft(x,y, pathLength);;
                endY = y;
            }
            else // right
            {
                endX = x + makePathRight(x,y, pathLength);
                endY = y;
            }
            //add end of new random path to the list of intersections
            intersections.add(new int[] {endX, endY});
            if(debug)
                System.out.println("new intersection: {" + endX + "," + endY + "}");
        }
    }

    private int makePathDown(int x, int y, int stepSize)
    {
        int count = 0;
        for(int i = 1; i <= stepSize && y + i < height; i++)
        {
            maze[y + i][x] = pathChar;
            count++;
        }
        return count;
    }

    private int makePathUp(int x, int y, int stepSize)
    {
        int count = 0;
        for(int i = 1; i <= stepSize && y - i >= 0; i++)
        {
            maze[y - i][x] = pathChar;
            count++;
        }
        return count;
    }

    private int makePathLeft(int x, int y, int stepSize)
    {
        int count = 0;
        for(int i = 1; i <= stepSize && x - i >= 0; i++)
        {
            maze[y][x - i] = pathChar;
            count++;
        }
        return count;
    }

    private int makePathRight(int x, int y, int stepSize)
    {
        int count = 0;
        for(int i = 1; i <= stepSize && x + i < width; i++)
        {
            maze[y][x + i] = pathChar;
            count++;
        }
        return count;
    }

    public int whereAreMostWalls(int x, int y) //returns 0 for up, 1 for down, 2 for left, 3 for right
    {
        int xUp = 0;
        int xDown = 0;
        int xLeft = 0;
        int xRight = 0;
        //calculate all methods
        for(int i = 0; i < 5; i++)
        {
            //calcuate x up
            try
            {
                if(getCell(x, y - i) == openChar)
                    xUp++;
            }
            catch(ArrayIndexOutOfBoundsException e) { }
            //calcuate x down
            try
            {
                if(getCell(x, y + i) == openChar)
                    xDown++;
            }
            catch(ArrayIndexOutOfBoundsException e) { }
            //calculate x left
            try
            {
                if(getCell(x - i, y) == openChar)
                    xLeft++;
            }
            catch(ArrayIndexOutOfBoundsException e) { }
            //calculate x right
            try
            {
                if(getCell(x + i, y) == openChar)
                    xRight++;
            }
            catch(ArrayIndexOutOfBoundsException e) { }
        }
        //return the direction witht the highest x
        int highestDirection = 0;
        int highestX = xUp;
        if(highestX <= xDown)
        {
            highestDirection = 1;
            highestX =xDown;
        }
        if(highestX <= xLeft)
        {
            highestDirection = 2;
            highestX = xLeft;
        }
        if(highestX < xRight)
        {
            highestDirection = 3;
        }
        if((highestDirection == 2 || highestDirection == 3) && xLeft == xRight)
        {
            if(rng.nextBoolean())
            {
                highestDirection = 2;
            }
            else
            {
                highestDirection = 3;
            }
        }
        return highestDirection;
    }

    private void makeWalls()
    {
        //printMaze();
        // turn every open cell into a wall
        //remove e and b from the maze
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                char mazeChar = maze[i][j];
                if(mazeChar == pathChar)
                {
                    maze[i][j] = openChar;
                }
                else if(mazeChar == openChar)
                {
                    maze[i][j] = wallchar;
                }
                else if(mazeChar == startChar || mazeChar == endChar)
                {
                    //System.out.println("found " + mazeChar);
                    maze[i][j] = openChar;
                }
            }
        }
    }

    public void printMaze()
    {
        //print top ______
        System.out.print("_");
        for(int i = 0; i < width; i++)
        {
            if(i == beginCoords[1])
            {
                System.out.print("_" + startChar + "_");
            }
            else
            {
                System.out.print("___");
            }
        }
        System.out.print("_\n");
        //print content of maze
        for(int i = 0 ; i < height; i++)
        {
            System.out.print("|");
            for(int j = 0; j < width; j++)
            {
                System.out.print(" " + maze[i][j] + " ");
            }
            System.out.println("|");
        }
        //print bottem ______
        System.out.print("_");
        for(int i = 0; i < width; i++)
        {
            if(i == endCoords[1])
            {
                System.out.print("_" + endChar + "_");
            }
            else
            {
                System.out.print("___");
            }
        }
        System.out.print("_\n");
    }

    public char[][] getMaze()
    {
        makeCopy();
        return mazeCopy;
    }

    private void makeCopy()
    {
        mazeCopy = new char[height][width];
        for(int i = 0 ; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                mazeCopy[i][j] = maze[i][j];
            }
        }
    }

    public char getCell(int x, int y) throws ArrayIndexOutOfBoundsException
    {
        try
        {
            return maze[x][y];
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            throw e;
        }
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public void addPath(ArrayList<MapNode> path)
    {
        for (MapNode node : path)
        {
            MazeMapNode n = (MazeMapNode) node;
            maze[n.getY()][n.getX()] = pathChar;
        }
    }

    public void deletePath(ArrayList<MapNode> path)
    {
        for (MapNode node : path)
        {
            MazeMapNode n = (MazeMapNode) node;
            maze[n.getY()][n.getX()] = openChar;
        }
    }
}
