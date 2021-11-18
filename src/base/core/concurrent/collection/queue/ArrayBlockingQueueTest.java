package base.core.concurrent.collection.queue;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * （1）ArrayBlockingQueue不需要扩容，因为是初始化时指定容量，并循环利用数组；
 * （2）ArrayBlockingQueue利用takeIndex和putIndex循环利用数组；
 * （3）入队【add(E e),offer(E e),offer(E e,long timeout,TimeUnit unit),put(E e)】和出队【remove(),poll(),poll(long timeout,TimeUnit unit),take()】各定义了四组方法为满足不同的用途；
 * （4）利用重入锁ReentrantLock和两个条件（notEmpty，notFull）保证并发安全；
 */
public class ArrayBlockingQueueTest {

    public static void main(String[] args) {
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
        new Thread(()->{
            Random random = new Random();
            while (!Thread.interrupted()){
                try {
                    int value = random.nextInt(100);
                    queue.put(value);
                    System.out.println("in queue:"+value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(()->{
            while (!Thread.interrupted()){
                try {
                    Thread.sleep(1000);
                    Object value = queue.take();
                    System.out.println("out queue:"+value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
