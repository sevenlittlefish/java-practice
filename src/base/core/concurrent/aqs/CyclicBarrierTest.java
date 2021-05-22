package base.core.concurrent.aqs;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CyclicBarrier内部维护了一个ReentrantLock锁、Condition锁、Generation内部类，实例化时需要指定初始parties变量值，同时也赋值给count变量
 *
 * await流程：
 * 1.调用dowait(boolean timed, long nanos)方法，ReentrantLock加锁
 * 2.获取当前Generation判断是否broken，是则抛BrokenBarrierException，线程中断也抛异常InterruptedException
 * 3.对count-1，判断count是否为0（是否为最后一个到达的线程），是则调用Condition.signalAll()条件锁唤醒所有线程，并重新new Generation()创建下一代，重置count值为parties
 * 4.若count不为0，调用Condition.await()等待被唤醒，若timed为true则调用Condition.awaitNanos(long nanosTimeout)超时等待，超时则抛出异常
 * 5.ReentrantLock释放锁
 */
public class CyclicBarrierTest {

    private static ExecutorService threadpool = Executors.newCachedThreadPool();
    private static int length = 50;
    private static int threadNum = 5;
    private static Random random = new Random();
    private static CyclicBarrier cb = new CyclicBarrier(threadNum, new Runnable() {
        @Override
        public void run() {
            threadpool.shutdownNow();
            System.out.println("比赛结束！");
        }
    });

    static class Horse implements Runnable{

        public volatile int num = 0;
        public StringBuilder sb = new StringBuilder();
        public StringBuilder curSb = new StringBuilder();

        @Override
        public void run() {
            while(!Thread.interrupted()){
                try {
                    Thread.sleep(500);
                    int step = random.nextInt(3)+1;
                    for (int i = 0; i < step; i++) {
                        sb.append("=");
                        curSb.append("-");
                    }
                    num += step;
                    System.out.println(Thread.currentThread().getName()+"："+curSb);
                    if(num >= length){
                        StringBuilder result = sb.append(num);
                        System.out.println(Thread.currentThread().getName()+"："+result+"（"+Thread.currentThread().getId()+"号马到达终点啦）");
                        cb.await();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(length+"米赛马比赛开始！");
        for (int i = 0; i < threadNum; i++) {
            threadpool.execute(new Horse());
        }
    }
}
