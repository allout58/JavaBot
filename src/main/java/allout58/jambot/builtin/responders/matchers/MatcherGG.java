package allout58.jambot.builtin.responders.matchers;

import allout58.jambot.api.IChannel;
import allout58.jambot.api.IMatcher;

/**
 * Created by James Hollowell on 8/19/2014.
 */
public class MatcherGG implements IMatcher
{
    @Override
    public String getName()
    {
        return "gg";
    }

    @Override
    public String getDescription()
    {
        return "GG all the things!";
    }

    @Override
    public void match(IChannel sender, String msg)
    {
        if (msg.toLowerCase().contains("gg"))
        {
            sender.sendMessage("http://youtu.be/5es0NNtSNCU");
        }
    }
}
