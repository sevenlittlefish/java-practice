package base.core.concurrent.collection.queue;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

/**
 * SynchronousQueue没有容量，是无缓冲等待队列，是一个不存储元素的阻塞队列，会直接将任务交给消费者，必须等队列中的添加元素被消费后才能继续添加新的元素
 */
public class SynchronousQueueTest {

    public static void main(String[] args) {
        //构造函数入参，true：FIFO队列，false（默认）：栈
        SynchronousQueue<Object> queue = new SynchronousQueue<>(true);
        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                Random random = new Random();
//                while (!Thread.interrupted()){
                    try {
                        int value = random.nextInt(100);
                        queue.put(value);
                        System.out.println(Thread.currentThread().getName()+": in queue:"+value);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                }
            }).start();
        }
        for (int i = 0; i < 2; i++) {
            new Thread(()->{
//                while (!Thread.interrupted()){
                    try {
                        Thread.sleep(1000);
                        Object value = queue.take();
                        System.out.println(Thread.currentThread().getName()+":out queue:"+value);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                }
            }).start();
        }
    }
}
