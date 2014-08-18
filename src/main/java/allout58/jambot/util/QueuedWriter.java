package allout58.jambot.util;

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
    private final Thread daThread;

    private boolean isRunning = false;
    private BufferedWriter writer;

    public QueuedWriter()
    {
        this("");
    }

    public QueuedWriter(String name)
    {
        daThread = new Thread(this);
        if (!name.trim().equals(""))
        {
            daThread.setName(name);
        }
    }

    public void setWriter(BufferedWriter writer)
    {
        this.writer = writer;
    }

    public void start()
    {
        assert !isRunning;
        assert writer != null;
        isRunning = true;
        daThread.start();
    }

    public void stop()
    {
        isRunning = false;
    }

    public void addToQueue(String message)
    {
        assert writer!=null; //why add to queue if not already started...
        synchronized (queue)
        {
            queue.addLast(message);
        }
    }

    @Override
    public void run()
    {
        try
        {
            while (isRunning)
            {
                synchronized (queue)
                {
                    while (!queue.isEmpty())
                    {
                        writer.write(queue.removeFirst());
                        writer.write(CARRIAGE_RETURN);
                    }
                    writer.flush();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
