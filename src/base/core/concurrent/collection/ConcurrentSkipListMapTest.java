package base.core.concurrent.collection;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 跳表
 * 1.跳表是一个随机化的数据结构，实质就是一种可以进行二分查找的有序链表。
 * 2.跳表在原有的有序链表上面增加了多级索引，通过索引来实现快速查找。
 * 3.跳表不仅能提高搜索性能，同时也可以提高插入和删除操作的性能。
 */
public class ConcurrentSkipListMapTest {

    public static void main(String[] args) {
        Map concurrentSkipListMap = new ConcurrentSkipListMap<>();
        Map concurrentHashMap = new ConcurrentHashMap();

        int size = 1000000;
        Random random = new Random();
        long start1 = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            concurrentHashMap.put(i,i);
        }
        System.out.println("ConcurrentHashMap spend time:"+(System.currentTimeMillis() - start1)+" ms");

        long start2 = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            concurrentSkipListMap.put(i,i);
        }
        System.out.println("ConcurrentSkipListMap spend time:"+(System.currentTimeMillis() - start2)+" ms");

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        List list = new LinkedList();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        for (int i = 0; i < 10; i++) {
            int key = random.nextInt(size);

            long start3 = System.nanoTime();
            list.get(key);
            System.out.println("LinkedList get key ["+key+"] spend time:"+(System.nanoTime() - start3)+" ns");

            long start4 = System.nanoTime();
            concurrentSkipListMap.get(key);
            System.out.println("ConcurrentSkipListMap get key ["+key+"] spend time:"+(System.nanoTime() - start4)+" ns");

            System.out.println("=========================================");
        }
    }
}
