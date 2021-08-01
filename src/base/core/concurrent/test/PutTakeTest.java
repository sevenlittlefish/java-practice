package base.core.concurrent.test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PutTakeTest {
    private static final ExecutorService pool = Executors.newCachedThreadPool();
    private final AtomicInteger putSum = new AtomicInteger(0);
    private final AtomicInteger takeSum = new AtomicInteger(0);
    private final CyclicBarrier barrier;
    private final BoundedBuffer<Integer> bb;
    private final int nTrials, nPairs;

    public static void main(String[] args) {
        new PutTakeTest(10, 10, 100000).test();
        pool.shutdown();
    }

    public PutTakeTest(int capacity, int nPairs, int nTrials) {
        this.barrier = new CyclicBarrier(nPairs * 2 + 1);
        this.bb = new BoundedBuffer<>(capacity);
        this.nTrials = nTrials;
        this.nPairs = nPairs;
    }

    public void test() {
        try {
            for (int i = 0; i < nPairs; i++) {
                pool.execute(new Producer());
                pool.execute(new Consumer());
            }
            barrier.await();
            barrier.await();
            System.out.println(String.format("putSum:%s, takeSum:%s", putSum.get(), takeSum.get()));
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

    //随机数生成器
    static int xorShift(int y) {
        y ^= (y << 6);
        y ^= (y >> 21);
        y ^= (y << 7);
        return y;
    }

    class Producer implements Runnable {
        @Override
        public void run() {
            try {
                int seed = (this.hashCode() ^ (int) System.nanoTime());
                int sum = 0;
                barrier.await();
                for (int i = nTrials; i > 0; --i) {
                    bb.put(seed);
                    sum += seed;
                    seed = xorShift(seed);
                }
                putSum.getAndAdd(sum);
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }

    class Consumer implements Runnable {
        @Override
        public void run() {
            try {
                barrier.await();
                int sum = 0;
                for (int i = nTrials; i > 0; --i) {
                    sum += bb.take();
                }
                takeSum.getAndAdd(sum);
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
