package allout58.jambot.api;

/**
 * Created by James Hollowell on 8/16/2014.
 */
public interface IClient
{
    boolean canRecievePM();

    boolean isOp();

    boolean isVoice();

    void sendPM(String message);

    IChannel getChannel();
}
