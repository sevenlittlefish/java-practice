package base.core.concurrent.collection;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 采用CopyOnWriteArrayList的addIfAbsent实现去重，因为是数组所以是有序的（插入顺序排序）
 */
public class CopyOnWriteArraySetTest {

    public static void main(String[] args) throws InterruptedException {
        CopyOnWriteArraySet set = new CopyOnWriteArraySet();
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 100; j++) {
                    set.add(j);
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
