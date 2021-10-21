package base.core.concurrent.sync;

public class VolatileTest2 {

    //分别测试有volatile和没volatile情况
    private static volatile int MY_INT = 0;

    public static void main(String[] args) {
        new ChangeListener().start();
        new ChangeMaker().start();
    }

    static class ChangeListener extends Thread {
        @Override
        public void run() {
            int localValue = MY_INT;
            while (localValue < 5) {
                if (localValue != MY_INT) {
                    System.out.println(String.format("Got Change for MY_INT : %d", MY_INT));
                    localValue = MY_INT;
                }
            }
        }
    }

    static class ChangeMaker extends Thread {
        @Override
        public void run() {
            int localValue = MY_INT;
            while (MY_INT < 5) {
                System.out.println(String.format("Incrementing MY_INT to %d", localValue + 1));
                MY_INT = ++localValue;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
