package base.core.concurrent.thread.pool.custom;

public interface RejectPolicy {

    void reject(Runnable task, MyThreadPoolExecutor executor);
}
