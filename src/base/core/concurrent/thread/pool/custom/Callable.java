package base.core.concurrent.thread.pool.custom;

public interface Callable<V> {
    V call() throws InterruptedException;
}
