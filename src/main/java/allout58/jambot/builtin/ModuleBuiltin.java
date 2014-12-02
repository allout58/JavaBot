package allout58.jambot.builtin;

import allout58.jambot.api.API;
import allout58.jambot.builtin.commands.CommandBug;
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
import allout58.jambot.loader.Module;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by James Hollowell on 11/9/2014.
 */
public class ModuleBuiltin implements Module
{
    private static final Logger log = LogManager.getLogger("CoreModule");
    private static Config ircConfig;

    @Override
    public void init()
    {
        ircConfig = new Config("IRCServer");
        ircConfig.readConfig();
        API.registerResponder(new CommandHelp());
        API.registerResponder(new CommandRestart());
        API.registerResponder(new CommandDie());
        API.registerResponder(new CommandListChan());
        API.registerResponder(new CommandJoin());
        API.registerResponder(new CommandPart());
        API.registerResponder(new CommandWhois());
        API.registerResponder(new CommandBug());

        API.registerResponder(new ResponderGG());
        API.registerResponder(new ResponderBlame());

        log.info("Core Module Initialized!");
    }

    @Override
    public void startServers()
    {
        String[] servers = (String[]) ircConfig.getValue("servers");
        if (servers != null)
        {
            for (String s : servers)
            {
                IRCServer server = new IRCServer(s, ircConfig);
                API.registerServer("IRC:" + s, server);
                server.connect();
            }
        }
        ircConfig.save();
        ircConfig.dumpConfig();
    }
}
