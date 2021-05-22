package base.core.concurrent.collection.queue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorsQueue {

    //Executors各线程池所用到的阻塞队列
    /**
     * new ThreadPoolExecutor(1 , 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>())
     */
    private static ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
    /**
     * new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>())
     */
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
    /**
     * new ScheduledThreadPoolExecutor(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS, new DelayedWorkQueue())
     */
    private static ExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);
    /**
     * new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>())
     */
    private static ExecutorService cacheThread = Executors.newCachedThreadPool();
}
