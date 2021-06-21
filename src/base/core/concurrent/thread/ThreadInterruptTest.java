package base.core.concurrent.thread;

public class ThreadInterruptTest {

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            long start = System.currentTimeMillis();
            while (!Thread.interrupted()) {
                System.out.println(Thread.currentThread().getName() + " sleep/wait start");
                try {
//                    Thread.sleep(2000);
                    synchronized (Thread.currentThread()) {
                        Thread.currentThread().wait(2000);
                    }
                } catch (InterruptedException e) {
                    //直接捕获中断异常如sleep或wait，Thread.interrupted()依旧是未中断状态，需要手动设置中断
                    //恢复中断状态以避免屏蔽中断
                    Thread.currentThread().interrupt();
                }
                System.out.println(Thread.currentThread().getName() + " sleep/wait end");
            }
            System.out.println("Time consume " + (System.currentTimeMillis() - start)+" ms");
        });
        t.start();

        Thread.sleep(1000);
        if (!t.isInterrupted()) {
            System.out.println("Interrupt " + t.getName());
            t.interrupt();
        }
    }
}
