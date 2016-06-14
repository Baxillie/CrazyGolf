package nl.dke12.util;

/**
 * Created by Ajki on 22/04/2016.
 */
public class Log
{
    private static Logger logger = Logger.getInstance();
    private static int frame = 0;

    public static void log(String information)
    {
        logger.log(information);
    }

    public static void createFrameBreak()
    {
        frame++;
        logger.createBreak("frame " + Integer.toString(frame));
    }

    public static void load()
    {
        if(logger == null)
        {
            logger = Logger.getInstance();
        }
    }

}
