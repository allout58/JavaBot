package allout58.jambot.builtin.servers.irc;

import allout58.jambot.api.IChannel;
import allout58.jambot.api.IClient;

/**
 * Created by James Hollowell on 8/19/2014.
 */
public class IRCClient implements IClient
{
    private String name;
    private IChannel channel;
    private boolean isOp = false;
    private boolean isVoice = false;

    public IRCClient(String name, IRCChannel channel)
    {
        if (name.startsWith("@"))
            isOp = true;
        if (name.startsWith("+"))
            isVoice = true;

        this.name = (isVoice || isOp) ? name.substring(1) : name; //remove the '@' or '+' for ops and voice
        this.channel = channel;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public boolean canRecievePM()
    {
        return true;
    }

    @Override
    public boolean isOp()
    {
        return isOp;
    }

    @Override
    public boolean isVoice()
    {
        return isVoice;
    }

    @Override
    public void sendPM(String message)
    {
        channel.getWriter().addToQueue("PRIVMSG " + name.substring(0, name.indexOf("!")) + " :" + message);
    }

    @Override
    public IChannel getChannel()
    {
        return channel;
    }
}
