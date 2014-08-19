package allout58.jambot.api;

/**
 * Created by James Hollowell on 8/16/2014.
 */
public interface ICommand extends IResponder
{
    void processCommand(IClient sender, String[] args);
}
