package allout58.jambot.util;

/**
 * Created by James Hollowell on 8/18/2014.
 */
public enum EnumCommandPrefix
{
    BANG("!"),
    TILDE("~"),
    DOT("."),
    SLASH("/");

    private String pre;

    private EnumCommandPrefix(String prefix)
    {
        this.pre = prefix;
    }

    public String getPrefix()
    {
        return pre;
    }
}
