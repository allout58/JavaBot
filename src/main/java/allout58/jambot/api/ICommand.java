package allout58.jambot.api;

import allout58.jambot.util.Permissions;

/**
 * Created by James Hollowell on 8/16/2014.
 */
public interface ICommand extends IResponder
{
    void processCommand(IClient sender, IChannel channel, String[] args);

    Permissions.EnumCommandPermission getCommandLevel();
}
