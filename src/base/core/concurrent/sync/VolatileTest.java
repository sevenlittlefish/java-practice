package base.core.concurrent.sync;

/**
 * volatile：
 * 1.java内存模型规定，volatile变量的每次修改都必须立即回写到主内存中，volatile变量的每次使用都必须从主内存刷新最新的值同步到工作内存中
 * 2.能保证有序性和可见性（采用内存屏障实现），不能保证原子性
 *
 * 伪共享：
 * 为提高cpu从主内存读取数据的效率，在cpu和主内存中设置高速缓存，高速缓存都以缓存行加载，每条缓存行加载64个字节，也就是可加载连续的8个long类型，
 * 但是若设置了volatile，某个线程修改了缓存行的数据，就需要将结果同步回主内存，清除缓存行，其他线程若就享受不到缓存行所带来的收益，需要重新从
 * 主内存中读取数据
 */
public class VolatileTest {

    // a不使用volatile修饰
    public static long a = 0;
    // 消除缓存行的影响，也可用@sun.misc.Contended注解消除缓存行影响
    public static long p1, p2, p3, p4, p5, p6, p7;
    // b使用volatile修饰
    public static volatile long b = 0;
    // 消除缓存行的影响
    public static long q1, q2, q3, q4, q5, q6, q7;
    // c不使用volatile修饰
    public static long c = 0;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            while (a == 0) {
                //试着注释下面这行代码查看结果
                long x = b;
            }
            System.out.println("a=" + a);
        }).start();
        new Thread(() -> {
            while (c == 0) {
                //试着注释下面这行代码查看结果
                long x = b;
            }
            System.out.println("c=" + c);
        }).start();
        //若不sleep主线程先于上述两个子线程，根据happens-before规则后续事件对当前事件操作可见
        Thread.sleep(100);
        a = 1;
        b = 1;
        c = 1;
    }
}
