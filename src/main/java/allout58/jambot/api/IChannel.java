package allout58.jambot.api;

import allout58.jambot.config.Config;
import allout58.jambot.util.QueuedWriter;

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

    void setServer(IServer server);

    void setWriter(QueuedWriter writer);

    void sendMessage(String message);

    void sendNotice(String message);

    void sendEmote(String message);

    void activate();

    void deactivate();

    QueuedWriter getWriter();
}
