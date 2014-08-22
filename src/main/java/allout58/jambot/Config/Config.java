package allout58.jambot.config;

import allout58.jambot.util.EnumCommandPrefix;

import java.io.File;

/**
 * Created by James Hollowell on 8/18/2014.
 */
public class Config
{
    public static EnumCommandPrefix commandPrefix;
    public static boolean debugMode = false;
    public static File homeDir;
    public static String botNick;
    public static String owner;

    public static void init()
    {
        commandPrefix = EnumCommandPrefix.TILDE;
    }
}
