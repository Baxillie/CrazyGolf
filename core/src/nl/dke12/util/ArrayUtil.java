package nl.dke12.util;

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
