package allout58.jambot.builtin.commands;

import allout58.jambot.JamBot;
import allout58.jambot.api.IClient;
import allout58.jambot.api.ICommand;

/**
 * Created by James Hollowell on 8/19/2014.
 */
public class CommandDie implements ICommand
{
    @Override
    public void processCommand(IClient sender, String[] args)
    {
//        if (sender.isOp())
//        {
            JamBot.daServer.sendToAllChannels("Ay, ay, a scratch, a scratch; marry, 'tis enough.");
            JamBot.daServer.disconnect();
//        }
//        else
//        {
//            sender.sendPM("YOU CAN'T DO DAT!!!!");
//        }
    }

    @Override
    public String getName()
    {
        return "die";
    }

    @Override
    public String getDescription()
    {
        return "Kills the bot";
    }
}
