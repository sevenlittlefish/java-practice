package base.algorithms.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * 希尔排序
 */
public class ShellSort {

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
        //增量每次都/2
        for (int incr = arr.length/2; incr > 0; incr /= 2) {
            //同组相邻下标偏移量
            int offset = incr;
            //插入排序
            for (int j = offset; j < arr.length; j++) {
                for (int k = j-offset; k >= 0 ; k -= offset) {
                    if(arr[k] > arr[k+offset]){
                        int temp = arr[k];
                        arr[k] = arr[k+offset];
                        arr[k+offset] = temp;
                    }else{
                        break;
                    }
                }
            }
        }
    }
}
