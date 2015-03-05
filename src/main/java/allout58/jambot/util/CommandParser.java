package allout58.jambot.util;

import allout58.jambot.JamBot;
import allout58.jambot.api.API;
import allout58.jambot.api.IChannel;
import allout58.jambot.api.IClient;
import allout58.jambot.api.ICommand;
import allout58.jambot.api.IMatcher;
import allout58.jambot.api.IResponder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by James Hollowell on 8/18/2014.
 */
public class CommandParser
{
    private static final Logger log = LogManager.getLogger("CommandParser");
    private static final ArrayBlockingQueue<String> commands = new ArrayBlockingQueue<String>(8);
    private static final ArrayBlockingQueue<String> matchers = new ArrayBlockingQueue<String>(8);

    private static boolean lockCommands = false;
    private static boolean lockMatchers = false;

    public static boolean tryCommand(IClient sender, final IChannel chan, String message)
    {
        EnumCommandPrefix prefix = (EnumCommandPrefix) JamBot.config.getValue("prefix");
        if (message.startsWith(prefix.getPrefix()))
        {
            String cmdName = message.substring(prefix.getPrefix().length());
            if (cmdName.contains(" "))
                cmdName = cmdName.substring(0, cmdName.indexOf(" "));
            for (IResponder r : API.responders)
            {
                if (!(r instanceof ICommand)) continue;
                if (r.getName().equals(cmdName) && !lockCommands)
                {
                    if (!commands.offer(cmdName))
                    {
                        int count = 0;
                        int countEl = 0;
                        commands.iterator();
                        for (Iterator<String> it = commands.iterator(); it.hasNext(); )
                        {
                            String test = it.next();
                            if (test.equals(cmdName)) count++;
                            if ("...".equals(test)) countEl++;
                        }
                        if (countEl >= 3)
                        {
                            lockCommands = true;
                            chan.sendMessage("Commands disabled for 15 seconds.");
                            new Timer(false).schedule(new TimerTask()
                            {
                                @Override
                                public void run()
                                {
                                    lockCommands = false;
                                    commands.clear();
                                    chan.sendMessage("Commands re-enabled.");
                                }
                            }, 15000);

                        }
                        if (count >= 5)
                        {
                            chan.sendMessage("...");
                            commands.poll();
                            commands.offer("...");
                        }
                        else
                            lockCommands = false;
                        commands.poll();
                    }
                    if (Permissions.canDo(sender.getPermLevel(chan), ((ICommand) r).getCommandLevel()))
                    {
                        String woComName = message.substring(prefix.getPrefix().length() + cmdName.length());
                        List<String> a1 = Arrays.asList(woComName.split(" "));
                        ArrayList<String> a2 = new ArrayList<String>(a1);
                        if ("".equals(a2.get(0))) a2.remove(0);
                        try
                        {
                            ((ICommand) r).processCommand(sender, chan, a2.toArray(new String[a2.size()]));
                        }
                        catch (Exception e)
                        {
                            log.error("Error processing command " + r.getName(), e);
                        }
                        return true;
                    }
                    else
                    {
                        sender.sendPM("You don't have permission to do this. Your perms: " + sender.getPermLevel(chan).name() + ". Required: " + ((ICommand) r).getCommandLevel().name());
                    }
                }
            }
            return false;
        }
        else return false;
    }

    public static void doMatchers(IChannel sender, String message)
    {
        for (IResponder r : API.responders)
        {
            if (!(r instanceof IMatcher)) continue;
            ((IMatcher) r).match(sender, message);
        }
    }
}
