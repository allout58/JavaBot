package allout58.jambot.builtin.servers.irc;

import allout58.jambot.JamBot;
import allout58.jambot.api.IChannel;
import allout58.jambot.api.IClient;
import allout58.jambot.api.IServer;
import allout58.jambot.config.Config;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by James Hollowell on 8/18/2014.
 */
public class IRCServer implements IServer, CallbackReader.IReaderCallback
{
    private Logger log = LogManager.getLogger("IRCServer");

    private final Map<String, IRCChannel> channels = new HashMap<String, IRCChannel>();
    private final Map<String, IRCClient> clients = new HashMap<String, IRCClient>();

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
            authenticate(Config.botNick, "");

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
        //catch (InterruptedException e)
        //{
        //    e.printStackTrace();
        //}
    }

    @Override
    public IChannel[] getChannels()
    {
        return channels.values().toArray(new IChannel[channels.size()]);
    }

    @Override
    public IRCChannel getChannel(String name)
    {
        return channels.get(name.toLowerCase());
    }

    @Override
    public void readerCallback(String message)
    {
        if (message.startsWith("PING"))
        {
            writer.addToQueue("PONG " + message.substring(6));
            return;
        }
        IRCMessage msg = new IRCMessage(message);
        IChannel c = getChannel(msg.getArgs()[0]);
        tryStringCommand(msg);
        if (!tryNumericCommand(msg))
        {
            if (!CommandParser.tryCommand(getOrCreateClient(msg.getSender()), c, msg.getMessage().substring(msg.getMessage().indexOf(":") + 1)))
            {
                log.info("IRC Regular message: " + message);
            }
            else
            {
                log.info("IRC Command parsed: " + message);
            }
        }
        else
        {
            log.info("IRC Numeric Command parsed: " + message);
        }
    }

    @Override
    public IChannel joinChannel(String name)
    {
        IRCChannel channel = new IRCChannel(name, this);
        channel.setWriter(this.writer);
        channel.activate();
        channel.sendMessage("Hi there!");
        channels.put(name.toLowerCase(), channel);
        return channel;
    }

    @Override
    public void partChannel(String name)
    {
        IRCChannel chan = getChannel(name);
        if (chan != null)
        {
            chan.deactivate();
            channels.remove(name.toLowerCase());
        }
    }

    @Override
    public void sendToAllChannels(String message)
    {
        for (IChannel channel : getChannels())
        {
            channel.sendMessage(message);
        }
    }

    private boolean tryNumericCommand(IRCMessage msg)
    {
        int command = msg.getIntCommand();

        switch (command)
        {
            case -1:
                return false;
            case 311:
                String[] arg = msg.getArgs();
                String nick = arg[1];
                String user = arg[2];
                String server = arg[3];
                log.info("Whois recieved: " + nick + "!" + user + "@" + server);
                //Map used to be nick -> client; this will convert it to the more appropriate uname -> client
                IClient c = renameClient(nick, user);
                c.setFullName(nick + "!" + user + "@" + server);
                return false;
            case 353:
                String message = msg.getMessage();
                if (message.indexOf(":") == 0) message = message.substring(1);
                String[] args = message.split(" ");
                IRCChannel chan = getChannel(args[2]);
                for (int i = 4; i < args.length; i++)
                {
                    if (args[i].startsWith(":")) args[i] = args[i].substring(1);
                    String stripName = IRCClient.stripName(args[i]);
                    IRCClient client = getOrCreateClient(stripName);
                    client.addChannel(chan);
                    client.setOp(IRCClient.nameIsOp(args[i]), chan);
                    client.setVoice(IRCClient.nameIsVoice(args[i]), chan);
                    chan.addClient(client);
                    writer.addToQueue("WHOIS " + stripName); //request full name
                }
                return true;
            case 376:
                isConnected = true;
                for (String name : JamBot.channels)
                {
                    joinChannel(name);
                }
                return true;
            default:
                return false;
        }
    }

    private void tryStringCommand(IRCMessage msg)
    {
        String command = msg.getCommand();
        if ("PRIVMSG".equals(command))
        {
            if (msg.getArgs()[0].startsWith("#")) //its a channel
            {
                IChannel c = getChannel(msg.getArgs()[0]);
                CommandParser.doMatchers(c, msg.getMessage());
            }
            else
            {
                getOrCreateClient(msg.getSender()).sendPM("You talk only to meh? You got somtin to hide??");
            }
        }
        else if ("NICK".equals(command))
        {
            renameClient(msg.getSender(), msg.getArgs()[0].substring(1));
        }
        else if ("JOIN".equals(command))
        {
            IRCClient c = getOrCreateClient(msg.getSender());
            if (Config.botNick.equals(c.getNick()))
                return;
            IRCChannel chan = getChannel(msg.getArgs()[0]);
            c.addChannel(chan);
            chan.addClient(c);
        }
        else if ("PART".equals(command))
        {
            IRCClient c = getOrCreateClient(msg.getSender());
            if (Config.botNick.equals(c.getNick()))
                return;
            IRCChannel chan = getChannel(msg.getArgs()[0]);
            c.removeChannel(chan);
            chan.removeClient(c);
        }
        else if ("QUIT".equals(command))
        {
            IRCClient c = getOrCreateClient(msg.getSender());
            for (IRCChannel chan : channels.values())
            {
                chan.removeClient(c);
                c.removeChannel(chan);
            }
        }
        else if ("MODE".equals(command))
        {
            if (msg.getArgs().length == 3)
            {
                IRCClient client = getOrCreateClient(msg.getArgs()[2]);
                IRCChannel chan = getChannel(msg.getArgs()[0]);
                boolean adding = msg.getArgs()[1].startsWith("+");
                boolean opMode = msg.getArgs()[1].contains("o");
                boolean voiceMode = msg.getArgs()[1].contains("v");
                client.setOp(adding && opMode, chan);
                client.setVoice(adding && voiceMode, chan);
            }
        }
    }

    private IRCClient getOrCreateClient(String name)
    {
        String n = name;
        if (name.contains("!"))
            n = name.substring(0, name.indexOf("!"));
        IRCClient c = clients.get(n);
        if (c == null)
        {
            c = new IRCClient(n);
            clients.put(n, c);
        }
        return c;
    }

    private IClient renameClient(String oldName, String newName)
    {
        IRCClient c = getOrCreateClient(oldName);
        clients.remove(oldName);
        c.setNick(newName);
        clients.put(newName, c);
        return c;
    }
}
