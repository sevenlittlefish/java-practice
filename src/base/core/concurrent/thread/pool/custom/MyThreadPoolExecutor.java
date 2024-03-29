package base.core.concurrent.thread.pool.custom;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadPoolExecutor implements Executor {
    /**
     * 核心线程数
     */
    private int coreSize;
    /**
     * 最大线程数
     */
    private int maxSize;
    /**
     * 线程空闲时间
     */
    private long keepAliveTime;
    /**
     * 时间单位
     */
    private TimeUnit unit;
    /**
     * 任务队列
     */
    private BlockingQueue<Runnable> taskQueue;
    /**
     * 线程工厂
     */
    private ThreadFactory threadFactory;
    /**
     * 拒绝策略
     */
    private RejectPolicy rejectPolicy;
    /**
     * 当前正在运行的线程数
     * 需要修改时线程间立即感知，所以使用AtomicInteger
     * 或者也可以使用volatile并结合Unsafe做CAS操作
     */
    private AtomicInteger runningCount = new AtomicInteger(0);

    private AtomicInteger enqueueCount = new AtomicInteger(0);

    public MyThreadPoolExecutor(int coreSize, int maxSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> taskQueue) {
        this(coreSize, maxSize, keepAliveTime, unit, taskQueue, Executors.defaultThreadFactory(), new DiscardRejectPolicy());
    }

    public MyThreadPoolExecutor(int coreSize, int maxSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> taskQueue, ThreadFactory threadFactory) {
        this(coreSize, maxSize, keepAliveTime, unit, taskQueue, threadFactory, new DiscardRejectPolicy());
    }

    public MyThreadPoolExecutor(int coreSize, int maxSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> taskQueue, RejectPolicy rejectPolicy) {
        this(coreSize, maxSize, keepAliveTime, unit, taskQueue, Executors.defaultThreadFactory(), rejectPolicy);
    }

    public MyThreadPoolExecutor(int coreSize, int maxSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> taskQueue, ThreadFactory threadFactory, RejectPolicy rejectPolicy) {
        this.coreSize = coreSize;
        this.maxSize = maxSize;
        this.keepAliveTime = keepAliveTime;
        this.unit = unit;
        this.taskQueue = taskQueue;
        this.threadFactory = threadFactory;
        this.rejectPolicy = rejectPolicy;
    }

    @Override
    public void execute(Runnable task) {
        int count = runningCount.get();
        if (count < coreSize) {
            if (addWord(task, true)) {
                return;
            }
        }
        if (taskQueue.offer(task)) {
            System.out.println("***task queue count:" + enqueueCount.incrementAndGet());
        } else {
            if (!addWord(task, false)) {
                rejectPolicy.reject(task, this);
            }
        }
    }

    private boolean addWord(Runnable newTask, boolean core) {
        for (;;) {
            int count = runningCount.get();
            int max = core ? coreSize : maxSize;
            if (count >= max) {
                return false;
            }
            if (runningCount.compareAndSet(count, count + 1)) {
                //通过线程工厂创建线程，需要实现ThreadFactory的newThread(Runnable r)方法
                threadFactory.newThread(() -> {
                    System.out.println("===thread name:" + Thread.currentThread().getName());
                    Runnable task = newTask;
                    try {
                        //通过不断地取任务，阻塞当前线程，线程不被销毁而达到线程复用的目的
                        while (task != null || (task = getTask()) != null) {
                            try {
                                task.run();
                            } finally {
                                task = null;
                            }
                        }
                    } finally {
                        //线程执行完回收线程数
                        do {} while (!runningCount.compareAndSet(runningCount.get(), runningCount.get() - 1));
                        System.out.println("===thread name:" + Thread.currentThread().getName() + " release source");
                    }
                }).start();
                break;
            }
        }
        return true;
    }

    private Runnable getTask() {
        boolean timeout = false;
        for (;;) {
            //大于核心线程取任务采用非阻塞poll()/超时方式poll(long timeout, TimeUnit unit)，否则采用阻塞方式take()，达到线程复用
            boolean timed = runningCount.get() > coreSize;
            try {
                //非核心线程获取队列任务超时，返回null后直接在#addWord中finally代码块内回收线程数
                if (timed && timeout)
                    return null;

                Runnable r = timed ? taskQueue.poll(keepAliveTime, unit) : taskQueue.take();
                if (r != null)
                    return r;

                timeout = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                runningCount.decrementAndGet();
                return null;
            }
        }
    }
}
