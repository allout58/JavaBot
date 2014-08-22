package allout58.jambot.util;

import allout58.jambot.api.API;
import allout58.jambot.api.IChannel;
import allout58.jambot.api.IClient;
import allout58.jambot.api.ICommand;
import allout58.jambot.api.IMatcher;
import allout58.jambot.api.IResponder;
import allout58.jambot.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by James Hollowell on 8/18/2014.
 */
public class CommandParser
{
    private static final Logger log = LogManager.getLogger("CommandParser");

    public static boolean tryCommand(IClient sender, IChannel chan, String message)
    {
        if (message.startsWith(Config.commandPrefix.getPrefix()))
        {
            String cmdName = message.substring(Config.commandPrefix.getPrefix().length());
            if (cmdName.contains(" "))
                cmdName = cmdName.substring(0, cmdName.indexOf(" "));
            for (IResponder r : API.responders)
            {
                if (!(r instanceof ICommand)) continue;
                if (r.getName().equals(cmdName))
                {
                    if (Permissions.canDo(sender.getPermLevel(chan), ((ICommand) r).getCommandLevel()))
                    {
                        String woComName = message.substring(Config.commandPrefix.getPrefix().length() + cmdName.length());
                        List<String> a1 = Arrays.asList(woComName.split(" "));
                        ArrayList<String> a2 = new ArrayList<String>(a1);
                        if ("".equals(a2.get(0))) a2.remove(0);
                        try
                        {
                            ((ICommand) r).processCommand(sender, chan, a2.toArray(new String[a2.size()]));
                        }
                        catch (Exception e)
                        {
                            log.error("Error processing command " + r.getName(), e);
                        }
                        return true;
                    }
                    else
                    {
                        sender.sendPM("You don't have permission to do this. Your perms: " + sender.getPermLevel(chan).name() + ". Required: " + ((ICommand) r).getCommandLevel().name());
                    }
                }
            }
            return false;
        }
        else return false;
    }

    public static void doMatchers(IChannel sender, String message)
    {
        for (IResponder r : API.responders)
        {
            if (!(r instanceof IMatcher)) continue;
            ((IMatcher) r).match(sender, message);
        }
    }
}
