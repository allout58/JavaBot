package allout58.jambot.commands;

import allout58.jambot.api.*;

/**
 * Created by James Hollowell on 8/16/2014.
 */
public class CommandHelp implements ICommand
{
    private static final String format="%20s - %s"

    @Override
    public void processCommand(IClient sender, String[] args)
    {
        if(sender.canRecievePM())
        {
            for (IResponder responder: API.responders)
            {
                sender.sendPM(String.format(format,responder.getName(),responder.getDescription()));
            }
        }
        else
        {
            IServer server=sender.getChannel().getServer();
            for (IResponder responder: API.responders)
            {
                server.sendMessage(String.format(format,responder.getName(),responder.getDescription()),sender.getChannel());
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
