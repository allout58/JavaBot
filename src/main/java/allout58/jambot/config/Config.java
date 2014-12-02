package allout58.jambot.config;

import allout58.jambot.util.EnumCommandPrefix;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by James Hollowell on 11/27/2014.
 */
public class Config
{
    interface Parser
    {
        Object parse(String val);

        String encode(Object val);
    }

    private class Property
    {
        public String comment = "";
        public Object value;

        Property(String comment, Object value)
        {
            this.comment = comment;
            this.value = value;
        }

        Property(Object value)
        {
            this.value = value;
        }

    }

    private static final HashSet<Parser> parsers = new HashSet<Parser>();
    private static final Logger log = LogManager.getLogger("ModuleLoader");

    private File cfgFile;

    private HashMap<String, Property> configs = new HashMap<String, Property>();
    private boolean hasChanged = false;

    public Config(String moduleName)
    {
        try
        {
            File folder = new File(CmdOptions.homeDir.getCanonicalPath() + "/config/");
            if (!folder.exists())
                folder.mkdir();
            setFile(new File(folder.getCanonicalPath() + "/" + moduleName + ".cfg"));
        }
        catch (IOException e)
        {
            log.error("Error creating config folder");
        }
    }

    public Config(File file)
    {
        setFile(file);
    }

    public static void addParser(Parser p)
    {
        parsers.add(p);
    }

    private void setFile(File file)
    {
        try
        {
            cfgFile = file;
            if (!cfgFile.exists())
                cfgFile.createNewFile();
        }
        catch (IOException e)
        {
            log.error("Error creating config file " + cfgFile.toString());
        }
    }

    /**
     * Loads the config from disk
     */
    public void readConfig()
    {
        configs.clear();
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(cfgFile)));
            String line;
            String comment = "";
            while ((line = reader.readLine()) != null)
            {
                if (line.startsWith("#"))
                {
                    comment += line + System.getProperty("line.separator");
                    continue;
                }
                int colNdx = line.indexOf(":");
                String first = line.substring(0, colNdx).trim();
                String second = line.substring(colNdx + 1).trim();
                Object parseSecond = null;
                for (Parser p : parsers)
                {
                    parseSecond = p.parse(second);
                    if (parseSecond != null) break;
                }
                //If none of the parsers work, make it a string
                if (parseSecond == null) parseSecond = second;

                Property p = ("".equals(comment)) ? new Property(parseSecond) : new Property(comment, parseSecond);
                configs.put(first, p);
                comment = "";
            }
        }
        catch (IOException e)
        {
            log.error("Error reading config file " + cfgFile.toString(), e);
        }
    }

    /**
     * Returns the value of the specified config.
     *
     * @param key Key name to find the value of.
     * @return The found value.
     */
    public Object getValue(String key)
    {
        if (configs.get(key) == null) return null;
        return configs.get(key).value;
    }

    /**
     * Returns the value of the specified config.
     * If it does not exist, sets the value to <code>defaultVal</code> and returns it.
     *
     * @param key        Key name to find the value of.
     * @param defaultVal Default value; returned if a config with key <code>key</code> does not exist.
     * @return The found value.
     */
    public Object getValue(String key, Object defaultVal)
    {
        if (configs.get(key) == null)
            setValue(key, defaultVal);
        return getValue(key);
    }

    /**
     * Set a configuration value
     *
     * @param key   Key name of the value being stored
     * @param value The value being stored. It can be any object, but if it has no registered encoder, it will be saved to disk as a string and read in that way.
     */
    public void setValue(String key, Object value)
    {
        hasChanged = true;
        configs.put(key, new Property(value));
    }

    /**
     * Save the config to disk. Overwrites any changes made to the file externally.
     */
    public void save()
    {
        if (hasChanged)
        {
            hasChanged = false;
            try
            {
                BufferedWriter writer = new BufferedWriter(new FileWriter(cfgFile));
                for (String key : configs.keySet())
                {
                    Property prop = configs.get(key);
                    if (!"".equals(prop.comment))
                        writer.write(prop.comment);
                    Object o = prop.value;
                    String out = null;
                    for (Parser p : parsers)
                    {
                        out = p.encode(o);
                        if (out != null) break;
                    }
                    if (out == null) out = (String) o;

                    writer.write(key + ": " + out);
                    writer.newLine();
                }
                writer.flush();
            }
            catch (IOException e)
            {
                log.error("Error saving config file " + cfgFile.toString(), e);
            }
        }
    }

    /**
     * @return Whether the config has changed in memory and not yet on disk
     */
    public boolean hasChanged()
    {
        return hasChanged;
    }

    /**
     * Dumps the current state of this config objet to stderr.
     */
    public void dumpConfig()
    {
        for (String key : configs.keySet())
        {
            System.err.println(key + "->" + configs.get(key).value + ":" + configs.get(key).value.getClass() + "#" + configs.get(key).comment);
        }
    }

    static
    {
        //Integer Parser
        addParser(new Parser()
        {
            @Override
            public Object parse(String val)
            {
                try
                {
                    return Integer.decode(val);
                }
                catch (Exception e)
                {
                    return null;
                }
            }

            @Override
            public String encode(Object val)
            {
                if (val instanceof Integer)
                {
                    return val.toString();
                }
                return null;
            }
        });

        //Double Parser
        addParser(new Parser()
        {
            @Override
            public Object parse(String val)
            {
                try
                {
                    return Double.parseDouble(val);
                }
                catch (Exception e)
                {
                    return null;
                }
            }

            @Override
            public String encode(Object val)
            {
                if (val instanceof Double)
                {
                    return val.toString();
                }
                return null;
            }
        });

        //Boolean Parser
        addParser(new Parser()
        {
            @Override
            public Object parse(String val)
            {
                Boolean b;
                if ("true".equalsIgnoreCase(val)) return true;
                if ("false".equalsIgnoreCase(val)) return false;
                return null;
            }

            @Override
            public String encode(Object val)
            {
                if (val instanceof Boolean)
                {
                    return val.toString();
                }
                return null;
            }
        });

        //EnumCommandPrefix Parser
        addParser(new Parser()
        {
            @Override
            public Object parse(String val)
            {
                try
                {
                    return EnumCommandPrefix.valueOf(val);
                }
                catch (IllegalArgumentException e)
                {
                    return null;
                }
            }

            @Override
            public String encode(Object val)
            {
                if (val instanceof EnumCommandPrefix)
                    return val.toString();
                return null;
            }
        });

        addParser(new Parser()
        {
            @Override
            public Object parse(String val)
            {
                if (val.startsWith("[") && val.endsWith("]"))
                {
                    val = val.substring(1);
                    val = val.substring(0, val.length() - 1);
                    return val.split(CmdOptions.arrayDelimeter);
                }
                return null;
            }

            @Override
            public String encode(Object val)
            {
                if (val instanceof String[])
                {
                    String out = "[";
                    for (String s : (String[]) val)
                        out += s + CmdOptions.arrayDelimeter;
                    out += "]";
                    return out;
                }
                return null;
            }
        });
    }
}
