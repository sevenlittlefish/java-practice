package base.core.concurrent.thread.pool.custom;

public interface Executor {
    void execute(Runnable command);
}
