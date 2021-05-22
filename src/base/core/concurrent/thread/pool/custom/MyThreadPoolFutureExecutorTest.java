package base.core.concurrent.thread.pool.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class MyThreadPoolFutureExecutorTest {

    public static void main(String[] args) {
        MyThreadPoolFutureExecutor executor = new MyThreadPoolFutureExecutor("test", 5, 10, new LinkedBlockingQueue<>(5), new DiscardRejectPolicy());
        List<Future<Integer>> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int num = i;
            Future<Integer> future = executor.submit(() -> {
                Thread.sleep(1000);
                return num;
            });
            list.add(future);
        }
        for (Future<Integer> future : list) {
            //当实际线程数超出线程池可处理线程数，剩余部分future会阻塞在这里
            System.out.println(future.get());
        }
    }
}
