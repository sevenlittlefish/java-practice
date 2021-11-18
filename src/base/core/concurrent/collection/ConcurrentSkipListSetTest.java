package base.core.concurrent.collection;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 采用ConcurrentSkipListMap实现（自然顺序排序）
 */
public class ConcurrentSkipListSetTest {

    public static void main(String[] args) throws InterruptedException {
        ConcurrentSkipListSet<Integer> set = new ConcurrentSkipListSet<>();
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 100; j++) {
                    set.add(100-j);
                }
            }).start();
        }
        Thread.sleep(1000);
        System.out.println("size:"+set.size());
        for (Object o : set) {
            System.out.println(o);
        }
    }
}
