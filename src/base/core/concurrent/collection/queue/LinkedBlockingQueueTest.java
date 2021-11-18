package base.core.concurrent.collection.queue;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *（1）LinkedBlockingQueue采用单链表的形式实现；
 *（2）LinkedBlockingQueue采用两把锁的锁分离技术实现入队出队互不阻塞，提高效率；
 *（3）LinkedBlockingQueue是有界队列，不传入容量时默认为最大int值；
 *
 * LinkedBlockingQueue与ArrayBlockingQueue对比？
 *（a）后者入队出队采用一把锁，导致入队出队相互阻塞，效率低下；
 *（b）前才入队出队采用两把锁，入队出队互不干扰，效率较高；
 *（c）二者都是有界队列，如果长度相等且出队速度跟不上入队速度，都会导致大量线程阻塞；
 *（d）前者如果初始化不传入初始容量，则使用最大int值，如果出队速度跟不上入队速度，会导致队列特别长，占用大量内存；
 */
public class LinkedBlockingQueueTest {

    public static void main(String[] args) {
        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>(5);
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
