package base.core.concurrent.thread.pool.custom;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class MyThreadPoolFutureExecutor extends MyThreadPoolExecutor implements FutureExecutor, Executor {

    public MyThreadPoolFutureExecutor(int coreSize, int maxSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> taskQueue) {
        super(coreSize, maxSize, keepAliveTime, unit, taskQueue);
    }

    public MyThreadPoolFutureExecutor(int coreSize, int maxSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> taskQueue, ThreadFactory threadFactory) {
        super(coreSize, maxSize, keepAliveTime, unit, taskQueue, threadFactory);
    }

    public MyThreadPoolFutureExecutor(int coreSize, int maxSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> taskQueue, RejectPolicy rejectPolicy) {
        super(coreSize, maxSize, keepAliveTime, unit, taskQueue, rejectPolicy);
    }

    public MyThreadPoolFutureExecutor(int coreSize, int maxSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> taskQueue, ThreadFactory threadFactory, RejectPolicy rejectPolicy) {
        super(coreSize, maxSize, keepAliveTime, unit, taskQueue, threadFactory, rejectPolicy);
    }

    @Override
    public <T> Future<T> submit(Callable<T> command) {
        //包装成将来获取返回值的任务
        FutureTask<T> futureTask = new FutureTask<>(command);
        execute(futureTask);
        // 返回将来的任务，只需要返回其get返回值的能力即可
        // 所以这里返回的是Future而不是FutureTask类型
        return futureTask;
    }
}
