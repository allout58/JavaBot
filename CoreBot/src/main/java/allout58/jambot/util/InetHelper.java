package allout58.jambot.util;

/**
 * Created by James Hollowell on 8/18/2014.
 */
public class InetHelper
{
    public static final int DEFAULT_PORT = 6667;

    public static String getHost(String address)
    {
        return address.contains(":") ? address.substring(0, address.indexOf(":")) : address;
    }

    public static int getPort(String address)
    {
        if (!address.contains(":")) return DEFAULT_PORT;
        try
        {
            return Integer.parseInt(address.substring(address.indexOf(":") + 1));
        }
        catch (NumberFormatException e)
        {
            return DEFAULT_PORT;
        }
    }
}

