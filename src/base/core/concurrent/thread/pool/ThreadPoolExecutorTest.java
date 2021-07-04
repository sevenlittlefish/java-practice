package base.core.concurrent.thread.pool;

import java.util.concurrent.*;

public class ThreadPoolExecutorTest {

    /**
     * corePoolSize 核心线程数
     * maximumPoolSize 最大线程数
     * keepAliveTime 线程空闲时间
     * TimeUnit 时间单位
     * BlockingQueue 阻塞队列
     * ThreadFactory 线程工厂
     * RejectedExecutionHandler 拒绝策略：
     * 1.CallerRunsPolicy：若线程池未shutdown，则直接调用任务的run方法
     * 2.AbortPolicy：默认，直接抛出RejectedExecutionException
     * 3.DiscardPolicy：什么也不做，相当于直接丢弃
     * 4.DiscardOldestPolicy：若线程池未shutdown，把队列中头结点（最先入队的节点）抛弃，并execute执行当前任务
     */
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            5,
            10,
            60,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(5),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(Thread t, Throwable e) {
                            throw new RuntimeException(e);
                        }
                    });
                    return thread;
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            executor.execute(() -> {
                try {
                    //表示CallerRunsPolicy采用调用者线程（也就是当前调用线程池的线程）执行任务
                    if (Thread.currentThread().getName().contains("main")) {
                        Thread.sleep(5000);
                    } else {
                        Thread.sleep(1000);
                    }
                    System.out.println(Thread.currentThread().getName() + " is execute!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
    }
}
