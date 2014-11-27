package allout58.jambot.api;

/**
 * Created by James Hollowell on 8/16/2014.
 */
public interface IMatcher extends IResponder
{
    void match(IChannel sender, String msg);
}
