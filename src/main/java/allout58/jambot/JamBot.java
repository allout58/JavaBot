package allout58.jambot;

import allout58.jambot.api.API;
import allout58.jambot.api.IServer;
import allout58.jambot.builtin.commands.CommandDie;
import allout58.jambot.builtin.commands.CommandHelp;
import allout58.jambot.builtin.commands.CommandJoin;
import allout58.jambot.builtin.commands.CommandListChan;
import allout58.jambot.builtin.commands.CommandPart;
import allout58.jambot.builtin.commands.CommandRestart;
import allout58.jambot.builtin.commands.CommandWhois;
import allout58.jambot.builtin.commands.ResponderBlame;
import allout58.jambot.builtin.commands.ResponderGG;
import allout58.jambot.builtin.servers.irc.IRCServer;
import allout58.jambot.config.Config;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by James Hollowell on 8/16/2014.
 */
public class JamBot
{
    public static final Logger log = LogManager.getLogger("JamBot");


    public static final String[] channels = new String[] { "#ChowTime", "#turbulantgames" };

    public static IServer daServer;

    private final OptionParser parser = new OptionParser();
    private OptionSet options;

    private final OptionSpec<File> optionHome = parser.accepts("home", "Home directory for the bot").withRequiredArg().ofType(File.class).defaultsTo(new File("."));
    private final OptionSpec<String> optionNick = parser.acceptsAll(Arrays.asList("nick", "n"), "Sets the bots nickname for servers that can recognize it.").withOptionalArg();
    private final OptionSpec<String> optionOwner = parser.acceptsAll(Arrays.asList("owner", "o"), "Sets the bots owner nickname.").withOptionalArg();

    /**
     * Initializes the bot.
     * Starts and connects to the configured server(s)
     *
     * @param args The command-line arguments passed to the program.
     */
    public void init(String[] args)
    {
        if (!parseOptions(args)) return;
        Config.init();
        parseOptions2();

        log.info("Config.debugMode: " + Config.debugMode);
        log.info("Config.botNick: " + Config.botNick);
        log.info("Config.owner: " + Config.owner);
        log.info("Config.commandPrefix: " + Config.commandPrefix.name());

        registerDefaultResponders();

        daServer = new IRCServer("irc.esper.net:5555");
        daServer.connect();
    }

    private void parseOptions2()
    {
        Config.debugMode = options.has("debug") || options.has("d");
        if (options.hasArgument(optionNick))
            Config.botNick = options.valueOf(optionNick);
        if (options.hasArgument(optionOwner))
            Config.owner = options.valueOf(optionOwner);
    }

    private boolean parseOptions(String[] args)
    {
        parser.acceptsAll(Arrays.asList("help", "h", "?")).forHelp();
        parser.acceptsAll(Arrays.asList("debug", "d"), "Turn debug code on. (Could spam console)");

        options = parser.parse(args);

        if (options.has("help") || options.has("h") || options.has("?"))
        {
            try
            {
                parser.printHelpOn(System.out);
                return false;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        Config.homeDir = options.valueOf(optionHome);
        return true;
    }

    private void logTest()
    {
        log.fatal("Ya screwed up pretty bad dude... GG");
        log.error("Error error Will Robinson!");
        log.warn("WARNING!!! Um, er, well, at least it's not an error");
        log.info("Information Central!");
        log.debug("Debug all the bots");
        log.trace("Trace dat code up!");

    }

    private void registerDefaultResponders()
    {
        API.registerResponder(new CommandHelp());
        API.registerResponder(new CommandRestart());
        API.registerResponder(new CommandDie());
        API.registerResponder(new CommandListChan());
        API.registerResponder(new CommandJoin());
        API.registerResponder(new CommandPart());
        API.registerResponder(new CommandWhois());

        API.registerResponder(new ResponderGG());
        API.registerResponder(new ResponderBlame());
    }

}
