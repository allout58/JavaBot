package allout58.jambot.builtin.commands;

import allout58.jambot.api.*;

/**
 * Created by James Hollowell on 8/16/2014.
 */
public class CommandHelp implements ICommand
{
    private static final String format = "%15s - %s";

    @Override
    public void processCommand(IClient sender, String[] args)
    {
        if (sender.canRecievePM())
        {
            for (IResponder responder : API.responders)
            {
                sender.sendPM(String.format(format, responder.getName(), responder.getDescription()));
            }
        }
        else
        {
            IChannel channel = sender.getChannel();
            for (IResponder responder : API.responders)
            {
                channel.sendMessage(String.format(format, responder.getName(), responder.getDescription()));
            }
        }
    }

    @Override
    public String getName()
    {
        return "help";
    }

    @Override
    public String getDescription()
    {
        return "Shows this help text.";
    }
}
