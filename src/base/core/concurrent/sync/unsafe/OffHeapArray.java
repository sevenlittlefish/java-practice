package base.core.concurrent.sync.unsafe;

import sun.misc.Unsafe;

public class OffHeapArray {

    //一个int等于4个字节
    private static final int INT = 4;
    private long size;
    private long address;
    private static Unsafe unsafe;
    static{
        unsafe = UnsafeClass.getInstance();
    }

    //构造方法，分配内存
    public OffHeapArray(int size){
        this.size = size;
        address = unsafe.allocateMemory(size*INT);
    }
    //获取指定索引处元素
    public int get(int i){
        return unsafe.getInt(address + i*INT);
    }
    //设置指定索引处元素
    public void set(long i, int value){
        unsafe.putInt(address + i*INT, value);
    }
    public long size(){
        return size;
    }
    //释放堆外内存
    public void freeMemory(){
        unsafe.freeMemory(address);
    }

    public static void main(String[] args) {
        OffHeapArray offHeapArray = new OffHeapArray(4);
        offHeapArray.set(0,1);
        offHeapArray.set(1,2);
        offHeapArray.set(2,3);
        offHeapArray.set(3,4);
        offHeapArray.set(1,5);//在索引的位置重复放入元素
        int sum = 0;
        for (int i = 0; i < offHeapArray.size(); i++) {
            sum += offHeapArray.get(i);
        }
        System.out.println(sum);
        offHeapArray.freeMemory();
    }
}
