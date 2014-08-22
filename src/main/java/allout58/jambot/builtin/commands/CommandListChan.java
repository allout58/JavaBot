package allout58.jambot.builtin.commands;

import allout58.jambot.api.IChannel;
import allout58.jambot.api.IClient;
import allout58.jambot.api.ICommand;
import allout58.jambot.util.Permissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by James Hollowell on 8/20/2014.
 */
public class CommandListChan implements ICommand
{
    @Override
    public void processCommand(IClient sender, IChannel chan, String[] args)
    {
        List<String> m = new ArrayList<String>();

        m.add("Channels this bot is connected to:");
        for (IChannel c : sender.getServer().getChannels())
        {
            m.add("  " + c.getName());
        }

        for (String arg : args)
        {
            IChannel c = sender.getServer().getChannel(arg);
            if (c == null) continue;
            IClient[] norm = c.getAllClients();
            List<IClient> nl = Arrays.asList(norm);
            ArrayList<IClient> nList = new ArrayList<IClient>(nl);
            if (c.getOps().length > 0)
            {
                m.add("Ops:");
                for (IClient client : c.getOps())
                {
                    m.add(" @" + client.getNick() + "!" + client.getUser());
                    nList.remove(client);
                }
            }
            if (c.getVoice().length > 0)
            {
                m.add("Voice:");
                for (IClient client : c.getVoice())
                {
                    m.add(" +" + client.getNick());
                    nList.remove(client);
                }
            }
            if (nList.size() > 0)
            {
                m.add("Normal: ");
                for (IClient client : nList)
                {
                    m.add(" " + client.getNick());
                }
            }
        }
        for (String msg : m)
            sender.sendPM(msg);
    }

    @Override
    public Permissions.EnumCommandPermission getCommandLevel()
    {
        return Permissions.EnumCommandPermission.Everyone;
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
