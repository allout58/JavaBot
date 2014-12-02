package allout58.jambot.builtin.commands;

import allout58.jambot.api.IChannel;
import allout58.jambot.api.IMatcher;

/**
 * Created by James Hollowell on 9/15/2014.
 */
public class ResponderBlame implements IMatcher
{

    @Override
    public String getName()
    {
        return "blame";
    }

    @Override
    public String getDescription()
    {
        return "kill the bot slowly by blaming it.";
    }

    @Override
    public void match(IChannel sender, String msg)
    {
        if (msg.toLowerCase().contains("#blame" + ((String) sender.getServer().getConfig().getValue("botNick")).toLowerCase()))
        {
            sender.sendEmote("dies a little inside");
        }
    }
}
