package allout58.jambot.api;

/**
 * Created by James Hollowell on 8/16/2014.
 */
public interface IServer
{
    // Status checks
    boolean canAuthenticate();

    boolean canPM();

    boolean canNotice();

    //
    void connect();

    void authenticate(String user, String password, Object... other);

    void disconnect();

    IChannel[] getChannels();

    IChannel joinChannel(String name);
}
