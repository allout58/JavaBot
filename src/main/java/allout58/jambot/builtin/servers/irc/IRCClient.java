package allout58.jambot.builtin.servers.irc;

import allout58.jambot.api.IChannel;
import allout58.jambot.api.IClient;
import allout58.jambot.api.IServer;
import allout58.jambot.util.Permissions;
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
    private String nick;
    private String uname;
    private String serverAddr;
    private Map<String, IChannel> channels = new HashMap<String, IChannel>();
    private Map<IChannel, Boolean> opChannels = new HashMap<IChannel, Boolean>();
    private Map<IChannel, Boolean> voiceChannels = new HashMap<IChannel, Boolean>();

    public IRCClient(String name)
    {
        setFullName(name);
    }

    public IRCClient(String name, IChannel channel)
    {
        this(name);
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
        this.nick = newNick;
    }

    @Override
    public String getNick()
    {
        return nick;
    }

    @Override
    public String getUser()
    {
        return uname;
    }

    @Override
    public String getServerAddress()
    {
        return serverAddr;
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
        writer.addToQueue("PRIVMSG " + nick + " :" + message); //nick.substring(0, nick.indexOf("!"))
    }

    @Override
    public void sendNotice(String message)
    {
        writer.addToQueue("NOTICE " + nick + " :" + message);
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

    @Override
    public void setFullName(String name)
    {
        String[] parseName = splitID(name);
        this.nick = parseName[0];
        this.uname = parseName[1];
        this.serverAddr = parseName[2];
    }

    @Override
    public Permissions.EnumCommandPermission getPermLevel(IChannel chan)
    {
        if (Permissions.isOwner(this))
            return Permissions.EnumCommandPermission.BotAdmin;
        if (isOp(chan)) return Permissions.EnumCommandPermission.ChannelOp;
        if (isVoice(chan))
            return Permissions.EnumCommandPermission.ChannelVoice;
        return Permissions.EnumCommandPermission.Everyone;
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
        String[] out = new String[] {
                "", "", ""
        };
        int idxBang = nname.indexOf("!");
        int idxAt = nname.indexOf("@");
        out[0] = idxBang == -1 ? nname : nname.substring(0, idxBang);
        if (idxAt != -1)
        {
            out[1] = nname.substring(idxBang + 1, idxAt + 1);
            out[2] = nname.substring(idxAt);
        }
        return out;
    }
}
