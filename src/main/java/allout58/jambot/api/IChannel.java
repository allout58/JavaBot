package allout58.jambot.api;

/**
 * Created by James Hollowell on 8/16/2014.
 */
public interface IChannel
{
    String getName();

    IClient[] getOps();

    IClient[] getVoice();

    IClient[] getAllClients();

    IServer getServer();
}
