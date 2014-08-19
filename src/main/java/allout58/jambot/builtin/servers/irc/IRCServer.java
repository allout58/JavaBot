package allout58.jambot.builtin.servers.irc;

import allout58.jambot.JamBot;
import allout58.jambot.api.IChannel;
import allout58.jambot.api.IServer;
import allout58.jambot.util.CallbackReader;
import allout58.jambot.util.CommandParser;
import allout58.jambot.util.InetHelper;
import allout58.jambot.util.QueuedWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by James Hollowell on 8/18/2014.
 */
public class IRCServer implements IServer, CallbackReader.IReaderCallback
{
    private Logger log = LogManager.getLogger();

    private final List<IRCChannel> channels = new ArrayList<IRCChannel>();

    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private QueuedWriter writer = new QueuedWriter("IRCServerWriter");
    private CallbackReader reader = new CallbackReader("IRCServerReader");

    private final String host;
    private final int port;
    private final IRCChannel defaultChannel;

    private boolean isConnected = false;

    public IRCServer(String address)
    {
        host = InetHelper.getHost(address);
        port = InetHelper.getPort(address);
        defaultChannel = new IRCChannel(host, this);
        defaultChannel.setWriter(writer);
    }

    @Override

    public boolean canAuthenticate()
    {
        return true;
    }

    @Override
    public boolean canPM()
    {
        return true;
    }

    @Override
    public boolean canNotice()
    {
        return true;
    }

    @Override
    public void connect()
    {
        try
        {
            Socket daSocket = new Socket(host, port);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(daSocket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(daSocket.getInputStream()));
            authenticate("daBot", "");

            writer.setWriter(bufferedWriter);
            writer.start();
            reader.setReader(bufferedReader);
            reader.registerCallBack(this);
            reader.start();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void authenticate(String user, String password, Object... other)
    {
        try
        {
            bufferedWriter.write("NICK " + user + "\r\n");
            bufferedWriter.write("USER " + user + " 8 * :JamBot \r\n");
            bufferedWriter.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect()
    {
        try
        {
            writer.stop();
            reader.stop();
            bufferedWriter.write("QUIT :Leaving \r\n");
            bufferedWriter.flush();
            bufferedReader.close();
            bufferedWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public IChannel[] getChannels()
    {
        return channels.toArray(new IChannel[]{});
    }

    @Override
    public void readerCallback(String message)
    {
        if (message.startsWith("PING"))
        {
            writer.addToQueue("PONG " + message.substring(6));
            return;
        }
        IRCMessage msg = new IRCMessage(message, defaultChannel);
        if ("376".equals(msg.getCommand()))
        {
            isConnected = true;
            for (String name : JamBot.channels)
            {
                channels.add((IRCChannel) joinChannel(name));
            }
            return;
        }

        for (IRCChannel channel : channels)
        {
            channel.onMessage(msg);
        }

        if (!CommandParser.tryCommand(msg.getSender(), msg.getMessage()))
        {
            log.info("IRC Regular message: " + message);
        }
        else
        {
            log.info("IRC Command parsed: " + message);
        }
    }

    @Override
    public IChannel joinChannel(String name)
    {
        IRCChannel channel = new IRCChannel(name, this);
        channel.setWriter(this.writer);
        channel.activate();
        channel.sendMessage("Hi there!");
        return channel;
    }

}
