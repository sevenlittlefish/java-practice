package base.core.concurrent.sync;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalTest {
    public static ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private static ThreadLocal<SimpleDateFormat> threadMap = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            int time = i*1000;
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    SimpleDateFormat dateFormat = threadMap.get();
                    String result = dateFormat.format(time);
                    System.out.println(result);
                }
            });
        }
        threadPool.shutdown();
    }
}
