package allout58.jambot.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by James Hollowell on 8/16/2014.
 */
public class API
{
    public static final List<IResponder> responders = new LinkedList<IResponder>();
    public static final Map<String, IServer> servers = new HashMap<String, IServer>();

    /**
     * Registers a responder with the API manager.
     *
     * @param responder The IResponder to register
     */
    @SuppressWarnings("unused")
    public static void registerResponder(IResponder responder)
    {
        responders.add(responder);
    }

    /**
     * Registers a server instance with the API manager.
     *
     * @param commonName A name to reference the server object by.
     * @param serverObj  The server object being registered.
     */
    @SuppressWarnings("unused")
    public static void registerServer(String commonName, IServer serverObj)
    {
        servers.put(commonName, serverObj);
    }

    public static void dumpServers()
    {
        for (Map.Entry<String, IServer> serverEntry : servers.entrySet())
        {
            System.err.println(serverEntry.getKey() + "=>" + serverEntry.getValue());
        }
    }

    public static void dumpResponders()
    {
        for (IResponder serverEntry : responders)
        {
            System.err.println(serverEntry);
        }
    }
}
