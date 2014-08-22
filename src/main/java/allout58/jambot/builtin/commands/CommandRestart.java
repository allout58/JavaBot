package allout58.jambot.builtin.commands;

import allout58.jambot.JamBot;
import allout58.jambot.api.IChannel;
import allout58.jambot.api.IClient;
import allout58.jambot.api.ICommand;
import allout58.jambot.util.Permissions;

/**
 * Created by James Hollowell on 8/19/2014.
 */
public class CommandRestart implements ICommand
{
    @Override
    public void processCommand(IClient sender, IChannel channel, String[] args)
    {
        if (Permissions.isOwner(sender))
        {
            try
            {
                JamBot.daServer.sendToAllChannels("Rebooting... Goodbye!");
                JamBot.daServer.disconnect();
                Thread.sleep(10000);
                JamBot.daServer.connect();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            sender.sendPM("YOU CAN'T DO DAT!!!!");
        }
    }

    @Override
    public Permissions.EnumCommandPermission getCommandLevel()
    {
        return Permissions.EnumCommandPermission.BotAdmin;
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
