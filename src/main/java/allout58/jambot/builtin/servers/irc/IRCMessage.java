package allout58.jambot.builtin.servers.irc;

/**
 * Created by James Hollowell on 8/19/2014.
 */
public class IRCMessage
{
    private IRCClient sender;
    private String command = "";
    private String message = "";
    private IRCClient recipient;

    public IRCMessage(String raw, IRCChannel channel)
    {
        String[] splits = raw.split(" ");
        if (raw.startsWith(":"))
        {
            sender = new IRCClient(splits[0].substring(1), channel);
        }
        if (splits.length > 1)
        {
            command = splits[1];
        }
        if (splits.length > 2)
        {
            recipient = new IRCClient(splits[2], channel);
        }
        for (int i = 3; i < splits.length; i++)
        {
            message += (splits[i].startsWith(":") ? splits[i].substring(1) : splits[i]) + " ";
        }
        message = message.trim(); //remove last " " from string
    }

    public IRCClient getSender()
    {
        return sender;
    }

    public String getCommand()
    {
        return command;
    }

    public String getMessage()
    {
        return message;
    }
}
