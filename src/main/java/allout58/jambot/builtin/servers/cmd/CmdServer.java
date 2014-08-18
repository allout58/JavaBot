package allout58.jambot.builtin.servers.cmd;

import allout58.jambot.api.IChannel;
import allout58.jambot.api.IServer;

/**
 * Created by James Hollowell on 8/17/2014.
 */
public class CmdServer implements IServer
{
    public static CmdChannel fakeChannel;
    public static CmdClient fakeClient=new CmdClient();

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
    }

    @Override
    public void authenticate(String user, String password, Object... other)
    {
    }

    @Override
    public void disconnect()
    {
        fakeChannel.deactivate();
    }

    @Override
    public IChannel[] getChannels()
    {
        return new IChannel[] { fakeChannel };
    }
}
