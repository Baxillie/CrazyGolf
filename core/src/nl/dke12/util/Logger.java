package nl.dke12.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Ajki on 22/04/2016.
 */
public class Logger
{
    private static Logger logger;

    private File logFile;
    private PrintWriter writer;

    private String breakPattern = "##########";


    private Logger()
    {
        logFile = new File("logs/logfile " + TimeStrings.getCurrentDateAndTimeString() + ".txt");
        try
        {
            logFile.createNewFile();
            writer = new PrintWriter(new FileWriter(logFile));
            writer.println("start of log file");
            writer.flush();
        }
        catch (IOException e)
        {
            System.out.println("failed to initialize the logfile");
            e.printStackTrace();
        }
    }

    public static Logger getInstance()
    {
        if(logger == null)
        {
            logger = new Logger();
        }
        return logger;
    }

    public void log(String toLog)
    {
        writer.println(toLog);
        writer.flush();
    }

    public void createBreak(String breakInfo)
    {
        writer.println();
        writer.print(breakPattern + " ");
        writer.print(breakInfo);
        writer.print(" " + breakPattern + "\n");
        writer.println();
        writer.flush();
    }
}
