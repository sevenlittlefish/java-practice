package base.core.concurrent.thread.pool.custom;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class FutureTask<T> implements Runnable,Future<T> {

    /**
     * 任务执行状态，0未开始，1完成，2异常
     * 也可使用volatile+Unsafe实现CAS操作
     */
    private AtomicInteger state = new AtomicInteger(NEW);
    public static final int NEW = 0;
    public static final int FINISHED = 1;
    public static final int EXCEPTION = 2;

    private AtomicInteger tryTimes = new AtomicInteger();

    /**
     * 任务执行结果
     * 执行正常，返回结果为 T
     * 执行异常，返回结果为 Exception
     */
    private Object result;

    /**
     * 调用者线程
     * 也可使用volatile+Unsafe实现CAS操作
     */
    private AtomicReference<Thread> caller = new AtomicReference<>();

    private Callable<T> task;

    public FutureTask(Callable<T> task){
        this.task = task;
    }

    @Override
    public T get() {
        int s = state.get();
        //若任务还未完成，判断是否需要进入阻塞状态
        if(s == NEW){
            //标识调用者线程是否被标记过
            boolean marked = false;
            for (;;){
                //重新获取state的值
                s = state.get();
                //如果state大于NEW说明完成了，跳出循环
                if(s > NEW){
                    break;
                }
                //此处必须把caller的CAS更新和park()方法分成两步处理，不能把park()放在CAS里面
                else if(!marked){
                    /**
                     * 尝试更新调用者线程
                     * 试想断点停在此处
                     * 此时state为NEW，让run()方法执行到底，它不会执行finish()中的unpark()方法
                     * 这时打开断点，这里会更新caller成功，但是循环从头再执行一遍发现state已经变了
                     * 直接在上面的if(s>NEW)处跳出循环了，因为finish()在修改state内部
                     */
                    marked = caller.compareAndSet(null,Thread.currentThread());
                }else{
                    /**
                     * 调用者线程更新之后park当前线程
                     * 试想断点停在此处
                     * 此时state为NEW，让run()方法执行到底，因为上面的caller已经设置值了
                     * 所以会执行finish()方法中的unpark()方法
                     * 这时再打开断点，这里不会park
                     * 见unpark()方法的注释，上面写得很清楚：
                     * 如果线程执行了park()方法，那么执行unpark()方法会唤醒那个线程
                     * 如果先执行了unpark()方法，那么线程下一次执行park()方法将不会阻塞
                     */
                    LockSupport.park();//当实际线程数超出线程池可处理线程数，多余线程将会永远阻塞在这里，可使用以下代码处理
//                    tryTimes.getAndIncrement();
//                    if(tryTimes.get() > 1) break;
//                    LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
                }
            }
        }
        if(s == FINISHED)
            return (T)result;
        if(result instanceof Exception)
            throw new RuntimeException((Throwable)result);
        return null;
    }

    @Override
    public void run() {
        if(state.get() != NEW){
            return;
        }
        try {
            T r = task.call();
            /**
             * CAS更新state值为FINISHED
             * 执行成功，把结果r赋给result
             * 执行失败，说明state状态不为NEW，说明已经执行过了
             */
            if(state.compareAndSet(NEW,FINISHED)){
                this.result = r;
            }
            finish();
        } catch (Exception e) {
            /**
             * CAS更新state值为EXCEPTION
             * 执行成功，把异常e赋给result
             * 执行失败，说明state状态不为NEW，说明已经执行过了
             */
            if(state.compareAndSet(NEW,EXCEPTION)){
                this.result = e;
            }
        }
    }

    private void finish(){
        /**
         * 检查调用者是否为空，如果不为空，唤醒它
         * 调用者在调用get()方法的时候进入阻塞状态
         */
        for (Thread c;(c = caller.get()) != null;){
            if(caller.compareAndSet(c,null)){
                LockSupport.unpark(c);
            }
        }
    }
}
