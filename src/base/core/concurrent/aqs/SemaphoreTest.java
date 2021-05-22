package base.core.concurrent.aqs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Semaphore内部维护了一个Sync内部类同步器继承了AQS，并且实现了FairSync公平模式和NonFairSync非公平模式，实例化时需要指定初始的permits值设置到AQS的state变量中
 *
 * tryAcquire流程：
 * 1.调用nonfairTryAcquireShared(int acquires)方法，自旋cas扣减permits许可（对state变量操作），扣减成功并且剩余permits>=0则返回true，否则放回false
 *
 * acquire流程：
 * 1.调用acquireSharedInterruptibly(int arg)方法，若线程中断则抛出异常
 * 2.调用获取共享锁tryAcquireShared(int acquires)方法，返回剩余的permits（state变量值），若大于0则获取锁成功，否则失败
 * 3.若步骤2失败则调用doAcquireSharedInterruptibly(int arg)方法
 * 4.调用addWaiter(Node mode)方法，节点类型为Node.SHARED共享模式，根据cas添加到队列尾部进入同步队列中，并返回当前节点
 * 5.自旋，判断当前节点的上一个节点是否是头节点，是头节点并且tryAcquireShared(int acquires)>=0则调用setHeadAndPropagate(Node node, int propagate)方法传入当前节点，
 * 否则更新节点waitStatus状态为SIGNAL待唤醒状态，并park阻塞当前线程等待唤醒（等待release唤醒）
 * 6.步骤5成功则设置传入的节点为头节点，该为null或节点waitStatus状态为SIGNAL，则找到该节点下一个节点，为null或者节点类型为Node.SHARED共享模式则调用doReleaseShared()方法
 * 7.更新头节点的waitStatus为0并唤醒下一个节点
 *
 * release流程：
 * 1.调用releaseShared(int arg)方法
 * 2.调用tryReleaseShared(int releases)方法以自旋cas增加permits许可（对state变量操作），更新成功返回true
 * 3.步骤2返回true则调用doReleaseShared()，更新头节点的waitStatus为0并唤醒下一个节点
 */
public class SemaphoreTest {

    private static final Semaphore semaphore = new Semaphore(10);
    public static final AtomicInteger successCount = new AtomicInteger(0);
    public static final AtomicInteger failCount = new AtomicInteger(0);
    private static ExecutorService threadpool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            threadpool.execute(()->skill());
        }
        threadpool.shutdown();
    }

    public static boolean skill(){
//        if(semaphore.tryAcquire()){
            try {
                semaphore.acquire();
                Thread.sleep(1000);
                System.out.println("seckill success, count="+successCount.incrementAndGet());
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
//        }
        System.out.println("no permits, count="+failCount.incrementAndGet());
        return false;
    }
}
