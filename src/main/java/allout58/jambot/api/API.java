package allout58.jambot.api;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by James Hollowell on 8/16/2014.
 */
public class API
{
    public static final List<IResponder> responders = new LinkedList<IResponder>();

    public static void registerResponder(IResponder responder)
    {
        responders.add(responder);
    }
}
