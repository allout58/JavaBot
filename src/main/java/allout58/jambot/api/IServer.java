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

    boolean hasChannels();

    // I/O
    void sendMessage(String message, IChannel channel);

    void onMessageReceived(String message, IClient sender);

    //
    void connect();

    void authenticate(String user, String password, Object... other);

    void disconnect();
}
