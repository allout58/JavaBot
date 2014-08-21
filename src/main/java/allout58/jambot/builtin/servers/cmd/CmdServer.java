package allout58.jambot.builtin.servers.cmd;

import allout58.jambot.api.IChannel;
import allout58.jambot.api.IServer;
import allout58.jambot.util.CallbackReader;
import allout58.jambot.util.CommandParser;
import allout58.jambot.util.QueuedWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Created by James Hollowell on 8/17/2014.
 */
public class CmdServer implements IServer, CallbackReader.IReaderCallback
{
    public static CmdChannel fakeChannel;
    public static CmdClient fakeClient = new CmdClient();

    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private QueuedWriter writer = new QueuedWriter("CmdChannelWriter");
    private CallbackReader reader = new CallbackReader("CmdChannelReader");
    private Logger log = LogManager.getLogger();

    @Override
    public boolean canAuthenticate()
    {
        return false;
    }

    @Override
    public boolean canPM()
    {
        return false;
    }

    @Override
    public boolean canNotice()
    {
        return false;
    }

    @Override
    public void connect()
    {
        fakeChannel = new CmdChannel();
        fakeChannel.activate();
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        bufferedWriter = new BufferedWriter(new PrintWriter(System.out));
        writer.setWriter(bufferedWriter);
        writer.start();
        reader.setReader(bufferedReader);
        reader.registerCallBack(this);
        reader.start();
    }

    @Override
    public void authenticate(String user, String password, Object... other)
    {
    }

    @Override
    public void disconnect()
    {
        fakeChannel.deactivate();
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
    public IChannel[] getChannels()
    {
        return new IChannel[] { fakeChannel };
    }

    @Override
    public IChannel getChannel(String name)
    {
        return fakeChannel;
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

    public IChannel joinChannel(String channel)
    {
        return fakeChannel;
    }

    @Override
    public void sendToAllChannels(String message)
    {
        for (IChannel channel : getChannels())
        {
            channel.sendMessage(message);
        }
    }
}
