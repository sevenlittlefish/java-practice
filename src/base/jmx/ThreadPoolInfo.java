package base.jmx;

import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolInfo implements ThreadPoolInfoMBean {

    private ThreadPoolExecutor executor;

    ThreadPoolInfo(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    @Override
    public boolean shutdown() {
        return executor.isShutdown();
    }

    @Override
    public boolean terminated() {
        return executor.isTerminated();
    }

    @Override
    public boolean terminating() {
        return executor.isTerminating();
    }

    @Override
    public int activeCount() {
        return executor.getActiveCount();
    }

    @Override
    public long completedTaskCount() {
        return executor.getCompletedTaskCount();
    }

    @Override
    public int corePoolSize() {
        return executor.getCorePoolSize();
    }

    @Override
    public int largestPoolSize() {
        return executor.getLargestPoolSize();
    }

    @Override
    public int maximumPoolSize() {
        return executor.getMaximumPoolSize();
    }

    @Override
    public int poolSize() {
        return executor.getPoolSize();
    }

    @Override
    public long taskCount() {
        return executor.getTaskCount();
    }
}
