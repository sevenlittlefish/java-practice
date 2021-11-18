package base.core.concurrent.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadLifeTest {

    public static void main(String[] args) {
        Object object = new Object();
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            synchronized (object) {
                try {
                    System.out.println("Thread1 waiting");
                    object.wait();
                    System.out.println("Thread1 after waiting");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread1").start();

        new Thread(() -> {
            synchronized (object) {
                try {
                    System.out.println("Thread2 notify");
                    object.notify();//打开或注释观察Thread1状态
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread2").start();

        new Thread(() -> {
            lock.lock();
            System.out.println("Thread3 waiting");
            try {
                condition.await();
                System.out.println("Thread3 after waiting");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "Thread3").start();

        new Thread(() -> {
            lock.lock();
            System.out.println("Thread4");
            condition.signal();//打开或注释观察Thread3状态
            try {
                Thread.sleep(10000);
                System.out.println("Thread3 after waiting");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "Thread3").start();
    }
}
