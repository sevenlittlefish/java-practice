package base.core.concurrent.thread.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * CompletionService中维护了一个Future的阻塞队列，最先完成的Future任务最先添加到队列头部，可最先取出
 */
public class CompletionServiceTest {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CompletionService<Integer> service = new ExecutorCompletionService<>(executor);
        Random random = new Random();
        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            futures.add(service.submit(() -> {
                int val = random.nextInt(10)+1;
                Thread.sleep(val * 1000);
                System.out.println(val);
                return val;
            }));
        }

        int result = 0;
        for (Future<Integer> future : futures) {
            try {
                result += service.take().get();
                System.out.println("Current result is "+result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                future.cancel(true);
            }
        }
        executor.shutdown();
    }
}
