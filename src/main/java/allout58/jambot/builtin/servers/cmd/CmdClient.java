package allout58.jambot.builtin.servers.cmd;

import allout58.jambot.api.IChannel;
import allout58.jambot.api.IClient;

/**
 * Created by James Hollowell on 8/17/2014.
 */
public class CmdClient implements IClient
{
    @Override
    public boolean canRecievePM()
    {
        return false;
    }

    @Override
    public boolean isOp()
    {
        return true;
    }

    @Override
    public boolean isVoice()
    {
        return false;
    }

    @Override
    public void sendPM(String message)
    {
        return;
    }

    @Override
    public IChannel getChannel()
    {
        return CmdServer.fakeChannel;
    }
}
