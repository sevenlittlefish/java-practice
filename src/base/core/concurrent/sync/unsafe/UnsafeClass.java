package base.core.concurrent.sync.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * （1）实例化一个类；base.core.concurrent.sync.unsafe.Instance
 * （2）修改私有字段的值；base.core.concurrent.sync.unsafe.Instance
 * （3）抛出checked异常；
 * （4）使用堆外内存；base.core.concurrent.sync.unsafe.OffHeapArray
 * （5）CAS操作；base.core.concurrent.sync.unsafe.Counter
 * （6）阻塞/唤醒线程；base.core.concurrent.lock.MyLock
 */
public class UnsafeClass {
    private static Unsafe unsafe;
    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe)field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public static Unsafe getInstance(){
        return unsafe;
    }
}
