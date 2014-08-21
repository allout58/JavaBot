package allout58.jambot.builtin.servers.irc;

import allout58.jambot.api.IChannel;
import allout58.jambot.api.IClient;
import allout58.jambot.api.IServer;
import allout58.jambot.util.QueuedWriter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by James Hollowell on 8/19/2014.
 */
public class IRCClient implements IClient
{
    private QueuedWriter writer;
    private IServer server;
    private String name;
    private Map<String, IChannel> channels = new HashMap<String, IChannel>();
    private Map<IChannel, Boolean> opChannels = new HashMap<IChannel, Boolean>();
    private Map<IChannel, Boolean> voiceChannels = new HashMap<IChannel, Boolean>();

    public IRCClient(String name)
    {
        this.name = stripName(name);
    }

    public IRCClient(String name, IChannel channel)
    {
        this.name = stripName(name);
        addChannel(channel);
    }

    public void addChannel(IChannel channel)
    {
        if (writer == null)
            writer = channel.getWriter();
        if (server == null)
            server = channel.getServer();
        channels.put(channel.getName(), channel);
        setOp(false, channel);
        setVoice(false, channel);
    }

    public void removeChannel(IChannel channel)
    {
        channels.remove(channel.getName());
        opChannels.remove(channel);
        voiceChannels.remove(channel);
    }

    @Override
    public void setOp(boolean op, IChannel opChannel)
    {
        opChannels.put(opChannel, op);
    }

    @Override
    public void setVoice(boolean voice, IChannel voiceChannel)
    {
        voiceChannels.put(voiceChannel, voice);
    }

    @Override
    public void setNick(String newNick)
    {
        this.name = newNick;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public boolean canReceivePM()
    {
        return true;
    }

    @Override
    public boolean canReceiveNotice()
    {
        return true;
    }

    @Override
    public boolean isOp(IChannel channel)
    {
        if (channel == null) return false;
        return opChannels.get(channel);
    }

    @Override
    public boolean isVoice(IChannel channel)
    {
        if (channel == null) return false;
        return voiceChannels.get(channel);
    }

    @Override
    public void sendPM(String message)
    {
        writer.addToQueue("PRIVMSG " + name + " :" + message); //name.substring(0, name.indexOf("!"))
    }

    @Override
    public void sendNotice(String message)
    {
        writer.addToQueue("NOTICE " + name + " :" + message);
    }

    @Override
    public Collection<IChannel> getChannels()
    {
        return channels.values();
    }

    @Override
    public IServer getServer()
    {
        return server;
    }

    //Helpful

    public static boolean nameIsOp(String name)
    {
        return name.startsWith("@");
    }

    public static boolean nameIsVoice(String name)
    {
        return name.startsWith("+");
    }

    public static String stripName(String name)
    {
        return nameIsOp(name) || nameIsVoice(name) ? name.substring(1) : name;
    }

    public String[] splitID(String name)
    {
        String nname = stripName(name); //remove first @ or +
        String[] out = new String[3];
        out[0] = nname.contains("@") ? nname : nname.substring(0, nname.indexOf("@"));
        return out;
    }
}
