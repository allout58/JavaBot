package allout58.jambot.builtin.commands;

import allout58.jambot.JamBot;
import allout58.jambot.api.IChannel;
import allout58.jambot.api.IClient;
import allout58.jambot.api.ICommand;

/**
 * Created by James Hollowell on 8/19/2014.
 */
public class CommandRestart implements ICommand
{
    @Override
    public void processCommand(IClient sender, String[] args)
    {
        try
        {
            for (IChannel channel : JamBot.daServer.getChannels())
            {
                channel.sendMessage("Rebooting... Goodbye");
            }
            JamBot.daServer.disconnect();
            Thread.sleep(10000);
            JamBot.daServer.connect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getName()
    {
        return "reboot";
    }

    @Override
    public String getDescription()
    {
        return "Reboots this bot instance";
    }
}
