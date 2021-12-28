package base.core.concurrent.aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 通过AQS实现lock
 */
public class AQSTest {

    private static int result;

    public static void main(String[] args) throws InterruptedException {
        int threadCount = 10;
        ExecutorService threadPool = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        Sync sync = new Sync();
        for (int i = 0; i < threadCount; i++) {
            threadPool.execute(() -> {
                for (int j = 0; j < 1000; j++) {
                    sync.lock();
                    result++;
                    sync.unlock();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        System.out.println(result);
        threadPool.shutdown();
    }

    static class Sync extends AbstractQueuedSynchronizer {

        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        public void lock() {
            super.acquire(1);
        }

        public void unlock() {
            super.release(0);
        }
    }
}
