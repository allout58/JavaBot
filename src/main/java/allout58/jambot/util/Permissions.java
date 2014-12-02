package allout58.jambot.util;

import allout58.jambot.JamBot;
import allout58.jambot.api.IClient;

/**
 * Created by James Hollowell on 8/22/2014.
 */
public class Permissions
{
    public static enum EnumCommandPermission
    {
        Everyone(0),
        ChannelVoice(1),
        ChannelOp(2),
        BotAdmin(3);

        private int val;

        private EnumCommandPermission(int value)
        {
            this.val = value;
        }
    }

    public static boolean isOwner(IClient client)
    {
        return client.getUser().equals(JamBot.config.getValue("botOwner"))|| client.getNick().equals(JamBot.config.getValue("botOwner"));
    }

    public static boolean canDo(EnumCommandPermission tryPerm, EnumCommandPermission requiredPerm)
    {
        return tryPerm.val >= requiredPerm.val;
    }
}
