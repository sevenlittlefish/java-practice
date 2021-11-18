package base.core.concurrent.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadAlternateTest {

    public static void main(String[] args) throws InterruptedException {
//        method1();
//        method2();
        method3();
    }

    /**
     * 线程交替打印1~100的数，一个打印奇数一个打印偶数
     * synchronized实现
     */
    public static void method1(){
        Object obj = new Object();
        new Thread(() -> {
            for (int i = 1; i <= 100; i++) {
                if(i%2==1) {
                    synchronized (obj){
                        System.out.println(Thread.currentThread().getName() + ":" + i);
                        obj.notify();
                        if(i == 99) break;
                        try {
                            obj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
        new Thread(() -> {
            for (int i = 1; i <= 100; i++) {
                if(i%2==0) {
                    synchronized (obj){
                        System.out.println(Thread.currentThread().getName() + ":" + i);
                        obj.notify();
                        if(i == 100) break;
                        try {
                            obj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    /**
     * 线程交替打印1~100的数，一个打印奇数一个打印偶数
     * ReentrantLock实现
     */
    public static void method2(){
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        new Thread(() -> {
            for (int i = 1; i <= 100; i++) {
                if(i%2==1) {
                    try {
                        lock.lock();
                        System.out.println(Thread.currentThread().getName() + ":" + i);
                        condition.signal();
                        if(i == 99) break;
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }).start();
        new Thread(() -> {
            for (int i = 1; i <= 100; i++) {
                if(i%2==0) {
                    try {
                        lock.lock();
                        System.out.println(Thread.currentThread().getName() + ":" + i);
                        condition.signal();
                        if(i == 100) break;
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }).start();
    }

    /**
     * 三个线程交替打印ABC，各打印10次
     * synchronized实现
     */
    public static void method3() throws InterruptedException {
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                synchronized (a){
                    synchronized (b){
                        b.notify();
                        System.out.println("A");
                    }
                    try {
                        if (i==9) break;
                        a.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        Thread.sleep(10);
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                synchronized (b){
                    synchronized (c){
                        c.notify();
                        System.out.println("B");
                    }
                    try {
                        if (i==9) break;
                        b.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        Thread.sleep(10);
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                synchronized (c){
                    synchronized (a){
                        a.notify();
                        System.out.println("C");
                    }
                    try {
                        if (i==9) break;
                        c.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
