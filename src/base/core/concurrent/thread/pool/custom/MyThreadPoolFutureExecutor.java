package base.core.concurrent.thread.pool.custom;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyThreadPoolFutureExecutor extends MyThreadPoolExecutor implements FutureExecutor, Executor {

    public MyThreadPoolFutureExecutor(String name, int coreSize, int maxSize, BlockingQueue<Runnable> taskQueue, RejectPolicy rejectPolicy) {
        super(coreSize, maxSize, 60, TimeUnit.SECONDS, taskQueue, Executors.defaultThreadFactory(), rejectPolicy);
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
