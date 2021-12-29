package base.core.concurrent.thread.pool.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MyThreadPoolFutureExecutorTest {

    public static void main(String[] args) {
        MyThreadPoolFutureExecutor executor = new MyThreadPoolFutureExecutor(4, 10, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(5));
        List<Future<Integer>> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int num = i;
            Future<Integer> future = executor.submit(() -> {
                Thread.sleep(100);
                return num;
            });
            list.add(future);
        }
        for (Future<Integer> future : list) {
            //当实际线程数超出线程池可处理线程数，由于拒绝策略忽略了该任务，FutureTask不会调用run方法，因此剩余部分future会阻塞在这里，可采用下面的超时get方法避免
//            System.out.println("Task result:" + future.get());
            System.out.println("Task result:" + future.get(5, TimeUnit.SECONDS));
        }
    }
}
