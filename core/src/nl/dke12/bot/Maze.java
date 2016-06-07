package nl.dke12.bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by nik on 6/6/16.
 */
public class Maze {

    private static boolean debug = false;

    //general information and utility
    private int width;
    private int height;
    private int[] beginCoords;
    private int[] endCoords;
    private Random rng;

    //maze details
    private char[][] maze; // b is beginning, e is exit, x is wall
    public final char startChar = 'b';
    public final char endChar   = 'e';
    public final char wallchar  = 'x';
    public final char openChar  = '-';
    private final char pathChar = '*';

    //copy of maze to give when someone asks to prevent changes in actual maze
    private char[][] mazeCopy;

    public Maze(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.rng = new Random(System.currentTimeMillis());
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
        ArrayList<int[]> intersections = new ArrayList<>();

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
                    makePathRight(currentX, currentY, stepsize);
                    currentX += stepsize;
                }
                else
                {
                    stepsize = currentX - endX;
                    makePathLeft(currentX, currentY, stepsize);
                    currentX -= stepsize;
                }
            }
            else if(goDown) // go down
            {
                if(currentY + stepsize >= height) // check for going down out of maze
                {
                    stepsize = endY - currentY;
                }
                if(debug){
                    System.out.println("deciding to make a path down with stepsize: " + stepsize);
                }
                makePathDown(currentX, currentY, stepsize);
                currentY += stepsize;
            }
            else // decide to go left or right
            {
                if(currentX <= 0) //should definitely go to right
                {
                    if(currentX + stepsize >= width)
                    {
                        stepsize = width - currentX;
                    }
                    if(debug)
                    {
                        System.out.println("deciding to make a path to the right with stepsize: " + stepsize);
                    }
                    makePathRight(currentX, currentY, stepsize);
                    currentX += stepsize;
                }
                else if(currentX >= width - 1) //should definitely go left
                {
                    if(currentX - stepsize < 0)
                    {
                        stepsize = currentX;
                    }
                    if(debug)
                    {
                        System.out.println("deciding to make a path to the left with stepsize: " + stepsize);
                    }
                    makePathLeft(currentX, currentY, stepsize);
                    currentX -= stepsize;
                }
                else // randomly decide
                {
                    if(rng.nextBoolean()) // go left
                    {
                        if(currentX - stepsize < 0)
                        {
                            stepsize = currentX;
                        }
                        if(debug)
                        {
                            System.out.println("deciding to make a path to the left with stepsize: " + stepsize);
                        }
                        makePathLeft(currentX, currentY, stepsize);
                        currentX -= stepsize;
                    }
                    else //go right
                    {
                        if(currentX + stepsize >= width)
                        {
                            stepsize = width - currentX;
                        }
                        if(debug)
                        {
                            System.out.println("deciding to make a path to the right with stepsize: " + stepsize);
                        }
                        makePathRight(currentX, currentY, stepsize);
                        currentX += stepsize;
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
                System.out.println("##################################################################3");
            }
        }

        //make random paths away from the correct path
        createRandomPaths(intersections, rng);
    }

    private void createRandomPaths(ArrayList<int[]> intersections, Random rng)
    {
        final int minRandomPaths = 3;
        final int maxRandomPaths = 6;
        final int minRandomPathLength = 4;
        final int maxRandomPathLength = 8;
        final int randomPaths = rng.nextInt(maxRandomPaths - minRandomPaths) + minRandomPaths;
        final int intersectionSize = intersections.size();

        for(int i = 0; i < randomPaths; i++)
        {
            //select a intersection randomly
            int[] intersection = intersections.get(rng.nextInt(intersectionSize));
            int x = intersection[0];
            int y = intersection[1];
            //decide to make a path up, down, left or right
            int direction = rng.nextInt(4);
            int pathLength = rng.nextInt(maxRandomPathLength - minRandomPathLength) + minRandomPaths;
            if(direction == 0) // up
            {
                makePathUp(x, y, pathLength);
            }
            else if(direction == 1) // down
            {
                makePathDown(x, y, pathLength);
            }
            else if(direction == 2) // left
            {
                makePathLeft(x,y, pathLength);
            }
            else // right
            {
                makePathRight(x,y, pathLength);
            }
        }
    }

    private void makePathDown(int x, int y, int stepSize)
    {
        for(int i = 1; i <= stepSize && y + i < height; i++)
        {
            maze[y + i][x] = pathChar;
        }
    }

    private void makePathUp(int x, int y, int stepSize)
    {
        for(int i = 1; i <= stepSize && y - i >= 0; i++)
        {
            maze[y - i][x] = pathChar;
        }
    }

    private void makePathLeft(int x, int y, int stepSize)
    {
        for(int i = 1; i <= stepSize && x - i >= 0; i++)
        {
            maze[y][x - i] = pathChar;
        }
    }

    private void makePathRight(int x, int y, int stepSize)
    {
        for(int i = 1; i <= stepSize && x + i < width; i++)
        {
            maze[y][x + i] = pathChar;
        }
    }

    private void makeWalls()
    {
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

    public char getCell(int x, int y) throws IndexOutOfBoundsException
    {
        try
        {
            return maze[x][y];
        }
        catch (IndexOutOfBoundsException e)
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

    public static void main(String[] args)
    {
        Maze maze = new Maze(10,10);
        maze.printMaze();
    }

}
