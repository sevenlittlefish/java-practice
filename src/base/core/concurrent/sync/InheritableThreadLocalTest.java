package base.core.concurrent.sync;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 创建线程时，会把父线程inheritableThreadLocals的数据全部复制到子线程的Entry中
 */
public class InheritableThreadLocalTest {

    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
    private static ThreadLocal<Integer> inheritableThreadLocal = new InheritableThreadLocal<>();

    public static void main(String[] args) {
        threadLocal.set(ThreadLocalRandom.current().nextInt());
        inheritableThreadLocal.set(ThreadLocalRandom.current().nextInt());
        new Thread(() -> {
            System.out.println("Child Thread catch data:" + threadLocal.get());
            System.out.println("Child Thread catch data:" + inheritableThreadLocal.get());
        }).start();
    }
}
