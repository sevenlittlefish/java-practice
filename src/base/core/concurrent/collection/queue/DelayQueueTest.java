package base.core.concurrent.collection.queue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * （1）DelayQueue是阻塞队列；
 * （2）DelayQueue内部存储结构使用优先级队列PriorityQueue；
 * （3）DelayQueue使用重入锁和条件锁来控制并发安全；
 * （4）DelayQueue常用于定时任务；
 */
public class DelayQueueTest {

    public static void main(String[] args) {
        DelayQueue<Message> queue = new DelayQueue<>();
        long now = System.currentTimeMillis();

        new Thread(()->{
           while(true){
               try {
                   System.out.println(queue.take().deadline - now);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        }).start();

        queue.add(new Message(now + 5000));
        queue.add(new Message(now + 8000));
        queue.add(new Message(now + 2000));
        queue.add(new Message(now + 1000));
        queue.add(new Message(now + 7000));
    }

    static class Message implements Delayed{
        long deadline;

        public Message(long deadline){
            this.deadline = deadline;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return deadline - System.currentTimeMillis();
        }

        @Override
        public int compareTo(Delayed o) {
            return (int)(getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public String toString() {
            return "Message{" +
                    "deadline=" + deadline +
                    '}';
        }
    }
}
