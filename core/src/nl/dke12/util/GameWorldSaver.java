package nl.dke12.util;

import com.badlogic.gdx.Gdx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Saves games created in editor in a new .txt file
 */
public class GameWorldSaver
{
    public static void fileWriter(String name, int[][][] level)
    {
        try {
            File file = ((Gdx.files.internal(name)).file());
            PrintWriter out = new PrintWriter(file);

            for(int i=0;i<level.length;i++)
            {
                for(int j=0;j<level[i][1].length;j++)
                {
                    //System.out.print(level[i][1][j]);
                    out.print(level[i][j][1]);
                }
                out.print(";");
            }

            out.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
