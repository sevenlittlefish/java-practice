package base.core.concurrent.thread.pool.custom;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadPoolExecutorTest {
    public static void main(String[] args) {
        Executor threadPool = new MyThreadPoolExecutor(2, 4, 10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1), Executors.defaultThreadFactory(), new DiscardRejectPolicy());
        AtomicInteger num = new AtomicInteger();
        for (int i = 0; i < 10; i++) {
            threadPool.execute(() -> {
                try {
                    Thread.sleep(1000);
                    System.out.println("running:" + System.currentTimeMillis() + ":" + num.incrementAndGet());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        try {
            Thread.sleep(5000);
            System.out.println("====================================");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 5; i++) {
            threadPool.execute(() -> {
                try {
                    Thread.sleep(1000);
                    System.out.println("running:" + System.currentTimeMillis() + ":" + num.incrementAndGet());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
