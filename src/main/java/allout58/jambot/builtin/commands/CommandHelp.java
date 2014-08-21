package allout58.jambot.builtin.commands;

import allout58.jambot.api.API;
import allout58.jambot.api.IClient;
import allout58.jambot.api.ICommand;
import allout58.jambot.api.IMatcher;
import allout58.jambot.api.IResponder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James Hollowell on 8/16/2014.
 */
public class CommandHelp implements ICommand
{
    private static final String format = "%15s - %s";

    private List<ICommand> commands = new ArrayList<ICommand>();
    private List<IMatcher> matchers = new ArrayList<IMatcher>();

    @Override
    public void processCommand(IClient sender, String[] args)
    {
        List<String> message = new ArrayList<String>();
        if (commands.size() == 0 && matchers.size() == 0)
        {
            for (IResponder r : API.responders)
            {
                if (r instanceof ICommand) commands.add((ICommand) r);
                else if (r instanceof IMatcher) matchers.add((IMatcher) r);
            }
        }
        if (commands.size() > 0)
        {
            message.add("Commands:");
            for (ICommand com : commands)
            {
                message.add(String.format(format, com.getName(), com.getDescription()));
            }
            message.add(" ");
        }
        if (matchers.size() > 0)
        {
            message.add("Matchers:");
            for (IMatcher matcher : matchers)
            {
                message.add(String.format(format, matcher.getName(), matcher.getDescription()));
            }
            message.add(" ");
        }

        if (sender.canReceivePM())

        {
            for (String m : message)
            {
                sender.sendPM(m);
            }
        }

        else if (sender.canReceiveNotice())

        {
            for (String m : message)
            {
                sender.sendNotice(m);
            }
        }
        //        else
        //        {
        //            IChannel channel = sender.getChannel();
        //            for (String m : message)
        //            {
        //                channel.sendMessage(m);
        //            }
        //        }
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
