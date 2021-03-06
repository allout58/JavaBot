package allout58.jambot.builtin.commands;

import allout58.jambot.api.IChannel;
import allout58.jambot.api.IClient;
import allout58.jambot.api.ICommand;
import allout58.jambot.util.Permissions;

/**
 * Created by James Hollowell on 8/19/2014.
 */
public class CommandDie implements ICommand
{
    @Override
    public void processCommand(IClient sender, IChannel chan, String[] args)
    {
        chan.getServer().sendToAllChannels("Tis only a flesh wound!");
        chan.getServer().disconnect();
    }

    @Override
    public Permissions.EnumCommandPermission getCommandLevel()
    {
        return Permissions.EnumCommandPermission.BotAdmin;
    }

    @Override
    public String getName()
    {
        return "die";
    }

    @Override
    public String getDescription()
    {
        return "Kills the bot";
    }
}
