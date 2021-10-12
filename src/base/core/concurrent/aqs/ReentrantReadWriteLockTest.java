package base.core.concurrent.aqs;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock内部维护了一个Sync内部类同步器继承了AQS，并且实现了FairSync公平模式和NonFairSync非公平模式，
 * 还包含了ReadLock和WriteLock两个内部类，同时以32位二进制存储读锁（高16位）和写锁（低16位）
 *
 * ReadLock.lock流程：
 * 1.调用tryAcquireShared(int unused)方法，获取state变量值并exclusiveCount(int c)计算当前独享线程数（获取写锁）是否不为0，是并且当前线程不是设置的独显线程则返回-1
 * 2.若步骤1返回-1则调用doAcquireShared(int arg)方法，加入同步队列并依次唤醒后续节点（注：只会唤醒节点为Node.SHARED，读锁不会唤醒，除非unlock释放共享锁），
 * 若需要阻塞则park当前线程，和CountDownLatch、Semaphore的doAcquireSharedInterruptibly(int arg)方法类似
 * 3.若不是独享线程，则继续sharedCount(int c)计算当前共享线程数（获取读锁），调用readerShouldBlock()方法判断共享线程是否要阻塞（注：若节点类型不是Node.SHARED则放回true），
 * 不需要阻塞并且共享线程数小于最大线程数并且cas更新共享线程数+1成功，则返回1，即成功获取锁
 * 4.步骤3不在判断条件内，则调用fullTryAcquireShared(Thread current)方法自旋，类似重复步骤1、2、3，失败返回-1，成功返回1
 *
 * ReadLock.unlock流程：
 * 1.调用tryReleaseShared(int unused)方法，cas自旋扣减共享线程数，为0则表示共享锁释放完成
 * 2.调用doReleaseShared()方法，更新头节点的waitStatus为0并唤醒下一个节点（写锁）
 *
 * WriteLock.lock流程：
 * 1.调用tryAcquire(int acquires)方法，获取state变量值并exclusiveCount(int c)计算当前独享线程数 w
 * 2.若state不为0，且(w == 0 || current != getExclusiveOwnerThread())成立，说明当前锁被共享线程占用或当前锁被独享线程占用但非当前线程，
 * 则调用acquireQueued(addWaiter(Node.EXCLUSIVE), arg))方法，以Node.EXCLUSIVE独享模式进入同步队列中；否则独享线程数state+1，成功获取锁（同一线程可重入）
 * 3.若state为0，则cas更新state+1更新独享线程数，更新失败一样加入同步队列，更新成功设置该线程为独享线程，返回true，获取锁成功
 *
 * WriteLock.unlock流程：
 * 1.调用tryRelease(int releases)方法，对state-1若不为0返回false，若等于0则表示释放完成，清空当前独显线程，返回true
 * 2.步骤1返回true则调用unparkSuccessor(Node node)方法，若当前节点waitStatus状态小于0（SIGNAL）则更新为0，再找到下一个不为null且waitStatus状态小于0（SIGNAL）的节点，unpark唤醒该线程
 */
public class ReentrantReadWriteLockTest {

    private static ExecutorService threadPool = Executors.newCachedThreadPool();

    public static class SafeTreeMap {
        public final Map<Object, Object> map = new TreeMap<>();
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        private final Lock readLock = lock.readLock();
        private final Lock writeLock = lock.writeLock();

        public Object get(String key) throws InterruptedException {
            readLock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "获取到read锁");
                Thread.sleep(1000);
                return map.get(key);
            } finally {
                readLock.unlock();
            }
        }

        public Object put(String key, Object value) throws InterruptedException {
            writeLock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "获取到write锁");
                Thread.sleep(1000);
                return map.put(key, value);
            } finally {
                writeLock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        SafeTreeMap safeTreeMap = new SafeTreeMap();
        for (int i = 0; i < 5; i++) {
            int num = i;
            threadPool.execute(() -> {
                Random random = new Random();
                for (int j = 0; j < 10; j++) {
                    try {
                        StringBuilder sb = new StringBuilder();
                        if (num / 2 != 0) {
                            String key1 = sb.append(Thread.currentThread().getId()).append("_").append(random.nextInt(10)).toString();
                            safeTreeMap.get(key1);
                        } else {
                            String key2 = sb.append(Thread.currentThread().getId()).append("_").append(j).toString();
                            safeTreeMap.put(key2, j);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        threadPool.shutdown();
    }
}
