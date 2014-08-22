package allout58.jambot.builtin.servers.cmd;

import allout58.jambot.api.IChannel;
import allout58.jambot.api.IClient;
import allout58.jambot.api.IServer;
import allout58.jambot.util.QueuedWriter;

/**
 * Created by James Hollowell on 8/17/2014.
 */
public class CmdChannel implements IChannel
{
    private QueuedWriter writer;

    private IServer server;

    @Override
    public String getName()
    {
        return "cmdChannel";
    }

    @Override
    public IClient[] getOps()
    {
        return new IClient[0];
    }

    @Override
    public IClient[] getVoice()
    {
        return new IClient[0];
    }

    @Override
    public IClient[] getAllClients()
    {
        return new IClient[0];
    }

    @Override
    public IServer getServer()
    {
        return server;
    }

    @Override
    public void setServer(IServer server)
    {
        if (this.server == null && server instanceof CmdServer)
            this.server = (CmdServer) server;
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
        writer.addToQueue(message);
    }

    @Override
    public void sendNotice(String message)
    {
        sendMessage(message);
    }

    @Override
    public void activate()
    {

    }

    @Override
    public void deactivate()
    {

    }

}

