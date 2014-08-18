package allout58.jambot.api;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by James Hollowell on 8/16/2014.
 */
public class API
{
    public static final List<IResponder> responders = new LinkedList<IResponder>();

    /**Registers a responder with the API manager
     *
     * @param responder The IResponder to register
     */
    public static void registerResponder(IResponder responder)
    {
        responders.add(responder);
    }
}
