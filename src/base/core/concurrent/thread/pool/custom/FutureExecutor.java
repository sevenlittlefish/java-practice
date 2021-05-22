package base.core.concurrent.thread.pool.custom;

public interface FutureExecutor extends Executor {
    <T> Future<T> submit(Callable<T> command);
}
