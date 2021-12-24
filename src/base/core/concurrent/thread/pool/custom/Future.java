package base.core.concurrent.thread.pool.custom;

import java.util.concurrent.TimeUnit;

public interface Future<T> {
    T get();

    T get(long timeout, TimeUnit unit);
}
