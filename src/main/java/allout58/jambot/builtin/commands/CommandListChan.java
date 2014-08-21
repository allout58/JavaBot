package allout58.jambot.builtin.commands;

import allout58.jambot.api.IChannel;
import allout58.jambot.api.IClient;
import allout58.jambot.api.ICommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James Hollowell on 8/20/2014.
 */
public class CommandListChan implements ICommand
{
    @Override
    public void processCommand(IClient sender, String[] args)
    {
        List<String> m = new ArrayList<String>();

        m.add("Channels this bot is connected to:");
        for (IChannel c : sender.getServer().getChannels())
        {
            m.add(c.getName());
        }

        for (String arg : args)
        {
            IChannel c = sender.getServer().getChannel(arg);
            if (c == null) continue;
            if (c.getOps().length > 0)
            {
                m.add("Ops:");
                for (IClient client : c.getOps())
                {
                    m.add(" @" + client.getName());
                }
            }
            if (c.getVoice().length > 0)
            {
                m.add("Voice:");
                for (IClient client : c.getVoice())
                {
                    m.add(" +" + client.getName());
                }
            }
        }
        for (String msg : m)
            sender.sendPM(msg);
    }

    @Override
    public String getName()
    {
        return "listchan";
    }

    @Override
    public String getDescription()
    {
        return "List statistics about a channel";
    }
}
