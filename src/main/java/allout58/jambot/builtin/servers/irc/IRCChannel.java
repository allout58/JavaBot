package allout58.jambot.builtin.servers.irc;

import allout58.jambot.api.IChannel;
import allout58.jambot.api.IClient;
import allout58.jambot.api.IServer;
import allout58.jambot.util.QueuedWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James Hollowell on 8/19/2014.
 */
public class IRCChannel implements IChannel
{
    QueuedWriter writer;
    String name;
    List<IRCClient> clients = new ArrayList<IRCClient>();
    IRCServer server;

    public IRCChannel(String name, IRCServer server)
    {
        this.name = name;
        this.server = server;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public IClient[] getOps()
    {
        List<IClient> ops = new ArrayList<IClient>();
        for (IClient c : clients)
        {
            if (c.isOp()) ops.add(c);
        }
        return (IClient[]) ops.toArray();
    }

    @Override
    public IClient[] getVoice()
    {
        List<IClient> ops = new ArrayList<IClient>();
        for (IClient c : clients)
        {
            if (c.isVoice()) ops.add(c);
        }
        return (IClient[]) ops.toArray();
    }

    @Override
    public IClient[] getAllClients()
    {
        return (IClient[]) clients.toArray();
    }

    @Override
    public IServer getServer()
    {
        return server;
    }

    @Override
    public void setServer(IServer server)
    {
        this.server = (IRCServer) server;
    }

    @Override
    public void setWriter(QueuedWriter writer)
    {
        this.writer = writer;
    }

    @Override
    public QueuedWriter getWriter()
    {
        return writer;
    }

    @Override
    public void sendMessage(String message)
    {
        writer.addToQueue("PRIVMSG " + getName() + " :"+message);
    }

    @Override
    public void activate()
    {
        writer.addToQueue("JOIN " + getName());
    }

    @Override
    public void deactivate()
    {
        writer.addToQueue("PART " + getName());
    }

    public void onMessage(IRCMessage message)
    {
        if ("353".equals(message.getCommand()))// ./MODES response
        {
            String msg = message.getMessage();
            String[] names = msg.split(" ");
            for (String n : names)
            {
                IRCClient c = new IRCClient(name, this);
                clients.add(c);
            }
        }
    }
}
