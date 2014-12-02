package allout58.jambot.builtin.commands;

import allout58.jambot.api.IChannel;
import allout58.jambot.api.IClient;
import allout58.jambot.api.ICommand;
import allout58.jambot.util.Permissions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by James Hollowell on 12/1/2014.
 */
public class CommandBug implements ICommand
{
    @Override
    public void processCommand(IClient sender, IChannel channel, String[] args)
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter("bugs.txt", true));
            String s = "";
            s += sender.getNick() + "!" + sender.getUser() + "--";
            for (String s1 : args)
                s += s1 + " ";
            bw.write(s);
            bw.newLine();
            bw.close();
            sender.sendPM("Bug reported. Thanks!");
        }
        catch (IOException e)
        {
            sender.sendPM("Error reporting! Try again or ping the bot owner!");
        }
    }

    @Override
    public Permissions.EnumCommandPermission getCommandLevel()
    {
        return Permissions.EnumCommandPermission.Everyone;
    }

    @Override
    public String getName()
    {
        return "bug";
    }

    @Override
    public String getDescription()
    {
        return "Reports a bug/ request a feature, command, matcher, etc. Don't abuse this or I will disable it.";
    }
}
