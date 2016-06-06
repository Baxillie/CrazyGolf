package nl.dke12.bot;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by nik on 6/6/16.
 */
public class Maze {

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
        final int minstep = 1;
        final int maxstep = 3;
        final int endX = endCoords[1];
        final int endY = endCoords[0];

        //create randomish correct path from b to e
        int currentX = beginCoords[1];
        int currentY = beginCoords[0];
        boolean goDown = rng.nextBoolean();
        int stepsize;

        while(currentX != endX && currentY != endY)
        {
            stepsize = rng.nextInt(maxstep) - minstep;
            if(currentY == endY) //make straight line to the end
            {

            }
            else if(goDown) // go down
            {
                if(currentY + stepsize < height) // check for going down out of maze
                {
                    stepsize = endY - currentY;
                }
                makePathDown(currentX, currentY, stepsize);
                currentY += stepsize;
            }
            else // decide to go left or right
            {
                if(currentX <= 0) //should definitely go to right
                {
                    if(currentX + stepsize > width)
                    {
                        stepsize = width - currentX;
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
                        makePathLeft(currentX, currentY, stepsize);
                        currentX -= stepsize;
                    }
                    else //go right
                    {
                        if(currentX + stepsize > width)
                        {
                            stepsize = width - currentX;
                        }
                        makePathRight(currentX, currentY, stepsize);
                        currentX += stepsize;

                    }
                }
            }
            goDown = !goDown; //switch from going down to going left or right every loop
        }

    }

    private void makePathDown(int x, int y, int stepSize)
    {
        for(int i = 1; i <= stepSize; i++)
        {
            maze[y + i][x] = pathChar;
        }
    }

    private void makePathLeft(int x, int y, int stepSize)
    {
        for(int i = 1; i <= stepSize; i++)
        {
            maze[y][x - i] = pathChar;
        }
    }

    private void makePathRight(int x, int y, int stepSize)
    {
        for(int i = 1; i <= stepSize; i++)
        {
            maze[y][x + i] = pathChar;
        }
    }

    private void makeWalls()
    {

    }

    public void printMaze()
    {
        //print top ______
        for(int i = 0; i < width * 3 + 2; i++)
        {
            System.out.print("_");
        }
        System.out.print("\n");
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
        for(int i = 0; i < width * 3 + 2; i++)
        {
            System.out.print("_");
        }
        System.out.print("\n");
    }

    public static void main(String[] args)
    {
        Maze maze = new Maze(10,10);
        maze.printMaze();
    }

}
