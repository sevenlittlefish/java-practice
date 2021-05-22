package base.core.concurrent.collection.queue;

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * （1）PriorityBlockingQueue整个入队出队的过程与PriorityQueue基本是保持一致的；
 * （2）PriorityBlockingQueue使用ReentrantLock锁+一个notEmpty条件锁控制并发安全；
 * （3）PriorityBlockingQueue扩容时使用一个单独变量的CAS操作来控制只有一个线程进行扩容；
 * （4）入队使用自下而上的堆化；
 * （5）出队使用自上而下的堆化；
 */
public class PriorityBlockingQueueTest {

    public static void main(String[] args) {
        Random random = new Random();
        PriorityBlockingQueue<Integer> queue = new PriorityBlockingQueue<>(11, (o1, o2) -> (o1-o2) > 0 ? -1 : 1);
        new Thread(()->{
            while(!Thread.interrupted()){
                try {
                    Thread.sleep(500);
                    int value = random.nextInt(100);
                    queue.put(value);
                    System.out.println(Thread.currentThread().getName()+": in queue:"+value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(()->{
            while(!Thread.interrupted()){
                try {
                    Thread.sleep(2000);
                    Integer value = queue.take();
                    System.out.println(Thread.currentThread().getName()+":out queue:"+value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
