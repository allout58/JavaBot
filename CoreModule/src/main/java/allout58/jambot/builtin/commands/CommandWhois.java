package allout58.jambot.builtin.commands;

import allout58.jambot.api.IChannel;
import allout58.jambot.api.IClient;
import allout58.jambot.api.ICommand;
import allout58.jambot.util.Permissions;

/**
 * Created by James Hollowell on 8/22/2014.
 */
public class CommandWhois implements ICommand
{
    @Override
    public void processCommand(IClient sender, IChannel channel, String[] args)
    {
        channel.getWriter().addToQueue("WHOIS " + args[0]);
    }

    @Override
    public Permissions.EnumCommandPermission getCommandLevel()
    {
        return Permissions.EnumCommandPermission.BotAdmin;
    }

    @Override
    public String getName()
    {
        return "whois";
    }

    @Override
    public String getDescription()
    {
        return "Asks the server WHOIS";
    }
}
