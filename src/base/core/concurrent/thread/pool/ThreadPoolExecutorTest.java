package base.core.concurrent.thread.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorTest {

    /**
     * corePoolSize 核心线程数
     * maximumPoolSize 最大线程数
     * keepAliveTime 线程空闲时间
     * TimeUnit 时间单位
     * BlockingQueue 阻塞队列
     * ThreadFactory 线程工厂
     * RejectedExecutionHandler 拒绝策略：
     *   1.CallerRunsPolicy：若线程池未shutdown，则直接调用任务的run方法
     *   2.AbortPolicy：默认，直接抛出RejectedExecutionException
     *   3.DiscardPolicy：什么也不做，相当于直接丢弃
     *   4.DiscardOldestPolicy：若线程池未shutdown，把队列中头结点（最先入队的节点）抛弃，并execute执行当前任务
     */
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5));

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            executor.execute(()->{
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+" is execute!");
            });
        }
    }
}
