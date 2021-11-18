package base.core.concurrent.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 采用ReentrantLock实现并发赠删改，读不需要加锁，因为每次修改都是拷贝一份新的数组进行操作（性能较低），因此是读写分离的
 */
public class CopyOnWriteArrayListTest {

    private static ExecutorService threadpool = Executors.newCachedThreadPool();

    public static void main(String[] args) throws InterruptedException {
        int threadCount = 100;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            threadpool.execute(() -> {
                for (int j = 0; j < 1000; j++) {
                    //list.addIfAbsent(j);
                    list.add(j);
                }
                countDownLatch.countDown();
            });
        }
        threadpool.shutdown();
        countDownLatch.await();
        System.out.println("size："+list.size());
    }
}
