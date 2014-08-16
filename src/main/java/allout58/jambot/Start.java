package allout58.jambot;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Created by James Hollowell on 8/15/2014.
 */
public class Start
{
    public static void main(String[] args)
    {
        JamBot bot=new JamBot();
        bot.init(args);
    }
}
