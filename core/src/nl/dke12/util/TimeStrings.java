package nl.dke12.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by Ajki on 22/04/2016.
 */
public class TimeStrings
{

    public static String getCurrentDateAndTimeString()
    {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM___HH-mm-ss").withZone(ZoneId.systemDefault());
        Instant now = Instant.now();
        return formatter.format(now);
    }


}
