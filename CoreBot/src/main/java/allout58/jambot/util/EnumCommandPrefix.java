package allout58.jambot.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by James Hollowell on 8/18/2014.
 */
public enum EnumCommandPrefix
{
    BANG("!"),
    TILDE("~"),
    DOT("."),
    SLASH("/");

    private static final Map<String, EnumCommandPrefix> nameMap = new HashMap<String, EnumCommandPrefix>();

    private String pre;

    private EnumCommandPrefix(String prefix)
    {
        this.pre = prefix;
    }

    public String getPrefix()
    {
        return pre;
    }

    public static EnumCommandPrefix getPrefixFromName(String name)
    {
        return nameMap.get(name.toLowerCase());
    }

    static
    {
        EnumCommandPrefix[] val = values();
        for (EnumCommandPrefix pre : val)
        {
            nameMap.put(pre.name().toLowerCase(), pre);
        }
    }
}
