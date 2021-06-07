package base.core.concurrent.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorsTest {
    /**
     * new ThreadPoolExecutor(1 , 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>())
     * 作用：该方法创建自定义线程个数的线程池，如果提交的任务没有空闲的线程去处理，就会被放入阻塞队列中
     * 缺点：LinkedBlockingQueue默认容量为Integer.MAX_VALUE，容量过大，可能会堆积大量的任务，从而造成OOM
     */
    private static ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
    /**
     * new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>())
     * 作用：该方法创建了只有一个线程的线程池，如果提交的任务没有空闲的线程去处理，就会被放入阻塞队列中
     * 缺点：LinkedBlockingQueue默认容量为Integer.MAX_VALUE，容量过大，可能会堆积大量的任务，从而造成OOM
     */
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
    /**
     * new ScheduledThreadPoolExecutor(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS, new DelayedWorkQueue())
     * 作用：该方法可以创建自定义核心线程容量的线程池，而且该线程池支持定时以及周期性的任务执行
     * 缺点：该线程池允许创建的最大线程数量为Integer.MAX_VALUE，可能会创建出大量线程（每个线程都有自己的栈内存），导致OOM
     */
    private static ExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);
    /**
     * new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>()
     * 作用：该方法返回一个可根据实际需求调整线程数量的线程池，如果提交的任务没有空闲的线程处理，就会创建新的线程去处理该任务，
     * 如果有线程空闲时间超过60秒，就会被销毁
     * 缺点：该线程池允许创建的最大线程数量为Integer.MAX_VALUE，可能会创建出大量线程（每个线程都有自己的栈内存），导致OOM
     */
    private static ExecutorService cacheThread = Executors.newCachedThreadPool();
}
