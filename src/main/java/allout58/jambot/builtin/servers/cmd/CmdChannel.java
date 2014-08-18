package allout58.jambot.builtin.servers.cmd;

import allout58.jambot.api.IChannel;
import allout58.jambot.api.IClient;
import allout58.jambot.api.IServer;
import allout58.jambot.util.CallbackReader;
import allout58.jambot.util.CommandParser;
import allout58.jambot.util.QueuedWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * Created by James Hollowell on 8/17/2014.
 */
public class CmdChannel implements IChannel, CallbackReader.IReaderCallback
{
    private Logger log = LogManager.getLogger();
    private CmdServer server;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private QueuedWriter writer = new QueuedWriter("CmdChannelWriter");
    private CallbackReader reader = new CallbackReader("CmdChannelReader");

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
    public void sendMessage(String message)
    {
        writer.addToQueue(message);
    }

    @Override
    public void activate()
    {
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        bufferedWriter = new BufferedWriter(new PrintWriter(System.out));
        writer.setWriter(bufferedWriter);
        writer.start();
        reader.setReader(bufferedReader);
        reader.registerCallBack(this);
        reader.start();
    }

    @Override
    public void deactivate()
    {
        try
        {
            bufferedReader.close();
            bufferedWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        writer.stop();
        reader.stop();
    }

    @Override
    public void readerCallback(String message)
    {
        writer.addToQueue("Message received: " + message);
        if (!CommandParser.tryCommand(CmdServer.fakeClient, message))
        {
            log.info("Regular message: " + message);
        }
        else
        {
            log.info("Command parsed: " + message);
        }
    }
}

