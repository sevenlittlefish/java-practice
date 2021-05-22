package base.core.concurrent.aqs;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch内部维护了一个Sync内部类同步器继承了AQS，实例化时需要指定初始的count值设置到AQS的state变量中
 *
 * await流程：
 * 1.调用acquireSharedInterruptibly(int arg)方法，若线程中断则抛出异常
 * 2.调用获取共享锁tryAcquireShared(int acquires)方法，若state变量等于0返回1获取锁成功，否则返回-1
 * 3.若步骤2返回-1则调用doAcquireSharedInterruptibly(int arg)方法
 * 4.调用addWaiter(Node mode)方法，节点类型为Node.SHARED共享模式，根据cas添加到队列尾部进入同步队列中，并返回当前节点
 * 5.自旋，判断当前节点的上一个节点是否是头节点，是头节点并且tryAcquireShared(int acquires)>=0则调用setHeadAndPropagate(Node node, int propagate)方法传入当前节点，
 * 否则更新节点waitStatus状态为SIGNAL待唤醒状态，并park阻塞当前线程等待唤醒（等待countDown唤醒）
 * 6.步骤5成功则设置传入的节点为头节点，该节点为null或节点waitStatus状态为SIGNAL，则找到该节点下一个节点，该节点为null或者节点类型为Node.SHARED共享模式则调用doReleaseShared()方法
 * 7.更新头节点的waitStatus为0并唤醒下一个节点
 *
 * countDown流程：
 * 1.调用releaseShared(int arg)方法
 * 2.调用tryReleaseShared(int releases)方法以自旋的方式对state变量-1并cas更新，state为0返回true
 * 3.步骤2返回true则调用doReleaseShared()，更新头节点的waitStatus为0并唤醒下一个节点
 */
public class CountDownLatchTest {

    public static void main(String[] args) {
        CountDownLatch cdl = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName());
                    cdl.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        System.out.println("Wait all thread reach");
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("All thread is reached");
    }
}
