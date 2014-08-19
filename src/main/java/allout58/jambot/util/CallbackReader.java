package allout58.jambot.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by James Hollowell on 8/18/2014.
 */
public class CallbackReader implements Runnable
{
    public interface IReaderCallback
    {
        /**
         * Callback for @link{allout58.jambot.util.CallbackReader}
         *
         * @param message Message recieved on the reader
         */
        void readerCallback(String message);
    }

    private static final String CARRIAGE_RETURN = "\r\n";
    private Thread daThread;
    private final Logger log;
    private final String name;

    private boolean isRunning = false;
    private BufferedReader reader;

    private final List<IReaderCallback> callbacks = new ArrayList<IReaderCallback>();

    public CallbackReader()
    {
        this("");
    }

    public CallbackReader(String name)
    {
        this.name = name;
        log = LogManager.getLogger("".equals(name.trim()) ? null : name);
    }

    public void setReader(BufferedReader reader)
    {
        this.reader = reader;
    }

    public void registerCallBack(IReaderCallback callback)
    {
        synchronized (callbacks)
        {
            callbacks.add(callback);
        }
    }

    public void start()
    {
        assert !isRunning;
        assert reader != null;
        isRunning = true;
        daThread = new Thread(this);
        if (!"".equals(name.trim()))
        {
            daThread.setName(name);
        }
        daThread.start();
    }

    public void stop()
    {
        try
        {
            isRunning = false;
            daThread.join(100);
            callbacks.clear();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        try
        {
            String line;
            while (isRunning && (line = reader.readLine()) != null)
            {
                if (callbacks.size() < 1)
                    log.warn("No callback objects defined; messages being received and not processed");
                //                if (Config.debugMode)
                //                {
                //                    log.info("New message: " + line);
                //                }
                synchronized (callbacks)
                {
                    for (IReaderCallback callback : callbacks)
                    {
                        callback.readerCallback(line);
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
