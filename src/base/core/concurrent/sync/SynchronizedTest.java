package base.core.concurrent.sync;

/**
 * （1）synchronized在编译时会在同步块前后生成monitorenter和monitorexit字节码指令；
 * （2）monitorenter和monitorexit字节码指令需要一个引用类型的参数，基本类型不可以；
 * （3）monitorenter和monitorexit字节码指令更底层是使用Java内存模型的lock和unlock指令；
 * （4）synchronized是可重入锁；
 * （5）synchronized是非公平锁；
 * （6）synchronized可以同时保证原子性、可见性、有序性；
 * （7）synchronized有三种状态：偏向锁、轻量级锁、重量级锁；
 *      1.偏向锁，是指一段同步代码一直被一个线程访问，那么这个线程会自动获取锁，降低获取锁的代价。
 *      2.轻量级锁，是指当锁是偏向锁时，被另一个线程所访问，偏向锁会升级为轻量级锁，这个线程会通过自旋的方式尝试获取锁，不会阻塞，提高性能。
 *      3.重量级锁，是指当锁是轻量级锁时，当自旋的线程自旋了一定的次数后，还没有获取到锁，就会进入阻塞状态，该锁升级为重量级锁，重量级锁会使其他线程阻塞，性能降低。
 */
public class SynchronizedTest {

    public static int num = 0;

    public static void syncAddByClass(String name){
        synchronized (SynchronizedTest.class){
            try {
                System.out.println(name+"-add:"+(++num));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized void syncSubByClass(String name){
        try {
            System.out.println(name+"-sub:"+(--num));
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void syncAddByInstance(String name){
        try {
            System.out.println(name+"-add:"+(++num));
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void syncSubByInstance(String name){
        synchronized (this){
            try {
                System.out.println(name+"-sub:"+(--num));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        SynchronizedTest obj = new SynchronizedTest();
        for (int i = 0; i < 5; i++) {
            int num = i;
            /*new Thread(()-> syncAddByClass("thread-"+ num)).start();
            new Thread(()-> syncSubByClass("thread-"+ num)).start();*/
            new Thread(()->obj.syncAddByInstance("thread-"+ num)).start();
            new Thread(()->obj.syncSubByInstance("thread-"+ num)).start();
        }
    }
}
