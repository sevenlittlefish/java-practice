package base.core.collection.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * iterator的remove：
 * iterator的remove都会将修改的modCount设置到iterator的expectedModCount中，在调用Iterator.next()方法时判断modCount和expectedModCount是否相等，
 * 不相等才会抛出ConcurrentModificationException，因此在自身遍历过程中删除元素不会触发ConcurrentModificationException，除非有其他线程操作了集合修改了modCount
 *
 * 集合的remove：
 * 集合的remove会修改modCount的值，foreach循环还是会调用Iterator.next()方法，由于iterator的expectedModCount未和modCount同步，
 * 因此会抛出ConcurrentModificationException
 */
public class IteratorRemoveTest {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i+"");
        }
//        Iterator<String> iterator = list.iterator();
//        while (iterator.hasNext()){
//            String next = iterator.next();
//            if (next.equals("7"))
//                iterator.remove();
//        }
        for (String s : list) {
            if (s.equals("7"))
                list.remove(s);
        }
        System.out.println(list);
    }
}
