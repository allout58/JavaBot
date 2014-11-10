package allout58.jambot.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by James Hollowell on 8/23/2014.
 */
public class LoaderMain
{
    private static final LoaderMain instance = new LoaderMain();

    private static final File modulesLoc = new File("modules/");
    private static final Logger log = LogManager.getLogger("ModuleLoader");

    private final List<Module> modules = new ArrayList<Module>();

    public static LoaderMain getInstance()
    {
        return instance;
    }

    public void beginLoad()
    {
        try
        {
            findModules();
        }
        catch (IOException ignored)
        {
        }
    }

    private void findModules() throws IOException
    {
        //Ensure that the directory exists.
        if (!modulesLoc.exists())
            //If not, create a new module directory.
            modulesLoc.mkdir();

        //Find all the .jar files
        List<URL> urls = new ArrayList<URL>();
        List<JarFile> jars = new ArrayList<JarFile>();
        for (File jar : modulesLoc.listFiles())
        {
            if (!jar.getName().endsWith(".jar")) continue;
            urls.add(jar.toURI().toURL());
            jars.add(new JarFile(jar));
        }

        URLClassLoader classLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]));

        //Go through all the jars, loading all the .class files and seeing if it is a module class;
        for (JarFile jarFile : jars)
        {
            for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); )
            {
                try
                {

                    JarEntry entry = entries.nextElement();
                    if (!entry.getName().endsWith(".class")) continue;
                    String clzName = entry.getName();
                    clzName = clzName.replace('/', '.');
                    clzName = clzName.substring(0, clzName.indexOf(".class"));
                    Class<?> clz = classLoader.loadClass(clzName);

                    if (Module.class.isAssignableFrom(clz))
                    {
                        try
                        {
                            log.info("Initializing module " + clz.toString());
                            Module mod = (Module) clz.newInstance();
                            mod.init();
                            modules.add(mod);
                        }
                        catch (Exception e)
                        {
                            log.error("Unable to load module " + clzName, e);
                        }
                    }
                }
                catch (ClassNotFoundException e)
                {
                    log.error("Unable to find class", e);
                }
            }
        }
    }
}
