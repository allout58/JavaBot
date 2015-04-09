package allout58.jambot;

import allout58.jambot.api.config.Config;
import allout58.jambot.config.DefaultOptions;
import allout58.jambot.loader.LoaderMain;
import allout58.jambot.util.EnumCommandPrefix;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by James Hollowell on 8/16/2014.
 */
public class JamBot
{
    public static final Logger log = LogManager.getLogger("JamBot-Core");
    public static final String[] channels = new String[] { "#ChowTime" };
    public static Config config;

    //    private final OptionParser parser = new OptionParser();
    //    private OptionSet options;
    //
    //    private final OptionSpec<File> optionHome = parser.accepts("home", "Home directory for the bot").withRequiredArg().ofType(File.class).defaultsTo(new File("."));
    //    private final OptionSpec<String> optionNick = parser.acceptsAll(Arrays.asList("nick", "n"), "Sets the bots nickname for servers that can recognize it.").withOptionalArg();
    //    private final OptionSpec<String> optionOwner = parser.acceptsAll(Arrays.asList("owner", "o"), "Sets the bots owner nickname.").withOptionalArg();

    /**
     * Initializes the bot.
     * Starts and connects to the configured server(s)
     *
     * @param args The command-line arguments passed to the program.
     */
    public void init(String[] args)
    {
        //        if (!parseOptions(args)) return;
        //        parseOptions2();
        config = new Config("bot");
        config.readConfig();
        config.dumpConfig();

        log.info("Config.debugMode: " + DefaultOptions.debugMode);
        log.info("Config.owner: " + config.getValue("botOwner", "allout58"));
        log.info("Config.commandPrefix: " + config.getValue("prefix", EnumCommandPrefix.DOT));

        config.dumpConfig();

        if (config.hasChanged())
            config.save();

        LoaderMain.getInstance().beginLoad();
        LoaderMain.getInstance().startServers();
    }

    //    private void parseOptions2()
    //    {
    //        CmdOptions.debugMode = options.has("debug") || options.has("d");
    //        if (options.hasArgument(optionNick))
    //            config.setValue("botNick", options.valueOf(optionNick));
    //        if (options.hasArgument(optionOwner))
    //            config.setValue("botOwner", options.valueOf(optionOwner));
    //    }
    //
    //    private boolean parseOptions(String[] args)
    //    {
    //        parser.acceptsAll(Arrays.asList("help", "h", "?")).forHelp();
    //        parser.acceptsAll(Arrays.asList("debug", "d"), "Turn debug code on. (Could spam console)");
    //
    //        options = parser.parse(args);
    //
    //        if (options.has("help") || options.has("h") || options.has("?"))
    //        {
    //            try
    //            {
    //                parser.printHelpOn(System.out);
    //                return false;
    //            }
    //            catch (IOException e)
    //            {
    //                e.printStackTrace();
    //            }
    //        }
    //
    //        CmdOptions.homeDir = options.valueOf(optionHome);
    //        return true;
    //    }
}
