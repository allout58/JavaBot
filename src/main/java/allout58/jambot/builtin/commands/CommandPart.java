package allout58.jambot.builtin.commands;

import allout58.jambot.api.IChannel;
import allout58.jambot.api.IClient;
import allout58.jambot.api.ICommand;

/**
 * Created by James Hollowell on 8/21/2014.
 */
public class CommandPart implements ICommand
{
    @Override
    public void processCommand(IClient sender, IChannel channel, String[] args)
    {
        if (sender.isOp(channel))
        {
            channel.getServer().partChannel(args[1]);
        }
    }

    @Override
    public String getName()
    {
        return "part";
    }

    @Override
    public String getDescription()
    {
        return "Tell the bot to leave a channel";
    }
}
