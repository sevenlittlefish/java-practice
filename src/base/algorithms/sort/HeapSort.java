package base.algorithms.sort;

import java.util.Arrays;
import java.util.Random;

public class HeapSort {

    public static void main(String[] args) {
        int[] arr = new int[10];
        Random random = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(100);
        }
        System.out.println(Arrays.toString(arr));
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private static void sort(int[] arr) {
        //将乱序数组进行堆化
        heapSort(arr);
        for (int i = 0; i < arr.length; i++) {
            //每次将堆顶最大元素替换到最后面，再对其余项堆化，生成新的堆，再替换到最后面，如此往复
            int temp = arr[0];
            arr[0] = arr[arr.length-1-i];
            arr[arr.length-1-i] = temp;
            heapify(arr,0,arr.length-1-i);
        }
    }

    private static void heapSort(int[] arr){
        //自下而上堆化，保证当前节点的父节点都大于或小于子节点
        for (int i = arr.length - 1; i >= 0; i--) {
            heapify(arr,i,arr.length);
        }
    }

    /**
     * 堆化
     * @param arr
     * @param curNode
     * @param size
     */
    private static void heapify(int[] arr, int curNode, int size){
        if(curNode < size){
            int left = curNode*2+1;
            int right = curNode*2+2;
            int index = left;
            //从左右子节点中找到最大值
            if(left < size && right < size){
                if(arr[left] < arr[right]){
                    index = right;
                }
            }
            //若无右子节点只有左节点，则直接比较找出最大值替换给当前节点
            if(left < size){
                if(arr[curNode] < arr[index]){
                    int temp = arr[curNode];
                    arr[curNode] = arr[index];
                    arr[index] = temp;
                    //若替换后的那个子节点也有子节点，需要重新比较，因此递归再比较替换
                    heapify(arr,index,size);
                }
            }
        }
    }
}
