package allout58.jambot.builtin.responders.commands;

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
                channel.getServer().sendToAllChannels("Reconnecting... Goodbye!");
                channel.getServer().disconnect();
                //Reload Config for good measure
                sender.getServer().getConfig().readConfig();
                Thread.sleep(10000);
                channel.getServer().connect();
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
