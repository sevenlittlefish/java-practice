package base.algorithms.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * 插入排序，从第2位开始，第2个数与第1个数比较，第1个比第2个数大则替换，再从第3位开始，以此类推，每次插入后前面的数据都是有序的
 */
public class InsertionSort {

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
        for (int i = 1; i < arr.length; i++) {
            for (int j = i-1; j >= 0; j--) {
                if(arr[j] > arr[j+1]){
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }else{
                    //说明已排好序了，不需要再比较了
                    break;
                }
            }
        }
    }

}
