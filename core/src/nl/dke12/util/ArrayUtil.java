package nl.dke12.util;

import nl.dke12.bot.maze.MazeMapNode;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Ajki on 16/06/2016.
 */
public class ArrayUtil<E>
{
    public static String arrayToString(int[][] array)
    {
        String toReturn = new String();
        for(int i = 0; i < array.length; i++)
        {
            toReturn += "[";
            for(int j = 0; j < array[i].length; j++)
            {
                toReturn += String.format("%s,",array[i][j]);
            }
            toReturn += "]\n";
        }
        toReturn += "\n";

        return toReturn;
    }

    public static String arrayToStringWithPath(int[][] array1, ArrayList<MazeMapNode> path)
    {
        String toReturn = new String();
        int[][] array = array1.clone();
        for(MazeMapNode node: path)
        {
            array[node.getY()][node.getX()] = 9;
        }

        for(int i = 0; i < array.length; i++)
        {
            toReturn += "[";
            for(int j = 0; j < array[i].length; j++)
            {
                toReturn += String.format("%s,",array[i][j]);
            }
            toReturn += "]\n";
        }
        toReturn += "\n";
        return toReturn;
    }

    public static String arrayToString(String[][] array)
    {
        String toReturn = new String();
        for(int i = 0; i < array.length; i++)
        {
            toReturn += "[";
            for(int j = 0; j < array[i].length; j++)
            {
                toReturn += String.format("%s,",array[i][j]);
            }
            toReturn += "]\n";
        }
        toReturn += "\n";

        return toReturn;
    }

}
