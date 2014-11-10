package allout58.jambot.builtin.servers.irc;

/**
 * Created by James Hollowell on 8/19/2014.
 */
public class IRCMessage
{
    private String sender = "";
    private String command = "";
    private String message = "";
    private String[] mArgs;

    public IRCMessage(String raw)
    {
        String[] splits = raw.split(" ");
        if (raw.startsWith(":"))
        {
            sender = splits[0].substring(1);
        }
        if (splits.length > 1)
        {
            command = splits[1];
        }
        for (int i = 2; i < splits.length; i++)
        {
            message += splits[i] + " ";
        }
        message = message.trim(); //remove last " " from string
        mArgs = message.split(" ");
    }

    public String getSender()
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

    public String[] getArgs()
    {
        return mArgs;
    }

    public int getIntCommand()
    {
        try
        {
            return Integer.parseInt(command);
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }
}
