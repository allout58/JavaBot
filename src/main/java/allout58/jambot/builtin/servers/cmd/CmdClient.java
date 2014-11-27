package allout58.jambot.builtin.servers.cmd;

import allout58.jambot.api.IChannel;
import allout58.jambot.api.IClient;
import allout58.jambot.api.IServer;
import allout58.jambot.util.Permissions;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by James Hollowell on 8/17/2014.
 */
public class CmdClient implements IClient
{
    @Override
    public String getNick()
    {
        return "CmdClient";
    }

    @Override
    public String getUser()
    {
        return "CmdClient";
    }

    @Override
    public String getServerAddress()
    {
        return "127.0.0.1";
    }

    @Override
    public boolean canReceivePM()
    {
        return false;
    }

    @Override
    public boolean canReceiveNotice()
    {
        return false;
    }

    @Override
    public void setOp(boolean op, IChannel channel)
    {

    }

    @Override
    public void setVoice(boolean voice, IChannel channel)
    {

    }

    @Override
    public boolean isOp(IChannel channel)
    {
        return true;
    }

    @Override
    public boolean isVoice(IChannel channel)
    {
        return false;
    }

    @Override
    public void sendPM(String message)
    {
        return;
    }

    @Override
    public void sendNotice(String message)
    {

    }

    @Override
    public Collection<IChannel> getChannels()
    {
        return Arrays.asList((IChannel) CmdServer.fakeChannel);
    }

    @Override
    public IServer getServer()
    {
        return CmdServer.fakeChannel.getServer();
    }

    @Override
    public void setFullName(String name)
    {

    }

    @Override
    public Permissions.EnumCommandPermission getPermLevel(IChannel chan)
    {
        return Permissions.EnumCommandPermission.BotAdmin;
    }

    @Override
    public void setNick(String newNick)
    {

    }

}
