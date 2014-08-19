package allout58.jambot.util;

import allout58.jambot.api.API;
import allout58.jambot.api.IClient;
import allout58.jambot.api.ICommand;
import allout58.jambot.api.IResponder;
import allout58.jambot.config.Config;

/**
 * Created by James Hollowell on 8/18/2014.
 */
public class CommandParser
{
    public static boolean tryCommand(IClient sender, String message)
    {
        if (message.startsWith(Config.commandPrefix.getPrefix()))
        {
            String cmdName = message.substring(Config.commandPrefix.getPrefix().length());
            for (IResponder r : API.responders)
            {
                if (!(r instanceof ICommand)) continue;
                if (r.getName().equals(cmdName))
                {
                    String woComName = message.substring(Config.commandPrefix.getPrefix().length() + cmdName.length());
                    ((ICommand) r).processCommand(sender, woComName.split(" "));
                    return true;
                }
            }
            return false;
        }
        else return false;
    }
}
