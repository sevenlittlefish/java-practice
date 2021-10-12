package base.core.concurrent.aqs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.StampedLock;

public class StampedLockTest {

    private static int count;

    private static StampedLock lock = new StampedLock();

    private static ExecutorService threadPool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        readWriteTest();
//        lockUpgradeTest();
    }

    /**
     * 读写测试
     */
    private static void readWriteTest() {
        threadPool.execute(() -> {
            try {
                readTemp();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        threadPool.execute(() -> {
            try {
//                readTemp();
                writeTemp();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        threadPool.shutdown();
    }

    /**
     * 锁升级测试
     */
    private static void lockUpgradeTest() {
        for (int i = 0; i < 2; i++) {
            threadPool.execute(() -> {
                try {
                    lockUpgrade();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        threadPool.execute(() -> {
            try {
                writeTemp();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threadPool.shutdown();
    }

    /**
     * 读模式模板
     */
    private static void readTemp() throws InterruptedException {
        //乐观读
        long stamp = lock.tryOptimisticRead();
        System.out.println(String.format(Thread.currentThread().getName() + "  get optimistic read stamp:%s, read count:%s", stamp, count));
        Thread.sleep(500);
        //校验stamp
        if (!lock.validate(stamp)) {
            //升级为悲观读锁
            stamp = lock.readLock();
            try {
                System.out.println(String.format(Thread.currentThread().getName() + " get read stamp:%s, read again count:%s", stamp, count));
            } finally {
                //释放悲观读锁
                System.out.println(String.format(Thread.currentThread().getName() + " release read Stamp:%s", stamp));
                lock.unlockRead(stamp);
            }
        }
    }

    /**
     * 写模式模板
     */
    private static void writeTemp() throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            long stamp = lock.writeLock();
            System.out.println(String.format(Thread.currentThread().getName() + " get write Stamp:%s", stamp));
            try {
                Thread.sleep(1000);
                count++;
            } finally {
                System.out.println(String.format(Thread.currentThread().getName() + " release write Stamp:%s", stamp));
                lock.unlockWrite(stamp);
                Thread.sleep(1000);
            }
        }
    }

    /**
     * StampedLock 支持锁的降级（通过 tryConvertToReadLock() 方法实现）和升级（通过 tryConvertToWriteLock() 方法实现）
     */
    private static void lockUpgrade() throws InterruptedException {
        long stamp = lock.readLock();
        System.out.println(String.format(Thread.currentThread().getName() + " get read stamp:%s", stamp));
        long curCount = count;
        try {
            while (curCount == 0) {
                Thread.sleep(1000);
                long ws = lock.tryConvertToWriteLock(stamp);
                System.out.println(String.format(Thread.currentThread().getName() + " try stamp:%s", ws));
                if (ws != 0) {
                    stamp = ws;
                    curCount = count;
                    break;
                } else {
                    System.out.println(String.format(Thread.currentThread().getName() + " release read stamp:%s", stamp));
                    lock.unlockRead(stamp);
                    stamp = lock.writeLock();
                }
            }
        } finally {
            System.out.println(String.format(Thread.currentThread().getName() + " release stamp:%s", stamp));
            lock.unlock(stamp);
        }
        System.out.println(String.format(Thread.currentThread().getName() + " curStamp:%s, current count:%s", stamp, curCount));
    }
}
