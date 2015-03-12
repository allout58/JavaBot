package allout58.jambot.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by James Hollowell on 8/18/2014.
 */
public class QueuedWriter implements Runnable
{
    private static final String CARRIAGE_RETURN = "\r\n";
    private final LinkedList<String> queue = new LinkedList<String>();
    private final String name;
    private Thread daThread;

    private boolean isRunning = false;
    private BufferedWriter writer;

    private Logger log = LogManager.getLogger("QueuedWriter");

    public QueuedWriter()
    {
        this("");
    }

    public QueuedWriter(String name)
    {
        this.name = name;
    }

    public synchronized void setWriter(BufferedWriter writer)
    {
        this.writer = writer;
    }

    public void start()
    {
        assert !isRunning;
        assert writer != null;
        daThread = new Thread(this);
        if (!"".equals(name.trim()))
        {
            daThread.setName(name);
        }
        isRunning = true;
        daThread.start();
    }

    public void stop()
    {
        try
        {
            isRunning = false;
            daThread.interrupt();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public synchronized void addToQueue(String message)
    {
        assert writer != null; //why add to queue if not already started...
        queue.addLast(message);
    }

    @Override
    public void run()
    {
        try
        {
            while (isRunning)
            {
                synchronized (this)
                {
                    while (!queue.isEmpty())
                    {
                        String msg = queue.removeFirst();
                        writer.write(msg);
                        log.debug("Output msg: " + msg);
                        writer.write(CARRIAGE_RETURN);
                        writer.flush();
                        //                        try
                        //                        {
                        //                            Thread.sleep(1000);
                        //                        }
                        //                        catch (InterruptedException ignored)
                        //                        {
                        //                        }
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
