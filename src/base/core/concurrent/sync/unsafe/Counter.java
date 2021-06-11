package base.core.concurrent.sync.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Counter {
    private volatile int count = 0;
    private static long offset;
    private static Unsafe unsafe;
    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe)field.get(null);
            offset = unsafe.objectFieldOffset(Counter.class.getDeclaredField("count"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void increment(){
        int before = count;
        while(!unsafe.compareAndSwapInt(this,offset,before,before+1)){
            before = count;
        }
    }

    public int getCount(){
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        IntStream.range(0,10).forEach(i->threadPool.submit(()->IntStream.range(0,1000).forEach(j->counter.increment())));
        threadPool.shutdown();
        Thread.sleep(1000);
        System.out.println(counter.getCount());
    }
}
