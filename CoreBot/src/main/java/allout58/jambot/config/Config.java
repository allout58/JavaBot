package allout58.jambot.config;

import allout58.jambot.util.EnumCommandPrefix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

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
        try
        {
            File cfgFile = new File(homeDir.getCanonicalPath() + "/bot.cfg");
            boolean flag = cfgFile.createNewFile();
            Properties props = new Properties();
            if (flag)
            {
                props.setProperty("CommandPrefix", EnumCommandPrefix.TILDE.name());
                props.setProperty("BotNick", "JavaBot");
                props.setProperty("BotOwner", "allout58");
                FileOutputStream fos = new FileOutputStream(cfgFile);
                props.store(fos, "");
                fos.close();
            }
            FileInputStream fis = new FileInputStream(cfgFile);
            props.load(fis);
            fis.close();

            botNick = props.getProperty("BotNick");
            owner = props.getProperty("BotOwner");
            commandPrefix = EnumCommandPrefix.valueOf(props.getProperty("CommandPrefix"));
            if (commandPrefix == null) commandPrefix = EnumCommandPrefix.TILDE;
        }
        catch (IOException ignored)
        {

        }
    }
}
