package base.algorithms.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * 快速排序，通过⼀趟排序将要排序的数据分割成独⽴的两部分，其中⼀部分的所有数据都⽐另外⼀部分的所有数据
 * 都要⼩，再对分割的部分再比较，递归直至排序所有部分排序完成
 */
public class QuickSort {

    public static void main(String[] args) {
        int[] arr = new int[10];
        Random random = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(100);
        }
        System.out.println(Arrays.toString(arr));
        sort(arr,0,arr.length-1);
        System.out.println(Arrays.toString(arr));
    }

    private static void sort(int[] arr, int left, int right) {
        int i = left;
        int j = right;
        //支点，不一定非取中间值
        int pivot = arr[(left+right)/2];
        //左右两端进⾏扫描，只要两端还没有交替，就⼀直扫描
        while(i <= j){
            //寻找直到找到⽐⽀点⼤的数
            while(pivot > arr[i]){
                i++;
            }
            //寻找直到找到⽐⽀点⼩的数
            while(pivot < arr[j]){
                j--;
            }
            //此时已经分别找到了⽐⽀点⼩的数(右边)、⽐⽀点⼤的数(左边)，它们进⾏交换
            if(i <= j){
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
                j--;
            }
        }
        //上⾯⼀个while保证了第⼀趟排序⽀点的左边⽐⽀点⼩，⽀点的右边⽐⽀点⼤
        //“左边”再做排序，直到左边剩下⼀个数(递归出⼝)
        if(left < j){
            sort(arr,left,j);
        }
        //“右边”再做排序，直到右边剩下⼀个数(递归出⼝)
        if(i < right){
            sort(arr,i,right);
        }
    }
}
