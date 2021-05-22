package base.core.concurrent.thread.pool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinPoolTest {

    private static final long maxNum = 1000;
    public static final long threadDealNum = 10;

    public static void main(String[] args) {
        simpleAdd();
        System.out.println("---------------------");
        forJoinAdd();
    }

    public static void simpleAdd(){
        long start = System.currentTimeMillis();
        long result = 0;
        for (long i = 0; i <= maxNum; i++) {
            result+=i;
            try {
                //模拟任务耗时
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Simple add require "+(end-start)+" ms");
        System.out.println(result);
    }

    /**
     * ForkJoinPool参数：
     *      parallelism：并行等级，默认取当前处理器可处理线程数Runtime.getRuntime().availableProcessors()
     *      ForkJoinWorkerThreadFactory：线程工厂
     *      UncaughtExceptionHandler：异常处理handler，默认null
     *      asyncMode：异步模型，true采用FIFO队列，false采用LIFO栈
     * ForkJoinPool提供了三种调度子任务的方法：
     *      execute：异步执行指定任务，无返回结果
     *      invoke、invokeAll：异步执行指定任务，等待完成才返回结果
     *      submit：异步执行指定任务，并立即返回一个Future对象
     */
    public static void forJoinAdd(){
        long start = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool(10);
        Long result = pool.invoke(new ForkJoinTask(1, maxNum));
        long end = System.currentTimeMillis();
        System.out.println("ForJoin add require "+(end-start)+" ms");
        System.out.println(result);
    }

    /**
     * Fork/Join框架中ForkJoinTask实际的执行任务类，有以下两种实现，一般继承这两种实现类即可
     *      RecursiveAction：用于无结果返回的子任务
     *      RecursiveTask：用于有结果返回的子任务
     */
    static class ForkJoinTask extends RecursiveTask<Long>{
        private long start;
        private long end;

        public ForkJoinTask(long start,long end){
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if(end - start <= threadDealNum){
                long result = 0;
                for (long i = start; i <= end; i++) {
                    result += i;
                    try {
                        //模拟任务耗时
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return result;
            }else{
                long middle = (start+end)/2;
                ForkJoinTask task1 = new ForkJoinTask(start, middle);
                task1.fork();
                ForkJoinTask task2 = new ForkJoinTask(middle + 1, end);
                task2.fork();
                return task1.join() + task2.join();
            }
        }
    }
}
