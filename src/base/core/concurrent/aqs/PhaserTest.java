package base.core.concurrent.aqs;

import java.util.concurrent.Phaser;

/**
 * 定义一个需要4个阶段完成的大任务，每个阶段需要3个小任务，针对这些小任务，我们分别起3个线程来执行这些小任务
 */
public class PhaserTest {
    public static final int parties = 3;
    public static final int phases = 4;

    public static void main(String[] args) {
        Phaser phaser = new Phaser(parties){
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                System.out.println("===============phase:"+phase+" finished=================");
                return super.onAdvance(phase, registeredParties);
            }
        };
        for (int i = 0; i < parties; i++) {
            new Thread(()->{
                for (int j = 0; j < phases; j++) {
                    System.out.println(String.format("%s：phase：%d",Thread.currentThread().getName(),j));
                    phaser.arriveAndAwaitAdvance();
                }
            },"Thread "+i).start();
        }
    }
}
