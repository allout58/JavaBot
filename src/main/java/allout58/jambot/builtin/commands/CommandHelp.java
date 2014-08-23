package allout58.jambot.builtin.commands;

import allout58.jambot.api.API;
import allout58.jambot.api.IChannel;
import allout58.jambot.api.IClient;
import allout58.jambot.api.ICommand;
import allout58.jambot.api.IMatcher;
import allout58.jambot.api.IResponder;
import allout58.jambot.util.Permissions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James Hollowell on 8/16/2014.
 */
public class CommandHelp implements ICommand
{
    private static final String format = "%9s | %10s - %s";
    private static final String format2 = "%19s - %s";

    private List<ICommand> commands = new ArrayList<ICommand>();
    private List<IMatcher> matchers = new ArrayList<IMatcher>();

    @Override
    public void processCommand(IClient sender, IChannel chan, String[] args)
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
            message.add(" ");
            message.add(String.format(format, "PermLvl", "CmdName", "Desc"));
            for (ICommand com : commands)
            {
                message.add(String.format(format, com.getCommandLevel().name(), com.getName(), com.getDescription()));
            }
            message.add(" ");
        }
        if (matchers.size() > 0)
        {
            message.add("Matchers:");
            for (IMatcher matcher : matchers)
            {
                message.add(String.format(format2, matcher.getName(), matcher.getDescription()));
            }
            message.add(" ");
        }

        if (sender.canReceiveNotice())
        {
            for (String m : message)
            {
                sender.sendNotice(m);
            }
        }
        else if (sender.canReceivePM())
        {
            for (String m : message)
            {
                sender.sendPM(m);
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
    public Permissions.EnumCommandPermission getCommandLevel()
    {
        return Permissions.EnumCommandPermission.Everyone;
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
