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
    static Logger log = LogManager.getLogger("JamBot");

    public static void main(String[] args)
    {
        final OptionParser parser = new OptionParser();

        parser.accepts("help").forHelp();
        parser.accepts("h").forHelp();
        parser.accepts("?").forHelp();
        final OptionSpec<String> opt1 = parser.accepts("option1", "The first option. MUAHAHAHAHA").withOptionalArg();
        final OptionSpec<File> homeOption = parser.accepts("home", "Home directory for the bot").withRequiredArg().ofType(File.class).defaultsTo(new File("."));

        final OptionSet options = parser.parse(args);

        File homeLoc = options.valueOf(homeOption);
        String test1 = options.valueOf(opt1);

        log.error("O/ world");
        log.error("Opt1: " + test1);
        log.error("HomeLoc: " + homeLoc.getAbsolutePath());
    }
}
