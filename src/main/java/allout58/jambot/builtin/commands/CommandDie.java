package allout58.jambot.builtin.commands;

import allout58.jambot.JamBot;
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
        if (Permissions.isOwner(sender))
        {
            JamBot.daServer.sendToAllChannels("Ay, ay, a scratch, a scratch; marry, 'tis enough.");
            JamBot.daServer.disconnect();
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
        return "die";
    }

    @Override
    public String getDescription()
    {
        return "Kills the bot";
    }
}
