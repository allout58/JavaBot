package allout58.jambot.api;

import allout58.jambot.util.Permissions;

import java.util.Collection;

/**
 * Created by James Hollowell on 8/16/2014.
 */
public interface IClient
{
    String getNick();

    String getUser();

    String getServerAddress();

    boolean canReceivePM();

    boolean canReceiveNotice();

    void setOp(boolean op, IChannel channel);

    void setVoice(boolean voice, IChannel channel);

    void setNick(String newNick);

    boolean isOp(IChannel channel);

    boolean isVoice(IChannel channel);

    void sendPM(String message);

    void sendNotice(String message);

    Collection<IChannel> getChannels();

    IServer getServer();

    void setFullName(String name);

    Permissions.EnumCommandPermission getPermLevel(IChannel chan);
}
