package allout58.jambot.api;

import allout58.jambot.config.Config;
import allout58.jambot.util.QueuedWriter;

/**
 * Created by James Hollowell on 8/16/2014.
 */
public interface IServer
{
    // Status checks
    boolean canAuthenticate();

    boolean canPM();

    boolean canNotice();

    Config getConfig();

    QueuedWriter getWriter();

    //
    void connect();

    void authenticate(String user, String password, Object... other);

    void disconnect();

    IChannel[] getChannels();

    IChannel getChannel(String name);

    IChannel joinChannel(String name);

    void partChannel(String name);

    void sendToAllChannels(String message);
}
