package base.core.concurrent.aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * ReentrantLock内部维护了一个Sync内部类同步器继承了AQS，并且实现了FairSync公平模式和NonFairSync非公平模式
 *
 * lock流程：
 * 1.调用tryAcquire(int acquires)方法cas更新state变量尝试获取锁，公平模式还需要调用hasQueuedPredecessors()判断同步队列中头结点的下个节点是否是
 * 当前线程，不是或cas更新失败则获取锁失败，成功则设置当前线程为独享线程，若是同一线程再次获取锁则直接获取锁并对state+1
 * 2.步骤1失败的线程调用addWaiter(Node mode)方法，根据cas添加到队列尾部进入同步队列中，并返回当前节点
 * 3.成功进入同步队列后调用acquireQueued(final Node node, int arg)方法，自旋，判断当前节点的上一个节点是否是头节点，是头节点并且
 * tryAcquire(int acquires)成功则获取锁成功，否则更新节点waitStatus状态为SIGNAL待唤醒状态，并park阻塞当前线程
 *
 * unlock流程：
 * 1.调用tryRelease(int releases)方法，对state-1，若state等于0则清空当前独享线程，并返回true
 * 2.若步骤1返回true，则调用unparkSuccessor(Node node)，若当前节点waitStatus状态小于0（SIGNAL）则更新为0，再找到下一个不为null且waitStatus
 * 状态小于0（SIGNAL）的节点，unpark唤醒该线程
 *
 * 条件锁
 * await()流程：
 * 1.若当前线程已中断则直接抛出InterruptedException，否则调用addConditionWaiter()加入条件队列，如果最后一个节点waitStatus状态
 * 不是CONDITION是CANCEL则调用unlinkCancelledWaiters()抛弃所有条件队列中是CANCEL状态的节点，再为当前线程新建节点加入条件队列
 * 2.调用fullyRelease(Node node)释放当前线程锁
 * 3.循环判断isOnSyncQueue(Node node)该节点是否在同步队列中，不在则阻塞当前线程（此时等待signal()或signalAll()将节点加入同步队列，最后unlock唤醒该线程）
 * 4.被唤醒后继续循环判断是否在同步队列中，在则跳出循环
 * 5.接着进行lock流程步骤3排队获取锁
 *
 * signal()流程：
 * 1.调用isHeldExclusively()判断当前线程是否是设置的独享线程，不是则抛出IllegalMonitorStateException
 * 2.获取条件队列头节点并调用doSignal(Node first)，循环将条件队列节点加入到同步队列中，节点waitStatus状态均设置为SIGNAL，是CANCEL状态的节点则
 * 抛弃（注：signal或signalAll并没有唤醒节点，只是将节点加入了同步队列中，signal后unlock才是唤醒操作）
 */
class ReentrantLockTest {

    private static int count = 0;

    private static ReentrantLock lock = new ReentrantLock();

    private static Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        simpleLock();
//        conditionLock();
    }

    public static void simpleLock() throws InterruptedException {
        int threadCount = 1000;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        IntStream.range(0, threadCount).forEach(i -> new Thread(() -> {
            IntStream.range(0, 10000).forEach(j -> {
                try {
                    lock.lock();
                    count++;
                } finally {
                    lock.unlock();
                }
            });
            System.out.println(Thread.currentThread().getName());
            countDownLatch.countDown();
        }, "tt-" + i).start());

        countDownLatch.await();

        System.out.println(count);
    }

    public static void conditionLock() throws InterruptedException {
        new Thread(()->{
            try {
                lock.lock();
                System.out.println("child thread wait...");
                condition.await();
                System.out.println("child thread start");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println("child thread release lock");
                lock.unlock();
            }
        }).start();
        Thread.sleep(1000);
        try {
            System.out.println("main thread start");
            lock.lock();
            System.out.println("main thread signal...");
            condition.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("main thread release lock");
            lock.unlock();
        }
    }
}
