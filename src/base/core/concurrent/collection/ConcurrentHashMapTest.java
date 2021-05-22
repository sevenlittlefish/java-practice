package base.core.concurrent.collection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class ConcurrentHashMapTest {

    public static void main(String[] args) throws InterruptedException {
        int threadCount = 100;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        Map map = new ConcurrentHashMap();
        for (int i = 0; i < threadCount; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    map.put(Thread.currentThread().getId()+"-key-"+j,j);
                }
                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await();
        System.out.println("size："+ map.size());
    }
}
